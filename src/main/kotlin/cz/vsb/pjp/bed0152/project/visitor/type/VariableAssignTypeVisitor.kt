package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.util.VariablesHolder
import cz.vsb.pjp.bed0152.project.util.castType

class VariableAssignTypeVisitor(
    variablesHolder: VariablesHolder,
    variableType: Type
) : ExpressionTypeVisitor(variablesHolder, setOf(variableType)) {

    override fun visitR_assignment(ctx: ProjectParser.R_assignmentContext): Type {
//        ctx.assignment?.let { assignmentCtx ->
//            return assignmentCtx.accept(this)
//        }
//
        val varName = ctx.var_name?.text!!
        val varType = variablesHolder.getVariable(varName)
//
//        val expr = ctx.expr!!
//        val exprType = expr.accept(ExpressionTypeVisitor(variablesHolder, setOf(varType)))!!

        val exprType = super.visitR_assignment(ctx)

        ctx.castType(expectedTypes, exprType) {
            "Expected type '$varType' for variable '$varName', but got '$exprType'"
        }

        return exprType
    }
}