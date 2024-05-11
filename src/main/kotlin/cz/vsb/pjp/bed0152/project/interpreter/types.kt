package cz.vsb.pjp.bed0152.project.interpreter

import cz.vsb.pjp.bed0152.project.interpreter.value.Value

typealias Stack = ArrayDeque<Value>

data class BinaryEntry(val leftValue: Value, val rightValue: Value)

fun Stack.pop() = this.removeLast()

fun Stack.push(value: Value) = this.addLast(value)

fun Stack.popBinary(): BinaryEntry {
    val right = pop()
    val left = pop()

    return BinaryEntry(left, right)
}