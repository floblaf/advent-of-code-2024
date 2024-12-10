fun main() {
    val directions = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )

    fun List<List<Int>>.getAccessibleSummits(x: Int, y: Int, value: Int = 0): List<Pair<Int,Int>> {
        return if (value == 9) {
            listOf(x to y)
        } else {
            directions.flatMap { direction ->
                if (this.getOrNull(x + direction.first)?.getOrNull(y + direction.second) == value + 1) {
                    this.getAccessibleSummits(x + direction.first, y + direction.second, value +1)
                } else {
                    emptyList()
                }
            }
        }
    }

    fun List<List<Int>>.getPossiblePaths(x: Int, y: Int, value: Int = 0): Int {
        return if (value == 9) {
            1
        } else {
            directions.sumOf { direction ->
                if (this.getOrNull(x + direction.first)?.getOrNull(y + direction.second) == value + 1) {
                    this.getPossiblePaths(x + direction.first, y + direction.second, value +1)
                } else {
                    0
                }
            }
        }
    }

    fun part1(trailheads: List<Pair<Int, Int>>, map: List<List<Int>>): Int {
        return trailheads.sumOf {
            val accessible = map.getAccessibleSummits(it.first, it.second)
            accessible.distinct().count()
        }
    }

    fun part2(trailheads: List<Pair<Int, Int>>, map: List<List<Int>>): Int {
        return trailheads.sumOf {
            map.getPossiblePaths(it.first, it.second)
        }
    }

    val map = readInput("Day10").map { it.toList().map { it.digitToInt() } }
    val trailheads = map.flatMapIndexed { x, line ->
        line.mapIndexedNotNull { y, height ->
            if (height == 0) {
                x to y
            } else {
                null
            }
        }
    }

    part1(trailheads, map).println() // 789
    part2(trailheads, map).println() // 1735
}