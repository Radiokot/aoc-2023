import java.util.*

fun main() {
    fun part1(input: InputStrings): Any {
        return input
            .toHistoryValues()
            .sumOf { historyValues ->
                var differenceValues = historyValues
                val lastValues = LinkedList<Long>()

                do {
                    lastValues.push(differenceValues.last())
                    differenceValues = differenceValues.mapIndexedNotNull { index, value ->
                        differenceValues
                            .getOrNull(index + 1)
                            ?.let { it - value }
                    }
                } while (!differenceValues.all { it == 0L })

                return@sumOf lastValues.reduce { sumOfPrevious, value -> value + sumOfPrevious }
            }
    }

    fun part2(input: InputStrings): Any {
        return input
            .toHistoryValues()
            .sumOf { historyValues ->
                var differenceValues = historyValues
                val firstValues = LinkedList<Long>()

                do {
                    firstValues.push(differenceValues.first())
                    differenceValues = differenceValues.mapIndexedNotNull { index, value ->
                        differenceValues
                            .getOrNull(index + 1)
                            ?.let { it - value }
                    }
                } while (!differenceValues.all { it == 0L })

                return@sumOf firstValues.reduce { differenceOfPrevious, value -> value - differenceOfPrevious }
            }
    }

    val input =
//        readInput("Day09_test")
        readInput("Day09")

    part1(input).println()
    part2(input).println()
}

private fun InputStrings.toHistoryValues(): List<List<Long>> =
    this
        .filterNotEmpty()
        .map { line ->
            line
                .split(' ')
                .map(String::toLong)
        }
