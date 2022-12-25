package days


class Day19 : Day(19) {

    data class ObsidianRobotBlueprint(
        val oreCost: Int,
        val clayCost: Int
    )

    data class GeodeRobotBlueprint(
        val oreCost: Int,
        val obsidianCost: Int
    )

    data class Blueprint(
        val id: Int,
        val oreRobotCost: Int,
        val clayRobotCost: Int,
        val obsidianRobot: ObsidianRobotBlueprint,
        val geodeRobot: GeodeRobotBlueprint,
        val maxOreCost: Int
    )

    data class Stock(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0
    )

    data class Step(
        val robots: Stock = Stock(ore = 1),
        val stockpile: Stock = Stock(),
        val minute: Int = 1
    )

    var projectedGeodesAtLeast: Int = 0
    var maxGeodes: Int = 0
    val maxMinute = 33
    val seenSteps = mutableMapOf<String, Stock>()

    fun addStocks(a: Stock, b: Stock): Stock {
        return Stock(a.ore + b.ore, a.clay + b.clay, a.obsidian + b.obsidian, a.geode + b.geode)
    }

    fun isStockGreaterEqualThan(a: Stock, b: Stock): Boolean {
        return a.ore >= b.ore && a.clay >= b.clay && a.obsidian >= b.obsidian && a.geode >= b.geode
    }

    fun runStep(step: Step, blueprint: Blueprint) {
        if (step.minute == maxMinute) {
            if (step.stockpile.geode > maxGeodes) {
                maxGeodes = step.stockpile.geode
            }
            return
        }

        val newMinute = step.minute + 1
        val hashKey = "${step.robots}|${step.minute}"
        if (seenSteps[hashKey] !== null && isStockGreaterEqualThan(seenSteps[hashKey]!!, step.stockpile))
            return

        if (seenSteps[hashKey] == null || isStockGreaterEqualThan(step.stockpile, seenSteps[hashKey]!!))
            seenSteps[hashKey] = step.stockpile

        val timeRemaining = maxMinute - newMinute
        val newStocks = addStocks(step.stockpile, step.robots)

        val projectedGeodesAtCurrentRate = step.stockpile.geode + (timeRemaining) * step.robots.geode
        projectedGeodesAtLeast = listOf(projectedGeodesAtLeast, projectedGeodesAtCurrentRate).max()

        if (projectedGeodesAtCurrentRate + (timeRemaining) * (timeRemaining + 1) / 2 < projectedGeodesAtLeast)
            return

        if (step.stockpile.ore >= blueprint.geodeRobot.oreCost && step.stockpile.obsidian >= blueprint.geodeRobot.obsidianCost) {
            runStep(
                Step(
                    addStocks(step.robots, Stock(geode = 1)),
                    addStocks(
                        newStocks,
                        Stock(ore = 0 - blueprint.geodeRobot.oreCost, obsidian = 0 - blueprint.geodeRobot.obsidianCost)
                    ),
                    newMinute
                ), blueprint
            )
        }

        if (newMinute < maxMinute) {
            if (step.stockpile.ore >= blueprint.oreRobotCost && step.robots.ore < blueprint.maxOreCost) {
                runStep(
                    Step(
                        addStocks(step.robots, Stock(ore = 1)),
                        addStocks(newStocks, Stock(ore = 0 - blueprint.oreRobotCost)),
                        newMinute
                    ), blueprint
                )
            }

            if (step.stockpile.ore >= blueprint.clayRobotCost && step.robots.clay < blueprint.obsidianRobot.clayCost) {
                runStep(
                    Step(
                        addStocks(step.robots, Stock(clay = 1)),
                        addStocks(newStocks, Stock(ore = 0 - blueprint.clayRobotCost)),
                        newMinute
                    ), blueprint
                )
            }

            if (step.stockpile.ore >= blueprint.obsidianRobot.oreCost && step.stockpile.clay >= blueprint.obsidianRobot.clayCost && step.robots.obsidian < blueprint.geodeRobot.obsidianCost) {
                runStep(
                    Step(
                        addStocks(step.robots, Stock(obsidian = 1)),
                        addStocks(
                            newStocks,
                            Stock(
                                ore = 0 - blueprint.obsidianRobot.oreCost,
                                clay = 0 - blueprint.obsidianRobot.clayCost
                            )
                        ),
                        newMinute
                    ), blueprint
                )
            }
        }

        runStep(Step(step.robots, newStocks, newMinute), blueprint)
    }


    override fun run(part: String): String {
        val lines = read(part).map {
            """
            Blueprint ([0-9]*): Each ore robot costs ([0-9]*) ore. Each clay robot costs ([0-9]*) ore. Each obsidian robot costs ([0-9]*) ore and ([0-9]*) clay. Each geode robot costs ([0-9]*) ore and ([0-9]*) obsidian.
            """.trimIndent().toRegex().find(it)!!.groupValues
        }
        val blueprints = lines.subList(0, if (part == "1") lines.size - 1 else listOf(3, lines.size).min()).map {
            Blueprint(
                it[1].toInt(),
                it[2].toInt(),
                it[3].toInt(),
                ObsidianRobotBlueprint(it[4].toInt(), it[5].toInt()),
                GeodeRobotBlueprint(it[6].toInt(), it[7].toInt()),
                listOf(it[2].toInt(), it[3].toInt(), it[4].toInt(), it[6].toInt()).max()
            )
        }

        val qualityLevels = mutableListOf<Int>()
        val maxGeodesPerBlueprint = mutableListOf<Int>()
        blueprints.forEach { blueprint ->
            projectedGeodesAtLeast = 0
            maxGeodes = 0
            seenSteps.clear()
            runStep(Step(), blueprint)

            qualityLevels.plusAssign(blueprint.id * maxGeodes)
            maxGeodesPerBlueprint.plusAssign(maxGeodes)
        }

        return if (part == "1") qualityLevels.sum().toString() else maxGeodesPerBlueprint.reduce(Int::times).toString()
    }
}