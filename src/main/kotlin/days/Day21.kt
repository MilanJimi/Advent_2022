package days

import java.util.*


class Day21 : Day(21) {


    class Monkey(
        val name: String,
        var value: String? = null,
        var expression: MutableList<String>? = null,
        val variables: MutableMap<String, String> = mutableMapOf(),
        val subscribers: MutableList<Monkey> = mutableListOf()
    )

    override fun run(part: String): String {
        val lines = read(part).map { it.split(": ") }
        val names = lines.map { it[0] }

        val monkeys = names.associateWith { Monkey(it) }

        val readyToShout: Queue<Monkey> = LinkedList()

        lines.forEach { line ->
            val monkey = monkeys[line[0]]!!
            val expression = line[1].split(' ')
            if (expression.size == 1) {
                monkey.value = if (part == "1" || monkey.name != "humn") expression[0] else "x"
                readyToShout.add(monkey)
                return@forEach
            }
            monkeys[expression[0]]!!.subscribers.plusAssign(monkey)
            monkeys[expression[2]]!!.subscribers.plusAssign(monkey)
            monkey.expression = expression.toMutableList()
        }

        while (readyToShout.size > 0) {
            val shoutingMonkey = readyToShout.remove()

            if (shoutingMonkey.value == null) {
                shoutingMonkey.expression!![0] = shoutingMonkey.variables[shoutingMonkey.expression!![0]].toString()
                shoutingMonkey.expression!![2] = shoutingMonkey.variables[shoutingMonkey.expression!![2]].toString()
                if (shoutingMonkey.expression.toString().contains('x'))
                    shoutingMonkey.value =
                        "(${shoutingMonkey.expression!![0]}${shoutingMonkey.expression!![1]}${shoutingMonkey.expression!![2]})"
                else
                    when (shoutingMonkey.expression!![1]) {
                        "+" -> shoutingMonkey.value =
                            (shoutingMonkey.expression!![0].toLong() + shoutingMonkey.expression!![2].toLong()).toString()

                        "-" -> shoutingMonkey.value =
                            (shoutingMonkey.expression!![0].toLong() - shoutingMonkey.expression!![2].toLong()).toString()

                        "*" -> shoutingMonkey.value =
                            (shoutingMonkey.expression!![0].toLong() * shoutingMonkey.expression!![2].toLong()).toString()

                        "/" -> shoutingMonkey.value =
                            (shoutingMonkey.expression!![0].toLong() / shoutingMonkey.expression!![2].toLong()).toString()
                    }
            }

            if (shoutingMonkey.name == "root") return if (part == "1") shoutingMonkey.value.toString() else "${shoutingMonkey.expression!![0]}=${shoutingMonkey.expression}"

            shoutingMonkey.subscribers.forEach {
                it.variables.plusAssign(Pair(shoutingMonkey.name, shoutingMonkey.value!!))
                if (it.value !== null || it.variables.size == 2)
                    readyToShout.plusAssign(it)
            }

        }

        return monkeys.toString()
    }
}