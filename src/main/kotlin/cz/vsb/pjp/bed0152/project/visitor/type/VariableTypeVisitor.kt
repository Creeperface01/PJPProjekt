package cz.vsb.pjp.bed0152.project.visitor.type

import cz.vsb.pjp.bed0152.project.parser.ProjectBaseVisitor
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.util.Type
import cz.vsb.pjp.bed0152.project.util.VariablesHolder

class VariableTypeVisitor(
    private val variableHolder: VariablesHolder
) : ProjectBaseVisitor<Type>() {

    override fun visitNumericVariableExpression(ctx: ProjectParser.NumericVariableExpressionContext): Type {
        return variableHolder.getVariable(ctx.var_name?.text!!)
    }

    override fun visitBoolVariableExpression(ctx: ProjectParser.BoolVariableExpressionContext): Type {
        return variableHolder.getVariable(ctx.var_name?.text!!)
    }

    override fun visitStringVariableExpression(ctx: ProjectParser.StringVariableExpressionContext): Type {
        return variableHolder.getVariable(ctx.var_name?.text!!)
    }

    override fun visitBoolVariableComparison(ctx: ProjectParser.BoolVariableComparisonContext): Type {
        return super.visitBoolVariableComparison(ctx)
    }
}