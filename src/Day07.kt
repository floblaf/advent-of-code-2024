fun main() {
    data class Equation(
        val total: Long,
        val numbers: List<Long>
    )

    fun solvableTwoOp(total: Long, numbers: List<Long>): Boolean {
        if (numbers.size == 1) {
            return total == numbers[0]
        }

        val last = numbers.last()
        val subNumbers = numbers.dropLast(1)

        return (total % last == 0L && solvableTwoOp(total / last, subNumbers)) ||
                solvableTwoOp(total - last, subNumbers)
    }

    fun solvableThreeOp(total: Long, numbers: List<Long>): Boolean {
        if (numbers.size == 1) {
            return total == numbers[0]
        }

        val last = numbers.last()
        val subNumbers = numbers.dropLast(1)
        return (total.toString().endsWith(last.toString()) && solvableThreeOp(total.toString().removeSuffix(last.toString()).toLongOrNull() ?: 0L, subNumbers)) ||
               return (total % last == 0L && solvableThreeOp(total / last, subNumbers)) ||
                solvableThreeOp(total - last, subNumbers)
    }

    val equations = readInput("Day07").map {
        Equation(
            total = it.split(": ")[0].toLong(),
            numbers = it.split(": ")[1].split(" ").map { e -> e.toLong() }
        )
    }

    fun part1(equations: List<Equation>): Long {
        return equations.filter { solvableTwoOp(it.total, it.numbers) }
            .sumOf { it.total }
    }

    fun part2(equations: List<Equation>): Long {
        return equations.filter { solvableThreeOp(it.total, it.numbers) }
            .sumOf { it.total }
    }

    part1(equations).println() // 1985268524462
    part2(equations).println() // 150077710195188
}