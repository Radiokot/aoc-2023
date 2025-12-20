fun main() {
    fun part1(input: InputStrings): Any {
        val numberRegex = "(\\d+)".toRegex()
        val symbolPredicate = { char: Char ->
            char != '.' && !char.isDigit()
        }

        return input
            .filterNotEmpty()
            .flatMapIndexed { lineIndex, line ->
                numberRegex
                    .findAll(line)
                    .mapNotNull { numberMatch ->
                        // Check indices of the number and 1 char before and after,
                        // in this line and lines around.

                        val adjacentCheckRange = IntRange(
                            start =
                                (numberMatch.range.first - 1)
                                    .coerceAtLeast(0),
                            endInclusive =
                                (numberMatch.range.last + 1)
                                    .coerceAtMost(line.length - 1)
                        )

                        val isPartNumber =
                            // Check this line and lines around.
                            sequenceOf(
                                lineIndex - 1,
                                lineIndex,
                                lineIndex + 1
                            ).any { lineIndexToCheck ->
                                input
                                    .getOrNull(lineIndexToCheck)
                                    ?.substring(adjacentCheckRange)
                                    ?.any(symbolPredicate) == true
                            }

                        return@mapNotNull if (isPartNumber) numberMatch.value.toLong() else null
                    }
            }
            .sum()
    }

    fun part2(input: InputStrings): Any {
        val numberRegex = "(\\d+)".toRegex()
        val partNumbersByGear = mutableMapOf<Int, MutableList<Long>>()

        input
            .filterNotEmpty()
            .forEachIndexed { lineIndex, line ->
                numberRegex
                    .findAll(line)
                    .forEach { numberMatch ->
                        val adjacentCheckRange = IntRange(
                            start =
                                (numberMatch.range.first - 1)
                                    .coerceAtLeast(0),
                            endInclusive =
                                (numberMatch.range.last + 1)
                                    .coerceAtMost(line.length - 1)
                        )

                        // Check this line and lines around.
                        sequenceOf(
                            lineIndex - 1,
                            lineIndex,
                            lineIndex + 1
                        ).forEach { lineIndexToCheck ->
                            // If there is an adjacent gear in the line,
                            // add the part number to it.
                            val adjacentGearIndex =
                                input
                                    .getOrNull(lineIndexToCheck)
                                    ?.substring(adjacentCheckRange)
                                    ?.indexOf('*')
                                    ?.takeIf { it >= 0 }
                                    // Turn substring index into the global gear index in the input.
                                    ?.let { lineIndexToCheck * line.length + adjacentCheckRange.first + it }

                            if (adjacentGearIndex != null) {
                                partNumbersByGear
                                    .getOrPut(adjacentGearIndex, ::mutableListOf)
                                    .add(numberMatch.value.toLong())
                            }
                        }
                    }
            }

        return partNumbersByGear
            .values
            .filter { it.size == 2 }
            .sumOf { it[0] * it[1] }
    }

    val input =
//        readInput("Day03_test")
        readInput("Day03")

    part1(input).println()
    part2(input).println()
}
