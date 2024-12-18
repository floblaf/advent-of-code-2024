
fun main() {
    data class Position(
        val x: Int,
        val y: Int,
        val steps: Int,
    )

    fun solve(length:Int, bytes: List<Pair<Int, Int>>): Int {
        val visitedPosition = mutableSetOf<Pair<Int,Int>>()
        val toVisit = mutableListOf<Position>()
        var bestSteps = Int.MAX_VALUE
        var endReached = false
        toVisit.add(Position(0, 0, 0))

        while(toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()

            if (current.x == length && current.y == length && current.steps <= bestSteps) {
                bestSteps = current.steps
                endReached = true
                continue
            }

            if (current.steps > bestSteps) {
                continue
            }

            listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (dX, dY) ->
                val newPos = Position(current.x + dX, current.y + dY, current.steps + 1)

                if (newPos.x in 0..length && newPos.y in 0..length) {
                    if (!bytes.contains(newPos.x to newPos.y) && !visitedPosition.contains(newPos.x to newPos.y)) {
                        toVisit.add(newPos)
                        visitedPosition.add(newPos.x to newPos.y)
                    }
                }
            }
        }

        return if (endReached) bestSteps else -1
    }

    fun part1(length: Int, bytes: List<Pair<Int, Int>>): Int {
        return solve(length, bytes.take(1024))
    }

    fun part2(length: Int, bytes: List<Pair<Int, Int>>): Pair<Int, Int> {
        for (i in bytes.indices) {
            if (solve(length, bytes.take(i)) == -1) {
                return bytes[i-1]
            } else {
                println("$i:solved")
            }
        }

        return -1 to -1
    }

    val length = 70
    val bytes = readInput("Day18")
        .map { it.split(",").let { it[0].toInt() to it[1].toInt() } }

    part1(length, bytes).println() // 340
    part2(length, bytes).println() // 34,32
}