package cz.vsb.pjp.bed0152.project.interpreter.value

import cz.vsb.pjp.bed0152.project.util.Type

class StringValue(
    override val value: String
) : Value(Type.STRING) {
    override fun concat(other: Value): Value {
        require(other is StringValue)

        return StringValue(this.value + other.value)
    }
}