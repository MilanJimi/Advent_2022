package days

import readFileAsLinesUsingUseLines


fun getShapeScore1(shape: String): Int {
    return when (shape) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> throw Error("Unknown shape $shape")
    }
}

fun getMatchScore1(opp: String, you: String): Int {
    val match = opp + you
    if (listOf("AX", "BY", "CZ").contains(match)) return 3
    if (listOf("CX", "AY", "BZ").contains(match)) return 6
    if (listOf("BX", "CY", "AZ").contains(match)) return 0
    throw Error("Unknown match $match")
}

fun getMatchScore2(outcome: String): Int {
    return when (outcome) {
        "X" -> 0
        "Y" -> 3
        "Z" -> 6
        else -> throw Error("Unknown outcome $outcome")
    }
}

fun getShapeScore2(opp: String, you: String): Int {
    val match = opp + you
    if (listOf("BX", "AY", "CZ").contains(match)) return 1
    if (listOf("CX", "AZ", "BY").contains(match)) return 2
    if (listOf("BZ", "CY", "AX").contains(match)) return 3
    throw Error("Unknown match $match")
}

fun runDay2(part: String): Int {
    val path = if (part == "test") "2/test.in" else "2/full.in"
    val lines = readFileAsLinesUsingUseLines(path)
    var score = 0
    lines.forEach {
        val opp = it.split(" ")[0]
        val you = it.split(" ")[1]
        if (part == "1") {
            score += getMatchScore1(opp, you) + getShapeScore1(you)
            return@forEach
        }
        score += getMatchScore2(you) + getShapeScore2(opp, you)
    }

    return score
}
