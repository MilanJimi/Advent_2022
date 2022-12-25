package days

import java.lang.Integer.min
import java.lang.Math.max

class Day14 : Day(14) {

    fun <T> MutableList<T>.prependAll(elements: List<T>) {
        addAll(0, elements)
    }

    override fun run(part: String): String {
        val lines = read(part).map { line -> line.split(" -> ").map { it.split(",").map { num -> num.toInt() } } }
        val minH = lines.flatten().map { it[0] }.min()
        val sizeH = lines.flatten().map { it[0] }.max() - minH
        val sizeV = lines.flatten().map { it[1] }.max()
        val walls = MutableList(sizeH + 1) { MutableList(sizeV + 1) { false } }
        lines.forEach { line ->
            line.forEachIndexed { i, segment ->
                if (i == 0) return@forEachIndexed
                if (segment[0] == line[i - 1][0]) {
                    for (j in min(segment[1], line[i - 1][1])..max(segment[1], line[i - 1][1])) {
                        walls[segment[0] - minH][j] = true
                    }
                    return@forEachIndexed
                }
                if (segment[1] == line[i - 1][1])
                    for (j in min(segment[0], line[i - 1][0])..max(segment[0], line[i - 1][0]))
                        walls[j - minH][segment[1]] = true
            }
        }
        val originSandH = 500 - minH
        var sandTotal = 0

        if (part == "1") {
            var outOfBounds = false
            while (!outOfBounds) {
                sandTotal += 1
                var x = originSandH
                for (y in 0..sizeV) {
                    if (y == sizeV) {
                        outOfBounds = true
                        break
                    }
                    if (!walls[x][y + 1]) continue
                    if (x == 0) {
                        outOfBounds = true
                        break
                    }
                    if (!walls[x - 1][y + 1]) {
                        x--
                        continue
                    }
                    if (x == sizeH - 1) {
                        outOfBounds = true
                        break
                    }
                    if (!walls[x + 1][y + 1]) {
                        x++
                        continue
                    }
                    walls[x][y] = true
                    break
                }
            }

            return (sandTotal - 1).toString()
        }

        val padding = sizeV + 1
        walls.prependAll(MutableList(padding) { MutableList(padding) { false } })
        walls.plusAssign(MutableList(padding) { MutableList(padding) { false } })
        walls.forEach { x -> x.plusAssign(listOf(false, true)) }
        val sand = MutableList(walls.size) { MutableList(walls[0].size) { false } }

        fun fillWithSand(x: Int, y: Int) {
            sandTotal++
            sand[x][y] = true
            if (!walls[x][y + 1] && !sand[x][y + 1]) fillWithSand(x, y + 1)
            if (!walls[x - 1][y + 1] && !sand[x - 1][y + 1]) fillWithSand(x - 1, y + 1)
            if (!walls[x + 1][y + 1] && !sand[x + 1][y + 1]) fillWithSand(x + 1, y + 1)
        }

        fillWithSand(originSandH + padding, 0)
        return sandTotal.toString()
    }
}