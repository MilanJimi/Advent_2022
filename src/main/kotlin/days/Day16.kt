package days


class Day16 : Day(16) {

    data class Node(
        val flowRate: Int,
        val tunnels: List<String>
    )

    data class AgentTimeSpace(
        var location: String,
        val timeRemaining: Int
    )

    override fun run(part: String): String {
        val lines = read(part).map {
            """([A-Z]{2}) has flow rate=([0-9]*); tunnel[s]? lead[s]? to valve[s]? ([A-Z, ]+)""".toRegex()
                .find(it)!!.groupValues
        }
        val schema: MutableMap<String, Node> = mutableMapOf()
        lines.forEach {
            schema.plusAssign(Pair(it[1], Node(it[2].toInt(), it[3].split(", "))))
        }

        val nonzeroValves = schema.filter { it.value.flowRate > 0 }.map { it.key }.sorted()
        val distanceMap: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

        nonzeroValves.plus("AA").forEach { origin ->
            val minStepsToReach: MutableMap<String, Int> = mutableMapOf(Pair(origin, 0))
            fun calculate(key: String, step: Int) {
                if (step >= 29) return
                schema[key]!!.tunnels.forEach {
                    if (minStepsToReach[it] == null || step + 1 < minStepsToReach[it]!!) {
                        minStepsToReach[it] = step + 1
                        calculate(it, step + 1)
                    }
                }
            }
            calculate(origin, 0)
            distanceMap[origin] = minStepsToReach
        }

        var bestPressure = 0

        val openedAlready = mutableMapOf<String, Int>()

        var stepPart = 1
        fun runStep(
            agents: List<AgentTimeSpace>,
            unopenedValves: List<String>,
            pressure: Int,
            opened: List<Pair<String, Int>>
        ) {
            var newPressure = pressure
            val newOpened = opened.toMutableList()
            agents.forEachIndexed { index, agent ->
                if (unopenedValves.contains(agent.location) && agent.timeRemaining > 0) {
                    newPressure += (agent.timeRemaining - 1) * schema[agent.location]!!.flowRate
                    newOpened.plusAssign(Pair(agent.location, 26 - agent.timeRemaining + 1))
                }
            }
            val hashKey = "${agents[0].location}|${agents[1].location}|${newOpened.map { it.first }.sorted()}"
            if (openedAlready[hashKey] !== null && openedAlready[hashKey]!! > newPressure)
                return
            openedAlready[hashKey] = newPressure
            val newValves = unopenedValves.filter { !agents.map { a -> a.location }.contains(it) }

            newValves.forEachIndexed { i, valve ->

                if (newOpened.size == 1) {
                    println((stepPart / (nonzeroValves.size.toDouble() * (nonzeroValves.size.toDouble() - 1)) * 20000).toInt() / 100.0)
                    stepPart++
                }
                val timeAfter0Arrival =
                    agents[0].timeRemaining - distanceMap[valve]!![agents[0].location]!! - if (schema[agents[0].location]!!.flowRate != 0) 1 else 0
                val timeAfter1Arrival =
                    agents[1].timeRemaining - distanceMap[valve]!![agents[1].location]!! - if (schema[agents[1].location]!!.flowRate != 0) 1 else 0
                if (timeAfter0Arrival > 0)
                    runStep(
                        listOf(AgentTimeSpace(valve, timeAfter0Arrival), agents[1]),
                        newValves,
                        newPressure,
                        newOpened
                    )
                if (timeAfter1Arrival > 0)
                    runStep(
                        listOf(agents[0], AgentTimeSpace(valve, timeAfter1Arrival)),
                        newValves,
                        newPressure,
                        newOpened
                    )
            }
            if (newPressure > bestPressure) {
                newOpened.sortBy { it.second }
                println("$newPressure | $hashKey")
                bestPressure = newPressure
            }
        }

        val maxMinutes = if (part == "1") 30 else 26
        runStep(listOf(AgentTimeSpace("AA", maxMinutes), AgentTimeSpace("AA", maxMinutes)), nonzeroValves, 0, listOf())
        return bestPressure.toString()
    }
}