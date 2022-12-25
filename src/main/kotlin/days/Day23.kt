package days

import kotlin.math.abs


class Day23 : Day(23) {

    data class Elf(
        var location: Pair<Long, Long>,
    )

    val elfLocations = mutableMapOf<String, Elf>()
    val proposedLocations = mutableMapOf<String, MutableList<Elf>>()

    fun getHash(location: Pair<Long, Long>): String {
        return "${location.first},${location.second}"
    }

    fun deparseHash(hash: String): Pair<Long, Long> {
        val (x, y) = hash.split(',').map { it.toLong() }
        return Pair(x, y)
    }

    fun isDirectionFree(location: Pair<Long, Long>, direction: Char): Boolean {
        val (x, y) = location
        when (direction) {
            'N' -> return elfLocations[getHash(Pair(x - 1, y - 1))] == null && elfLocations[getHash(
                Pair(
                    x,
                    y - 1
                )
            )] == null && elfLocations[getHash(Pair(x + 1, y - 1))] == null

            'S' -> return elfLocations[getHash(Pair(x - 1, y + 1))] == null && elfLocations[getHash(
                Pair(
                    x,
                    y + 1
                )
            )] == null && elfLocations[getHash(Pair(x + 1, y + 1))] == null

            'W' -> return elfLocations[getHash(Pair(x - 1, y - 1))] == null && elfLocations[getHash(
                Pair(
                    x - 1,
                    y
                )
            )] == null && elfLocations[getHash(Pair(x - 1, y + 1))] == null

            'E' -> return elfLocations[getHash(Pair(x + 1, y - 1))] == null && elfLocations[getHash(
                Pair(
                    x + 1,
                    y
                )
            )] == null && elfLocations[getHash(Pair(x + 1, y + 1))] == null
        }
        throw Error("Unknown direction $direction")
    }

    fun getProposedLocation(location: Pair<Long, Long>, direction: Char): Pair<Long, Long> {
        val (x, y) = location
        when (direction) {
            'N' -> return Pair(x, y - 1)
            'S' -> return Pair(x, y + 1)
            'W' -> return Pair(x - 1, y)
            'E' -> return Pair(x + 1, y)
        }
        throw Error("Unknown direction $direction")
    }

    fun printSituation() {
        val minX = elfLocations.map { deparseHash(it.key).first }.min()
        val maxX = elfLocations.map { deparseHash(it.key).first }.max()
        val minY = elfLocations.map { deparseHash(it.key).second }.min()
        val maxY = elfLocations.map { deparseHash(it.key).second }.max()
        for (y in -2..9.toLong()) {
            for (x in -3..10.toLong()) {
                if (elfLocations[getHash(Pair(x, y))] == null) print('.') else print('#')
            }
            println()
        }
        println()
    }

    override fun run(part: String): String {
        val lines = read(part).map { it.toList() }

        lines.forEachIndexed { i, line ->
            line.forEachIndexed { j, ch ->
                if (ch == '#') elfLocations.plusAssign(
                    Pair(
                        getHash(Pair(j.toLong(), i.toLong())),
                        Elf(Pair(j.toLong(), i.toLong()))
                    )
                )
            }
        }

        val directionPriority = listOf('N', 'S', 'W', 'E')
        var elfMoved = true
        var round = -1
        while (elfMoved) {
            round += 1
            elfMoved = false
//            printSituation()
            proposedLocations.clear()
            elfLocations.forEach { elf ->
                if (!directionPriority.map { isDirectionFree(elf.value.location, it) }.contains(false)) {
                    proposedLocations[getHash(elf.value.location)] = mutableListOf(elf.value)
                    return@forEach
                }
                elfMoved = true
                for (localDirection in 0..3) {
                    val direction = directionPriority[(localDirection + round) % 4]
                    if (isDirectionFree(elf.value.location, direction)) {
                        val proposedLocation = getProposedLocation(elf.value.location, direction)
                        if (proposedLocations[getHash(proposedLocation)] == null) {
                            proposedLocations[getHash(proposedLocation)] = mutableListOf(elf.value)
                            return@forEach
                        }
                        proposedLocations[getHash((proposedLocation))]!!.plusAssign(elf.value)
                        return@forEach
                    }
                }
                proposedLocations[getHash(elf.value.location)] = mutableListOf(elf.value)
            }
            elfLocations.clear()
            proposedLocations.forEach {
                if (it.value.size == 1) {
                    it.value[0].location = deparseHash(it.key)
                    elfLocations.plusAssign(Pair(it.key, it.value[0]))
                    return@forEach
                }

                it.value.forEach { elf ->
                    elfLocations.plusAssign(Pair(getHash(elf.location), elf))
                }
            }
            if (part == "1" && round == 9) {
                val minX = elfLocations.map { deparseHash(it.key).first }.min()
                val maxX = elfLocations.map { deparseHash(it.key).first }.max()
                val minY = elfLocations.map { deparseHash(it.key).second }.min()
                val maxY = elfLocations.map { deparseHash(it.key).second }.max()
                return (abs(maxX - minX + 1) * abs(maxY - minY + 1) - elfLocations.size).toString()
            }
        }
        return (round + 1).toString()
    }
}