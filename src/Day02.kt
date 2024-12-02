import kotlin.math.abs

fun main() {
    fun part1(reports: List<List<Int>>): Int {
        return reports.count { it.isSafe() }
    }

    fun part2(reports: List<List<Int>>): Int {
        return reports
            .count { levels ->
                if (levels.isSafe()) return@count true

                for (index in 0..levels.size) {
                    if (
                        levels
                            .filterIndexed { i, _ -> i != index }
                            .isSafe()
                    )
                        return@count true
                }

                false
            }
    }

    val input = readInput("Day02")

    val reports = input
        .map {
            it.split(" ").map { level -> level.toInt() }
        }

    part1(reports).println() // 356
    part2(reports).println() // 413
}

fun List<Int>.isSafe(): Boolean {
    if (this != this.sorted() && this != this.sortedDescending())
        return false

    this.reduce { prev, next ->
        val diff = abs(prev - next)
        if (diff < 1 || diff > 3) {
            return false
        }
        next
    }

    return true
}
