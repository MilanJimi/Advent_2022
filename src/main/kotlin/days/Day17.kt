package days

import java.math.BigInteger
import kotlin.math.floor
import kotlin.math.max

abstract class Shape(
    val name: Char,
    val canMoveDown: (Int, Int) -> Boolean,
    val canMoveRight: (Int, Int) -> Boolean,
    val canMoveLeft: (Int, Int) -> Boolean,
    val getMaxHeight: (Int) -> Int,
    val addToMap: (Int, Int) -> Unit
)

var cave = mutableListOf(MutableList(9) { true })

class Day17 : Day(17) {


    class Dash : Shape(
        '-',
        { x, y -> !(cave[y - 1][x] || cave[y - 1][x + 1] || cave[y - 1][x + 2] || cave[y - 1][x + 3]) },
        { x, y -> !cave[y][x + 4] },
        { x, y -> !cave[y][x - 1] },
        { y -> y },
        { x, y -> cave[y][x] = true; cave[y][x + 1] = true; cave[y][x + 2] = true; cave[y][x + 3] = true }
    )

    class Plus : Shape(
        '+',
        { x, y -> !(cave[y][x] || cave[y - 1][x + 1] || cave[y][x + 2]) },
        { x, y -> !(cave[y][x + 2] || cave[y + 1][x + 3] || cave[y + 2][x + 2]) },
        { x, y -> !(cave[y][x] || cave[y + 1][x - 1] || cave[y + 2][x]) },
        { y -> y + 2 },
        { x, y ->
            cave[y][x + 1] = true; cave[y + 1][x] = true; cave[y + 1][x + 1] = true; cave[y + 1][x + 2] =
            true; cave[y + 2][x + 1] = true
        }
    )

    class L : Shape(
        'L',
        { x, y -> !(cave[y - 1][x] || cave[y - 1][x + 1] || cave[y - 1][x + 2]) },
        { x, y -> !(cave[y][x + 3] || cave[y + 1][x + 3] || cave[y + 2][x + 3]) },
        { x, y -> !(cave[y][x - 1] || cave[y + 1][x + 1] || cave[y + 2][x + 1]) },
        { y -> y + 2 },
        { x, y ->
            cave[y][x] = true; cave[y][x + 1] = true; cave[y][x + 2] = true; cave[y + 1][x + 2] =
            true; cave[y + 2][x + 2] = true
        }
    )

    class Column : Shape(
        '|',
        { x, y -> !cave[y - 1][x] },
        { x, y -> !(cave[y][x + 1] || cave[y + 1][x + 1] || cave[y + 2][x + 1] || cave[y + 3][x + 1]) },
        { x, y -> !(cave[y][x - 1] || cave[y + 1][x - 1] || cave[y + 2][x - 1] || cave[y + 3][x - 1]) },
        { y -> y + 3 },
        { x, y -> cave[y][x] = true; cave[y + 1][x] = true; cave[y + 2][x] = true; cave[y + 3][x] = true; }
    )

    class Box : Shape(
        '□',
        { x, y -> !(cave[y - 1][x] || cave[y - 1][x + 1]) },
        { x, y -> !(cave[y][x + 2] || cave[y + 1][x + 2]) },
        { x, y -> !(cave[y][x - 1] || cave[y + 1][x - 1]) },
        { y -> y + 1 },
        { x, y -> cave[y][x] = true; cave[y + 1][x] = true; cave[y + 1][x + 1] = true; cave[y][x + 1] = true; }
    )

    override fun run(part: String): String {
        val line = read(part)[0].toList()
        var lineIndex = 0.toLong()
        cave = mutableListOf(MutableList(9) { true })
        var maxHeight = 0

        val shapes = listOf(Dash(), Plus(), L(), Column(), Box())
        val seenCombinations = mutableMapOf<String, Long>()
        val heightHistory = mutableListOf<BigInteger>()

        val numOfShapes = if (part == "1") 2021 else 1000000000000

        for (i in 0..numOfShapes) {
            val newShape = shapes[i.mod(shapes.size)]
            for (j in cave.size..newShape.getMaxHeight(maxHeight + 4))
                cave.plusAssign(mutableListOf(true, *Array(7) { false }, true))

            var (x, y) = listOf(3, maxHeight + 4)
            val startIndex = lineIndex.mod(line.size)


            while (y > 0) {
                val direction = line[lineIndex.mod(line.size)]
                lineIndex++
                if (direction == '>' && newShape.canMoveRight(x, y))
                    x++
                if (direction == '<' && newShape.canMoveLeft(x, y))
                    x--
                if (newShape.canMoveDown(x, y)) {
                    y--
                    continue
                }
                newShape.addToMap(x, y)
//                println(i)
//                cave.reversed().forEach{it.forEach{if(it)print('#') else print(' ')}; println()}

                maxHeight = max(maxHeight, newShape.getMaxHeight(y))
                heightHistory.plusAssign(maxHeight.toBigInteger())
                break
            }

            if (y > 3) {
                val hashLastRows =
                    cave.slice(y - 3..y + 1).map { it.map { ch -> if (ch) '#' else '_' }.toString() }.toString()
                val hashKey = "${startIndex}_${newShape.name}_${hashLastRows}"
                if (seenCombinations[hashKey] !== null) {
                    val cycleSeenAtIndex = seenCombinations[hashKey]!!.toInt()
                    val cycleSize = i - cycleSeenAtIndex
                    val heightAtStartOfCycle = heightHistory[cycleSeenAtIndex]
                    val cycleHeight = maxHeight.toBigInteger() - heightAtStartOfCycle
                    val fitsFullCycles =
                        floor((numOfShapes - cycleSeenAtIndex) / cycleSize.toDouble()).toBigDecimal().toBigInteger()
                    val reminderSteps = (numOfShapes - i).mod(cycleSize)
                    println("size: $cycleSize index: $cycleSeenAtIndex height: $cycleHeight repeats: $fitsFullCycles reminder: $reminderSteps $hashKey")
                    val height = fitsFullCycles * cycleHeight +
                            heightHistory[(cycleSeenAtIndex + reminderSteps).toInt()] - 1.toBigInteger()
                    return height.toString()
                } else seenCombinations.plusAssign(Pair(hashKey, i))
            }

        }
        return maxHeight.toString()
    }
}