package cz.vsb.pjp.bed0152.project.interpreter.value

import cz.vsb.pjp.bed0152.project.util.Type

class BoolValue(
    override val value: Boolean
) : Value(Type.BOOL) {

    override fun and(other: Value): Value {
        require(other is BoolValue)
        return BoolValue(this.value && other.value)
    }

    override fun or(other: Value): Value {
        require(other is BoolValue)
        return BoolValue(this.value || other.value)
    }

    override fun not(): Value {
        return BoolValue(!this.value)
    }
}