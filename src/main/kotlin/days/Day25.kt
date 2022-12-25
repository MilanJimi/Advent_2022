package days

import kotlin.math.pow

class Day25 : Day(25) {

    fun decode(num: List<Char>): Long {
        var value = 0.toLong()
        num.forEachIndexed { index, c ->
            when (c) {
                '=' -> value -= 2 * 5.0.pow((num.size - index - 1).toDouble()).toLong()
                '-' -> value -= 5.0.pow((num.size - index - 1).toDouble()).toLong()
                else -> value += c.toString().toInt() * 5.0.pow((num.size - index - 1).toDouble()).toLong()
            }
        }
        return value
    }

    fun encode(num: Long): String {
        var currentOrder = 1.toLong()
        var output = ""
        var reminder = num
        while (reminder > 0) {
            if (reminder > num * 5 || currentOrder > reminder * 5) throw Error()
            when ((reminder % (currentOrder * 5)) / currentOrder) {
                3.toLong() -> {
                    output = "=$output"
                    reminder += 2 * currentOrder
                }

                4.toLong() -> {
                    output = "-$output"
                    reminder += currentOrder
                }

                else -> {
                    val modulo = (reminder % (currentOrder * 5)) / currentOrder
                    output = "$modulo$output"
                    reminder -= modulo * currentOrder
                }
            }
            currentOrder *= 5
        }
        return output
    }

    override fun run(part: String): String {
        val lines = read(part).map { it.toList() }

        val sum = lines.map { decode(it) }.sum()
        return encode(sum)
    }
}