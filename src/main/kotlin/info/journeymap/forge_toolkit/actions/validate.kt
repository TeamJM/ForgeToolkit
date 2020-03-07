package info.journeymap.forge_toolkit.actions

import com.github.javaparser.StaticJavaParser
import com.xenomachina.argparser.ArgParser
import info.journeymap.forge_toolkit.*
import java.io.File

fun validate(args: Array<String>) {
    ArgParser(args.slice(1 until args.size).toTypedArray()).parseInto(::ValidateArgs).run {
        var translationKeys: MutableSet<String>? = null
        val quotedStrings: MutableSet<String>

        val visitor = Visitor(mutableSetOf<String>())
        val jsonData = parseJSON(File(this.langFile).inputStream())

        try {
            translationKeys = jsonData?.keys?.filter {
                it != "_comment" &&
                ! it.startsWith("jm.common.location_") &&
                ! it.startsWith("jm.webmap.")
            }?.toMutableSet()
        } catch (e: Exception) {
            System.err.println(e)
        }

        if (translationKeys == null) {
            return println("Failed to load translations data.")
        }

        File(this.javaDir).walkTopDown().filter { it.path.endsWith(".java") }.forEach {
            try {
                val statements = StaticJavaParser.parse(it)
                visitor.visit(statements, null)
            } catch (e: Exception) {
                System.err.println("Failed to parse file: ${it.path}\n\n$e")
            }
        }

        quotedStrings = visitor.strings

        val difference = translationKeys - quotedStrings

        if (difference.isEmpty()) {
            println("All translation keys are used.")
        } else {
            println("Found ${difference.size} (of ${translationKeys.size}) unused translation keys:\n")

            difference.forEach { println(it) }
        }
    }
}
