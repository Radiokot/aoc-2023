import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: InputStrings): Any = runBlocking {
        return@runBlocking input
            .toConditionRecords()
            .map { record ->
                async(Dispatchers.Default) {
                    record.getPossibleArrangementCount()
                }
            }
            .awaitAll()
            .sum()
    }

    fun part2(input: InputStrings): Any = runBlocking {
        return@runBlocking input
            .toConditionRecords()
//            .map { record ->
//                listOf(
//                    record,
//                    record.copy(
//                        conditions = record.conditions + "?",
//                    ),
//                    record.copy(
//                        conditions = "?" + record.conditions + "?",
//                    ),
//                    record.copy(
//                        conditions = "?" + record.conditions
//                    ),
//                )
//            }
            .map { record ->
                async(Dispatchers.Default) {
                    val arrangementsForOriginalRecord =
                        record
                            .getPossibleArrangementCount()

                    val arrangementsForEndingWithUnknown =
                        record
                            .copy(conditions = record.conditions + "?")
                            .getPossibleArrangementCount()

                    val arrangementsForStartingWithUnknown =
                        record
                            .copy(conditions = "?" + record.conditions)
                            .getPossibleArrangementCount()

                    val arrangementsForSurroundedWithUnknown =
                        record
                            .copy(conditions = "?" + record.conditions + "?")
                            .getPossibleArrangementCount()

                    println(
                        listOf(
                            arrangementsForOriginalRecord,
                            arrangementsForEndingWithUnknown,
                            arrangementsForSurroundedWithUnknown,
                            arrangementsForStartingWithUnknown,
                            arrangementsForEndingWithUnknown-arrangementsForOriginalRecord
                        )
                    )

                    1L *
                            arrangementsForEndingWithUnknown *
                            arrangementsForSurroundedWithUnknown *
                            arrangementsForSurroundedWithUnknown *
                            arrangementsForSurroundedWithUnknown *
                            arrangementsForStartingWithUnknown
                }
            }
            .awaitAll()
            .sum()
    }

    val input =
        readInput("Day12_test")
//        readInput("Day12")

    part1(input).println()
    part2(input).println()
}

private data class ConditionRecord(
    val conditions: String,
    val damagedGroupSizes: List<Int>,
) {
    fun getPossibleArrangementCount(): Int {
        val unknownIndices =
            unknownRegex
                .findAll(this.conditions)
                .mapTo(mutableListOf()) { match ->
                    match.range.first
                }

        // Brute-force all the possible combinations.
        //
        // When brute-forcing toggle (on/off) combinations,
        // it is convenient to think of each combination as a binary number
        // where each bit represent each toggle state.
        // For example, combinations for ?? in form of binary numbers are:
        // 0(00), 1(01), 2(10), 3(11).
        // To check them all, iterate from 0(00) until 4(100).
        return (0 until (1L shl unknownIndices.size)).count { variantNumber ->
            val variantConditionsArray =
                this
                    .conditions
                    .toCharArray()

            // Set each index present in this variant number binary form to '#'.
            unknownIndices.forEachIndexed { unknownIndexIndex, unknownIndex ->
                if (variantNumber and (1L shl unknownIndexIndex) != 0L) {
                    variantConditionsArray[unknownIndex] = '#'
                }
            }

            isValid(
                conditions = variantConditionsArray.concatToString(),
                damagedGroupSizes = this.damagedGroupSizes,
            )
        }
    }

    companion object {
        private val unknownRegex = "\\?".toRegex()
        private val damagedGroupRegex = "(#+)".toRegex()

        fun isValid(
            conditions: String,
            damagedGroupSizes: List<Int>,
        ): Boolean =
            damagedGroupSizes ==
                    damagedGroupRegex
                        .findAll(conditions)
                        .mapTo(mutableListOf()) { match ->
                            match.value.length
                        }
    }
}

private fun InputStrings.toConditionRecords(): List<ConditionRecord> =
    this
        .filterNotEmpty()
        .map { line ->
            val (conditions, damagedGroupSizesString) = line.split(' ')
            ConditionRecord(
                conditions = conditions,
                damagedGroupSizes =
                    damagedGroupSizesString
                        .split(',')
                        .map(String::toInt),
            )
        }
