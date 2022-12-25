package days

import java.math.BigInteger
import kotlin.math.floor

class Day11 : Day(11) {

    private fun operationFromString(line: String): (BigInteger) -> BigInteger {
        val operands = line.split(" = ")[1].split(" ")
        return { old ->
            if (operands[1] == "+")
                old + if (operands[2] == "old") old else operands[2].toBigInteger()
            else
                old * if (operands[2] == "old") old else operands[2].toBigInteger()
        }
    }

    data class Monkey(
        val index: Int,
        val items: MutableList<BigInteger>,
        val operation: (BigInteger) -> BigInteger,
        val test: Int,
        val ifTrueThrowTo: Int,
        val ifFalseThrowTo: Int,
        var inspected: Int = 0,
    )

    override fun run(part: String): String {
        val lines = read(part).joinToString().split(" , ").map { line -> line.split(",   ") }
        val monkeys = lines.mapIndexed { index, line ->
            Monkey(
                index = index,
                items = line[1].filter { it.isDigit() || it == ',' }.split(",").map { it.toBigInteger() }
                    .toMutableList(),
                operation = operationFromString(line[2]),
                test = line[3].filter { it.isDigit() }.toInt(),
                ifTrueThrowTo = line[4].filter { it.isDigit() }.toInt(),
                ifFalseThrowTo = line[5].filter { it.isDigit() }.toInt(),
            )
        }

        val worryDivider = monkeys.map { it.test }.reduce { acc, i -> acc * i }.toBigInteger()

        for (turn in 1..if (part == "1") 20 else 10000) {
            for (i in monkeys.indices) {
                monkeys[i].inspected += monkeys[i].items.size
                monkeys[i].items.forEach {
                    val new = if (part == "1")
                        floor((monkeys[i].operation(it) % worryDivider).toInt() / 3.0).toInt().toBigInteger()
                    else
                        monkeys[i].operation(it) % worryDivider
                    val throwTo =
                        if (new % monkeys[i].test.toBigInteger() == 0.toBigInteger()) monkeys[i].ifTrueThrowTo else monkeys[i].ifFalseThrowTo
                    monkeys[throwTo].items.plusAssign(new)
                }
                monkeys[i].items.clear()
            }
        }

        val business = monkeys.map { it.inspected }.sortedDescending()
        return (business[0].toBigInteger() * business[1].toBigInteger()).toString()
    }
}