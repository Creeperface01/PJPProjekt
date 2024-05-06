package cz.vsb.pjp.bed0152.project.util

enum class Type {
    INT,
    FLOAT,
    BOOL,
    STRING;

    fun isNumeric() = this == INT || this == FLOAT
}