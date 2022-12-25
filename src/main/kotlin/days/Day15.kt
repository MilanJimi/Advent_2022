package days

import kotlin.math.abs


class Point {
    val x: Int
    val y: Int

    constructor(coords: List<Int>) {
        this.x = coords[0]
        this.y = coords[1]
    }
}

private fun findManhattanDistance(a: Point, b: Point): Int {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

class Day15 : Day(15) {
    data class Sensor(
        val location: Point,
        val beacon: Point,
    ) {
        val distance: Int

        init {
            this.distance = findManhattanDistance(location, beacon)
        }

        fun isPointCovered(target: Point): Boolean {
            return findManhattanDistance(this.location, target) <= this.distance
        }

        fun getEdge(): List<Point> {
            val points = mutableListOf<Point>()
            for (i in 0..distance) {
                points.plusAssign(Point(listOf(this.location.x - distance - 1 + i, this.location.y + i)))
                points.plusAssign(Point(listOf(this.location.x - distance - 1 + i, this.location.y - distance - 1 + i)))
                points.plusAssign(Point(listOf(this.location.x + i, this.location.y + i)))
                points.plusAssign(Point(listOf(this.location.x + i, this.location.y - distance - 1 - i)))
            }
            return points.distinct().toList()
        }
    }


    override fun run(part: String): String {
        val lines = read(part).map { line ->
            line.filter { it.isDigit() || listOf(',', ':', '-').contains(it) }
                .split(":")
                .map { c ->
                    c.split(",")
                        .map { i -> i.toInt() }
                }
        }

        val sensors = lines.map { Sensor(Point(it[0]), Point(it[1])) }
        if (part == "1") {
            val xPoints = sensors.flatMap { listOf(it.location.x, it.beacon.x) }
            val (xMin, xMax) = Pair(xPoints.min(), xPoints.max())
            var excludedPoints = 0
            val rowOfInterest = if (part == "test") 10 else 2000000
            for (i in xMin * 6..xMax * 6) {
                val point = Point(listOf(i, rowOfInterest))
                run breaking@{
                    sensors.forEach {
                        if (point.x == it.beacon.x && point.y == it.beacon.y) return@breaking
                    }
                    sensors.forEach {
                        if (it.isPointCovered(point)) {
                            excludedPoints++
                            return@breaking
                        }
                    }
                }
            }
            return excludedPoints.toString()
        }
        val maxRange = if (part == "test") 20 else 4000000
        val possiblePoints = sensors.flatMap { it.getEdge() }
        possiblePoints
            .filter { it.x in 0..maxRange && it.y in 0..maxRange }
            .forEach pointForEach@{ p ->
                sensors.forEach { s -> if (s.isPointCovered(p)) return@pointForEach }
                println("${p.x} ${p.y}")
                return (p.x.toBigInteger() * 4000000.toBigInteger() + p.y.toBigInteger()).toString()
            }
        return "Error"
    }
}