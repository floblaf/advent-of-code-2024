fun main() {
    fun List<Int>.isWellOrdered(order: Map<Int, List<Int>>): Boolean {
        this.forEachIndexed { i, el ->
            val previous = this.subList(0, i)
            order[el]?.forEach {
                if (it in previous) {
                    return false
                }
            }
        }

        return true
    }

    fun List<Int>.reorder(order: Map<Int, List<Int>>): List<Int> {
        return this.sortedWith { a, b ->
            if (b in (order[a] ?: emptyList())) {
                -1
            } else {
                1
            }
        }
    }

    fun part1(order: Map<Int, List<Int>>, updates: List<List<Int>>): Int {
        return updates
            .filter { it.isWellOrdered(order) }
            .sumOf { it[it.size / 2] }
    }

    fun part2(order: Map<Int, List<Int>>, updates: List<List<Int>>): Int {
        return updates
            .filter { !it.isWellOrdered(order) }
            .map { it.reorder(order) }
            .sumOf { it[it.size / 2] }
    }

    val input = readInput("Day05")
    val order = input
        .filter { it.contains("|") }
        .map { it.split("|").let { it[0].toInt() to it[1].toInt() } }
        .groupBy({ it.first }, { it.second })

    val updates = input
        .filter { it.contains(",") }
        .map { list -> list.split(",").map { it.toInt() } }

    part1(order, updates).println() // 5374
    part2(order, updates).println() // 4260
}