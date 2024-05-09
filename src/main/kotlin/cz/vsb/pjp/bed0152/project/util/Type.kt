package cz.vsb.pjp.bed0152.project.util

enum class Type {
    INT,
    FLOAT,
    BOOL,
    STRING;

    fun isNumeric() = this == INT || this == FLOAT

    fun canBeCastedTo(type: Type) = this == type || (this == FLOAT && type == INT)
}