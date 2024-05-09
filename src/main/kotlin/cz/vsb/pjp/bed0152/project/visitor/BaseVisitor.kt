package cz.vsb.pjp.bed0152.project.visitor

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.visitor.type.ExpressionTypeVisitor
import cz.vsb.pjp.bed0152.project.visitor.type.VariableAssignTypeVisitor

class BaseVisitor : ProjectBaseVisitor<Unit>() {
    private val booleanExpressionVisitor = ExpressionTypeVisitor(this, setOf(Type.BOOL))
    private val numericExpressionVisitor = ExpressionTypeVisitor(this, setOf(Type.INT, Type.FLOAT))
    private val stringExpressionVisitor = ExpressionTypeVisitor(this, setOf(Type.STRING))

    val variables = mutableMapOf<String, Type>()

    fun getVariable(varName: String): Type {
        return variables[varName] ?: error("Variable $varName not found")
    }

    fun getVariableOrNull(varName: String): Type? {
        return variables[varName]
    }

    override fun defaultResult() {

    }

    override fun visitR_variable_declaration(ctx: ProjectParser.R_variable_declarationContext) {
        ctx.VARIABLE_NAME().forEach { terminal ->
            val varName = terminal.text

            require(!variables.containsKey(varName)) {
                "Variable '$varName' has already been declared"
            }

            variables[varName] = Type.valueOf(ctx.type?.text!!.uppercase())
        }


        super.visitR_variable_declaration(ctx)
    }

    override fun visitR_assignment(ctx: ProjectParser.R_assignmentContext) {
        ctx.accept(VariableAssignTypeVisitor(this, getVariable(ctx.var_name?.text!!)))
        super.visitR_assignment(ctx)
    }

    override fun visitNumericExpression(ctx: ProjectParser.NumericExpressionContext) {
        ctx.accept(numericExpressionVisitor)
        super.visitNumericExpression(ctx)
    }

    override fun visitBoolExpression(ctx: ProjectParser.BoolExpressionContext) {
        ctx.accept(booleanExpressionVisitor)
        super.visitBoolExpression(ctx)
    }

    override fun visitStringExpression(ctx: ProjectParser.StringExpressionContext) {
        ctx.accept(stringExpressionVisitor)
        super.visitStringExpression(ctx)
    }

    override fun visitNumericVariableExpression(ctx: ProjectParser.NumericVariableExpressionContext) {
        ctx.accept(numericExpressionVisitor)
        super.visitNumericVariableExpression(ctx)
    }

    override fun visitBoolVariableExpression(ctx: ProjectParser.BoolVariableExpressionContext) {
        ctx.accept(booleanExpressionVisitor)
        super.visitBoolVariableExpression(ctx)
    }

    override fun visitStringVariableExpression(ctx: ProjectParser.StringVariableExpressionContext) {
        ctx.accept(stringExpressionVisitor)
        super.visitStringVariableExpression(ctx)
    }

    override fun visitBoolVariableComparison(ctx: ProjectParser.BoolVariableComparisonContext) {
        ctx.accept(booleanExpressionVisitor)

        super.visitBoolVariableComparison(ctx)
    }
}