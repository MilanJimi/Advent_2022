package days

class Day7 : Day(7) {
    private var directories: MutableMap<String, Int> = mutableMapOf()

    override fun run(part: String): String {
        directories = mutableMapOf()
        val lines = this.read(part)
        var path = ""
        lines.forEach { line ->
            val command = line.split(" ")
            if (command[0] == "dir") return@forEach
            if (command[0] == "$") {
                when (command[1]) {
                    "cd" -> {
                        when (command[2]) {
                            "/" -> path = "home"
                            ".." -> path = path.substringBeforeLast("/")
                            else -> path += "/${command[2]}"
                        }
                    }

                    "ls" -> {}
                }
                return@forEach
            }
            var pathBuilder = ""
            path.split("/").forEach {
                pathBuilder += it
                directories[pathBuilder] = (directories[pathBuilder] ?: 0) + command[0].toInt()
                pathBuilder += "/"
            }
        }
        if (part == "2" || part == "test") {
            var min = 70000000
            val spaceNeeded = directories["home"]!! - 40000000
            directories.values.forEach {
                if (it in spaceNeeded..min) min = it
            }
            return min.toString()
        }
        var sum = 0
        directories.values.forEach {
            if (it <= 100000) sum += it
        }
        return sum.toString()
    }
}