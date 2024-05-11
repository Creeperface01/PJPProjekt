package cz.vsb.pjp.bed0152.project.interpreter.value

import cz.vsb.pjp.bed0152.project.util.Type

class IntValue(
    override val value: Int
) : Value(Type.INT) {

    override fun unaryMinus(): Value {
        return IntValue(-this.value)
    }

    override fun plus(other: Value): Value {
        require(other is IntValue)
        return IntValue(this.value + other.value)
    }

    override fun minus(other: Value): Value {
        require(other is IntValue)
        return IntValue(this.value - other.value)
    }

    override fun times(other: Value): Value {
        require(other is IntValue)
        return IntValue(this.value * other.value)
    }

    override fun div(other: Value): Value {
        require(other is IntValue)
        return IntValue(this.value / other.value)
    }

    override fun rem(other: Value): Value {
        require(other is IntValue)
        return IntValue(this.value % other.value)
    }

    override fun compareTo(other: Value): Int {
        val otherValue = when (other) {
            is FloatValue -> other.value
            is IntValue -> other.value.toFloat()
            else -> throw UnsupportedOperationException("Cannot compare $type to ${other.type}")
        }

        return this.value.compareTo(otherValue)
    }
}