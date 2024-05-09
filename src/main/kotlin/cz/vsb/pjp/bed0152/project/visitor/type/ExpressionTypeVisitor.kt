package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.parser.ProjectParser.R_numeric_expressionContext
import cz.vsb.pjp.bed0152.project.parser.ProjectParser.Tokens
import cz.vsb.pjp.bed0152.project.util.Operator
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.util.require
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor
import org.antlr.v4.kotlinruntime.ParserRuleContext

open class ExpressionTypeVisitor(
    protected val baseVisitor: BaseVisitor,
    protected val expectedTypes: Set<Type>
) : ProjectBaseVisitor<Type>() {
//    protected val expectedTypes = mutableSetOf<Type>()

    private val variableTypeVisitor = VariableTypeVisitor(baseVisitor)

//    fun <T> withExpectedTypes(vararg types: Type, block: ExpressionTypeVisitor.() -> T): T {
//        expectedTypes.clear()
//        expectedTypes.addAll(types)
//        return block()
//    }

    override fun visitNumericVariableExpression(ctx: ProjectParser.NumericVariableExpressionContext): Type {
        return checkVariable(ctx, setOf(Type.INT, Type.FLOAT))
    }

    override fun visitBoolVariableExpression(ctx: ProjectParser.BoolVariableExpressionContext): Type {
        return checkVariable(ctx, setOf(Type.BOOL))
    }

    override fun visitStringVariableExpression(ctx: ProjectParser.StringVariableExpressionContext): Type {
        return checkVariable(ctx, setOf(Type.STRING))
    }

    private fun checkVariable(ctx: ParserRuleContext, expressionTypes: Set<Type>): Type {
        val variableType = ctx.accept(variableTypeVisitor)!!

        val varName = ctx.getToken(Tokens.VARIABLE_NAME, 0)?.text!!
        val typeIntersection = expectedTypes.filter { expectedType ->
            expressionTypes.any { it.canBeCastedTo(expectedType) }
        }

        ctx.require(typeIntersection.isNotEmpty()) {
            "Expression types '$expressionTypes' are not compatible with variable $varName of type '$variableType'"
        }

        ctx.require(expectedTypes.isEmpty() || expectedTypes.contains(variableType)) {
            "Expected type '$expectedTypes', but got variable '$varName' of type '$variableType'"
        }

        return variableType
    }

    override fun visitNumericBinaryPriorityExpression(ctx: ProjectParser.NumericBinaryPriorityExpressionContext): Type {
        return processBinaryExpression(ctx.left!!, ctx.right!!)
    }

    override fun visitNumericBinaryExpression(ctx: ProjectParser.NumericBinaryExpressionContext): Type {
        return processBinaryExpression(ctx.left!!, ctx.right!!)
    }

    override fun visitNumericValueExpression(ctx: ProjectParser.NumericValueExpressionContext): Type {
        val number = ctx.value?.text!!

        if (number.contains(".")) {
            return Type.FLOAT
        }

        return Type.INT
    }

    override fun visitBoolExpression(ctx: ProjectParser.BoolExpressionContext): Type {
        return Type.BOOL
    }

    override fun visitStringExpression(ctx: ProjectParser.StringExpressionContext): Type {
        return Type.STRING
    }

    override fun visitBoolVariableComparison(ctx: ProjectParser.BoolVariableComparisonContext): Type {
        val leftVariableName = ctx.left?.text!!
        val rightVariableName = ctx.right?.text!!

        val leftType = baseVisitor.getVariable(leftVariableName)
        val rightType = baseVisitor.getVariable(rightVariableName)

        ctx.require(leftType == rightType || leftType.isNumeric() && rightType.isNumeric()) {
            "Incompatible types for comparison got '$leftType' and '$rightType'"
        }

        val operator = Operator.getByToken(ctx.op?.text!!)

        ctx.require(operator.isBinary()) {
            "Expected binary operator, but got '${operator.name}'"
        }

        return Type.BOOL
    }

    private fun processBinaryExpression(left: R_numeric_expressionContext, right: R_numeric_expressionContext): Type {
        val leftType = left.accept(this)!!
        val rightType = right.accept(this)!!

        left.require(leftType.isNumeric()) {
            "Expected numeric type, but got $leftType"
        }

        right.require(rightType.isNumeric()) {
            "Expected numeric type, but got $rightType"
        }

        if (leftType == Type.FLOAT || rightType == Type.FLOAT) {
            return Type.FLOAT
        }

        return Type.INT
    }
}