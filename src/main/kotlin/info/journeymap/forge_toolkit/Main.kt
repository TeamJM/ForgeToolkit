package info.journeymap.forge_toolkit

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import info.journeymap.forge_toolkit.actions.validate

fun main(args: Array<String>) = mainBody {
    ArgParser(args.slice(0..0).toTypedArray()).parseInto(::Args).run {
        val shortenedArgs = args.slice(1 until args.size).toTypedArray()

        when (this.mode) {
            Modes.VALIDATE -> validate(shortenedArgs)
            else -> println("Not implemented: ${this.mode.name}")
        }
    }
}
