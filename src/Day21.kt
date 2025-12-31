fun main() {
    fun part1(input: InputStrings): Any {
        val map =
            input
                .filterNotEmpty()
                .map(String::toList)
        val startPosition =
            map
                .indexOfFirst { it.contains('S') }
                .let { row ->
                    Position(row, map[row].indexOf('S'))
                }

        var possiblePositions = setOf(startPosition)
        repeat(64) {
            possiblePositions =
                possiblePositions
                    .flatMap { position ->
                        listOf(
                            position.left,
                            position.up,
                            position.right,
                            position.down,
                        )
                    }
                    .filterNot { map[it] == '#' || map[it] == 'X' }
                    .toSet()
        }
        return possiblePositions.size
    }

    fun part2(input: InputStrings): Any {
        return input.size
    }

    val input =
//        readInput("Day21_test")
        readInput("Day21")

    part1(input).println()
    part2(input).println()
}
