fun main() {
    data class Vector(val x: Int, val y: Int)
    data class Coordinates(val x: Int, val y: Int) {
        fun getVector(other: Coordinates): Vector {
            return Vector(
                x = other.x - x,
                y = other.y - y
            )
        }

        fun addVector(vector: Vector, multiplier: Int = 1): Coordinates {
            return Coordinates(
                x = x + multiplier * vector.x,
                y = y + multiplier * vector.y
            )
        }

        fun removeVector(vector: Vector): Coordinates {
            return Coordinates(
                x = x - vector.x,
                y = y - vector.y
            )
        }
    }


    fun part1(antennas: Map<Char, List<Coordinates>>, rangeX: IntRange, rangeY: IntRange): Int {
        val antinodes = mutableSetOf<Coordinates>()

        antennas.forEach { (_, list) ->
            list.forEach { a ->
                list.forEach { b ->
                    if (a != b) {
                        a.getVector(b).let {
                            val new = a.removeVector(it)
                            if (new.x in rangeX && new.y in rangeY) {
                                antinodes.add(new)
                            }
                        }
                    }
                }
            }
        }

        return antinodes.size
    }

    fun part2(antennas: Map<Char, List<Coordinates>>, rangeX: IntRange, rangeY: IntRange): Int {
        val antinodes = mutableSetOf<Coordinates>()

        antennas.forEach { (_, list) ->
            list.forEach { a ->
                list.forEach { b ->
                    if (a != b) {
                        a.getVector(b).let {
                            var multiplier = 1
                            var new = a.addVector(it, multiplier)
                            while (new.x in rangeX && new.y in rangeY) {
                                antinodes.add(new)
                                multiplier++
                                new = a.addVector(it, multiplier)
                            }
                        }
                    }
                }
            }
        }

        return antinodes.size
    }

    val input = readInput("Day08").map { it.toCharArray() }
    val antennas = mutableMapOf<Char, MutableList<Coordinates>>()

    input.forEachIndexed { x, line ->
        line.forEachIndexed { y, char ->
            if (char != '.') {
                if (!antennas.containsKey(char)) {
                    antennas[char] = mutableListOf()
                }
                antennas[char]?.add(Coordinates(x, y))
            }
        }
    }

    part1(antennas, input.indices, input[0].indices).println() // 293
    part2(antennas, input.indices, input[0].indices).println() // 934
}