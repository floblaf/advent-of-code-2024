import kotlin.math.pow

fun main() {

    data class Operation(
        val left: String,
        val right: String,
        val operator: Operator,
        val receiver: String
    ) {
        fun solve(values: Map<String, Int>) : Pair<String, Int>? {
            if (values.containsKey(left) && values.containsKey(right)) {
                return receiver to when(operator) {
                    Operator.OR -> values[left]!!.or(values[right]!!)
                    Operator.XOR -> values[left]!!.xor(values[right]!!)
                    Operator.AND -> values[left]!!.and(values[right]!!)
                }
            } else {
                return null
            }
        }
    }

    fun solve(values: Map<String, Int>, operations: Map<String, List<Operation>>): Map<String, Int> {
        val tmp = values.toMutableMap()
        val toProcess = tmp.keys.toMutableList()
        while(toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            operations[current]?.forEach { operation ->
                operation.solve(tmp)?.let { (key, value) ->
                    tmp[key] = value
                    toProcess.add(key)
                }
            }
        }
        return tmp.toMap()
    }

    fun Map<String, Int>.sumLetters(c: Char): Long {
        return this
            .filterKeys { it.startsWith(c) }
            .map { (key, value) -> (value * 2.0.pow(key.removePrefix(c.toString()).toInt())).toLong() }
            .sum()
    }

    fun part1(values: Map<String, Int>, operations: Map<String, List<Operation>>): Long {
        return solve(values, operations)
            .sumLetters('z')
    }

    fun part2(values: Map<String, Int>, operations: Map<String, List<Operation>>): Int {
        return 0
    }


    val values = mutableMapOf<String, Int>()
    val operations = mutableMapOf<String, MutableList<Operation>>()
    val regex = Regex("([a-z0-9]+) (OR|XOR|AND) ([a-z0-9]+) -> ([a-z0-9]+)")

    var readValues = true
    readInput("Day24")
        .forEach { line ->
            if (line.isEmpty()) {
                readValues = false
            }
            else if (readValues) {
                line.split(": ").let {
                    values[it[0]] = it[1].toInt()
                }
            }
            else {
                val result = regex.find(line)!!.groupValues
                val operation = Operation(
                    result[1],
                    result[3],
                    when (result[2]) {
                        "OR" -> Operator.OR
                        "XOR" -> Operator.XOR
                        "AND" -> Operator.AND
                        else -> error("should not happen")
                    },
                    result[4]
                )
                if (!operations.containsKey(result[1])) {
                    operations[result[1]] = mutableListOf(operation)
                } else {
                    operations[result[1]]!!.add(operation)
                }

                if (!operations.containsKey(result[3])) {
                    operations[result[3]] = mutableListOf(operation)
                } else {
                    operations[result[3]]!!.add(operation)
                }
            }
        }

    part1(values, operations).println() // 53190357879014
    part2(values, operations).println() //
}

enum class Operator {
    OR, XOR, AND
}