fun main() {
    val cache = mutableMapOf<Pair<Long, Int>, Long>()
    fun Long.blink(times: Int): Long {
        if (times == 0) return 1
        cache[this to times]?.let {
            return it
        }

        val count = when {
            this == 0L -> 1L.blink(times - 1)
            this.toString().length % 2L == 0L -> {
                this.toString().substring(0, this.toString().length / 2).toLong().blink(times - 1) +
                        this.toString().substring(this.toString().length / 2).toLong().blink(times - 1)
            }

            else -> (this * 2024L).blink(times - 1)
        }

        cache[this to times] = count

        return count
    }

    fun part1(stones: List<Long>): Long {
        return stones.sumOf { it.blink(25) }

    }

    fun part2(stones: List<Long>): Long {
        return stones.sumOf { it.blink(75) }
    }

    val stones = readInput("Day11")[0].split(" ").map { it.toLong() }

    part1(stones).println() // 190865
    part2(stones).println() // 225404711855335
}