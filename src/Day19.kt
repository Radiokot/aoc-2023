import java.util.*

fun main() {
    fun part1(input: InputStrings): Any {
        val workflows = input.getWorkflows()
        val parts = input.getParts()

        return parts.sumOf { partRatings ->
            var outcome = "in"
            do {
                outcome = workflows[outcome]!!.firstNotNullOf { rule ->
                    rule(partRatings)
                }
            } while (workflows.containsKey(outcome))

            return@sumOf if (outcome == "A") partRatings.sum() else 0L
        }
    }

    fun part2(input: InputStrings): Any {
        val workflows = input.getWorkflows()
        val state = LinkedList<Pair<List<LongRange>, String>>()

        state += Pair(
            listOf(
                (1..4000L), // x
                (1..4000L), // m
                (1..4000L), // a
                (1..4000L), // s
            ),
            "in"
        )

        var acceptedCombinationCount = 0L
        do {
            var (ratingRanges, outcome) = state.pop()
            val workflow = workflows[outcome]
            if (workflow == null) {
                if (outcome == "A") {
                    acceptedCombinationCount = ratingRanges.fold(1L) { product, range ->
                        product * (range.last + 1 - range.first)
                    }
                }
                continue
            }
            workflow.forEach { rule ->
                if (rule.ratingIndex == null) {
                    state.push(ratingRanges to rule.outcome)
                    return@forEach
                }

                val ratingRange = ratingRanges[rule.ratingIndex]
                if (rule.operator == '>') {
                    state.push(
                        Pair(
                            ratingRanges
                                .toMutableList()
                                .also {
                                    it[rule.ratingIndex] =
                                        LongRange(
                                            start = rule.threshold!! + 1,
                                            endInclusive = ratingRange.last
                                        )
                                },
                            rule.outcome
                        )
                    )
                    ratingRanges =
                        ratingRanges
                            .toMutableList()
                            .also {
                                it[rule.ratingIndex] =
                                    LongRange(
                                        start = ratingRange.first,
                                        endInclusive = rule.threshold!!
                                    )
                            }
                } else if (rule.operator == '<') {
                    state.push(
                        Pair(
                            ratingRanges
                                .toMutableList()
                                .also {
                                    it[rule.ratingIndex] =
                                        LongRange(
                                            start = ratingRange.first,
                                            endInclusive = rule.threshold!! - 1
                                        )
                                },
                            rule.outcome
                        )
                    )
                    ratingRanges =
                        ratingRanges
                            .toMutableList()
                            .also {
                                it[rule.ratingIndex] =
                                    LongRange(
                                        start = rule.threshold!!,
                                        endInclusive = ratingRange.last
                                    )
                            }
                }
            }
        } while (state.isNotEmpty())
        return acceptedCombinationCount
    }

    val input =
        readInput("Day19_test")
//        readInput("Day19")

    part1(input).println()
    part2(input).println()
}

private typealias MachinePartRatings = List<Long>

// Workflow rule is a function that returns A, R or next workflow name
// if the part matches the rule.
private class WorkflowRule(
    val outcome: String,
    val ratingIndex: Int?,
    val operator: Char?,
    val threshold: Long?,
) {
    operator fun invoke(partRatings: MachinePartRatings): String? =
        when (operator) {
            null -> outcome
            '>' if partRatings[ratingIndex!!] > threshold!! -> outcome
            '<' if partRatings[ratingIndex!!] < threshold!! -> outcome
            else -> null
        }
}

private typealias Workflow = List<WorkflowRule>

private fun InputStrings.getWorkflows(): Map<String, Workflow> =
    mapNotNull { line ->
        if (line.isEmpty() || line.startsWith('{')) {
            return@mapNotNull null
        }

        val name = line.substringBefore('{')
        val rules =
            line
                .substringAfter('{')
                .dropLast(1)
                .split(',')
                .map { ruleLine ->
                    // Rule without condition.
                    if (!ruleLine.contains(':')) {
                        return@map WorkflowRule(
                            outcome = ruleLine,
                            ratingIndex = null,
                            operator = null,
                            threshold = null,
                        )
                    }

                    // Otherwise rule with condition.
                    val operator = ruleLine[1]
                    return@map WorkflowRule(
                        outcome = ruleLine.substringAfter(':'),
                        ratingIndex = "xmas".indexOf(ruleLine[0]),
                        operator = operator,
                        threshold =
                            ruleLine
                                .substringAfter(operator)
                                .substringBefore(':')
                                .toLong(),
                    )
                }

        name to rules
    }.toMap()

private fun InputStrings.getParts(): List<MachinePartRatings> =
    mapNotNull { line ->
        if (!line.startsWith('{')) {
            return@mapNotNull null
        }

        line
            .trim('{', '}')
            .split(',')
            .map { it.substringAfter('=').toLong() }
    }
