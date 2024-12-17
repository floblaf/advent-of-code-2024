import kotlin.math.pow

fun main() {
    var a = 0L
    var b = 0L
    var c = 0L
    val output = mutableListOf<Int>()

    fun getCombo(operand: Int): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> error("invalid program")
        }
    }

    fun adv(operand: Int) {
        a = (a / 2.0.pow(getCombo(operand).toDouble())).toLong()
    }

    fun bxl(operand: Int) {
        b = b.xor(operand.toLong())
    }

    fun bst(operand: Int) {
        b = getCombo(operand) % 8L
    }

    fun jnz(pointer: Int, operand: Int): Int {
        return if (a == 0L) {
            pointer + 2
        } else {
            operand
        }
    }

    fun bxc() {
        b = b.xor(c)
    }

    fun out(operand: Int) {
        output.add((getCombo(operand) % 8L).toInt())
    }

    fun bdv(operand: Int) {
        b = (a / 2.0.pow(getCombo(operand).toDouble())).toLong()
    }

    fun cdv(operand: Int) {
        c = (a / 2.0.pow(getCombo(operand).toDouble())).toLong()
    }

    fun run(a1: Long, b1: Long, c1: Long, instructions: List<Int>): List<Int> {
        a = a1
        b = b1
        c = c1
        output.clear()

        var pointer = 0
        while (pointer < instructions.size) {
            val instruction = instructions[pointer]
            val operand = instructions[pointer + 1]
            when (instruction) {
                0 -> {
                    adv(operand)
                    pointer += 2
                }

                1 -> {
                    bxl(operand)
                    pointer += 2
                }

                2 -> {
                    bst(operand)
                    pointer += 2
                }

                3 -> {
                    pointer = jnz(pointer, operand)
                }

                4 -> {
                    bxc()
                    pointer += 2
                }

                5 -> {
                    out(operand)
                    pointer += 2
                }

                6 -> {
                    bdv(operand)
                    pointer += 2
                }

                7 -> {
                    cdv(operand)
                    pointer += 2
                }
            }
        }
        return output
    }


    fun part1(a: Long, b: Long, c: Long, instructions: List<Int>): String {
        return run(a, b, c, instructions).joinToString(",")
    }

    fun part2(b: Long, c: Long, instructions: List<Int>): Long {
        var searchA = 8L
        while (true) {
            run(searchA, b, c, instructions)
            if (output == instructions) {
                return searchA
            } else {
                if (output.size < instructions.size) {
                    searchA *= 8
                } else {
                    for (i in 1..instructions.size) {
                        if (output[output.size-i] != instructions[instructions.size-i]) {
                            searchA += Math.pow(8.0, (instructions.size.toDouble() - i)).toLong()
                            break
                        }
                    }
                }
            }
        }
    }

    val input = readInput("Day17")
    val aInput = input[0].split(" ").last().toLong()
    val bInput = input[1].split(" ").last().toLong()
    val cInput = input[2].split(" ").last().toLong()
    val instructions = input[4].split(" ").last().split(",").map { it.toInt() }

    part1(aInput, bInput, cInput, instructions).println() // 7,1,5,2,4,0,7,6,1
    part2(bInput, cInput, instructions).println() // 37222273957364
}