import kotlin.math.abs

fun main() {
    data class Position(
        val x: Int,
        val y: Int,
        val steps: Int,
    )

    fun computeDistancesFromStart(start: Pair<Int,Int>, end: Pair<Int, Int>, accessibleLocations: List<Pair<Int, Int>>): Map<Pair<Int, Int>, Int> {
        val visitedPosition = mutableMapOf<Pair<Int, Int>, Int>()
        val toVisit = mutableListOf<Position>()
        toVisit.add(Position(start.first, start.second, 0))


        while(toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            visitedPosition[current.x to current.y] = current.steps

            if (current.x == end.first && current.y == end.second) {
                continue
            }

            listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (dX, dY) ->
                val newPos = Position(current.x + dX, current.y + dY, current.steps + 1)

                if (accessibleLocations.contains(newPos.x to newPos.y)) {
                    if (!visitedPosition.contains(newPos.x to newPos.y)) {
                        toVisit.add(newPos)
                    }
                }
            }
        }

        return visitedPosition
    }

    fun getCheats(distance: Int): List<Triple<Int, Int, Int>> {
        return (-distance..distance).flatMap { x ->
            (-(distance - abs(x))..(distance - abs(x))).map { y -> Triple(x, y, abs(x) + abs(y)) }
        }
    }

    fun solve(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        accessibleLocations: List<Pair<Int, Int>>,
        cheat: Int,
    ): Int {
        val distances = computeDistancesFromStart(start, end, accessibleLocations)
        val cheats = getCheats(cheat)
        val results = accessibleLocations.flatMap { (x, y) ->
            cheats.mapNotNull { (deltaX, deltaY, distance) ->
                if (distances.contains(x+ deltaX to y + deltaY)) {
                    distances[x to y]!! - distances[x + deltaX to y + deltaY]!! - distance
                } else {
                    null
                }
            }
        }

        return results.count { it >= 100}
    }

    fun part1(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        accessibleLocations: List<Pair<Int, Int>>,
    ): Int {
        return solve(start, end, accessibleLocations, 2)
    }

    fun part2(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        accessibleLocations: List<Pair<Int, Int>>,
    ): Int {
        return solve(start, end, accessibleLocations, 20)
    }

    var start = 0 to 0
    var end = 0 to 0
    val accessibleLocations = mutableListOf<Pair<Int, Int>>()
    readInput("Day20").forEachIndexed { x, line ->
        line.forEachIndexed { y, c ->
            when (c) {
                'S' -> {
                    start = x to y
                    accessibleLocations.add(x to y)
                }
                'E' -> {
                    end = x to y
                    accessibleLocations.add(x to y)
                }
                '.' -> accessibleLocations.add(x to y)
            }
        }
    }

    part1(start, end, accessibleLocations).println() // 1450
    part2(start, end, accessibleLocations).println() // 1015247
}