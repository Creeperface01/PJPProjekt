package cz.vsb.pjp.bed0152.project.interpreter.value

import cz.vsb.pjp.bed0152.project.util.Type

class FloatValue(
    override val value: Float
) : Value(Type.FLOAT) {

    override fun unaryMinus(): Value {
        return FloatValue(-this.value)
    }

    override fun plus(other: Value): Value {
        require(other is FloatValue)
        return FloatValue(this.value + other.value)
    }

    override fun minus(other: Value): Value {
        require(other is FloatValue)
        return FloatValue(this.value - other.value)
    }

    override fun times(other: Value): Value {
        require(other is FloatValue)
        return FloatValue(this.value * other.value)
    }

    override fun div(other: Value): Value {
        require(other is FloatValue)
        return FloatValue(this.value / other.value)
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