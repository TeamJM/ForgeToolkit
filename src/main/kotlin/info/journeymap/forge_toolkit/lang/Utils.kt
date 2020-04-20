package info.journeymap.forge_toolkit.lang

import com.beust.klaxon.JsonObject
import com.github.javaparser.StaticJavaParser
import info.journeymap.forge_toolkit.Config
import info.journeymap.forge_toolkit.getConfig
import info.journeymap.forge_toolkit.parseJSON
import java.io.File

val config: Config = getConfig()

fun getKeysInFile(langFile: String): Set<String>? {
    return getKeysInFile(File(langFile))
}

fun getKeysInFile(langFile: File): Set<String>? {
    val jsonData = getJsonData(langFile)
    return getKeys(jsonData)
}

fun getKeys(jsonData: JsonObject?): Set<String>? {
    return jsonData?.keys?.filter {
        config.filterKey(it)
    }?.toSet()
}

fun getJsonData(langFile: File): JsonObject? {
    return parseJSON(langFile.inputStream())
}

fun visitJavaFiles(javaDir: String): Set<String> {
    val visitor = Visitor(mutableSetOf())

    File(javaDir).walkTopDown().filter { it.path.endsWith(".java") }.forEach {
        try {
            val statements = StaticJavaParser.parse(it)
            visitor.visit(statements, null)
        } catch (e: Exception) {
            System.err.println("Failed to parse file: ${it.path}\n\n$e")
        }
    }

    return visitor.strings
}