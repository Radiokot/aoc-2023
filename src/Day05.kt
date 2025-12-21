import kotlinx.coroutines.*

fun main() {
    fun part1(input: InputStrings): Any {
        var valuesToMap: List<Long> =
            // Initially – seeds.
            input[0]
                .substringAfter(": ")
                .split(' ')
                .map(String::toLong)

        val maps: List<List<Mapping>> = input.getMaps()

        maps.forEach { map ->
            // For each map, rewrite the values.
            valuesToMap = valuesToMap.map { valueToMap ->
                map.mapValue(valueToMap)
            }
        }

        return valuesToMap.min()
    }

    fun part2(input: InputStrings): Any = runBlocking {
        val maps: List<List<Mapping>> = input.getMaps()

        val seedsLineValuesIterator =
            input[0]
                .substringAfter(": ")
                .split(' ')
                .map(String::toLong)
                .iterator()

        val rangeCalculations = mutableListOf<Deferred<Long>>()

        while (seedsLineValuesIterator.hasNext()) {
            val rangeStart = seedsLineValuesIterator.next()
            val rangeLength = seedsLineValuesIterator.next()
            val rangeEnd = rangeStart + rangeLength

            rangeCalculations += async(Dispatchers.Default) {
                var minValueInRange = Long.MAX_VALUE
                (rangeStart..rangeEnd).forEach { valueToMap ->
                    var valueToMap = valueToMap
                    maps.forEach { map ->
                        valueToMap = map.mapValue(valueToMap)
                    }
                    if (valueToMap < minValueInRange) {
                        minValueInRange = valueToMap
                    }
                }
                minValueInRange
            }
        }

        return@runBlocking rangeCalculations
            .awaitAll()
            .min()
    }

    val input =
//        readInput("Day05_test")
        readInput("Day05")

    part1(input).println()
    part2(input).println()
}

private class Mapping(
    val sourceRangeStart: Long,
    val destinationRangeStart: Long,
    val length: Long,
) {
    operator fun contains(value: Long): Boolean =
        value >= sourceRangeStart && value < sourceRangeStart + length

    fun map(value: Long): Long =
        destinationRangeStart + (value - sourceRangeStart)
}

private fun String.toMapping(): Mapping {
    val (destinationRangeStart, sourceRangeStart, length) =
        split(' ')
            .map(String::toLong)

    return Mapping(sourceRangeStart, destinationRangeStart, length)
}

private fun List<Mapping>.mapValue(value: Long): Long =
// Map a value if there is a mapping for it,
    // otherwise return it as is.
    this
        .find { value in it }
        ?.map(value)
        ?: value

private fun InputStrings.getMaps(): List<List<Mapping>> {
    val input = this
    return listOf(
        input
            .subList(
                fromIndex = input.indexOf("seed-to-soil map:") + 1,
                toIndex = input.indexOf("soil-to-fertilizer map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("soil-to-fertilizer map:") + 1,
                toIndex = input.indexOf("fertilizer-to-water map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("fertilizer-to-water map:") + 1,
                toIndex = input.indexOf("water-to-light map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("water-to-light map:") + 1,
                toIndex = input.indexOf("light-to-temperature map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("light-to-temperature map:") + 1,
                toIndex = input.indexOf("temperature-to-humidity map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("temperature-to-humidity map:") + 1,
                toIndex = input.indexOf("humidity-to-location map:") - 1,
            )
            .map(String::toMapping),

        input
            .subList(
                fromIndex = input.indexOf("humidity-to-location map:") + 1,
                toIndex = input.size,
            )
            .filterNotEmpty()
            .map(String::toMapping),
    )
}
