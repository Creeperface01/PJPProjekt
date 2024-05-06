package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor

class VariableTypeVisitor(
    private val baseVisitor: BaseVisitor
) : ProjectBaseVisitor<Type>() {

    override fun visitVariableExpression(ctx: ProjectParser.VariableExpressionContext): Type {
        val varName = ctx.var_name?.text!!
        return baseVisitor.variables[varName] ?: error("Variable $varName not found")
    }
}