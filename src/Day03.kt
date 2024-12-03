fun main() {
    fun part1(input: String): Int {
        return Regex("mul\\(([0-9]+),([0-9]+)\\)")
            .findAll(input)
            .sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
    }

    fun part2(input: String): Int {
        data class Fold(val sum: Int = 0, val enable: Boolean = true)

        return Regex("mul\\(([0-9]+),([0-9]+)\\)|do\\(\\)|(don't\\(\\))")
            .findAll(input)
            .fold(Fold()) { fold, it ->
                when (it.groupValues[0]) {
                    "do()" -> fold.copy(enable = true)
                    "don't()" -> fold.copy(enable = false)
                    else -> if (fold.enable) {
                        fold.copy(sum = fold.sum + it.groupValues[1].toInt() * it.groupValues[2].toInt())
                    } else {
                        fold
                    }
                }
            }.sum
    }

    val input = readInput("Day03").joinToString("")
    part1(input).println() // 163931492
    part2(input).println() // 76911921
}