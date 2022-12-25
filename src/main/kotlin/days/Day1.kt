package days

import readFileAsLinesUsingUseLines

fun runDay1(part: String): Int {
    val path = if (part == "test") "1/1.test.in" else "1/1.full.in"
    val lines = readFileAsLinesUsingUseLines(path)
    val elves = mutableListOf<Int>()
    var sum = 0
    var max = 0
    lines.forEach {
        if (it == "") {
            elves.add(sum)
            if (sum > max) max = sum
            sum = 0
            return@forEach
        }
        sum += it.toInt()
    }
    if (part == "1" || part == "test")
        return max
    val sortedLines = elves.sortedDescending()
    return sortedLines[0] + sortedLines[1] + sortedLines[2]
}
