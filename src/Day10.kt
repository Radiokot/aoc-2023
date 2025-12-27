import java.util.*

fun main() {
    fun part1(input: InputStrings): Any {
        val map = input.toPipeMap()
        val startPosition = map.startPosition

        val traveler = PipeTraveler(
            position = startPosition,
            map = map,
            direction =
                if (map[startPosition.up] in setOf('|', 'F', '7'))
                    'U'
                else
                    'D',
        )

        do {
            traveler.move()
        } while (traveler.position != startPosition)

        return traveler.stepCount / 2
    }

    fun part2(input: InputStrings): Any {
        val map = input.toPipeMap()
        val startPosition = map.startPosition

        val traveler = PipeTraveler(
            position = startPosition,
            map = map,
            direction =
                if (map[startPosition.up] in setOf('|', 'F', '7'))
                    'U'
                else
                    'D',
        )
        val floodMapX3 = List(map.size * 3) {
            MutableList(map[0].size * 3) { '.' }
        }

        do {
            traveler.move()
            val positionX3 = traveler.position.x3
            // Trace the perimeter on the flood map.
            // Turn each cell into 3x3 to help flooding.
            // For example, `L` becomes
            // .#.
            // .#.
            // .##
            floodMapX3[positionX3] = '#'
            when (map[traveler.position]) {
                '|' -> {
                    floodMapX3[positionX3.up] = '#'
                    floodMapX3[positionX3.down] = '#'
                }

                '-' -> {
                    floodMapX3[positionX3.left] = '#'
                    floodMapX3[positionX3.right] = '#'
                }

                'F' -> {
                    floodMapX3[positionX3.down] = '#'
                    floodMapX3[positionX3.right] = '#'
                }

                '7' -> {
                    floodMapX3[positionX3.down] = '#'
                    floodMapX3[positionX3.left] = '#'
                }

                'L' -> {
                    floodMapX3[positionX3.up] = '#'
                    floodMapX3[positionX3.right] = '#'
                }

                'J' -> {
                    floodMapX3[positionX3.up] = '#'
                    floodMapX3[positionX3.left] = '#'
                }

                // After all it doesn't matter if the start position is a crossing.
                'S' -> {
                    floodMapX3[positionX3.up] = '#'
                    floodMapX3[positionX3.right] = '#'
                    floodMapX3[positionX3.down] = '#'
                    floodMapX3[positionX3.left] = '#'
                }
            }
        } while (traveler.position != startPosition)

        // Flood the outsideness with 'O's
        val positionsToFlood = LinkedList<Position>()
        positionsToFlood.push(Position(0, 0))
        while (!positionsToFlood.isEmpty()) {
            val position = positionsToFlood.pop()
            if (floodMapX3[position] != '.') {
                continue
            }
            floodMapX3[position] = 'O'
            positionsToFlood.push(position.up)
            positionsToFlood.push(position.down)
            positionsToFlood.push(position.left)
            positionsToFlood.push(position.right)
        }

//        mapX3.forEach {
//            println(it.joinToString(separator = ""))
//        }

        val insidenessArea = (1 until floodMapX3.size step 3).sumOf { rowX3 ->
            (1 until floodMapX3[0].size step 3).count { columnX3 ->
                floodMapX3[rowX3][columnX3] == '.'
            }
        }

        return insidenessArea
    }

    val input =
//        readInput("Day10_test")
        readInput("Day10")

    part1(input).println()
    part2(input).println()
}

private typealias PipeMap = List<MutableList<Char>>

private operator fun PipeMap.get(position: Position): Char {
    return this
        .getOrNull(position.row)
        ?.getOrNull(position.column)
        ?: 'X' // Out of bounds
}

private operator fun PipeMap.set(position: Position, value: Char) {
    this
        .getOrNull(position.row)
        ?.set(position.column, value)
}

private val PipeMap.startPosition: Position
    get() = this
        .indexOfFirst { it.contains('S') }
        .let { row ->
            Position(
                column = this[row].indexOf('S'),
                row = row,
            )
        }

private fun InputStrings.toPipeMap(): PipeMap =
    this
        .filterNotEmpty()
        .map(String::toMutableList)

private class PipeTraveler(
    var position: Position,
    var direction: Char,
    val map: PipeMap,
) {
    var stepCount = 0

    val nextPosition: Position
        get() = position.next(direction)

    fun move() {
        val nextPosition = nextPosition
        val nextPiece = map[nextPosition]
        val nextDirection = when (nextPiece) {
            // First time using guard conditions in Kotlin.
            'L' if direction == 'D' ->
                'R'

            'L' if direction == 'L' ->
                'U'

            'J' if direction == 'D' ->
                'L'

            'J' if direction == 'R' ->
                'U'

            '7' if direction == 'R' ->
                'D'

            '7' if direction == 'U' ->
                'L'

            'F' if direction == 'L' ->
                'D'

            'F' if direction == 'U' ->
                'R'

            else ->
                direction
        }
        position = nextPosition
        direction = nextDirection
        stepCount++
    }
}
