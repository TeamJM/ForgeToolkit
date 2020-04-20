package info.journeymap.forge_toolkit.commands

import com.beust.klaxon.JsonObject
import info.journeymap.forge_toolkit.lang.getJsonData
import info.journeymap.forge_toolkit.lang.getKeys
import info.journeymap.forge_toolkit.writeJSON
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "sort",
    description = [
        "Sorts the keys in all given lang files alphabetically."
    ]
)
class Sort : Callable<Int> {
    @CommandLine.Parameters(index = "0..*", arity = "1..*", description = ["Paths to each lang file to be sorted"])
    lateinit var targetFiles: List<File>

    override fun call(): Int {
        for (file in targetFiles) {
            var targetData: JsonObject?

            try {
                targetData = getJsonData(file)
            } catch (e: Exception) {
                println()
                println("Failed to get translation data for file: ${file.path}")

                System.err.println(e)
                println()

                continue
            }

            if (targetData == null) {
                println("Failed to get translation data for file: ${file.path}")
                continue
            }

            val keyMap = JsonObject(targetData.toSortedMap())

            writeJSON(keyMap, file)
            println("Sorted (${targetData.size} keys): ${file.path}")
        }

        return 0
    }
}