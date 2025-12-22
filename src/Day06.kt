fun main() {
    fun part1(input: InputStrings): Any {
        return input
            .toDurationsAndDistances()
            .map { (duration, distance) ->
                (1 until duration).count { chargeTime ->
                    val velocity = chargeTime
                    (duration - chargeTime) * velocity > distance
                }
            }
            .fold(1L) { product, value -> product * value }
    }

    fun part2(input: InputStrings): Any {
        val (duration, distance) = input.toDurationAndDistance()
        return (1 until duration).count { chargeTime ->
            val velocity = chargeTime
            (duration - chargeTime) * velocity > distance
        }
    }

    val input =
//        readInput("Day06_test")
        readInput("Day06")

    part1(input).println()
    part2(input).println()
}

private fun InputStrings.toDurationsAndDistances(): List<Pair<Int, Int>> {
    val spaceRegex = "\\s+".toRegex()
    return (0..1)
        .map { lineIndex ->
            this[lineIndex]
                .substringAfter(":")
                .trim()
                .split(spaceRegex)
                .map(String::toInt)
        }
        .let { it[0].zip(it[1]) }
}

private fun InputStrings.toDurationAndDistance(): Pair<Long, Long> {
    val spaceRegex = "\\s+".toRegex()
    return (0..1)
        .map { lineIndex ->
            this[lineIndex]
                .substringAfter(":")
                .trim()
                .replace(spaceRegex, "")
                .toLong()
        }
        .let { it[0] to it[1] }
}
