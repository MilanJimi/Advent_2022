fun readFileAsLinesUsingUseLines(fileName: String): List<String> =
    object {}.javaClass.getResource(fileName).readText().split("\n")