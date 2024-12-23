import kotlin.math.abs

fun main() {
    val numericKeypad = mutableMapOf(
        'A' to (3 to 2),
        '0' to (3 to 1),
        '1' to (2 to 0),
        '2' to (2 to 1),
        '3' to (2 to 2),
        '4' to (1 to 0),
        '5' to (1 to 1),
        '6' to (1 to 2),
        '7' to (0 to 0),
        '8' to (0 to 1),
        '9' to (0 to 2),
    )

    val directionalKeypad = mutableMapOf(
        '^' to (0 to 1),
        'A' to (0 to 2),
        '<' to (1 to 0),
        'v' to (1 to 1),
        '>' to (1 to 2),
    )

    fun possibleInstructions(pad: Map<Char, Pair<Int, Int>>, from: Char, to: Char): Set<String> {
        when (from to to) {
            '7' to 'A' -> ">>vvv"
            '7' to '0' -> ">vvv"
            '4' to 'A' -> ">>vv"
            '4' to '0' -> ">vv"
            '1' to 'A' -> ">>v"
            '1' to '0' -> ">v"
            '0' to '7' -> "^^^<"
            '0' to '4' -> "^^<"
            '0' to '1' -> "^<"
            'A' to '7' -> "^^^<<"
            'A' to '4' -> "^^<<"
            'A' to '1' -> "^<<"
            '<' to '^' -> ">^"
            '<' to 'A' -> ">>^"
            '^' to '<' -> "v<"
            'A' to '<' -> "v<<"
            else -> null
        }?.let { short ->
            return setOf(short + "A")
        }


        val deltaX = pad[from]!!.first - pad[to]!!.first
        val deltaY = pad[from]!!.second - pad[to]!!.second

        val horizontal = when {
            deltaY > 0 -> "<".repeat(abs(deltaY))
            deltaY < 0 -> ">".repeat(abs(deltaY))
            else -> ""
        }

        val vertical = when {
            deltaX > 0 -> "^".repeat(abs(deltaX))
            deltaX < 0 -> "v".repeat(abs(deltaX))
            else -> ""
        }

        return setOf(horizontal + vertical + "A", vertical + horizontal + "A")
    }


    fun codeToInstructions(code: String, pad: Map<Char, Pair<Int, Int>>): List<String> {
        var from = 'A'
        var instructions = listOf("")
        code.forEach { to ->
            instructions = possibleInstructions(pad, from, to).flatMap { possible ->
                instructions.map { it + possible  }
            }
            from = to
        }

        return instructions
    }

    fun part1(codes: List<String>): Int {
        val result = codes.associateWith { code ->
            codeToInstructions(code, numericKeypad)
                .flatMap { instruction -> codeToInstructions(instruction, directionalKeypad) }
                .flatMap { instruction -> codeToInstructions(instruction, directionalKeypad) }
                .minBy { it.length }.length
        }

        return result.map { (key, shortest) -> key.removeSuffix("A").toInt() * shortest }.sum()
    }

    val cache = mutableMapOf<Pair<String, Int>, Long>()

    fun findShortest(instruction: String, totalRobot: Int, currentRobot: Int): Long {
        if (totalRobot == currentRobot) {
            return instruction.length.toLong()
        }

        if (cache.containsKey(instruction to currentRobot)) {
            return cache[instruction to currentRobot]!!
        }

        return codeToInstructions(instruction, directionalKeypad).map { newInstruction ->
            newInstruction
                .split("A")
                .let { it.subList(0, it.size - 1) }
                .map { it + "A" }
                .sumOf {
                    val result = findShortest(it, totalRobot, currentRobot + 1)
                    cache[it to currentRobot+1] = result
                    result
                }
        }.min()
    }

    fun part2(codes: List<String>): Long {
        val result = codes.associateWith { code ->
            codeToInstructions(code, numericKeypad)
                .map { findShortest(it, 25, 0) }
                .minBy { it }
        }

        return result.map { (key, shortest) -> key.removeSuffix("A").toInt() * shortest }.sum()
    }

    val codes = readInput("Day21")

    part1(codes).println() // 197560
    part2(codes).println() // 242337182910752
}