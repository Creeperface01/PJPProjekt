package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor

class VariableAssignTypeVisitor(
    baseVisitor: BaseVisitor,
    variableType: Type
) : ExpressionTypeVisitor(baseVisitor, setOf(variableType)) {

    override fun visitR_assignment(ctx: ProjectParser.R_assignmentContext): Type {
        ctx.r_assignment()?.let { assignmentCtx ->
            return assignmentCtx.accept(this)
        }

        val varName = ctx.var_name?.text!!
        val varType = baseVisitor.getVariable(varName)

        val expr = ctx.r_expression()!!
        val exprType = expr.accept(ExpressionTypeVisitor(baseVisitor, setOf(varType)))!!

        ctx.castType(expectedTypes, exprType) {
            "Expected type '$varType' for variable '$varName', but got '$exprType'"
        }

        return exprType
    }
}