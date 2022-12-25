package days

class Day4 : Day(4) {
    fun splitRange(range: String): Pair<Int, Int> {
        val s = range.split("-")
        return Pair(s[0].toInt(), s[1].toInt())
    }

    fun splitLine(line: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val first = line.split(",")[0]
        val second = line.split(",")[1]
        return Pair(splitRange(first), splitRange(second))
    }

    fun isContainedIn(testRange: Pair<Int, Int>, inRange: Pair<Int, Int>): Boolean {
        return testRange.first >= inRange.first && testRange.second <= inRange.second
    }

    fun Int.isInRange(range: Pair<Int, Int>): Boolean {
        return this >= range.first && this <= range.second
    }

    fun hasOverlap(range1: Pair<Int, Int>, range2: Pair<Int, Int>): Boolean {
        return range2.first.isInRange(range1) || range1.first.isInRange(range2)
    }

    override fun run(part: String): String {
        val lines = this.read(part)
        var sum = 0
        lines.forEach { line ->
            val split = splitLine(line)
            if (part == "1") {
                if (isContainedIn(split.first, split.second) || isContainedIn(split.second, split.first)) sum += 1
                return@forEach
            }
            if (hasOverlap(split.first, split.second)) sum += 1
        }
        return sum.toString()
    }
}