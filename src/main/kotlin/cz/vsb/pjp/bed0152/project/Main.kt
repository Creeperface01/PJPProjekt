package cz.vsb.pjp.bed0152.project

import cz.vsb.pjp.bed0152.project.compiler.ByteCodeGenerator
import cz.vsb.pjp.bed0152.project.exception.ParseException
import cz.vsb.pjp.bed0152.project.interpreter.ByteCodeParser
import cz.vsb.pjp.bed0152.project.parser.ProjectLexer
import cz.vsb.pjp.bed0152.project.parser.ProjectParser
import cz.vsb.pjp.bed0152.project.visitor.BaseVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File

fun main() {
//    val source = """
//        {
//            int c;
//        }
//        int a, b;
//        a = 1 + 1 * 5;
//        string d;
//        d = "test";
//
//        float e;
//
//        if(e >= a) {
//        }
//    """.trimIndent()

//    val source = """
//        write "<Constants>";
//        write "10: ",10;
//        write " 1.25: ", 1.25;
//        write "";;
//
//        write "<Variables>";
//        string s;
//        s="Abcd";
//        write "s(Abcd): ", s;
//
//        float d;
//        d=3.141592;
//        write "d(3.141592): ", d;
//
//        int n;
//        n=-500;
//        write "n(-500): ", n;
//        write "";
//
//        bool boolean;
//        boolean=true;
//        write "boolean(true): ",boolean;
//        write "";
//
//        write "<Expressions>";
//        write "2+3*5(17): ",2+3*5;
//        write "17 / 3(5): ", 17 / 3;
//        write "17 % 3(2): ", 17 % 3;
//        write "2.5*2.5/6.25(1.0): ", 2.5*2.5/6.25;
//        write "1.5*3(4.5): ", 1.5*3;
//        write "abc+def (abcdef): ", "abc"."def";
//        write "";
//
//        write  "<Comments>"; // hidden
//        // write  "it is error, if you see this";
//
//        write "<Automatic int conversion>";
//        float y;
//        y = 10;
//        write "y (10.0): ", y;
//
//        write "<Multiple Assignments>";
//        int i,j,k;
//        i=j=k=55;
//        write "i=j=k=55: ",i,"=",j,"=",k;
//
//        write "<Input - a(int),b(float),c(string),d(bool)>";
//        int a;
//        float b;
//        string c;
//        bool e;
//        a = 0;
//        b = 0.0;
//        c = "";
//        e = true;
//        read a,b,c,e;
//        write "a,b,c,e: ", a, ",", b, ",", c, ",",e;
//    """.trimIndent()

    val source = """
        string a;
        a = "\"escaped \" string\""
        
        write a;
    """.trimIndent()

    val lexer = ProjectLexer(CharStreams.fromString(source))
    val tokens = CommonTokenStream(lexer)
    val parser = ProjectParser(tokens)

    val context = parser.program()

    val visitor = BaseVisitor()

    try {
        context.accept(visitor)
    } catch (e: ParseException) {
        val start = e.ctx.start!!
        println("Parse error at line ${start.line} - ${e.message}")
    }

    try {
        val instructions = ByteCodeGenerator.generate(context)
        File("output.bc").writeText(ByteCodeGenerator.serialize(instructions))

        val interpreter = ByteCodeParser.createInterpreterFromSourceFile("output.bc")
        interpreter.run()
    } catch (e: ParseException) {
        val start = e.ctx.start!!
        println("ByteCode generator parse error at line ${start.line} - ${e.message}")
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}