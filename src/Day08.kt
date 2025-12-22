fun main() {
    fun part1(input: InputStrings): Any {
        val instructions = input[0]
        val connections = input.getConnections()

        var stepCount = 0
        var currentNode = "AAA"
        do {
            val instruction = instructions[stepCount++ % instructions.length]
            currentNode =
                if (instruction == 'L')
                    connections[currentNode]!!.left
                else
                    connections[currentNode]!!.right
        } while (currentNode != "ZZZ")

        return stepCount
    }

    fun part2(input: InputStrings): Any {
        val instructions = input[0]
        val connections = input.getConnections()

        var stepCount = 0L
        val currentNodes =
            connections
                .keys
                .filterTo(mutableListOf()) { it.endsWith('A') }
        val stepCountsByNodeIndex = MutableList(currentNodes.size) { 0L }

        do {
            val instruction = instructions[(stepCount++ % instructions.length).toInt()]
            currentNodes.indices.forEach { nodeIndex ->
                if (currentNodes[nodeIndex].endsWith('Z')) {
                    return@forEach
                }

                currentNodes[nodeIndex] =
                    if (instruction == 'L')
                        connections[currentNodes[nodeIndex]]!!.left
                    else
                        connections[currentNodes[nodeIndex]]!!.right

                if (currentNodes[nodeIndex].endsWith('Z')) {
                    stepCountsByNodeIndex[nodeIndex] = stepCount
                }
            }
        } while (stepCountsByNodeIndex.any { it == 0L })

        return stepCountsByNodeIndex
            .joinToString(
                prefix = "https://www.calculatorsoup.com/calculators/math/lcm.php?input=",
                postfix = "&data=none&action=solve",
                separator = "+",
            )
    }

    val input =
//        readInput("Day08_test")
        readInput("Day08")

    part1(input).println()
    part2(input).println()
}

private typealias Connection = Pair<String, String>

private val Connection.left: String
    get() = first

private val Connection.right: String
    get() = second

private fun InputStrings.getConnections(): Map<String, Connection> =
    this
        .filterNotEmpty()
        .drop(1)
        .associate { line ->
            val node = line.substringBefore(" =")

            val (leftConnection, rightConnection) =
                line
                    .substringBefore(')')
                    .substringAfter('(')
                    .split(", ")

            node to Connection(leftConnection, rightConnection)
        }
