package days

class Day3 : Day(3) {
    fun split(backpack: String): Pair<String, String> {
        val mid = backpack.length / 2
        return Pair(backpack.substring(0, mid), backpack.substring(mid, backpack.length))
    }

    fun getPriority(ch: Char): Int {
        println(ch)
        if (ch.code >= 97) return ch.code - 96
        return ch.code - 64 + 26
    }

    override fun run(part: String): String {
        val lines = this.read(part)
        var sum = 0
        if (part == "1") {
            lines.forEach {
                val compartments = this.split(it)
                val mapFirst = mutableMapOf<Char, Int>()
                compartments.first.forEach { ch ->
                    if (mapFirst[ch] !== null)
                        mapFirst[ch] = mapFirst[ch]!! + 1
                    else mapFirst[ch] = 1
                }
                for (ch in compartments.second) {
                    if (mapFirst[ch] !== null) {
                        sum += this.getPriority(ch)
                        return@forEach
                    }
                }
            }
            return sum.toString()
        }
        val gropupedLines = lines.chunked(3)
        gropupedLines.forEach {
            val m = mutableMapOf<Char, Int>()
            it[0].forEach { ch ->
                if (m[ch] == null) m[ch] = 1
            }
            it[1].forEach { ch ->
                if (m[ch] == 1) m[ch] = 2
            }
            for (ch in it[2]) {
                if (m[ch] == 2) {
                    sum += this.getPriority(ch)
                    return@forEach
                }
            }
        }
        return sum.toString()
    }
}