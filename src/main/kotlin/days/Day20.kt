package days

import kotlin.math.abs


class Day20 : Day(20) {


    class Node(
        val value: Long,
        var previous: Node? = null,
        var next: Node? = null
    )

    override fun run(part: String): String {
        val values = read(part).map { if (part == "1") it.toLong() else it.toLong() * 811589153 }
        var head = Node(values[0])
        val tail = head
        val originalOrder = mutableListOf(head)
        values.forEachIndexed { i, v ->
            if (i == 0)
                return@forEachIndexed
            head.next = Node(v, head)
            head = head.next!!
            originalOrder.plusAssign(head)
        }
        head.next = tail
        tail.previous = head

        head = head.next!!
        for (step in 0 until if (part == "1") 1 else 10) {
            originalOrder.forEach {
                val offset = (abs(it.value)).mod(originalOrder.size - 1)
                if (offset == 0) return@forEach
                it.previous!!.next = it.next
                it.next!!.previous = it.previous
                var next = it.next
                for (i in 0 until if (it.value > 0) offset else originalOrder.size - offset - 1) {
                    next = next!!.next
                }
                it.next = next
                it.previous = next!!.previous
                next.previous!!.next = it
                next.previous = it
            }
        }
        val finalOrder = mutableListOf<Long>()
        var zeroLocation = -1
        for (i in 0 until originalOrder.size) {
            if (head.value == 0.toLong()) zeroLocation = finalOrder.size
            finalOrder.plusAssign(head.value)
            head = head.next!!
        }

        return (finalOrder[(zeroLocation + 1000) % finalOrder.size] + finalOrder[(zeroLocation + 2000) % finalOrder.size] + finalOrder[(zeroLocation + 3000) % finalOrder.size]).toString()
    }
}