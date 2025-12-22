fun main() {
    fun part1(input: InputStrings): Any {
        return input
            .toSortedHandsAndBids(part1HandComparator)
            .withIndex()
            .sumOf { (index, handAndBid) ->
                val rank = index + 1
                handAndBid.second * rank
            }
    }

    fun part2(input: InputStrings): Any {
        return input
            .toSortedHandsAndBids(part2HandComparator)
            .withIndex()
            .sumOf { (index, handAndBid) ->
                val rank = index + 1
                handAndBid.second * rank
            }
    }

    val input =
//        readInput("Day07_test")
        readInput("Day07")

    part1(input).println()
    part2(input).println()
}

private typealias Hand = String

private enum class HandType {
    /**
     * where all cards' labels are distinct: 23456
     */
    HighCard,

    /**
     * where two cards share one label, and the other three cards
     * have a different label from the pair and each other: A23A4
     */
    OnePair,

    /**
     * where two cards share one label, two other cards share a second label,
     * and the remaining card has a third label: 23432
     */
    TwoPair,

    /**
     * where three cards have the same label,
     * and the remaining two cards are each different from any other card in the hand: TTT98
     */
    ThreeOfAKind,

    /**
     * where three cards have the same label,
     * and the remaining two cards share a different label: 23332
     */
    FullHouse,

    /**
     * where four cards have the same label and one card has a different label: AA8AA
     */
    FourOfAKind,

    /**
     * where all five cards have the same label: AAAAA
     */
    FiveOfAKind,
}

private val Hand.part1Type: HandType
    get() {
        val countByCard = this.toSet().associateWith { card ->
            this.count { it == card }
        }

        return when {
            countByCard.size == 1 ->
                HandType.FiveOfAKind

            countByCard.any { it.value == 4 } ->
                HandType.FourOfAKind

            countByCard.any { it.value == 3 } && countByCard.any { it.value == 2 } ->
                HandType.FullHouse

            countByCard.any { it.value == 3 } ->
                HandType.ThreeOfAKind

            countByCard.count { it.value == 2 } == 2 ->
                HandType.TwoPair

            countByCard.size == length - 1 ->
                HandType.OnePair

            else ->
                HandType.HighCard
        }
    }

private val Hand.part2Type: HandType
    get() {
        val part1type = this.part1Type
        val jokerCount = this.count { it == 'J' }

        if (part1type == HandType.FiveOfAKind || jokerCount == 0) {
            return part1type
        }

        return when (part1type) {
            HandType.FourOfAKind ->
                // Either the only Joker matches the rest,
                // or 4 Jokers match the only card that differs.
                HandType.FiveOfAKind

            HandType.FullHouse ->
                // Either 2 Jokers match the equal 3 cards,
                // or 3 Jokers match the equal 2 cards.
                HandType.FiveOfAKind

            HandType.ThreeOfAKind ->
                // Either the only Joker matches the equal 3 cards,
                // or 3 Jokers match one of 2 different cards.
                HandType.FourOfAKind

            HandType.TwoPair ->
                // If one pair is Jokers, they match the other pair.
                if (jokerCount == 2)
                    HandType.FourOfAKind
                // If there's one Joker, it matches one of the pair
                // making full house.
                else
                    HandType.FullHouse

            HandType.OnePair ->
                // Either the pair is Jokers, and they match one of 3 different cards,
                // or one of the different cards is Joker, and it matches the pair.
                HandType.ThreeOfAKind

            HandType.HighCard ->
                // One Joker makes a pair with one of the other cards.
                HandType.OnePair

            else ->
                error("I missed this case: $this, $part1type")
        }
    }

private val part1CardComparator: Comparator<Char> =
    compareBy("23456789TJQKA"::indexOf)

private val part1HandComparator: Comparator<Hand> =
    compareBy(Hand::part1Type)
        .then { hand1, hand2 ->
            for (cardIndex in hand1.indices) {
                val cardComparison = part1CardComparator.compare(hand1[cardIndex], hand2[cardIndex])
                if (cardComparison != 0) {
                    return@then cardComparison
                }
            }
            return@then 0
        }

private val part2CardComparator: Comparator<Char> =
    compareBy("J23456789TQKA"::indexOf)

private val part2HandComparator: Comparator<Hand> =
    compareBy(Hand::part2Type)
        .then { hand1, hand2 ->
            for (cardIndex in hand1.indices) {
                val cardComparison = part2CardComparator.compare(hand1[cardIndex], hand2[cardIndex])
                if (cardComparison != 0) {
                    return@then cardComparison
                }
            }
            return@then 0
        }

private fun InputStrings.toSortedHandsAndBids(
    handComparator: Comparator<Hand>,
): List<Pair<Hand, Long>> =
    this
        .filterNotEmpty()
        .map { line ->
            val (hand, bidString) = line.split(' ')
            hand to bidString.toLong()
        }
        .sortedWith { handAndBid1, handAndBid2 ->
            handComparator.compare(handAndBid1.first, handAndBid2.first)
        }
