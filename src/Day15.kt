fun main() {
    fun part1(map: Map<Pair<Int, Int>, Char>, moves: List<Char>): Int {
        val tmp = map.toMutableMap()

        moves.forEach { direction ->
            val (deltaX, deltaY) = when (direction) {
                '^' -> -1 to 0
                'v' -> 1 to 0
                '>' -> 0 to 1
                '<' -> 0 to -1
                else -> error("should not happen")
            }

            var position = tmp.filterValues { it == '@' }.keys.first()
            val actions = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            var stop = false
            while (!stop) {
                val newPosition = position.first + deltaX to position.second + deltaY
                if (tmp[newPosition] == '#') {
                    stop = true
                    actions.clear()
                } else if (tmp[newPosition] == 'O') {
                    actions.add(position to newPosition)
                    position = newPosition
                } else if (tmp[newPosition] == '.') {
                    actions.add(position to newPosition)
                    stop = true
                } else {
                    error("should not happen")
                }
            }
            actions.reversed().forEachIndexed { i, it ->
                tmp[it.second] = tmp[it.first]!!
                if (i == actions.size - 1) {
                    tmp[actions.first().first] = '.'
                }
            }
        }

        return tmp
            .filterValues { it == 'O' }
            .keys
            .sumOf { it.first * 100 + it.second }
    }

    fun part2(map: Map<Pair<Int, Int>, Char>, moves: List<Char>): Int {
        val tmp = map.toMutableMap()

        moves.forEach { direction ->
            val (deltaX, deltaY) = when (direction) {
                '^' -> -1 to 0
                'v' -> 1 to 0
                '>' -> 0 to 1
                '<' -> 0 to -1
                else -> error("should not happen")
            }

            var positionToMove = tmp.filterValues { it == '@' }.keys.toMutableList()
            val changes = mutableMapOf<Pair<Int, Int>, Char>()
            changes[positionToMove.first()] = '.'
            while (positionToMove.isNotEmpty()) {
                val position = positionToMove.removeFirst()
                val newPosition = position.first + deltaX to position.second + deltaY
                if (tmp[newPosition] == '#') {
                    changes.clear()
                    break
                } else if (tmp[newPosition] == '[') {
                    changes[newPosition] = tmp[position]!!
                    positionToMove.add(newPosition)
                    if (direction == '^' || direction == 'v') {
                        if (changes[newPosition.first to newPosition.second + 1] == null) {
                            changes[newPosition.first to newPosition.second + 1] = '.'
                        }
                        positionToMove.add(newPosition.first to newPosition.second + 1)
                    }
                } else if (tmp[newPosition] == ']') {
                    changes[newPosition] = tmp[position]!!
                    positionToMove.add(newPosition)
                    if (direction == '^' || direction == 'v') {
                        if (changes[newPosition.first to newPosition.second - 1] == null) {
                            changes[newPosition.first to newPosition.second - 1] = '.'
                        }
                        positionToMove.add(newPosition.first to newPosition.second - 1)
                    }
                } else if (tmp[newPosition] == '.') {
                    changes[newPosition] = tmp[position]!!
                } else {
                    error("should not happen")
                }
            }

            changes.forEach {
                tmp[it.key] = it.value
            }
        }

        return tmp
            .filterValues { it == '[' }
            .keys
            .sumOf { it.first * 100 + it.second }
    }

    val map = mutableMapOf<Pair<Int, Int>, Char>()
    val duplicatedMap = mutableMapOf<Pair<Int, Int>, Char>()
    val moves = mutableListOf<Char>()
    var isMap = true
    readInput("Day15").forEachIndexed { x, list ->
        if (list.isEmpty()) {
            isMap = false
        } else if (isMap) {
            list.toList().forEachIndexed { y, c ->
                map[x to y] = c
                duplicatedMap[x to y*2] = when(c){
                    '#' -> '#'
                    'O' -> '['
                    '@' -> '@'
                    '.' -> '.'
                    else -> error("should not happen")
                }
                duplicatedMap[x to y*2+1] = when(c){
                    '#' -> '#'
                    'O' -> ']'
                    '@' -> '.'
                    '.' -> '.'
                    else -> error("should not happen")
                }
            }
        } else {
            moves.addAll(list.toList())
        }
    }

    part1(map, moves).println() // 1479679
    part2(duplicatedMap, moves).println() // 1509780
}