import kotlin.math.min

fun main() {
    fun part1(input: InputStrings): Any {
        val patterns = input.toPatterns()
        return patterns.sumOf { pattern ->
            val verticalResult = pattern.verticalMirrorColumnsToTheLeft()
            val horizontalResult = pattern.transpose().verticalMirrorColumnsToTheLeft()
            check(verticalResult != 0 || horizontalResult != 0)
            verticalResult + 100 * horizontalResult
        }
    }

    fun part2(input: InputStrings): Any {
        val patterns = input.toPatterns()
        return patterns.sumOf { pattern ->
            val verticalResult = pattern.verticalDirtyMirrorColumnsToTheLeft()
            val horizontalResult = pattern.transpose().verticalDirtyMirrorColumnsToTheLeft()
            check(verticalResult != 0 || horizontalResult != 0)
            verticalResult + 100 * horizontalResult
        }
    }

    val input =
//        readInput("Day13_test")
        readInput("Day13")

    part1(input).println()
    part2(input).println()
}

private fun InputStrings.toPatterns(): List<List<List<Char>>> {
    var remaining = this
        .map(String::toList)

    return buildList {
        do {
            val breakIndex = remaining.indexOfFirst(List<*>::isEmpty)
            if (breakIndex >= 0) {
                add(remaining.take(breakIndex))
                remaining = remaining.drop(breakIndex + 1)
            } else {
                add(remaining)
            }
        } while (breakIndex >= 0)
    }
}

private fun List<List<Char>>.verticalMirrorColumnsToTheLeft(): Int {
    val pattern = this
    val width = pattern[0].size
    for (columnsToTheLeft in (1..<width)) {
        val reflectionWidth = min(columnsToTheLeft, width - columnsToTheLeft)
        val isMirror = pattern.all { line ->
            val leftPart =
                line
                    .take(columnsToTheLeft)
                    .takeLast(reflectionWidth)
            val rightPart =
                line
                    .drop(columnsToTheLeft)
                    .take(reflectionWidth)
            leftPart == rightPart.reversed()
        }
        if (isMirror) {
            return columnsToTheLeft
        }
    }
    return 0
}

private fun List<List<Char>>.verticalDirtyMirrorColumnsToTheLeft(): Int {
    val pattern = this
    val width = pattern[0].size
    for (columnsToTheLeft in (1..<width)) {
        val reflectionWidth = min(columnsToTheLeft, width - columnsToTheLeft)
        val reflectionDifference = pattern.sumOf { line ->
            val leftPart =
                line
                    .take(columnsToTheLeft)
                    .takeLast(reflectionWidth)
            val rightPart =
                line
                    .drop(columnsToTheLeft)
                    .take(reflectionWidth)

            leftPart
                .zip(rightPart.reversed())
                .count { it.first != it.second }
        }
        if (reflectionDifference == 1) {
            return columnsToTheLeft
        }
    }
    return 0
}
