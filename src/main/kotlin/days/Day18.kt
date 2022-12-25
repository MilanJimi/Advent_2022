package days


class Day18 : Day(18) {

    fun coordsFromHash(hash: String): List<Int> {
        return hash.split(',').map { it.toInt() }
    }

    override fun run(part: String): String {
        val cubes = read(part).map { Pair(it, true) }.toMap()
        val coords = cubes.keys.map { it.split(',').map { it.toInt() } }
        val minX = coords.map { it[0] }.min()
        val maxX = coords.map { it[0] }.max()
        val minY = coords.map { it[1] }.min()
        val maxY = coords.map { it[1] }.max()
        val minZ = coords.map { it[2] }.min()
        val maxZ = coords.map { it[2] }.max()
        val air = mutableMapOf<String, Boolean>()
        var startPoint = ""
        for (x in minX..maxX)
            for (y in minY..maxY)
                for (z in minZ..maxZ) {
                    if (cubes["$x,$y,$z"] == null) {
                        air["$x,$y,$z"] = true
                        if (startPoint == "" && (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ))
                            startPoint = "$x,$y,$z"
                    }
                }

        fun floodFill(hash: String) {
            air.remove(hash)
            val (x, y, z) = coordsFromHash(hash)
            if (x > minX && air["${x - 1},$y,$z"] !== null) floodFill("${x - 1},$y,$z")
            if (x < maxX && air["${x + 1},$y,$z"] !== null) floodFill("${x + 1},$y,$z")
            if (y > minY && air["$x,${y - 1},$z"] !== null) floodFill("$x,${y - 1},$z")
            if (y < maxY && air["$x,${y + 1},$z"] !== null) floodFill("$x,${y + 1},$z")
            if (z > minZ && air["$x,$y,${z - 1}"] !== null) floodFill("$x,$y,${z - 1}")
            if (z < maxZ && air["$x,$y,${z + 1}"] !== null) floodFill("$x,$y,${z + 1}")
        }
        floodFill(startPoint)

        var surface = 0
        cubes.forEach {
            surface += 6
            val (x, y, z) = coordsFromHash(it.key)
            if (cubes["$x,$y,${z + 1}"] !== null) surface--
            if (cubes["$x,$y,${z - 1}"] !== null) surface--
            if (cubes["$x,${y + 1},$z"] !== null) surface--
            if (cubes["$x,${y - 1},$z"] !== null) surface--
            if (cubes["${x + 1},$y,$z"] !== null) surface--
            if (cubes["${x - 1},$y,$z"] !== null) surface--
        }
        if (part == "2")
            air.forEach {
                surface -= 6
                val (x, y, z) = coordsFromHash(it.key)
                if (air["$x,$y,${z + 1}"] !== null) surface++
                if (air["$x,$y,${z - 1}"] !== null) surface++
                if (air["$x,${y + 1},$z"] !== null) surface++
                if (air["$x,${y - 1},$z"] !== null) surface++
                if (air["${x + 1},$y,$z"] !== null) surface++
                if (air["${x - 1},$y,$z"] !== null) surface++
            }
        return surface.toString()
    }
}