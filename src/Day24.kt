import kotlin.math.pow

fun main() {

    data class Operation(
        val left: String,
        val right: String,
        val operator: Operator,
        val receiver: String
    ) {
        fun solve(values: Map<String, Int>): Pair<String, Int>? {
            if (values.containsKey(left) && values.containsKey(right)) {
                return receiver to when (operator) {
                    Operator.OR -> values[left]!!.or(values[right]!!)
                    Operator.XOR -> values[left]!!.xor(values[right]!!)
                    Operator.AND -> values[left]!!.and(values[right]!!)
                }
            } else {
                return null
            }
        }
    }

    fun solve(values: Map<String, Int>, operationsByGate: Map<String, List<Operation>>): Map<String, Int> {
        val tmp = values.toMutableMap()
        val toProcess = tmp.keys.toMutableList()
        while (toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            operationsByGate[current]?.forEach { operation ->
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

    fun part1(values: Map<String, Int>, operationsByGate: Map<String, List<Operation>>): Long {
        return solve(values, operationsByGate)
            .sumLetters('z')
    }

    fun part2(operations: List<Operation>): String {
        // Good xor reading x/y values
        val startXor = operations
            .filter {
                it.operator == Operator.XOR && (it.left.startsWith("x") && it.right.startsWith("y") || it.left.startsWith(
                    "y"
                ) && it.right.startsWith("x")) && (!it.receiver.startsWith("z") || it.receiver == "z00")
            }

        // Good operation making z
        val goodXorZ = operations.filter {
            it.operator == Operator.XOR && it.receiver.startsWith("z")
        }

        // Bad end xor (should be z if not from x y)
        val badEndXor = operations
            .filter {
                it.operator == Operator.XOR && it !in goodXorZ && it !in startXor
            }
            .map { it.receiver }

        val badStartXor = startXor.map { it.receiver }.filter { xor -> xor != "z00" && operations.firstOrNull { it.operator == Operator.XOR && (it.left == xor || it.right == xor) } == null }

        // Operation making z
        val setZ = operations.filter { it.receiver.startsWith("z") }

        // Operation making Z that are not XOR (except the last one)
        val badZ = setZ.filter { it.operator != Operator.XOR && it.receiver != "z45" }
            .map { it.receiver }


        // List of AND op receiving carry from x/y
        val andXY = operations
            .filter {
                it.operator == Operator.AND && (it.left.startsWith("x") || it.left.startsWith("y")) && !it.left.endsWith("00")
            }
            .map { it.receiver }

        // OR operations
        val orOp = operations.filter { it.operator == Operator.OR }

        val badOr = orOp.map { it.receiver }.filter { or -> operations.firstOrNull { (it.left == or || it.right == or) && it.operator == Operator.XOR } == null }
        println(badOr)

        // all and xy should be used in OR
        val badAnd = andXY.filter { and -> orOp.firstOrNull { it.left == and || it.right == and } == null}

        val result = (badZ + badEndXor + badAnd + badStartXor).toSortedSet().joinToString(",")
        return result
    }


    val values = mutableMapOf<String, Int>()
    val operations = mutableListOf<Operation>()
    val operationsByGate = mutableMapOf<String, MutableList<Operation>>()
    val regex = Regex("([a-z0-9]+) (OR|XOR|AND) ([a-z0-9]+) -> ([a-z0-9]+)")

    var readValues = true
    readInput("Day24")
        .forEach { line ->
            if (line.isEmpty()) {
                readValues = false
            } else if (readValues) {
                line.split(": ").let {
                    values[it[0]] = it[1].toInt()
                }
            } else {
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

                operations.add(operation)

                if (!operationsByGate.containsKey(result[1])) {
                    operationsByGate[result[1]] = mutableListOf(operation)
                } else {
                    operationsByGate[result[1]]!!.add(operation)
                }

                if (!operationsByGate.containsKey(result[3])) {
                    operationsByGate[result[3]] = mutableListOf(operation)
                } else {
                    operationsByGate[result[3]]!!.add(operation)
                }
            }
        }

    part1(values, operationsByGate).println() // 53190357879014
    part2(operations).println() // bks,hnd,nrn,tdv,tjp,z09,z16,z23
}

enum class Operator {
    OR, XOR, AND
}