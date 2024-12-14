fun main() {
    val height = 103
    val width = 101

    data class Robot(
        val position: Pair<Int, Int>,
        val speed: Pair<Int, Int>
    )

    fun List<Pair<Int, Int>>.print() {
        for (y in 0..height) {
            for (x in 0..width) {
                if (this.contains(x to y)) {
                    print("*")
                } else {
                    print(".")
                }
            }
            print("\n")
        }
    }

    fun part1(robots: List<Robot>): Int {
        return robots
            .map {
                (((it.position.first + it.speed.first * 100) % width) + width) % width to
                        (((it.position.second + it.speed.second * 100) % height) + height) % height

            }
            .groupBy {
                when {
                    it.first < width / 2 && it.second < height / 2 -> "a"
                    it.first > width / 2 && it.second < height / 2 -> "b"
                    it.first < width / 2 && it.second > height / 2 -> "c"
                    it.first > width / 2 && it.second > height / 2 -> "d"
                    else -> null
                }
            }
            .mapNotNull { if (it.key == null) null else it.value.size }
            .reduce { a, b -> a * b }
    }

    fun part2(robots: List<Robot>): Int {
        var seconds = 0
        var found = false
        var positions: List<Pair<Int, Int>>? = null
        while (!found) {
            seconds++
            positions = robots
                .map {
                    (((it.position.first + it.speed.first * seconds) % width) + width) % width to
                            (((it.position.second + it.speed.second * seconds) % height) + height) % height

                }
                .distinct()

            positions.forEach { (x, y) ->
                if (positions.containsAll(
                        listOf(
                            x - 1 to y + 1,
                            x to y + 1,
                            x + 1 to y + 1,
                            x - 2 to y + 2,
                            x - 1 to y + 2,
                            x to y + 2,
                            x + 1 to y + 2,
                            x + 2 to y + 2
                        )
                    )
                ) {
                    found = true
                }
            }
        }

        positions?.print()

        return seconds
    }

    val robotRegex = Regex("p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)")
    val robots = readInput("Day14")
        .map {
            robotRegex.findAll(it).first().let {
                Robot(
                    position = it.groupValues[1].toInt() to it.groupValues[2].toInt(),
                    speed = it.groupValues[3].toInt() to it.groupValues[4].toInt()
                )
            }
        }

    part1(robots).println() // 226548000
    part2(robots).println() // 7753
}