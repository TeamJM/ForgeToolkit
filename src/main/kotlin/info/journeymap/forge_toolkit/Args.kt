package info.journeymap.forge_toolkit

import com.xenomachina.argparser.ArgParser

class Args(parser: ArgParser) {
    val mode by parser.mapping(
        "--clean" to Modes.CLEAN,
        "--populate" to Modes.POPULATE,
        "--sort" to Modes.SORT,
        "--validate" to Modes.VALIDATE,
        help = "Operation mode"
    )
}

class CleanArgs(parser: ArgParser)

class PopulateArgs(parser: ArgParser)

class SortArgs(parser: ArgParser)

class ValidateArgs(parser: ArgParser) {
    val langFile by parser.positional("LANG_FILE", "Path to en_us.json")
    val javaDir by parser.positional("JAVA_DIR", "Path to root directory containing java files")
}
