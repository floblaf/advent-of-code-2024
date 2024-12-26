fun main() {

    fun part1(network: Map<String, List<String>>): Int {
        val triples = mutableSetOf<String>()
        network.forEach { (first, neighbors) ->
            neighbors.forEach { second ->
                network[second]?.forEach { third ->
                    if (third in neighbors) {
                        triples.add(listOf(first, second, third).sorted().joinToString("-"))
                    }
                }
            }
        }
        return triples.count { it.startsWith("t") || it.contains("-t") }
    }

    fun part2(network: Map<String, List<String>>): String {
        var lans = mutableSetOf<List<String>>()
        network.forEach { (node, neighbors) ->
            neighbors.forEach {
                lans.add(listOf(node, it).sorted())
            }
        }

        var newLans: MutableSet<List<String>>
        var foundOne = true
        while (foundOne) {
            newLans = mutableSetOf()
            network.forEach { (node, neighbors) ->
                lans.forEach { lan ->
                    if (neighbors.containsAll(lan)) {
                        newLans.add((lan + node).sorted())
                    }
                }
            }

            if (newLans.isEmpty()) {
                foundOne = false
            } else {
                lans = newLans
            }
        }

        return lans.first().joinToString(",")
    }


    val network = mutableMapOf<String, MutableList<String>>()
    readInput("Day23")
        .forEach { line ->
            val nodes = line.split("-")
            if (!network.containsKey(nodes[0])) {
                network[nodes[0]] = mutableListOf(nodes[1])
            } else {
                network[nodes[0]]!!.add(nodes[1])
            }

            if (!network.containsKey(nodes[1])) {
                network[nodes[1]] = mutableListOf(nodes[0])
            } else {
                network[nodes[1]]!!.add(nodes[0])
            }
        }

    part1(network).println() // 1054
    part2(network).println() // ch,cz,di,gb,ht,ku,lu,tw,vf,vt,wo,xz,zk
}