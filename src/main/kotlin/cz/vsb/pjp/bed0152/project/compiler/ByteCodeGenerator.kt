package cz.vsb.pjp.bed0152.project.compiler

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Operator
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.util.VariablesHolder
import cz.vsb.pjp.bed0152.project.util.accept
import cz.vsb.pjp.bed0152.project.visitor.type.ExpressionTypeVisitor
import org.antlr.v4.kotlinruntime.ParserRuleContext

class ByteCodeGenerator : ProjectBaseVisitor<List<Instruction>>(), VariablesHolder {

    private val variables = mutableMapOf<String, Type>()

    private var labelCounter = 0

    private val expressionTypeVisitor = ExpressionTypeVisitor(this, emptySet())

    override fun getVariable(varName: String): Type {
        return variables[varName] ?: error("Variable $varName not found")
    }

    override fun getVariableOrNull(varName: String): Type? {
        return variables[varName]
    }

    override fun defaultResult() = emptyList<Instruction>()

    // numeric expressions

    override fun visitNumericUnaryExpression(ctx: ProjectParser.NumericUnaryExpressionContext): List<Instruction> {
        return ctx.expr.accept(this) + listOf(InstructionType.UMINUS.create())
    }

    override fun visitNumericBinaryPriorityExpression(ctx: ProjectParser.NumericBinaryPriorityExpressionContext): List<Instruction> {
        return processBinaryNumericExpression(ctx.left!!, ctx.right!!, Operator.getByToken(ctx.op?.text!!))
    }

    override fun visitNumericBinaryExpression(ctx: ProjectParser.NumericBinaryExpressionContext): List<Instruction> {
        return processBinaryNumericExpression(ctx.left!!, ctx.right!!, Operator.getByToken(ctx.op?.text!!))
    }

    override fun visitNumericVariableExpression(ctx: ProjectParser.NumericVariableExpressionContext): List<Instruction> {
        return listOf(InstructionType.LOAD.create(ctx.var_name?.text!!))
    }

    override fun visitNumericValueExpression(ctx: ProjectParser.NumericValueExpressionContext): List<Instruction> {
        val type = ctx.accept(expressionTypeVisitor)
        return listOf(InstructionType.PUSH.create(type.short, ctx.value?.text!!))
    }

    override fun visitNumericParenthesisExpression(ctx: ProjectParser.NumericParenthesisExpressionContext): List<Instruction> {
        return ctx.expr.accept(this)
    }

    // boolean expressions

    override fun visitBoolVariableComparison(ctx: ProjectParser.BoolVariableComparisonContext): List<Instruction> {
        return processComparison(
            Operator.getByToken(ctx.op?.text!!),
            processVariableExpression(ctx.left?.text!!),
            processVariableExpression(ctx.right?.text!!)
        )
    }

    override fun visitBoolUnaryExpression(ctx: ProjectParser.BoolUnaryExpressionContext): List<Instruction> {
        return ctx.expr.accept(this) + Operator.getByToken(ctx.op?.text!!).instructions
    }

    override fun visitBoolAndBinaryExpression(ctx: ProjectParser.BoolAndBinaryExpressionContext): List<Instruction> {
        return ctx.left.accept(this) + ctx.right.accept(this) + Operator.AND.instructions
    }

    override fun visitBoolOrBinaryExpression(ctx: ProjectParser.BoolOrBinaryExpressionContext): List<Instruction> {
        return ctx.left.accept(this) + ctx.right.accept(this) + Operator.OR.instructions
    }

    override fun visitNumericComparisonExpression(ctx: ProjectParser.NumericComparisonExpressionContext): List<Instruction> {
        return processComparison(
            Operator.getByToken(ctx.op?.text!!),
            ctx.left.accept(this),
            ctx.right.accept(this)
        )
    }

    override fun visitStringComparisonExpression(ctx: ProjectParser.StringComparisonExpressionContext): List<Instruction> {
        return ctx.left.accept(this) + ctx.right.accept(this) + Operator.getByToken(ctx.op?.text!!).instructions
    }

    override fun visitBoolVariableExpression(ctx: ProjectParser.BoolVariableExpressionContext): List<Instruction> {
        return listOf(InstructionType.LOAD.create(ctx.var_name?.text!!))
    }

    override fun visitBoolValueExpression(ctx: ProjectParser.BoolValueExpressionContext): List<Instruction> {
        return listOf(InstructionType.PUSH.create(Type.BOOL.short, ctx.value?.text!!))
    }

    override fun visitBoolParenthesisExpression(ctx: ProjectParser.BoolParenthesisExpressionContext): List<Instruction> {
        return ctx.expr.accept(this)
    }

    // string expressions

    override fun visitStringConcatExpression(ctx: ProjectParser.StringConcatExpressionContext): List<Instruction> {
        return ctx.left.accept(this) + ctx.right.accept(this) + Operator.getByToken(ctx.op?.text!!).instructions
    }

    override fun visitStringVariableExpression(ctx: ProjectParser.StringVariableExpressionContext): List<Instruction> {
        return listOf(InstructionType.LOAD.create(ctx.var_name?.text!!))
    }

    override fun visitStringValueExpression(ctx: ProjectParser.StringValueExpressionContext): List<Instruction> {
        return listOf(InstructionType.PUSH.create(Type.STRING.short, ctx.value?.text!!))
    }

    override fun visitStringParenthesisExpression(ctx: ProjectParser.StringParenthesisExpressionContext): List<Instruction> {
        return ctx.expr.accept(this)
    }

    // statements

    override fun visitProgram(ctx: ProjectParser.ProgramContext): List<Instruction> {
        return ctx.r_statement().flatMap { it.accept(this) }
    }

    override fun visitR_block(ctx: ProjectParser.R_blockContext): List<Instruction> {
        return ctx.r_statement().flatMap { it.accept(this) }
    }

    override fun visitR_variable_declaration(ctx: ProjectParser.R_variable_declarationContext): List<Instruction> {
        return ctx.VARIABLE_NAME().flatMap { terminal ->
            val type = Type.valueOf(ctx.type?.text!!.uppercase())
            variables[terminal.text] = type

            listOf(
                InstructionType.PUSH.create(type.short, type.defaultValue),
                InstructionType.SAVE.create(terminal.text),
            )
        }
    }

    override fun visitR_assignment(ctx: ProjectParser.R_assignmentContext): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val expressionNode = (ctx.expr ?: ctx.assignment)
        val expression = expressionNode.accept(this)

        instructions += expression

        val varType = getVariable(ctx.var_name?.text!!)
        val expressionType = expressionTypeVisitor.visit(expressionNode!!)

        if (varType == Type.FLOAT && expressionType == Type.INT) {
            instructions += InstructionType.ITOF.create()
        }

        instructions += InstructionType.SAVE.create(ctx.var_name?.text!!)
        instructions += expression

        return instructions
    }

    override fun visitR_if(ctx: ProjectParser.R_ifContext): List<Instruction> {
        val startLabel = labelCounter++
        val elseLabel = labelCounter++
        val endLabel = labelCounter++

        val expression = ctx.expr.accept(this)

        val bodyStatements = (ctx.block ?: ctx.stmt).accept(this)
        val elseStatements = ctx.els?.accept(this)

        val instructions = mutableListOf<Instruction>()

        instructions += expression
        instructions += InstructionType.FJMP.create(
            (if (elseStatements != null) elseLabel else endLabel).toString()
        )

        instructions += InstructionType.LABEL.create(startLabel.toString())
        instructions += bodyStatements
        instructions += InstructionType.JMP.create(endLabel.toString())

        elseStatements?.let {
            instructions += InstructionType.LABEL.create(elseLabel.toString())
            instructions += elseStatements
            instructions += InstructionType.JMP.create(endLabel.toString())
        }

        instructions += InstructionType.LABEL.create(endLabel.toString())

        return instructions
    }

    override fun visitR_else(ctx: ProjectParser.R_elseContext): List<Instruction> {
        return (ctx.stmt ?: ctx.block).accept(this)
    }

    override fun visitR_while(ctx: ProjectParser.R_whileContext): List<Instruction> {
        val startLabel = labelCounter++
        val endLabel = labelCounter++

        val expression = ctx.expr.accept(this)
        val bodyStatements = (ctx.block ?: ctx.stmt).accept(this)

        val instructions = mutableListOf<Instruction>()

        instructions += InstructionType.LABEL.create(startLabel.toString())
        instructions += expression
        instructions += InstructionType.FJMP.create(endLabel.toString())

        instructions += bodyStatements
        instructions += InstructionType.JMP.create(startLabel.toString())
        instructions += InstructionType.LABEL.create(endLabel.toString())

        return instructions
    }

    override fun visitR_read(ctx: ProjectParser.R_readContext): List<Instruction> {
        return ctx.VARIABLE_NAME().flatMap {
            val varType = getVariable(it.text)

            listOf(
                InstructionType.READ.create(varType.short),
                InstructionType.SAVE.create(it.text)
            )
        }
    }

    override fun visitR_write(ctx: ProjectParser.R_writeContext): List<Instruction> {
        val writeArguments = ctx.r_write_expression()
        val expressions = writeArguments.reversed().flatMap { expr ->
            expr.var_name?.let {
                return@flatMap processVariableExpression(it.text!!)
            }

            return@flatMap expr.expr.accept(this)
        }

        return expressions + InstructionType.PRINT.create(writeArguments.size.toString())
    }

    // helper functions

    private fun processVariableExpression(varName: String): List<Instruction> {
        return listOf(InstructionType.LOAD.create(varName))
    }

    private fun processComparison(
        operator: Operator,
        left: List<Instruction>,
        right: List<Instruction>
    ): List<Instruction> {
        if (operator == Operator.GT || operator == Operator.LT || operator == Operator.EQ || operator == Operator.NEQ) {
            return left + right + operator.instructions
        }

        val nonEqualComparison = left + right + operator.instructions
        val equalComparison = left + right + Operator.EQ.instructions

        return nonEqualComparison + equalComparison + Operator.OR.instructions
    }

    private fun processBinaryNumericExpression(
        left: ParserRuleContext,
        right: ParserRuleContext,
        operator: Operator
    ): List<Instruction> {
        val leftType = left.accept(expressionTypeVisitor)
        val rightType = right.accept(expressionTypeVisitor)

        val leftInstructions = left.accept(this)!!
        val rightInstructions = right.accept(this)!!

        val instructions = mutableListOf<Instruction>()

        instructions += leftInstructions
        if (leftType == Type.INT && rightType == Type.FLOAT) {
            instructions += InstructionType.ITOF.create()
        }

        instructions += rightInstructions
        if (leftType == Type.FLOAT && rightType == Type.INT) {
            instructions += InstructionType.ITOF.create()
        }

        instructions += operator.instructions
        return instructions
    }

    companion object {
        fun generate(ctx: ProjectParser.ProgramContext): List<Instruction> {
            val generator = ByteCodeGenerator()
            return ctx.accept(generator)
        }

        fun serialize(instructions: List<Instruction>): String {
            return instructions.joinToString("\n") {
                var instructionName = it.type.name.lowercase()

                if (it.arguments.isNotEmpty()) {
                    instructionName += " " + it.arguments.joinToString(" ")
                }

                return@joinToString instructionName
            }
        }
    }
}