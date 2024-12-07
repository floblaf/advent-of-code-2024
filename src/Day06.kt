fun main() {
    fun List<List<String>>.getStart(): Triple<Int, Int, String> {
        var x = 0
        var y = 0
        var direction = ""
        this.forEachIndexed { i, line ->
            val j = line.indexOfFirst { it in listOf("<", ">", "^", "v") }
            if (j != -1) {
                x = i
                y = j
                direction = this[x][y]
            }
        }

        return Triple(x, y, direction)
    }

    fun List<List<String>>.getNextPosition(x: Int, y: Int, direction: String): Triple<Int, Int, String> {
        val (newX, newY) = when (direction) {
            "^" -> x - 1 to y
            "<" -> x to y - 1
            ">" -> x to y + 1
            "v" -> x + 1 to y
            else -> error("")
        }

        return if (this.getOrNull(newX)?.getOrNull(newY) == "#") {
            getNextPosition(x, y, when (direction) {
                "^" -> ">"
                "<" -> "^"
                ">" -> "v"
                "v" -> "<"
                else -> error("")
            })
        } else {
            Triple(newX, newY, direction)
        }
    }

    fun part1(input: List<List<String>>): Int {
        val map = input.map { it.toMutableList() }
        var (x, y, direction) = map.getStart()

        val width = map.indices
        val height = map[0].indices
        while (x in width  && y in height) {
            map[x][y] = "X"
            map.getNextPosition(x, y, direction).let {
                x = it.first
                y = it.second
                direction = it.third
            }
        }

        return map.sumOf { it.count { it == "X" } }
    }

    fun isStuck(input: List<List<String>>): Boolean {
        val map = input.map { it.toMutableList() }
        var (x, y, direction) = map.getStart()

        val width = map.indices
        val height = map[0].indices
        while (x in width  && y in height) {
            val next = map.getNextPosition(x, y, direction)

            if (next.third == direction) {
                map[x][y] = when {
                    direction in listOf("<", ">") && map[x][y] == "|" -> "+"
                    direction in listOf("<", ">") && map[x][y] != "|" -> "-"
                    direction in listOf("^", "v") && map[x][y] == "-" -> "+"
                    direction in listOf("^", "v") && map[x][y] != "-" -> "|"
                    else -> error("")
                }
            } else {
                if (map[x][y] == "+") {
                    return true
                } else {
                    map[x][y] = "+"
                }
            }

            next.let {
                x = it.first
                y = it.second
                direction = it.third
            }
        }

        return false
    }

    fun part2(input: List<List<String>>): Int {
        var counter = 0

        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] !in listOf("<", ">", "^", "v", "#")) {
                    val map = input.map { it.toMutableList() }
                    map[i][j] = "#"
                    if (isStuck(map)) {
                        counter++
                    }
                }
            }
        }

        return counter
    }

    val input = readInput("Day06")
    val map = input
        .map { it.split("") }

    part1(map).println() // 4647
    part2(map).println() // 1723
}