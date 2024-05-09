package cz.vsb.pjp.bed0152.project.util

enum class Operator(
    val token: String
) {
    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    EQ("=="),
    NEQ("!="),
    LT("<"),
    GT(">"),
    LTE("<="),
    GTE(">="),
    AND("&&"),
    OR("||"),
    NOT("!"),
    ASSIGN("="),
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    MOD_ASSIGN("%=");

    fun isComparison() = this in comparisonOperators

    fun isArithmetic() = this in arithmeticOperators

    fun isAssignment() = this in arithmeticAssignOperators

    fun isLogical() = this in logicalOperators

    fun isUnary() = this in setOf(NOT)

    fun isBinary() = this in binaryOperators

    companion object {
        val tokenOperatorMap = entries.associateBy { it.token }

        val binaryOperators = setOf(PLUS, MINUS, MUL, DIV, MOD, EQ, NEQ, LT, GT, LTE, GTE, AND, OR)
        val arithmeticOperators = setOf(PLUS, MINUS, MUL, DIV, MOD)
        val arithmeticAssignOperators = setOf(PLUS_ASSIGN, MINUS_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN)
        val logicalOperators = setOf(AND, OR, NOT)
        val comparisonOperators = setOf(EQ, NEQ, LT, GT, LTE, GTE)

        fun getByToken(token: String) = tokenOperatorMap[token]!!
    }
}