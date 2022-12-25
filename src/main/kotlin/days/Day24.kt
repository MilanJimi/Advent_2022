package days

import java.util.*

class Day24 : Day(24) {


    override fun run(part: String): String {
        val lines = read(part).map { it.toList() }
        val startPoint = Pair(0, lines[0].indexOf('.'))
        val endPoint = Pair(lines.size - 1, lines[lines.size - 1].indexOf('.'))
        val rowSize = lines[0].size - 2
        val colSize = lines.size - 2

        data class Point(
            var rowBlizzardOnTurn: MutableList<Int> = mutableListOf(),
            var colBlizzardOnTurn: MutableList<Int> = mutableListOf()
        ) {
            fun isPassable(turn: Int): Boolean {
                if (rowBlizzardOnTurn.contains(turn % rowSize) || colBlizzardOnTurn.contains(turn % colSize))
                    return false
                return true
            }
        }

        val points = List(colSize) { List(rowSize) { Point() } }
        lines.forEachIndexed { i, row ->
            row.forEachIndexed { j, ch ->
                when (ch) {
                    '>' -> for (x in 0 until rowSize)
                        points[i - 1][(x + j - 1) % rowSize].rowBlizzardOnTurn.plusAssign(x)

                    '<' -> for (x in 0 until rowSize)
                        points[i - 1][(rowSize + j - 1 - x) % rowSize].rowBlizzardOnTurn.plusAssign(x)

                    '^' -> for (y in 0 until colSize)
                        points[(colSize + i - 1 - y) % colSize][j - 1].colBlizzardOnTurn.plusAssign(y)

                    'v' -> for (y in 0 until colSize)
                        points[(colSize + i - 1 + y) % colSize][j - 1].colBlizzardOnTurn.plusAssign(y)
                }
            }
        }

        points.forEach { row ->
            row.forEach {
                it.rowBlizzardOnTurn = it.rowBlizzardOnTurn.distinct().sorted().toMutableList()
                it.colBlizzardOnTurn = it.colBlizzardOnTurn.distinct().sorted().toMutableList()
            }
        }

        data class Step(
            val turn: Int,
            val location: Pair<Int, Int>,
        ) : Comparable<Step> {
            val priority: Int

            init {
                priority = turn + endPoint.first - location.first + endPoint.second - location.second
            }

            fun getHash(): String {
                return "$turn|$location"
            }

            override fun compareTo(other: Step): Int {
                return priority - other.priority
            }
        }

        val queue: PriorityQueue<Step> = PriorityQueue<Step>()
        queue.add(Step(0, startPoint))
        val seenSteps = mutableMapOf<String, Boolean>()
        var minimumSteps: Int? = null

        while (queue.size > 0) {
            val thisStep = queue.remove()
            if (seenSteps[thisStep.getHash()] !== null)
                continue
            seenSteps[thisStep.getHash()] = true
            val (row, col) = thisStep.location
            if (minimumSteps !== null && minimumSteps <= thisStep.turn) continue
            if (row == endPoint.first && col == endPoint.second) {
                minimumSteps = thisStep.turn
                continue
            }

            val nextTurn = thisStep.turn + 1
            if (row + 1 == endPoint.first && col == endPoint.second) {
                queue.add(Step(nextTurn, endPoint))
                continue
            }
            if (row > 0 && points[row - 1][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, thisStep.location))
            if (row == startPoint.first && col == startPoint.second) {
                queue.add(Step(nextTurn, thisStep.location))
                if (points[1][1].isPassable(nextTurn))
                    queue.add(Step(nextTurn, Pair(1, 1)))
                continue
            }
            if (row > 1 && points[row - 2][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row - 1, col)))
            if (row < colSize && points[row][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row + 1, col)))
            if (col > 1 && points[row - 1][col - 2].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row, col - 1)))
            if (col < rowSize && points[row - 1][col].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row, col + 1)))
        }

        queue.clear()
        seenSteps.clear()

        data class StepBack(
            val turn: Int,
            val location: Pair<Int, Int>,
        ) : Comparable<StepBack> {
            val priority: Int

            init {
                priority = turn + location.first - startPoint.first + location.second - startPoint.second
            }

            fun getHash(): String {
                return "$turn|$location"
            }

            override fun compareTo(other: StepBack): Int {
                return priority - other.priority
            }
        }

        val queueBack: PriorityQueue<StepBack> = PriorityQueue<StepBack>()
        queueBack.add(StepBack(minimumSteps!!, endPoint))

        var minimumStepsBack: Int? = null
        while (queueBack.size > 0) {
            val thisStep = queueBack.remove()
            if (seenSteps[thisStep.getHash()] !== null)
                continue
            seenSteps[thisStep.getHash()] = true
            val (row, col) = thisStep.location
            if ((minimumStepsBack !== null) && (minimumStepsBack <= thisStep.turn)) continue
            if (row == startPoint.first && col == startPoint.second) {
                minimumStepsBack = thisStep.turn
                continue
            }

            val nextTurn = thisStep.turn + 1
            if (row - 1 == startPoint.first && col == startPoint.second) {
                queueBack.add(StepBack(nextTurn, startPoint))
                continue
            }
            if ((0..colSize).contains(row) && points[row - 1][col - 1].isPassable(nextTurn))
                queueBack.add(StepBack(nextTurn, thisStep.location))
            if (row == endPoint.first && col == endPoint.second) {
                queueBack.add(StepBack(nextTurn, thisStep.location))
                if (points[endPoint.first - 2][endPoint.second - 1].isPassable(nextTurn))
                    queueBack.add(StepBack(nextTurn, Pair(endPoint.first - 1, endPoint.second)))
                continue
            }
            if (row > 1 && points[row - 2][col - 1].isPassable(nextTurn))
                queueBack.add(StepBack(nextTurn, Pair(row - 1, col)))
            if (row < colSize && points[row][col - 1].isPassable(nextTurn))
                queueBack.add(StepBack(nextTurn, Pair(row + 1, col)))
            if (col > 1 && points[row - 1][col - 2].isPassable(nextTurn))
                queueBack.add(StepBack(nextTurn, Pair(row, col - 1)))
            if (col < rowSize && points[row - 1][col].isPassable(nextTurn))
                queueBack.add(StepBack(nextTurn, Pair(row, col + 1)))
        }

        queue.add(Step(minimumStepsBack!!, startPoint))
        seenSteps.clear()
        var minimumStepsForwardAgain: Int? = null

        while (queue.size > 0) {
            val thisStep = queue.remove()
            if (seenSteps[thisStep.getHash()] !== null)
                continue
            seenSteps[thisStep.getHash()] = true
            val (row, col) = thisStep.location
            if (minimumStepsForwardAgain !== null && minimumStepsForwardAgain <= thisStep.turn) continue
            if (row == endPoint.first && col == endPoint.second) {
                minimumStepsForwardAgain = thisStep.turn
                continue
            }

            val nextTurn = thisStep.turn + 1
            if (row + 1 == endPoint.first && col == endPoint.second) {
                queue.add(Step(nextTurn, endPoint))
                continue
            }
            if (row > 0 && points[row - 1][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, thisStep.location))
            if (row == startPoint.first && col == startPoint.second) {
                queue.add(Step(nextTurn, thisStep.location))
                if (points[1][1].isPassable(nextTurn))
                    queue.add(Step(nextTurn, Pair(1, 1)))
                continue
            }
            if (row > 1 && points[row - 2][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row - 1, col)))
            if (row < colSize && points[row][col - 1].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row + 1, col)))
            if (col > 1 && points[row - 1][col - 2].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row, col - 1)))
            if (col < rowSize && points[row - 1][col].isPassable(nextTurn))
                queue.add(Step(nextTurn, Pair(row, col + 1)))
        }

        queue.clear()
        seenSteps.clear()

        return minimumStepsForwardAgain.toString()
    }
}