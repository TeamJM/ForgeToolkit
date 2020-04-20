package info.journeymap.forge_toolkit

import com.beust.klaxon.Klaxon
import java.io.File

data class Config(
    val ignored_prefixes: Array<String> = arrayOf("_", "jm.common.location_", "jm.webmap."),
    val ignored_patterns: Array<String> = arrayOf()
) {
    fun filterPrefixes(key: String): Boolean {
        for (prefix in this.ignored_prefixes) {
            if (key.startsWith(prefix)) {
                return false
            }
        }

        return true
    }

    fun filterPatterns(key: String): Boolean {
        for (pattern in this.ignored_patterns) {
            if (key.matches(pattern.toRegex())) {
                return false
            }
        }

        return true
    }

    fun filterKey(key: String): Boolean {
        return this.filterPrefixes(key) && this.filterPatterns(key)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Config

        if (!ignored_prefixes.contentEquals(other.ignored_prefixes)) return false
        if (!ignored_patterns.contentEquals(other.ignored_patterns)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ignored_prefixes.contentHashCode()
        result = 31 * result + ignored_patterns.contentHashCode()
        return result
    }
}

fun getConfig(): Config {
    val config: Config?
    val file = File(".forge-toolkit.json")

    if (!file.exists()) {
        return Config()
    }

    try {
        config = Klaxon().parse<Config>(file)

        if (config == null) {
            println("Unable to load .forge-toolkit.json - using default options.")

            return Config()
        }

        return config
    } catch (e: Exception) {
        println("Unable to load .forge-toolkit.json: $e")
        println("Using default options.")

        return Config()
    }
}
