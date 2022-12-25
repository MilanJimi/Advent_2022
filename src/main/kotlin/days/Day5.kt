package days

class Day5 : Day(5) {

    fun <T> MutableList<T>.prepend(element: T) {
        add(0, element)
    }

    fun <T> MutableList<T>.appendList(element: List<T>) {
        addAll(this.size, element)
    }

    override fun run(part: String): String {
        val lines = this.read(part)
        val length = (lines[0].length + 1) / 4
        val stacks = List(length) { mutableListOf<Char>() }
        var lineIndex = 0
        while (lines[lineIndex] !== "") {
            val line = lines[lineIndex]
            val split = line.chunked(4)
            split.forEachIndexed { index, s -> if (s[0] == '[' && s[1] !== ' ') stacks[index].prepend(s[1]) }
            lineIndex++
        }
        lineIndex++

        while (lines[lineIndex] !== "") {
            val line = lines[lineIndex]
            val split = line.split(" ")
            val amount = split[1].toInt()
            val from = split[3].toInt()
            val to = split[5].toInt()
            val sFrom = stacks[from - 1]
            val sTo = stacks[to - 1]
            val moved = sFrom.subList(sFrom.size - amount, sFrom.size)
            if (part == "1") {
                sTo.appendList(moved.reversed())
            } else {
                sTo.appendList(moved)
            }
            repeat(amount) { sFrom.removeLast() }
            lineIndex++
        }
        var tops = ""
        stacks.forEach { tops += it[it.size - 1] }
        return tops
    }
}