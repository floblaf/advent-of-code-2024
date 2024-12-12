import kotlin.math.max

fun main() {

    fun part1(map: List<List<String>>): Int {
        val areas = mutableMapOf<String, Int>()
        val perimeter = mutableMapOf<String, Int>()

        for (x in map.indices) {
            for (y in map[0].indices) {
                val el = map[x][y]
                areas[el] = (areas[el] ?: 0) + 1

                listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (deltaX, deltaY) ->
                    if (map.getOrNull(x + deltaX)?.getOrNull(y + deltaY) != el) {
                        perimeter[el] = (perimeter[el] ?: 0) + 1
                    }
                }
            }
        }

        return areas.mapNotNull { (key, value) ->
            perimeter[key]?.times(value)
        }.sum()
    }

    fun part2(map: List<List<String>>): Int {
        val perimeter = mutableMapOf<String, Int>()
        val areas = map.flatten().groupBy { it }.map { it.key to it.value.count() }.toMap()

        for (i in 0..map.size) {
            val up = map.getOrNull(i - 1)
            val down = map.getOrNull(i)
            val length = max(up?.size ?: 0, down?.size ?: 0)
            var prevUp: String? = null
            var prevDown: String? = null
            for (j in 0..<length) {
                val currentUp = up?.getOrNull(j)
                val currentDown = down?.getOrNull(j)

                if (currentUp != null &&
                    (currentUp != currentDown && currentUp != prevUp ||
                            currentUp != currentDown && prevUp == prevDown)
                ) {
                    perimeter[currentUp] = (perimeter[currentUp] ?: 0) + 1
                }
                if (currentDown != null &&
                    (currentDown != currentUp && currentDown != prevDown ||
                            currentUp != currentDown && prevUp == prevDown)
                ) {
                    perimeter[currentDown] = (perimeter[currentDown] ?: 0) + 1
                }

                prevUp = currentUp
                prevDown = currentDown
            }
        }

        for (i in 0..map[0].size) {
            val left = map.getOrNull(i - 1)
            val right = map.getOrNull(i)
            val width = max(left?.size ?: 0, right?.size ?: 0)
            var prevLeft: String? = null
            var prevRight: String? = null
            for (j in 0..<width) {
                val currentLeft = left?.getOrNull(j)
                val currentRight = right?.getOrNull(j)

                if (currentLeft != null &&
                    (currentLeft != currentRight && currentLeft != prevLeft ||
                            currentLeft != currentRight && prevLeft == prevRight)
                ) {
                    perimeter[currentLeft] = (perimeter[currentLeft] ?: 0) + 1
                }
                if (currentRight != null &&
                    (currentRight != currentLeft && currentRight != prevRight ||
                            currentLeft != currentRight && prevLeft == prevRight)
                ) {
                    perimeter[currentRight] = (perimeter[currentRight] ?: 0) + 1
                }

                prevLeft = currentLeft
                prevRight = currentRight
            }
        }

        return areas.mapNotNull { (key, value) ->
            perimeter[key]?.times(value)
        }.sum()
    }

    val map = readInput("Day12").map {
        it.toList().map { c -> c.toString() }
    }

    fun List<MutableList<String>>.replaceSiblings(x: Int, y: Int, plant: String, counter: Int) {
        this[x][y] = counter.toString()
        listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (deltaX, deltaY) ->
            if (this.getOrNull(x + deltaX)?.getOrNull(y + deltaY) == plant) {
                this.replaceSiblings(x + deltaX, y + deltaY, plant, counter)
            }
        }
    }

    val reworked = map.map { it.toMutableList() }
    val regex = Regex("[A-Z]")
    var counter = 0
    for (i in reworked.indices) {
        for (j in reworked[0].indices) {
            val el = reworked[i][j]
            if (regex.matches(el)) {
                reworked.replaceSiblings(i, j, el, counter)
                counter++
            }
        }
    }

    part1(reworked).println() // 1452678
    part2(reworked).println() // 873584
}