package days

class Day8 : Day(8) {

    override fun run(part: String): String {
        val trees = read(part).map { it.toList().map { tree -> tree.toInt() } }
        val size = trees[0].size
        val isVisible = MutableList(size) { MutableList(size) { false } }
        var maxScenicScore = 0
        val scenicScores = MutableList(size) { MutableList(size) { 1 } }

        if (part == "1") {
            for (col in 0 until size) {
                isVisible[0][col] = true
                isVisible[size - 1][col] = true
            }
            for (row in 0 until size) {
                isVisible[row][0] = true
                isVisible[row][size - 1] = true
            }
            for (row in 1 until size) {
                var highest = trees[row][0]
                for (col in 1 until size)
                    if (trees[row][col] > trees[row][col - 1] && trees[row][col] > highest) {
                        isVisible[row][col] = true
                        highest = trees[row][col]
                    }
                highest = trees[row][size - 1]
                for (col in size - 2 downTo 1)
                    if (trees[row][col] > trees[row][col + 1] && trees[row][col] > highest) {
                        isVisible[row][col] = true
                        highest = trees[row][col]
                    }
            }
            for (col in 1 until size) {
                var highest = trees[0][col]
                for (row in 1 until size)
                    if (trees[row][col] > trees[row - 1][col] && trees[row][col] > highest) {
                        isVisible[row][col] = true
                        highest = trees[row][col]
                    }
                highest = trees[size - 1][col]
                for (row in size - 2 downTo 1)
                    if (trees[row][col] > trees[row + 1][col] && trees[row][col] > highest) {
                        isVisible[row][col] = true
                        highest = trees[row][col]
                    }
            }


            var sumVisible = 0
            isVisible.forEach { row ->
                row.forEach {
                    print(if (it) 1 else 0)
                    if (it) sumVisible += if (it) 1 else 0
                }
                println()
            }
            return sumVisible.toString()
        }

        for (row in 0 until size)
            for (col in 0 until size) {
                val height = trees[row][col]
                var tempScenicScore = 0
                // Row+
                for (rowOffset in row + 1 until size) {
                    tempScenicScore += 1
                    if (trees[rowOffset][col] >= height) break
                }
                scenicScores[row][col] *= tempScenicScore
                tempScenicScore = 0
                // Row-
                for (rowOffset in row - 1 downTo 0) {
                    tempScenicScore += 1
                    if (trees[rowOffset][col] >= height) break
                }
                scenicScores[row][col] *= tempScenicScore
                tempScenicScore = 0
                // Col+
                for (colOffset in col + 1 until size) {
                    tempScenicScore += 1
                    if (trees[row][colOffset] >= height) break
                }
                scenicScores[row][col] *= tempScenicScore
                tempScenicScore = 0
                // Col-
                for (colOffset in col - 1 downTo 0) {
                    tempScenicScore += 1
                    if (trees[row][colOffset] >= height) break
                }
                scenicScores[row][col] *= tempScenicScore
                if (scenicScores[row][col] > maxScenicScore) maxScenicScore = scenicScores[row][col]
            }
        return maxScenicScore.toString()
    }
}