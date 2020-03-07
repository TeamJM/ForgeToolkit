package info.journeymap.forge_toolkit

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import info.journeymap.forge_toolkit.actions.validate

fun main(args: Array<String>) = mainBody {
    ArgParser(args.slice(0..0).toTypedArray()).parseInto(::Args).run {
        when (this.mode) {
            Modes.VALIDATE -> validate(args)
            else -> println("Not implemented: ${this.mode.name}")
        }
    }
}
