package info.journeymap.forge_toolkit.lang

import com.github.javaparser.StaticJavaParser
import info.journeymap.forge_toolkit.parseJSON
import java.io.File

// TODO: Make this configurable
val IGNORE_PREFIXES = arrayOf("_", "jm.common.location_", "jm.webmap.")

fun getKeysInFile(langFile: String): Set<String>? {
    val jsonData = parseJSON(File(langFile).inputStream())

    return jsonData?.keys?.filter {
        filterKey(it)
    }?.toSet()
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