import kotlin.math.abs

fun main() {
    fun part1(listOne: List<Int>, listTwo: List<Int>): Int {
        return listOne.sorted().zip(listTwo.sorted())
            .sumOf { abs(it.first - it.second) }
    }

    fun part2(listOne: List<Int>, listTwo: List<Int>): Int {
        return listOne.sumOf { el -> listTwo.count { el == it } * el }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")

    val (listOne, listTwo) = input
        .map {
            val split = it.split("   ")
            split[0].toInt() to split[1].toInt()
        }
        .unzip()

    part1(listOne, listTwo).println() // 1882714
    part2(listOne, listTwo).println() // 19437052
}
