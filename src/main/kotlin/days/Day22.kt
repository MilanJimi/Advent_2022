package days


class Day22 : Day(22) {

    fun getNewDirection(oldDirection: Int, rotation: String): Int {
        return if (rotation == "R") (oldDirection + 1) % 4 else (oldDirection + 3) % 4
    }

    fun getNewDirection(oldDirection: Int, rotation: Int): Int {
        return (oldDirection + 4 + rotation) % 4
    }


    data class TeleportInformation(
        val teleportTo: Pair<Int, Int>,
        val rotateBy: Int
    )

    override fun run(part: String): String {
        val lines = read(part).joinToString(",").split(",,")
        val plan = lines[0].split(',').map { it.toList() }
        val path = lines[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)".toRegex());
        if (part == "1") {
            var rangesY = MutableList(plan.map { it.size }.max()) { Pair(-1, -1) }
            val rangesX = plan.mapIndexed { i, l ->
                var start = -1
                var end = -1
                for (j in l.indices) {
                    if (l[j] != ' ' && start == -1)
                        start = j
                    if (l[j] == ' ' && start != -1) {
                        end = j
                        break
                    }
                }
                end = if (end == -1) l.size else end
                Pair(start, end)
            }

            for (i in rangesY.indices) {
                var start = -1
                var end = -1
                for (j in plan.indices) {
                    if (plan[j][i] != ' ' && start == -1)
                        start = j
                    if (plan[j][i] == ' ' && start > -1) {
                        end = j
                        break
                    }
                }
                rangesY[i] = Pair(start, end)
            }

            rangesY = rangesY.map { if (it.second == -1) Pair(it.first, plan.size) else it }.toMutableList()

            var position = Pair(0, rangesX[0].first)
            var direction = 0 // 0 = R, 1 = D, 2 = L, 3 = U

            path.forEach { instruction ->
                if (instruction.matches("[0-9]+".toRegex())) {
                    val distance = instruction.toInt()
                    for (i in 0 until distance) {
                        when (direction) {
                            0 -> {
                                var proposedPosition = Pair(position.first, position.second + 1)
                                if (proposedPosition.second == rangesX[position.first].second)
                                    proposedPosition =
                                        Pair(proposedPosition.first, rangesX[proposedPosition.first].first)
                                if (plan[proposedPosition.first][proposedPosition.second] == '#')
                                    break
                                position = proposedPosition
                            }

                            1 -> {
                                var proposedPosition = Pair(position.first + 1, position.second)
                                if (proposedPosition.first == rangesY[position.second].second)
                                    proposedPosition =
                                        Pair(rangesY[proposedPosition.second].first, proposedPosition.second)
                                if (plan[proposedPosition.first][proposedPosition.second] == '#')
                                    break
                                position = proposedPosition
                            }

                            2 -> {
                                var proposedPosition = Pair(position.first, position.second - 1)
                                if (proposedPosition.second == rangesX[position.first].first - 1)
                                    proposedPosition =
                                        Pair(proposedPosition.first, rangesX[proposedPosition.first].second - 1)
                                if (plan[proposedPosition.first][proposedPosition.second] == '#')
                                    break
                                position = proposedPosition
                            }

                            3 -> {
                                var proposedPosition = Pair(position.first - 1, position.second)
                                if (proposedPosition.first == rangesY[position.second].first - 1)
                                    proposedPosition =
                                        Pair(rangesY[proposedPosition.second].second - 1, proposedPosition.second)
                                if (plan[proposedPosition.first][proposedPosition.second] == '#')
                                    break
                                position = proposedPosition
                            }
                        }
                    }
                } else direction = getNewDirection(direction, instruction)

            }

            return ((position.first + 1) * 1000 + (position.second + 1) * 4 + direction).toString()
        }

        val tileSize = if (part == "test") plan[0].size / 4 else plan[0].size / 3

        fun getNewRelativeLocation(oldLocation: Pair<Int, Int>, direction: Int, rotation: Int): Pair<Int, Int> {
            val maxRange = tileSize - 1
            when (direction) {
                0 -> when (rotation) {
                    0 -> return Pair(oldLocation.first, 0)
                    1 -> return Pair(0, maxRange - oldLocation.first)
                    2 -> return Pair(maxRange - oldLocation.first, maxRange)
                    3 -> return Pair(maxRange, oldLocation.first)
                }

                1 -> when (rotation) {
                    0 -> return Pair(0, oldLocation.second)
                    1 -> return Pair(oldLocation.second, maxRange)
                    2 -> return Pair(maxRange, maxRange - oldLocation.second)
                    3 -> return Pair(maxRange - oldLocation.second, 0)
                }

                2 -> when (rotation) {
                    0 -> return Pair(oldLocation.first, maxRange)
                    1 -> return Pair(maxRange, maxRange - oldLocation.first)
                    2 -> return Pair(maxRange - oldLocation.first, 0)
                    3 -> return Pair(0, oldLocation.first)
                }

                3 -> when (rotation) {
                    0 -> return Pair(maxRange, oldLocation.second)
                    1 -> return Pair(oldLocation.second, 0)
                    2 -> return Pair(0, maxRange - oldLocation.second)
                    3 -> return Pair(maxRange - oldLocation.second, maxRange)
                }
            }
            throw Error("Unknown Direction $direction or rotation $rotation")
        }


        val net = if (part == "test") List(3) { List(4) { MutableList(tileSize) { List(tileSize) { ' ' } } } }
        else List(4) { List(3) { MutableList(tileSize) { List(tileSize) { ' ' } } } }
        plan.forEachIndexed { i, line ->
            val chunks = line.chunked(line.size / net[0].size)
            chunks.forEachIndexed { j, chunk -> net[i / net[0][0].size][j][i % tileSize] = chunk }
        }

        val startingPositionReal = Pair(0, lines[0].indexOfFirst { it == '.' })
        var currentTile = Pair(0, startingPositionReal.second / tileSize)
        var currentRelativePosition = Pair(0, startingPositionReal.second % tileSize)
        var direction = 0

        val teleportInfoTable =
            if (part == "test")
                listOf(
                    listOf(
                        null,
                        null,
                        listOf(
                            TeleportInformation(Pair(2, 3), 2),
                            TeleportInformation(Pair(1, 2), 0),
                            TeleportInformation(Pair(1, 1), 3),
                            TeleportInformation(Pair(1, 0), 2)
                        ),
                        null
                    ),
                    listOf(
                        listOf(
                            TeleportInformation(Pair(1, 1), 0),
                            TeleportInformation(Pair(2, 2), 2),
                            TeleportInformation(Pair(2, 3), 1),
                            TeleportInformation(Pair(0, 2), 2)
                        ),
                        listOf(
                            TeleportInformation(Pair(1, 2), 0),
                            TeleportInformation(Pair(2, 2), 3),
                            TeleportInformation(Pair(1, 0), 0),
                            TeleportInformation(Pair(0, 2), 1)
                        ),
                        listOf(
                            TeleportInformation(Pair(2, 3), 1),
                            TeleportInformation(Pair(2, 2), 0),
                            TeleportInformation(Pair(1, 1), 0),
                            TeleportInformation(Pair(0, 2), 0)
                        ),
                        null
                    ),
                    listOf(
                        null,
                        null,
                        listOf(
                            TeleportInformation(Pair(2, 3), 0),
                            TeleportInformation(Pair(1, 0), 2),
                            TeleportInformation(Pair(1, 1), 1),
                            TeleportInformation(Pair(1, 2), 0)
                        ),
                        listOf(
                            TeleportInformation(Pair(0, 2), 2),
                            TeleportInformation(Pair(1, 0), 3),
                            TeleportInformation(Pair(2, 2), 0),
                            TeleportInformation(Pair(1, 2), 3)
                        ),
                    ),
                )
            else listOf(
                listOf(
                    null,
                    listOf(
                        TeleportInformation(Pair(0, 2), 0),
                        TeleportInformation(Pair(1, 1), 0),
                        TeleportInformation(Pair(2, 0), 2),
                        TeleportInformation(Pair(3, 0), 1)
                    ),
                    listOf(
                        TeleportInformation(Pair(2, 1), 2),
                        TeleportInformation(Pair(1, 1), 1),
                        TeleportInformation(Pair(0, 1), 0),
                        TeleportInformation(Pair(3, 0), 0)
                    ),
                ),
                listOf(
                    null,
                    listOf(
                        TeleportInformation(Pair(0, 2), 3),
                        TeleportInformation(Pair(2, 1), 0),
                        TeleportInformation(Pair(2, 0), 3),
                        TeleportInformation(Pair(0, 1), 0)
                    ),
                    null,
                ),
                listOf(
                    listOf(
                        TeleportInformation(Pair(2, 1), 0),
                        TeleportInformation(Pair(3, 0), 0),
                        TeleportInformation(Pair(0, 1), 2),
                        TeleportInformation(Pair(1, 1), 1)
                    ),
                    listOf(
                        TeleportInformation(Pair(0, 2), 2),
                        TeleportInformation(Pair(3, 0), 1),
                        TeleportInformation(Pair(2, 0), 0),
                        TeleportInformation(Pair(1, 1), 0)
                    ),
                    null,
                ),
                listOf(
                    listOf(
                        TeleportInformation(Pair(2, 1), 3),
                        TeleportInformation(Pair(0, 2), 0),
                        TeleportInformation(Pair(0, 1), 3),
                        TeleportInformation(Pair(2, 0), 0)
                    ),
                    null,
                    null,
                ),
            )

        path.forEach {
            println("${(currentRelativePosition.first + 1 + tileSize * currentTile.first)} ${(currentRelativePosition.second + 1 + tileSize * currentTile.second)} $direction $it")
            if (it.matches("[RL]".toRegex())) direction = getNewDirection(direction, it)
            else {
                for (i in 0 until it.toInt()) {
                    var proposedPosition = currentRelativePosition
                    var proposedNewDirection = direction
                    var proposedNewTile = currentTile
                    when (direction) {
                        0 -> proposedPosition = Pair(currentRelativePosition.first, currentRelativePosition.second + 1)
                        1 -> proposedPosition = Pair(currentRelativePosition.first + 1, currentRelativePosition.second)
                        2 -> proposedPosition = Pair(currentRelativePosition.first, currentRelativePosition.second - 1)
                        3 -> proposedPosition = Pair(currentRelativePosition.first - 1, currentRelativePosition.second)
                    }
//                    println("$currentRelativePosition -> $proposedPosition")

                    if (!(0 until tileSize).contains(proposedPosition.first) || !(0 until tileSize).contains(
                            proposedPosition.second
                        )
                    ) {
                        val teleportInfo = teleportInfoTable[currentTile.first][currentTile.second]!![direction]
                        proposedPosition =
                            getNewRelativeLocation(currentRelativePosition, direction, teleportInfo.rotateBy)
                        proposedNewDirection = getNewDirection(direction, teleportInfo.rotateBy)
                        proposedNewTile = teleportInfo.teleportTo
                    }
                    if (net[proposedNewTile.first][proposedNewTile.second][proposedPosition.first][proposedPosition.second] == '#')
                        break
                    currentRelativePosition = proposedPosition
                    currentTile = proposedNewTile
                    direction = proposedNewDirection
                }
            }
//            println("$it -> $currentTile | $currentRelativePosition | $direction")
//            for (i in 0 until tileSize) {
//                for (j in 0 until tileSize)
//                    print(if (currentRelativePosition.first == i && currentRelativePosition.second == j) '0' else net[currentTile.first][currentTile.second][i][j])
//                println()
//            }

        }

        return ((currentRelativePosition.first + 1 + tileSize * currentTile.first) * 1000 + (currentRelativePosition.second + 1 + tileSize * currentTile.second) * 4 + direction).toString()
    }
}