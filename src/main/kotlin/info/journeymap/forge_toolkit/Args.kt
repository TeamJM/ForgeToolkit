package info.journeymap.forge_toolkit

import com.xenomachina.argparser.ArgParser

class Args(parser: ArgParser) {
    val langFile by parser.positional("LANG_FILE", "Path to en_us.json")
    val javaDir by parser.positional("JAVA_DIR", "Path to root directory containing java files")
}