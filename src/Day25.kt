fun main() {

    fun part1(locks: List<List<Pair<Int, Int>>>, keys: List<List<Pair<Int, Int>>>): Int {
        var counter = 0
        locks.forEach { lock ->
            keys.forEach { key ->
                if (lock.intersect(key).isEmpty()) {
                    counter++
                }
            }
        }

        return counter
    }

    fun part2(): Int {
        return 0
    }

    val keys = mutableListOf<List<Pair<Int, Int>>>()
    val locks = mutableListOf<List<Pair<Int, Int>>>()

    var x = 0
    var isLock = false
    var tmpObject = mutableListOf<Pair<Int, Int>>()
    readInput("Day25").forEach { line ->
        if (line.isEmpty()) {
            if (isLock) {
                locks.add(tmpObject.toList())
            } else {
                keys.add(tmpObject.toList())
            }

            tmpObject.clear()
            x = 0
        } else {
            if (x == 0) {
                isLock = line.contains("#")
            }

            line.forEachIndexed { y, c ->
                if (c == '#') {
                    tmpObject.add(x to y)
                }
            }
            x++
        }
    }
    if (isLock) {
        locks.add(tmpObject.toList())
    } else {
        keys.add(tmpObject.toList())
    }

    part1(locks, keys).println() //
    part2().println() //
}