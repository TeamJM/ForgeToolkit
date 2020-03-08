package info.journeymap.forge_toolkit

import info.journeymap.forge_toolkit.commands.Validate
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@CommandLine.Command(
    name = "ForgeToolkit",
    subcommands = [Validate::class]
)

class ForgeToolkit : Callable<Int> {
    override fun call(): Int {
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(ForgeToolkit()).execute(*args))
