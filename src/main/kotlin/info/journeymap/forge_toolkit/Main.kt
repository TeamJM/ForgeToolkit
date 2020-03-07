package info.journeymap.forge_toolkit

import com.github.javaparser.StaticJavaParser
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import info.journeymap.forge_toolkit.actions.validate
import java.io.File

fun main(args: Array<String>) = mainBody {
    ArgParser(args.slice(0..0).toTypedArray()).parseInto(::Args).run {
        when (this.mode) {
            Modes.VALIDATE -> validate(args)
            else -> println("Not implemented: ${this.mode.name}")
        }
    }
}
