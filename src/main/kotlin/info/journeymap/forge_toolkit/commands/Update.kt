package info.journeymap.forge_toolkit.commands

import com.beust.klaxon.JsonObject
import info.journeymap.forge_toolkit.lang.getJsonData
import info.journeymap.forge_toolkit.lang.getKeys
import info.journeymap.forge_toolkit.writeJSON
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "update",
    description = [
        "Ensures that all keys from the primary lang file exist in the others provided.",
        "New keys will be placed in a new object named 'untranslated'."
    ]
)
class Update : Callable<Int> {
    @CommandLine.Parameters(index = "0", description = ["Path to the primary lang file containing the full set of keys"])
    lateinit var sourceFile: File

    @CommandLine.Parameters(index = "1..*", arity = "1..*", description = ["Paths to each lang file to be updated"])
    lateinit var targetFiles: List<File>

    override fun call(): Int {
        var sourceData: JsonObject? = null
        var sourceKeys: Set<String>? = null

        try {
            sourceData = getJsonData(sourceFile)
            sourceKeys = getKeys(sourceData)
        } catch (e: Exception) {
            System.err.println(e)
        }

        if (sourceKeys == null) {
            println("Failed to load translations data.")
            return 1
        }

        for (file in targetFiles) {
            if (file.equals(sourceFile)) {
                println("Skipping (same as source file): ${file.path}")
                continue
            }

            var targetData: JsonObject?
            var targetKeys: Set<String>?

            try {
                targetData = getJsonData(file)
                targetKeys = getKeys(targetData)

                if (targetKeys != null && targetKeys.contains("untranslated")) {
                    val untranslated = targetData!!["untranslated"] as JsonObject
                    targetKeys = targetKeys.union(untranslated.keys)
                }
            } catch (e: Exception) {
                println()
                println("Failed to get translation keys for file: ${file.path}")

                System.err.println(e)
                println()

                continue
            }

            if (targetKeys == null) {
                println("Failed to get translation keys for file: ${file.path}")
                continue
            }

            val missingKeys: Set<String> = sourceKeys - targetKeys

            if (missingKeys.isEmpty()) {
                println("Already up to date: ${file.path}")
                continue
            }

            val keyMap = JsonObject()

            for (missingKey in missingKeys) {
                keyMap[missingKey] = sourceData!![missingKey]
            }

            targetData!!["untranslated"] = keyMap

            writeJSON(targetData, file)
            println("Updated (${missingKeys.size} keys): ${file.path}")
        }

        return 0
    }
}