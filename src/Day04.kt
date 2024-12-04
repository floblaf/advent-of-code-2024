fun main() {
    fun part1(input: List<List<Char>>): Int {
        var occurrence = 0
        val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == 'X') {
                    directions.forEach { (x, y) ->
                        if (input.getOrNull(i + x)?.getOrNull(j + y) == 'M' &&
                            input.getOrNull(i + 2 * x)?.getOrNull(j + 2 * y) == 'A' &&
                            input.getOrNull(i + 3 * x)?.getOrNull(j + 3 * y) == 'S'
                        ) {
                            occurrence++
                        }
                    }
                }
            }
        }

        return occurrence
    }

    fun part2(input: List<List<Char>>): Int {
        val candidates = mutableListOf<String>()
        for (i in 1..input.size - 2) {
            for (j in 1..input[0].size - 2) {
                if (input[i][j] == 'A') {
                    candidates.add("" + input[i - 1][j - 1] + input[i - 1][j + 1] + input[i + 1][j - 1] + input[i + 1][j + 1])
                }
            }
        }

        return candidates.count {
            it in listOf("MMSS", "SMSM", "SSMM", "MSMS")
        }
    }

    val input = readInput("Day04").map { it.toList() }
    part1(input).println() // 2530
    part2(input).println() // 1921
}