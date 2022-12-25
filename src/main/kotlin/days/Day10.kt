package days

import kotlin.math.abs

class Day10 : Day(10) {

    override fun run(part: String): String {
        val commands = read(part)
        var x = 1
        var signalStrength = 0
        var actualCommands = mutableListOf<String>()
        commands.forEach { command ->
            if (command == "noop") {
                actualCommands.plusAssign(command)
                return@forEach
            }
            actualCommands.plusAssign("noop")
            actualCommands.plusAssign(command)
        }
        for (cycle in 1 until actualCommands.size) {
            if ((cycle - 20) % 40 == 0) signalStrength += x * cycle

            if (abs(((cycle - 1) % 40) - x) <= 1) print('#') else print(' ')
            if (cycle % 40 == 0) println()

            val command = actualCommands[cycle - 1]
            if (command == "noop") continue
            x += command.split(" ")[1].toInt()
        }
        println()
        return signalStrength.toString()
    }
}