package cz.vsb.pjp.bed0152.project.util

import cz.vsb.pjp.bed0152.project.exception.ParseException
import org.antlr.v4.kotlinruntime.ParserRuleContext

fun ParserRuleContext.castType(
    expected: Type,
    actual: Type,
    message: (() -> String)? = null
) = castType(setOf(expected), actual, message)

fun ParserRuleContext.castType(expected: Set<Type>, actual: Type, message: (() -> String)? = null) {
    if (expected.contains(actual)) {
        return
    }

    if (actual == Type.INT && expected.contains(Type.FLOAT)) {
        return
    }

    error(if (message != null) message() else "Expected type '$expected', but got '$actual'")
}

fun ParserRuleContext.require(condition: Boolean, message: () -> String) {
    if (!condition) {
        error(message())
    }
}

fun ParserRuleContext.error(message: String) {
    throw ParseException(this, message)
}