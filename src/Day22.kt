fun main() {

    fun Long.mix(secret: Long): Long {
        return this.xor(secret)
    }

    fun Long.prune(): Long {
        return this % 16777216
    }

    fun op1(secret: Long): Long {
        return (secret * 64L).mix(secret).prune()
    }

    fun op2(secret: Long): Long {
        return (secret / 32L).mix(secret).prune()
    }

    fun op3(secret: Long): Long {
        return (secret * 2048).mix(secret).prune()
    }

    fun Long.newSecret(): Long {
        return op3(op2(op1(this)))
    }

    fun part1(monkeys: List<Long>): Long {
        var secrets = monkeys
        repeat(2000) {
            secrets = secrets.map { it.newSecret() }
        }
        return secrets.sum()
    }

    fun part2(monkeys: List<Long>): Long {
        val bestPrices = mutableMapOf<String, Long>()

        monkeys.forEach { initial ->
            var secret = initial
            val secrets = mutableListOf<Pair<String, Long>>()
            repeat(2000) {
                val newSecret = secret.newSecret()
                val bananas = newSecret % 10
                val delta = (newSecret % 10) - (secret % 10)
                secrets.add(delta.toString() to bananas)

                secret = newSecret
            }

            val sequences = (0..secrets.size - 4)
                .map { i ->
                    val key =
                        "${secrets[i].first};${secrets[i + 1].first};${secrets[i + 2].first};${secrets[i + 3].first}"
                    key to secrets[i + 3].second
                }
                .groupBy { it.first }
                .mapValues { it.value.first().second }

            sequences.forEach { (key, value) ->
                if (!bestPrices.contains(key)) {
                    bestPrices[key] = value
                } else {
                    bestPrices[key] = bestPrices[key]!! + value
                }
            }
        }

        return bestPrices.values.max()
    }


    val monkeys = readInput("Day22").map { it.toLong() }

    part1(monkeys).println() // 18941802053
    part2(monkeys).println() // 2218
}