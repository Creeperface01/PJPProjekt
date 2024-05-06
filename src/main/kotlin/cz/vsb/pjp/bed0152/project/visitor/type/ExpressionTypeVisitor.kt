package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.exception.ParseException
import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.parser.ProjectParser.R_numeric_expressionContext
import cz.vsb.pjp.bed0152.project.util.Type
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

    override fun visitVariableExpression(ctx: ProjectParser.VariableExpressionContext): Type {
        val type = variableTypeVisitor.visitVariableExpression(ctx)

        ctx.require(expectedTypes.isEmpty() || expectedTypes.contains(type)) {
            val varName = ctx.var_name?.text!!
            "Expected type '$expectedTypes', but got variable '$varName' of type '$type'"
        }

        return type
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

    protected fun ParserRuleContext.castType(
        expected: Type,
        actual: Type,
        message: (() -> String)? = null
    ) = castType(setOf(expected), actual, message)

    protected fun ParserRuleContext.castType(expected: Set<Type>, actual: Type, message: (() -> String)? = null) {
        if (expected.contains(actual)) {
            return
        }

        if (actual == Type.INT && expected.contains(Type.FLOAT)) {
            return
        }

        error(if (message != null) message() else "Expected type '$expected', but got '$actual'")
    }

    protected fun ParserRuleContext.require(condition: Boolean, message: () -> String) {
        if (!condition) {
            error(message())
        }
    }

    protected fun ParserRuleContext.error(message: String) {
        throw ParseException(this, message)
    }
}