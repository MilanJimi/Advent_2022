package days

import readFileAsLinesUsingUseLines

abstract class Day(val day: Int) {


    fun read(input: String): List<String> {
        val path = if (input == "test") "${this.day}/test.in" else "${this.day}/full.in"
        return readFileAsLinesUsingUseLines(path)
    }

    abstract fun run(part: String): String

    open fun <T> log(data: T) {
        println(data)
    }
}