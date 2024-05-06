package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor

class NumericExpressionTypeVisitor(
    baseVisitor: BaseVisitor
) : ExpressionTypeVisitor(baseVisitor, setOf(Type.INT, Type.FLOAT)) {

//    override fun visitNumericBinaryPriorityExpression(ctx: ProjectParser.NumericBinaryPriorityExpressionContext): Type {
//        return processBinaryExpression(ctx.left!!, ctx.right!!)
//    }
//
//    override fun visitNumericBinaryExpression(ctx: ProjectParser.NumericBinaryExpressionContext): Type {
//        return processBinaryExpression(ctx.left!!, ctx.right!!)
//    }
//
//    override fun visitNumericValueExpression(ctx: ProjectParser.NumericValueExpressionContext): Type {
//        val number = ctx.value?.text!!
//
//        if (number.contains(".")) {
//            return Type.FLOAT
//        }
//
//        return Type.INT
//    }
//
//    private fun processBinaryExpression(left: R_numeric_expressionContext, right: R_numeric_expressionContext): Type {
//        val leftType = left.accept(this)!!
//        val rightType = right.accept(this)!!
//
//        require(leftType.isNumeric() && rightType.isNumeric()) {
//            "Expected numeric types, but got $leftType and $rightType"
//        }
//
//        if (leftType == Type.FLOAT || rightType == Type.FLOAT) {
//            return Type.FLOAT
//        }
//
//        return Type.INT
//    }
}