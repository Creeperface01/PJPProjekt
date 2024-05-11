package cz.vsb.pjp.bed0152.project.compiler

import cz.vsb.pjp.bed0152.project.interpreter.*
import cz.vsb.pjp.bed0152.project.interpreter.value.BoolValue
import cz.vsb.pjp.bed0152.project.interpreter.value.FloatValue
import cz.vsb.pjp.bed0152.project.interpreter.value.IntValue
import cz.vsb.pjp.bed0152.project.util.Type

enum class InstructionType(
    private val argumentsCount: Int = 0,
    val action: (Stack, ByteCodeInterpreter, args: List<String>) -> Unit
) {
    ADD(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left + right)
    }),
    SUB(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left - right)
    }),
    MUL(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left * right)
    }),
    DIV(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left / right)
    }),
    MOD(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left % right)
    }),
    UMINUS(0, { stack, _, _ ->
        stack.push(-stack.pop())
    }),
    CONCAT(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left concat right)
    }),
    AND(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left and right)
    }),
    OR(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(left or right)
    }),
    GT(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(BoolValue(left > right))
    }),
    LT(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(BoolValue(left < right))
    }),
    EQ(0, { stack, _, _ ->
        val (left, right) = stack.popBinary()
        stack.push(BoolValue(left == right))
    }),
    NOT(0, { stack, _, _ ->
        stack.push(stack.pop().not())
    }),
    ITOF(0, { stack, _, _ ->
        val value = stack.pop()
        require(value is IntValue)
        stack.push(FloatValue(value.value.toFloat()))
    }),
    PUSH(2, { stack, _, args ->
        val type = Type.fromShort(args[0])
        val value = type.getValueFromString(args[1])
        stack.push(value)
    }),
    POP(0, { stack, _, _ ->
        stack.pop()
    }),
    LOAD(1, { stack, interpreter, args ->
        val variable = args[0]
        val value = interpreter.getVariable(variable)
        stack.push(value)
    }),
    SAVE(1, { stack, interpreter, args ->
        val variable = args[0]
        val value = stack.pop()
        interpreter.setVariable(variable, value)
    }),
    LABEL(1, { _, _, _ ->
    }),
    JMP(1, { _, interpreter, args ->
        val label = args[0]
        interpreter.goTo(label.toInt())
    }),
    FJMP(1, { stack, interpreter, args ->
        val label = args[0]
        val condition = stack.pop()
        require(condition is BoolValue)

        if (!condition.value) {
            interpreter.goTo(label.toInt())
        }
    }),
    PRINT(1, { stack, _, args ->
        val length = args[0].toInt()

        for (i in 0 until length) {
            val value = stack.pop()
            println(value.value)
        }
    }),
    READ(1, { stack, _, args ->
        val type = Type.fromShort(args[0])
        val input = readln()

        stack.push(type.getValueFromString(input))
    });

    fun create(vararg args: String): Instruction {
        require(args.size == argumentsCount) { "Invalid number of arguments for instruction $this (${args.toList()})" }

        return Instruction(this, args.toList())
    }
}