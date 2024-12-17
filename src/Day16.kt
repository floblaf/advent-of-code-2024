fun main() {
    data class Location(
        val x: Int,
        val y: Int,
        val direction: Direction,
        val score: Long,
        val previous: Set<String>
    )

    data class Move(
        val deltaX: Int,
        val deltaY: Int,
        val direction: Direction,
        val weight: Long
    )

    val config = mapOf(
        Direction.EAST to listOf(
            Move(-1, 0, Direction.NORTH, 1001),
            Move(1, 0, Direction.SOUTH, 1001),
            Move(0, 1, Direction.EAST, 1),
        ),
        Direction.NORTH to listOf(
            Move(-1, 0, Direction.NORTH, 1),
            Move(0, -1, Direction.WEST, 1001),
            Move(0, 1, Direction.EAST, 1001),
        ),
        Direction.WEST to listOf(
            Move(-1, 0, Direction.NORTH, 1001),
            Move(1, 0, Direction.SOUTH, 1001),
            Move(0, -1, Direction.WEST, 1),
        ),
        Direction.SOUTH to listOf(
            Move(0, -1, Direction.WEST, 1001),
            Move(1, 0, Direction.SOUTH, 1),
            Move(0, 1, Direction.EAST, 1001),
        )
    )

    fun solve(accessibleLocations: Set<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Pair<Long, Int> {
        val visitedLocation = mutableMapOf<String, Long>()
        val toVisit = mutableListOf<Location>()
        var bestScore = Long.MAX_VALUE
        val bestPath = mutableSetOf<String>()
        toVisit.add(Location(start.first, start.second, Direction.EAST, 0, setOf()))

        while (toVisit.isNotEmpty()) {
            val location = toVisit.removeFirst()

            if (location.x == end.first && location.y == end.second && location.score <= bestScore) {
                if (location.score < bestScore) {
                    bestPath.clear()
                }
                bestScore = location.score
                bestPath.addAll(location.previous + "${location.x};${location.y}")
                continue
            }

            if (location.score > bestScore) {
                continue
            }

            val visitedScore = visitedLocation["${location.x};${location.y};${location.direction}"]
            if (visitedScore != null && visitedScore < location.score) {
                continue
            }
            visitedLocation["${location.x};${location.y};${location.direction}"] = location.score

            config[location.direction]?.forEach { move ->
                val newLocation = Location(
                    location.x + move.deltaX,
                    location.y + move.deltaY,
                    move.direction,
                    location.score + move.weight,
                    location.previous + "${location.x};${location.y}"
                )
                if (accessibleLocations.contains(newLocation.x to newLocation.y)) {
                    toVisit.add(newLocation)
                }
            }
        }

        return bestScore to bestPath.size
    }


    fun part1(accessibleLocations: Set<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Long {
        return solve(accessibleLocations,start, end).first
    }

    fun part2(accessibleLocations: Set<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        return solve(accessibleLocations,start, end).second
    }

    val accessibleLocations = mutableSetOf<Pair<Int, Int>>()
    var start = 0 to 0
    var end = 0 to 0
    readInput("Day16").forEachIndexed { x, list ->
        list.toList().forEachIndexed { y, c ->
            when (c) {
                '.' -> {
                    accessibleLocations.add(x to y)
                }

                'E' -> {
                    end = x to y
                    accessibleLocations.add(x to y)
                }

                'S' -> {
                    start = x to y
                }
            }
        }
    }

    part1(accessibleLocations, start, end).println() // 90440
    part2(accessibleLocations, start, end).println() // 479
}

enum class Direction {
    EAST, WEST, SOUTH, NORTH
}