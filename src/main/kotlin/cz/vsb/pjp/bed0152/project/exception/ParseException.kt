package cz.vsb.pjp.bed0152.project.exception

import org.antlr.v4.kotlinruntime.ParserRuleContext

class ParseException(
    val ctx: ParserRuleContext,
    message: String
) : RuntimeException(message) {

}