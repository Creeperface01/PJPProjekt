package cz.vsb.pjp.bed0152.project

import cz.vsb.pjp.bed0152.project.exception.ParseException
import cz.vsb.pjp.bed0152.project.parser.ProjectLexer
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

fun main() {
    val source = """
        {
            int c;
        }
        int a, b;
        a = 1 + 1 * 5;
        string d;
        d = "test";
        
        float e;
        
        if(e >= a) {
        }
    """.trimIndent()

    val lexer = ProjectLexer(CharStreams.fromString(source))
    val tokens = CommonTokenStream(lexer)
    val parser = ProjectParser(tokens)

    val context = parser.program()

    val visitor = BaseVisitor()

    try {
        visitor.visit(context)
    } catch (e: ParseException) {
        val start = e.ctx.start!!
        println("Parse error at line ${start.line} - ${e.message}")
    }
}