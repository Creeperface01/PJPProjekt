package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor

class VariableTypeVisitor(
    private val baseVisitor: BaseVisitor
) : ProjectBaseVisitor<Type>() {

    override fun visitNumericVariableExpression(ctx: ProjectParser.NumericVariableExpressionContext): Type {
        return checkVariable(ctx.var_name?.text!!)
    }

    override fun visitBoolVariableExpression(ctx: ProjectParser.BoolVariableExpressionContext): Type {
        return checkVariable(ctx.var_name?.text!!)
    }

    override fun visitStringVariableExpression(ctx: ProjectParser.StringVariableExpressionContext): Type {
        return checkVariable(ctx.var_name?.text!!)
    }

    override fun visitBoolVariableComparison(ctx: ProjectParser.BoolVariableComparisonContext): Type {
        return super.visitBoolVariableComparison(ctx)
    }

    private fun checkVariable(varName: String): Type {
        return baseVisitor.variables[varName] ?: error("Variable $varName not found")
    }
}