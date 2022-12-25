package days

import kotlin.math.abs

class Day9 : Day(9) {

    override fun run(part: String): String {
        val ropeLength = if (part == "1") 2 else 10
        var rope = MutableList(ropeLength) { Pair(0, 0) }
        val visited = mutableMapOf<String, Boolean>()
        val lines = read(part)
        lines.forEach { line ->
            val direction = line.split(" ")[0]
            val distance = line.split(" ")[1].toInt()
            for (i in 0 until distance) {
                when (direction) {
                    "R" -> rope[0] = Pair(rope[0].first + 1, rope[0].second)
                    "D" -> rope[0] = Pair(rope[0].first, rope[0].second - 1)
                    "L" -> rope[0] = Pair(rope[0].first - 1, rope[0].second)
                    "U" -> rope[0] = Pair(rope[0].first, rope[0].second + 1)
                }
                for (j in 1 until ropeLength) {
                    val diffVector = Pair(rope[j - 1].first - rope[j].first, rope[j - 1].second - rope[j].second)
                    if (abs(diffVector.first) > 1 && abs(diffVector.second) > 1) {
                        rope[j] = Pair(rope[j].first + diffVector.first / 2, rope[j].second + diffVector.second / 2)
                        continue
                    }
                    if (abs(diffVector.first) > 1)
                        rope[j] = Pair(rope[j].first + diffVector.first / 2, rope[j].second + diffVector.second)
                    if (abs(diffVector.second) > 1)
                        rope[j] = Pair(rope[j].first + diffVector.first, rope[j].second + diffVector.second / 2)
                }
                val tail = rope[ropeLength - 1]
                if (visited["${tail.first}/${tail.second}"] == null)
                    visited["${tail.first}/${tail.second}"] = true
            }
        }
        return visited.keys.size.toString()
    }
}