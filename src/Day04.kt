fun main() {
    fun part1(input: InputStrings): Any {
        return input
            .toCards()
            .sumOf(Card::points)
    }

    fun part2(input: InputStrings): Any {
        val cards = input.toCards()
        val cardCounts = MutableList<Long>(cards.size) { 1 }

        cards.forEachIndexed { cardIndex, card ->
            val thisCardCount = cardCounts[cardIndex]
            (1..card.presentWinningNumberCount)
                .map { (cardIndex + it).coerceAtMost(cards.size - 1) }
                .forEach { wonCardIndex ->
                    cardCounts[wonCardIndex] += 1 * thisCardCount
                }
        }

        return cardCounts.sum()
    }

    val input =
//        readInput("Day04_test")
        readInput("Day04")

    part1(input).println()
    part2(input).println()
}

private data class Card(
    val winningNumbers: Set<Int>,
    val presentNumbers: Set<Int>,
) {
    val presentWinningNumberCount: Int =
        presentNumbers.count { it in winningNumbers }

    val points: Long =
        if (presentWinningNumberCount != 0)
            1L shl (presentWinningNumberCount - 1)
        else
            0
}

private fun InputStrings.toCards(): List<Card> {
    val spaceRegex = "\\s+".toRegex()
    return filterNotEmpty()
        .map { line ->
            val winningNumbers =
                line
                    .substringAfter(':')
                    .substringBefore('|')
                    .trim()
                    .split(spaceRegex)
                    .mapTo(mutableSetOf(), String::toInt)

            val presentNumbers =
                line
                    .substringAfter('|')
                    .trim()
                    .split(spaceRegex)
                    .mapTo(mutableSetOf(), String::toInt)

            Card(winningNumbers, presentNumbers)
        }
}
