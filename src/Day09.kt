fun main() {

    fun part1(input: List<Int>): Long {
        val disk = input.flatMapIndexed { index: Int, s: Int ->
            List(s) { if (index % 2 == 0) index / 2 else -1 }
        }
        val result = mutableListOf<Int>()
        val tmp = disk.toMutableList()

        while (tmp.isNotEmpty()) {
            if (tmp.first() != -1) {
                result.add(tmp.first())
                tmp.removeFirst()
            } else {
                if (tmp.last() == -1) {
                    tmp.removeLast()
                } else {
                    result.add(tmp.last())
                    tmp.removeLast()
                    tmp.removeFirst()
                }
            }
        }

        return result.mapIndexed { index, i -> (index * i).toLong() }
            .sum()
    }

    fun part2(input: List<Int>): Long {
        val disk = input
            .mapIndexed { index, i ->
                if (index % 2 == 0) {
                    index / 2 to i
                } else {
                    -1 to i
                }
            }
            .toMutableList()

        val max = disk.findLast { it.first != -1 }!!.first
        // 00 99 2 111 777 . 44 . 333 .... 5555 . 6666 ..... 8888 ..
        for (i in max.downTo(0)) {
            val elIndex = disk.indexOfFirst { it.first == i }
            val el = disk[elIndex]
            val spaceIndex = disk.indexOfFirst { it.first == -1 && it.second >= el.second }

            if (spaceIndex != -1 && spaceIndex < elIndex) {
                val space = disk[spaceIndex]
                val delta = space.second - el.second

                disk[spaceIndex] = el
                disk[elIndex] = -1 to el.second
                disk.add(spaceIndex + 1, -1 to delta)
            }
        }

        return disk
            .flatMap { s: Pair<Int, Int> ->
                List(s.second) { s.first }
            }
            .mapIndexed { index, i -> (index * if (i != -1) i else 0).toLong() }
            .sum()
    }

    val input = readInput("Day09").flatMap { it.toCharArray().map { it.toString().toInt() }.toList() }

    part1(input).println() // 6353658451014
    part2(input).println() // 6382582136592
}