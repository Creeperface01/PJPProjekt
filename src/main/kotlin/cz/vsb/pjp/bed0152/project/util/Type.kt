package cz.vsb.pjp.bed0152.project.util

import cz.vsb.pjp.bed0152.project.interpreter.value.*

enum class Type(
    val short: String,
    val defaultValue: String
) {
    INT("I", "0") {
        override fun getValueFromString(input: String): Value {
            return IntValue(input.toInt())
        }
    },
    FLOAT("F", "0.0") {
        override fun getValueFromString(input: String): Value {
            return FloatValue(input.toFloat())
        }
    },
    BOOL("B", "false") {
        override fun getValueFromString(input: String): Value {
            return BoolValue(input.toBoolean())
        }
    },
    STRING("S", "\"\"") {
        override fun getValueFromString(input: String): Value {
            val regex = Regex("""\"(\\.|[^\"\\])*\"""")
            val matchResult = regex.find(input)
            return StringValue(
                matchResult?.value?.let {
                    it.substring(1, it.length - 1).replace("\\\"", "\"")
                        .replace("\\\\", "\\")
                        .replace("\\n", "\n")
                        .replace("\\t", "\t")
                } ?: ""
            )
        }
    };

    fun isNumeric() = this == INT || this == FLOAT

    fun canBeCastedTo(type: Type) = this == type || (this == FLOAT && type == INT)

    abstract fun getValueFromString(input: String): Value

    companion object {
        fun fromShort(short: String): Type {
            return entries.find { it.short == short } ?: throw IllegalArgumentException("Unknown type: $short")
        }
    }
}