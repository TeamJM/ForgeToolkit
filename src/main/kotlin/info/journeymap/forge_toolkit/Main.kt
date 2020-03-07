package info.journeymap.forge_toolkit

import com.github.javaparser.StaticJavaParser
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import java.io.File

fun main(args: Array<String>) = mainBody {
    var translationKeys: MutableSet<String>? = null
    var quotedStrings: MutableSet<String>

    val visitor = Visitor(mutableSetOf<String>())

    ArgParser(args).parseInto(::Args).run {
        val jsonData = parseJSON(File(this.langFile).inputStream())

        try {
            translationKeys = jsonData?.keys?.filter {
                it != "_comment" &&
                        !it.startsWith("jm.common.location_") &&
                        !it.startsWith("jm.webmap.")
            }?.toMutableSet()
        } catch (e: Exception) {
            System.err.println(e)
        }

        if (translationKeys == null) {
            return@mainBody println("Failed to load translations data.")
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

        val difference = translationKeys!! - quotedStrings

        if (difference.isEmpty()) {
            println("All translation keys are used.")
        } else {
            println("Found ${difference.size} (of ${translationKeys!!.size}) unused translation keys:\n")

            difference.forEach { println(it) }
        }
    }
}
