package info.journeymap.forge_toolkit.commands

import com.beust.klaxon.JsonObject
import info.journeymap.forge_toolkit.lang.getJsonData
import info.journeymap.forge_toolkit.lang.getKeys
import info.journeymap.forge_toolkit.writeJSON
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "finalise",
    aliases = ["finalize"],
    description = [
        "Clean up and sort a lang file in preparation for submission.",
        "This will both flatten and sort the keys in the file."
    ]
)
class Finalise : Callable<Int> {
    @CommandLine.Parameters(index = "0",description = ["Path to the lang file to be finalised."])
    lateinit var targetFile: File

    override fun call(): Int {
        val targetData: JsonObject?
        val targetKeys: Set<String>?

        try {
            targetData = getJsonData(targetFile)
            targetKeys = getKeys(targetData)
        } catch (e: Exception) {
            println()
            println("Failed to get translation data for file: ${targetFile.path}")

            System.err.println(e)
            println()

            return 1
        }

        if (targetData == null || targetKeys == null) {
            println("Failed to get translation data for file: ${targetFile.path}")
            return 1
        }

        val finalData = targetData.toSortedMap()

        if (targetData.containsKey("untranslated")) {
            val untranslated = targetData["untranslated"] as JsonObject

            for (item in untranslated.entries) {
                finalData[item.key] = item.value
            }

            finalData.remove("untranslated")
        }

        writeJSON(JsonObject(finalData), targetFile)

        print("Cleaned up and sorted file: ${targetFile.path}")

        return 0
    }
}