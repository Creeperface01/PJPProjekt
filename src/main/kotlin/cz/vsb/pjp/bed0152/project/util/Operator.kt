package cz.vsb.pjp.bed0152.project.util

import cz.vsb.pjp.bed0152.project.compiler.Instruction
import cz.vsb.pjp.bed0152.project.compiler.InstructionType

enum class Operator(
    val token: String,
    val instructionTypes: List<InstructionType>,
) {
    PLUS("+", InstructionType.ADD),
    MINUS("-", InstructionType.SUB),
    CONCAT(".", InstructionType.CONCAT),
    MUL("*", InstructionType.MUL),
    DIV("/", InstructionType.DIV),
    MOD("%", InstructionType.MOD),
    EQ("==", InstructionType.EQ),
    NEQ("!=", InstructionType.EQ, InstructionType.NOT),
    LT("<", InstructionType.LT),
    GT(">", InstructionType.GT),
    LTE("<=", InstructionType.LT),
    GTE(">=", InstructionType.GT),
    AND("&&", InstructionType.AND),
    OR("||", InstructionType.OR),
    NOT("!", InstructionType.NOT),
    ASSIGN("="),
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    MOD_ASSIGN("%=");

    val instructions: List<Instruction> by lazy {
        instructionTypes.map { it.create() }
    }

    constructor(token: String, vararg instructionTypes: InstructionType) : this(token, instructionTypes.toList())

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