package days

class Day6 : Day(6) {


    override fun run(part: String): String {
        val line = this.read(part)[0]
        val markerLength = if (part == "2") 14 else 4
        for (i in markerLength..line.length) {
            if (line.slice(i - markerLength until i).toList().distinct().size == markerLength) return i.toString()
        }
        throw Error("No match found")
    }
}