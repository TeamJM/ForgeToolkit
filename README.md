# ForgeToolkit

This repository contains a small toolkit that helps us to work with the main JourneyMap
mod project in a development environment.

For all options, simply run the release JAR with `--help`.

Please bear in mind that this was built specifically for the JourneyMap project, and
will need customising if you want to use it in your project. Simply make your changes
and `gradlew build` to rebuild.

## Features

Note: This project currently operates only on Java files, and not Kotlin files.

* When given the path to the primary JSON lang file (en_US.json) and the root directory
    of the Java project, a list of translation strings present in the JSON file but not
    present as a string literal in the Java project will be printed.
 