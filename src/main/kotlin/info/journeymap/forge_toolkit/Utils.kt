package info.journeymap.forge_toolkit

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File
import java.io.InputStream

fun parseJSON(data: InputStream): JsonObject? {
    return Parser.default().parse(data) as JsonObject
}

fun writeJSON(data: JsonObject, langFile: File) {
    langFile.writeText(data.toJsonString(true))
}