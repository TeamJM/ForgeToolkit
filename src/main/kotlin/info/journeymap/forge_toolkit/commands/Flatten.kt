package info.journeymap.forge_toolkit.commands

import com.beust.klaxon.JsonObject
import info.journeymap.forge_toolkit.lang.getJsonData
import info.journeymap.forge_toolkit.lang.getKeys
import info.journeymap.forge_toolkit.writeJSON
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "flatten",
    description = [
        "Moves all keys from the `untranslated` objects from the given lang files back into the main " +
                "objects, if any exist."
    ]
)
class Flatten : Callable<Int> {
    @CommandLine.Parameters(index = "0..*", arity = "1..*", description = ["Paths to each lang file to be flattened"])
    lateinit var targetFiles: List<File>

    override fun call(): Int {
        for (file in targetFiles) {
            var targetData: JsonObject?
            var targetKeys: Set<String>?
            var untranslated: JsonObject? = null

            try {
                targetData = getJsonData(file)
                targetKeys = getKeys(targetData)

                if (targetData != null && targetKeys != null && targetKeys.contains("untranslated")) {
                    untranslated = targetData["untranslated"] as JsonObject
                }
            } catch (e: Exception) {
                println()
                println("Failed to get translation keys for file: ${file.path}")

                System.err.println(e)
                println()

                continue
            }

            if (targetData == null || targetKeys == null) {
                println("Failed to get translation keys for file: ${file.path}")
                continue
            }

            if (untranslated == null) {
                println("Already flattened: ${file.path}")
                continue
            }

            for (item in untranslated.entries) {
                targetData[item.key] = item.value
            }

            targetData.remove("untranslated")

            writeJSON(targetData, file)
            println("Flattened (${untranslated.size} keys): ${file.path}")
        }

        return 0
    }
}