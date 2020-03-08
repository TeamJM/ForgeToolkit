package info.journeymap.forge_toolkit.commands

import com.github.javaparser.StaticJavaParser
import info.journeymap.forge_toolkit.Visitor
import info.journeymap.forge_toolkit.parseJSON
import picocli.CommandLine
import java.io.File
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
        var translationKeys: MutableSet<String>? = null

        val visitor = Visitor(mutableSetOf<String>())
        val jsonData = parseJSON(File(this.langFile).inputStream())

        try {
            translationKeys = jsonData?.keys?.filter {
                !it.startsWith("_") &&
                        !it.startsWith("jm.common.location_") &&
                        !it.startsWith("jm.webmap.")
            }?.toMutableSet()
        } catch (e: Exception) {
            System.err.println(e)
        }

        if (translationKeys == null) {
            println("Failed to load translations data.")
            return 1
        }

        File(this.javaDir).walkTopDown().filter { it.path.endsWith(".java") }.forEach {
            try {
                val statements = StaticJavaParser.parse(it)
                visitor.visit(statements, null)
            } catch (e: Exception) {
                System.err.println("Failed to parse file: ${it.path}\n\n$e")
            }
        }

        val difference = translationKeys - visitor.strings

        if (difference.isEmpty()) {
            println("All translation keys are used.")
        } else {
            println("Found ${difference.size} (of ${translationKeys.size}) unused translation keys:\n")

            difference.forEach { println(it) }
        }

        return 0
    }
}