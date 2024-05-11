package cz.vsb.pjp.bed0152.project.interpreter

import cz.vsb.pjp.bed0152.project.compiler.InstructionType
import java.io.File

object ByteCodeParser {

    fun parse(fileName: String) = File(fileName).readLines().map { line ->
        val split = line.split(" ")

        val instructionName = split.first()
        var args = split.drop(1)

        if (instructionName == "push") { // push hack
            args = listOf(args[0], args.drop(1).joinToString(" "))
        }

        InstructionType.valueOf(instructionName.uppercase()).create(*args.toTypedArray())
    }

    fun createInterpreterFromSourceFile(fileName: String) = ByteCodeInterpreter(parse(fileName))
}