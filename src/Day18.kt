import java.awt.Color

fun main() {
    fun part1(input: InputStrings): Any {
        val polygon = mutableListOf(Position(0, 0))
        input.filterNotEmpty().forEach { line ->
            val direction = line[0]
            val stepCount =
                line
                    .substringAfter(' ')
                    .substringBefore(' ')
                    .toInt()
            var nextPosition = polygon.last()
            repeat(stepCount) {
                nextPosition = nextPosition.next(direction)
            }
            polygon += nextPosition
        }

        // The digger explicitly returns to the start point,
        // but it's already there.
        polygon.removeLast()

        val startRow = polygon.minOf(Position::row)
        val startColumn = polygon.minOf(Position::column)
        val endRow = polygon.maxOf(Position::row) + 1
        val endColumn = polygon.maxOf(Position::column)
        val visualisationImage = createVisualisationImage(
            1 + endColumn - startColumn,
            1 + endRow - startRow
        )

        val area = (startRow..endRow).sumOf { row ->
            (startColumn..endColumn).count { column ->
                isPositionOfPolygon(
                    position = Position(row, column),
                    polygon = polygon,
                ).also { itIs ->
                    visualisationImage.setRGB(
                        column - startColumn,
                        row - startRow,
                        if (row == 0 || column == 0)
                            Color.PINK.rgb
                        else if (itIs)
                            Color.ORANGE.rgb
                        else
                            Color.DARK_GRAY.rgb
                    )
                }
            }
        }

        saveVisualisation(visualisationImage)
        return area
    }

    fun part2(input: InputStrings): Any {
        return input.size
    }

    val input =
//        readInput("Day18_test")
        readInput("Day18")

    part1(input).println()
    part2(input).println()
}

fun isPositionOfPolygon(
    position: Position,
    polygon: List<Position>
): Boolean {
    var intersectionCount = 0

    // Check all edges.
    for (vertexIndex in polygon.indices) {
        val vertexA = polygon[vertexIndex]
        val vertexB = polygon[(vertexIndex + 1) % polygon.size]

        val edgeColumnRange =
            minOf(vertexA.column, vertexB.column)..maxOf(vertexA.column, vertexB.column)
        val edgeRowRange =
            minOf(vertexA.row, vertexB.row)..maxOf(vertexA.row, vertexB.row)

        // Position on an edge is considered a part of polygon.
        if (position.row in edgeRowRange && position.column in edgeColumnRange) {
            return true
        }

        // Cast a ray from the given position to the right.
        // The ray intersects the edge if it is not below or above it,
        // and if its start (position column) is more to the left than the edge is.
        // To not count edges starting from the same corner twice (vertical and horizontal),
        // the min row index is discarded.
        if (position.row in edgeRowRange.drop(1)
            && position.column < vertexA.column
        ) {
            intersectionCount++
        }
    }

    // Odd number of intersections means the position is inside.
    return intersectionCount % 2 == 1
}
