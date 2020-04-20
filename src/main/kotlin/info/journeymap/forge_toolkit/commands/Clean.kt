package info.journeymap.forge_toolkit.commands

import com.beust.klaxon.JsonObject
import info.journeymap.forge_toolkit.lang.getJsonData
import info.journeymap.forge_toolkit.lang.getKeys
import info.journeymap.forge_toolkit.lang.getKeysInFile
import info.journeymap.forge_toolkit.lang.visitJavaFiles
import info.journeymap.forge_toolkit.writeJSON
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "clean",
    description = ["Remove all unused translation keys from a given set of lang files."]
)
class Clean : Callable<Int> {
    @CommandLine.Parameters(index = "0", description = ["Path to the directory containing the java sources to be checked"])
    lateinit var javaDir: String

    @CommandLine.Parameters(index = "1..*", arity = "1..*", description = ["Paths to the lang files containing the keys to check"])
    lateinit var langFiles: List<File>

    override fun call(): Int {
        for (langFile in langFiles) {
            var translationData: JsonObject? = null
            var translationKeys: Set<String>? = null

            try {
                translationData = getJsonData(langFile)
                translationKeys = getKeys(translationData)
            } catch (e: Exception) {
                println()
                println("Failed to get translation data for file: ${langFile.path}.")

                System.err.println(e)
                println()

                continue
            }

            if (translationData == null || translationKeys == null) {
                println("Failed to get translation data for file: ${langFile.path}.")
                return 1
            }

            val visitorStrings = visitJavaFiles(this.javaDir)
            val difference = translationKeys - visitorStrings

            if (difference.isEmpty()) {
                println("Already clean: ${langFile.path}")
            } else {
                println()
                println("== REMOVED KEYS: ${langFile.path} ==")
                println()

                for (key in difference) {
                    // Output each key here in case keys are removed in error
                    println("  \"${key}\": \"${translationData[key]}\",")
                    translationData.remove(key)
                }

                writeJSON(translationData, langFile)

                println()
                println("Removed ${difference.size} (of ${translationKeys.size}) unused translation keys.")
            }
        }

        return 0
    }
}