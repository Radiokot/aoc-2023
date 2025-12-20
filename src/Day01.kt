fun main() {
    fun part1(input: InputStrings): Any {
        return input
            .filterNotEmpty()
            .sumOf { line ->
                line.first(Char::isDigit).digitToInt() * 10 +
                        line.last(Char::isDigit).digitToInt()
            }
    }

    fun part2(input: InputStrings): Any {
        val digitMapping = mapOf(
            "1" to 1,
            "one" to 1,
            "2" to 2,
            "two" to 2,
            "3" to 3,
            "three" to 3,
            "4" to 4,
            "four" to 4,
            "5" to 5,
            "five" to 5,
            "6" to 6,
            "six" to 6,
            "7" to 7,
            "seven" to 7,
            "8" to 8,
            "eight" to 8,
            "9" to 9,
            "nine" to 9,
        )

        return input
            .filterNotEmpty()
            .sumOf { line ->
                // For the first digit,
                // take larger and larger line pieces, from the start,
                // until a known digit literal appears at its end.
                var firstDigit: Int? = null
                (1..line.length)
                    .asSequence()
                    .map(line::take)
                    .first { lineStart ->
                        firstDigit =
                            digitMapping
                                .entries
                                .find { lineStart.endsWith(it.key) }
                                ?.value
                        firstDigit != null
                    }

                // For the second digit,
                // take larger and larger line pieces, from the end,
                // until a known digit literal appears at its start.
                var lastDigit: Int? = null
                (1..line.length)
                    .asSequence()
                    .map(line::takeLast)
                    .first { lineEnd ->
                        lastDigit =
                            digitMapping
                                .entries
                                .find { lineEnd.startsWith(it.key) }
                                ?.value
                        lastDigit != null
                    }

                firstDigit!! * 10 + lastDigit!!
            }
    }

    val input =
//        readInput("Day01_test")
        readInput("Day01")

    part1(input).println()
    part2(input).println()
}
