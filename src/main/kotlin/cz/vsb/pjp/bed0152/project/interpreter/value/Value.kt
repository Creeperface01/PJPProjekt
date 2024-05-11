package cz.vsb.pjp.bed0152.project.interpreter.value

import cz.vsb.pjp.bed0152.project.util.Type

sealed class Value(
    val type: Type
) : Comparable<Value> {

    abstract val value: Any

    open operator fun unaryMinus(): Value {
        throw UnsupportedOperationException("Unary minus operation is not supported on value type $type")
    }

    open operator fun plus(other: Value): Value {
        throw UnsupportedOperationException("Plus operation is not supported on value type $type")
    }

    open operator fun minus(other: Value): Value {
        throw UnsupportedOperationException("Subtract operation is not supported on value type $type")
    }

    open operator fun times(other: Value): Value {
        throw UnsupportedOperationException("Multiply operation is not supported on value type $type")
    }

    open operator fun div(other: Value): Value {
        throw UnsupportedOperationException("Division operation is not supported on value type $type")
    }

    open operator fun rem(other: Value): Value {
        throw UnsupportedOperationException("Modulo operation is not supported on value type $type")
    }

    open infix fun concat(other: Value): Value {
        throw UnsupportedOperationException("Concat operation is not supported on value type $type")
    }

    open infix fun and(other: Value): Value {
        throw UnsupportedOperationException("And operation is not supported on value type $type")
    }

    open infix fun or(other: Value): Value {
        throw UnsupportedOperationException("Or operation is not supported on value type $type")
    }

    open fun not(): Value {
        throw UnsupportedOperationException("Not operation is not supported on value type $type")
    }

    override fun compareTo(other: Value): Int {
        throw UnsupportedOperationException("Compare operation is not supported on value type $type")
    }

    override fun equals(other: Any?): Boolean {
        return other is Value && value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}