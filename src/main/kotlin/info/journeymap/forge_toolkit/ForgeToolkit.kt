package info.journeymap.forge_toolkit

import info.journeymap.forge_toolkit.commands.Update
import info.journeymap.forge_toolkit.commands.Validate
import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@CommandLine.Command(
    name = "ForgeToolkit",
    subcommands = [Validate::class, Update::class]
)
class ForgeToolkit : Callable<Int> {
    override fun call(): Int {
        CommandLine.usage(this, System.out)
        return 0
    }
}

fun main(args: Array<String>) {
    AnsiConsole.systemInstall()
    val returnValue = CommandLine(ForgeToolkit()).execute(*args)
    AnsiConsole.systemUninstall()

    exitProcess(returnValue)
}
