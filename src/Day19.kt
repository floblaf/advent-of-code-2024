fun main() {

    val cache = mutableMapOf<String, Long>()

    fun countSolutions(pattern: String, towels: List<String>): Long {
        val cached = cache[pattern]
        if (cached != null) {
            return cached
        }

        if (pattern.isEmpty()) {
            return 1
        }

        var count = 0L
        towels.forEach {
            if (pattern.startsWith(it)) {
                count += countSolutions(pattern.removePrefix(it), towels)
            }
        }

        cache[pattern] = count
        return count
    }

    fun part1(patterns: List<String>, towels: List<String>): Long {
        return patterns.count {
            countSolutions(it, towels) > 0L
        }.toLong()
    }

    fun part2(patterns: List<String>, towels: List<String>): Long {
        return patterns.sumOf {
            countSolutions(it, towels)
        }
    }

    val input = readInput("Day19")
    val towels = input[0].split(", ")
    val patterns = input.subList(2, input.size)

    part1(patterns, towels).println() // 278
    part2(patterns, towels).println() // 569808947758890
}