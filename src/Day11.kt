import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: InputStrings): Any {
        var map = input.filterNotEmpty()
        val columnToExpandIndices =
            map[0]
                .indices
                .filterTo(mutableSetOf()) { columnIndex ->
                    map.all { row ->
                        row[columnIndex] == '.'
                    }
                }
        val rowsToExpandIndices =
            map
                .indices
                .filterTo(mutableSetOf()) { rowIndex ->
                    map[rowIndex].all { it == '.' }
                }
        map = map.flatMapIndexed { rowIndex, row ->
            val rowWithExpandedColumns =
                row
                    .mapIndexed { columnIndex, cell ->
                        if (columnIndex in columnToExpandIndices)
                            "$cell$cell"
                        else
                            "$cell"
                    }
                    .joinToString(separator = "")

            if (rowIndex in rowsToExpandIndices)
                listOf(rowWithExpandedColumns, rowWithExpandedColumns)
            else
                listOf(rowWithExpandedColumns)
        }

        val galaxies = mutableListOf<Position>()
        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell == '#') {
                    galaxies += Position(rowIndex, columnIndex)
                }
            }
        }

        var distanceSum = 0
        galaxies.forEachIndexed { galaxyIndex, galaxy ->
            (0 until galaxyIndex).forEach { anotherGalaxyIndex ->
                val anotherGalaxy = galaxies[anotherGalaxyIndex]
                distanceSum +=
                    abs(galaxy.row - anotherGalaxy.row) + abs(galaxy.column - anotherGalaxy.column)
            }
        }

        return distanceSum
    }

    fun part2(input: InputStrings): Any {
        val map = input.filterNotEmpty()
        val expansionFactor = 1000000

        val columnToExpandIndices =
            map[0]
                .indices
                .filterTo(mutableSetOf()) { columnIndex ->
                    map.all { row ->
                        row[columnIndex] == '.'
                    }
                }

        val rowsToExpandIndices =
            map
                .indices
                .filterTo(mutableSetOf()) { rowIndex ->
                    map[rowIndex].all { it == '.' }
                }

        val galaxies = mutableListOf<Position>()
        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell == '#') {
                    galaxies += Position(rowIndex, columnIndex)
                }
            }
        }

        var distanceSum = 0L
        galaxies.forEachIndexed { galaxyIndex, galaxyA ->
            (0 until galaxyIndex).forEach { anotherGalaxyIndex ->
                val galaxyB = galaxies[anotherGalaxyIndex]
                val rowRange =
                    IntRange(
                        start = min(galaxyA.row, galaxyB.row),
                        endInclusive = max(galaxyA.row, galaxyB.row),
                    )
                val columnRange =
                    IntRange(
                        start = min(galaxyA.column, galaxyB.column),
                        endInclusive = max(galaxyA.column, galaxyB.column),
                    )
                val expandedRowsBetween =
                    (expansionFactor - 1) *
                            rowsToExpandIndices
                                .count(rowRange::contains)
                val expandedColumnsBetween =
                    (expansionFactor - 1) *
                            columnToExpandIndices
                                .count(columnRange::contains)

                distanceSum +=
                    expandedRowsBetween + rowRange.last - rowRange.first +
                            expandedColumnsBetween + columnRange.last - columnRange.first
            }
        }

        return distanceSum
    }

    val input =
//        readInput("Day11_test")
        readInput("Day11")

    part1(input).println()
    part2(input).println()
}
