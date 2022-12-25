package days

import java.util.*

class Day12 : Day(12) {

    override fun run(part: String): String {
        val lines = read(part)
        val sizeX = lines.size
        val sizeY = lines[0].length
        val height = MutableList(sizeX) { List(sizeY) { -1 } }
        val steps = MutableList(sizeX) { MutableList(sizeY) { -1 } }
        var start = Pair(-1, -1)
        var end = Pair(-1, -1)
        lines.forEachIndexed { i, line ->
            height[i] = (line.toList().mapIndexed { j, ch ->
                if (ch == 'S') {
                    start = Pair(i, j)
                    return@mapIndexed 0
                }
                if (ch == 'E') {
                    end = Pair(i, j)
                    return@mapIndexed 'z'.code - 'a'.code
                }
                ch.code - 'a'.code
            })
        }

        if (part == "1") steps[start.first][start.second] = 0 else steps[end.first][end.second] = 0
        val queue: Queue<Pair<Int, Int>> = LinkedList(listOf(if (part == "1") start else end))

        while (queue.size > 0) {
            val (x, y) = queue.remove()
            val currentHeight = height[x][y]
            val currentSteps = steps[x][y]

            if (part == "1") {
                if (Pair(x, y) == end) return currentSteps.toString()
                if (x > 0 && height[x - 1][y] - currentHeight <= 1 && steps[x - 1][y] == -1) {
                    steps[x - 1][y] = currentSteps + 1
                    queue.add(Pair(x - 1, y))
                }
                if (x < sizeX - 1 && height[x + 1][y] - currentHeight <= 1 && steps[x + 1][y] == -1) {
                    steps[x + 1][y] = currentSteps + 1
                    queue.add(Pair(x + 1, y))
                }
                if (y > 0 && height[x][y - 1] - currentHeight <= 1 && steps[x][y - 1] == -1) {
                    steps[x][y - 1] = currentSteps + 1
                    queue.add(Pair(x, y - 1))
                }
                if (y < sizeY - 1 && height[x][y + 1] - currentHeight <= 1 && steps[x][y + 1] == -1) {
                    steps[x][y + 1] = currentSteps + 1
                    queue.add(Pair(x, y + 1))
                }
            } else {
                if (currentHeight == 0) return currentSteps.toString()
                if (x > 0 && height[x - 1][y] - currentHeight >= -1 && steps[x - 1][y] == -1) {
                    steps[x - 1][y] = currentSteps + 1
                    queue.add(Pair(x - 1, y))
                }
                if (x < sizeX - 1 && height[x + 1][y] - currentHeight >= -1 && steps[x + 1][y] == -1) {
                    steps[x + 1][y] = currentSteps + 1
                    queue.add(Pair(x + 1, y))
                }
                if (y > 0 && height[x][y - 1] - currentHeight >= -1 && steps[x][y - 1] == -1) {
                    steps[x][y - 1] = currentSteps + 1
                    queue.add(Pair(x, y - 1))
                }
                if (y < sizeY - 1 && height[x][y + 1] - currentHeight >= -1 && steps[x][y + 1] == -1) {
                    steps[x][y + 1] = currentSteps + 1
                    queue.add(Pair(x, y + 1))
                }
            }
        }
        return steps[end.first][end.second].toString()
    }
}