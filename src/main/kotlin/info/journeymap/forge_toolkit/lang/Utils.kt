package info.journeymap.forge_toolkit.lang

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import com.github.javaparser.StaticJavaParser
import info.journeymap.forge_toolkit.parseJSON
import java.io.File

// TODO: Make this configurable
val IGNORE_PREFIXES = arrayOf("_", "jm.common.location_", "jm.webmap.")

fun getKeysInFile(langFile: String): Set<String>? {
    return getKeysInFile(File(langFile))
}

fun getKeysInFile(langFile: File): Set<String>? {
    val jsonData = getJsonData(langFile)
    return getKeys(jsonData)
}

fun getKeys(jsonData: JsonObject?): Set<String>? {
    return jsonData?.keys?.filter {
        filterKey(it)
    }?.toSet()
}

fun getJsonData(langFile: File): JsonObject? {
    return parseJSON(langFile.inputStream())
}

fun filterKey(key: String): Boolean {
    for (prefix in IGNORE_PREFIXES) {
        if (key.startsWith(prefix)) {
            return false
        }
    }

    return true
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