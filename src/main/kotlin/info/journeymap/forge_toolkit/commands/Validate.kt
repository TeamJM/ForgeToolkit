package info.journeymap.forge_toolkit.commands

import info.journeymap.forge_toolkit.lang.getKeysInFile
import info.journeymap.forge_toolkit.lang.visitJavaFiles
import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "validate",
    description = ["Checks which translation keys haven't been used in a given set of Java sources"]
)
class Validate : Callable<Int> {
    @CommandLine.Parameters(index = "0", description = ["Path to the lang file containing the keys to check"])
    lateinit var langFile: String

    @CommandLine.Parameters(index = "1", description = ["Path to the directory containing the java sources to be checked"])
    lateinit var javaDir: String

    override fun call(): Int {
        var translationKeys: Set<String>? = null

        try {
            translationKeys = getKeysInFile(this.langFile)
        } catch (e: Exception) {
            System.err.println(e)
        }

        if (translationKeys == null) {
            println("Failed to load translations data.")
            return 1
        }

        val visitorStrings = visitJavaFiles(this.javaDir)
        val difference = translationKeys - visitorStrings

        if (difference.isEmpty()) {
            println("All translation keys are used.")
        } else {
            println("Found ${difference.size} (of ${translationKeys.size}) unused translation keys:\n")

            difference.forEach { println(it) }
        }

        return 0
    }
}