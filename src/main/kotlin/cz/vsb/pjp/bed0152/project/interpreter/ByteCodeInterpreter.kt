package cz.vsb.pjp.bed0152.project.interpreter

import cz.vsb.pjp.bed0152.project.compiler.Instruction
import cz.vsb.pjp.bed0152.project.compiler.InstructionType
import cz.vsb.pjp.bed0152.project.interpreter.value.Value
import cz.vsb.pjp.bed0152.project.util.Type

class ByteCodeInterpreter(
    private val instructions: List<Instruction>
) {

    private val stack = Stack()

    private val labels: Map<Int, Int> = processLabels()

    private val variableTypes = mutableMapOf<String, Type>()
    private val variables = mutableMapOf<String, Value>()

    private var instructionPointer = -1

    fun run() {
        while (instructionPointer++ < instructions.lastIndex) {
            instructions[instructionPointer].let { instruction ->
                try {
//                    println("[DEBUG] Executing instruction: '${instruction.type}' (${instruction.arguments}) at index '$instructionPointer'",)
                    instruction.type.action(stack, this, instruction.arguments)
                } catch (e: Exception) {
                    throw RuntimeException(
                        "Error executing instruction: '${instruction.type}' (${instruction.arguments}) at index '$instructionPointer'",
                        e
                    )
                }
            }
        }
    }

    fun goTo(labelIndex: Int) {
        instructionPointer = labels[labelIndex]!!
    }

    fun setVariable(name: String, value: Value) {
        variableTypes.putIfAbsent(name, value.type)
        require(variableTypes[name] == value.type) {
            "Cannot set variable '$name' of type '${variableTypes[name]}' to '${value.value}'"
        }

        variables[name] = value
    }

    fun getVariable(name: String, type: Type? = null): Value {
        require(type == null || variableTypes[name] == type) {
            "Expected type '$type' got variable '$name' of type '${variableTypes[name]}'"
        }

        return variables[name]!!
    }

    private fun processLabels() = instructions
        .mapIndexed { index, instruction -> index to instruction }
        .filter { it.second.type == InstructionType.LABEL }
        .associate { it.second.arguments[0].toInt() to it.first }
}