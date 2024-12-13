import kotlin.math.round

fun main() {

    data class Machine(
        val prize: Pair<Long, Long>,
        val a: Pair<Long, Long>,
        val b: Pair<Long, Long>
    ) {
        fun tryToWin(): Pair<Long, Long>? {
            val y = (prize.first * a.second - prize.second * a.first) * 1.0 / (b.first * a.second - b.second * a.first)
            val x = (prize.second - y * b.second) * 1.0 / a.second

            if (round(y) == y && round(x) == x) {
                return round(x).toLong() to round(y).toLong()
            }

            return null
        }
    }

    fun part1(machines: List<Machine>): Long {
        return machines.mapNotNull {
            it.tryToWin()
        }.sumOf {
            it.first * 3 + it.second
        }
    }

    fun part2(machines: List<Machine>): Long {
        return machines.map {
            it.copy(prize = it.prize.first + 10000000000000L to it.prize.second + 10000000000000L)
        }.mapNotNull {
            it.tryToWin()
        }.sumOf {
            it.first * 3 + it.second
        }
    }

    val machinesConfig = readInput("Day13")
    val machines = mutableListOf<Machine>()
    var a: Pair<Long, Long> = 0L to 0L
    var b: Pair<Long, Long> = 0L to 0L
    var prize: Pair<Long, Long> = 0L to 0L

    val regexA = Regex("Button A: X\\+([0-9]+), Y\\+([0-9]+)")
    val regexB = Regex("Button B: X\\+([0-9]+), Y\\+([0-9]+)")
    val regexPrize = Regex("Prize: X=([0-9]+), Y=([0-9]+)")

    machinesConfig.forEachIndexed { i, line ->
        when (i % 4) {
            0 -> {
                regexA.findAll(line).first().let {
                    a = it.groupValues[1].toLong() to it.groupValues[2].toLong()
                }
            }

            1 -> {
                regexB.findAll(line).first().let {
                    b = it.groupValues[1].toLong() to it.groupValues[2].toLong()
                }
            }

            2 -> {
                regexPrize.findAll(line).first().let {
                    prize = it.groupValues[1].toLong() to it.groupValues[2].toLong()
                }
            }

            3 -> {
                machines.add(Machine(prize, a, b))
            }
        }
    }
    machines.add(Machine(prize, a, b))

    part1(machines).println() // 40369
    part2(machines).println() // 72587986598368
}