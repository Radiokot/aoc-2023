import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
    fun part1(input: InputStrings): Any {
        val map = input
            .filterNotEmpty()
            .map(String::toList)

        return getEnergizedPositionCount(
            startBeamTip = BeamTip(
                position = Position(0, 0),
                direction = 'R',
                map = map,
            ),
        )
    }

    fun part2(input: InputStrings): Any = runBlocking {
        val map = input
            .filterNotEmpty()
            .map(String::toList)
        val width = map[0].size
        val height = map.size

        val startTips =
            (0 until height).flatMap { rowIndex ->
                listOf(
                    BeamTip(
                        position = Position(rowIndex, 0),
                        direction = 'R',
                        map = map,
                    ),
                    BeamTip(
                        position = Position(rowIndex, width - 1),
                        direction = 'L',
                        map = map,
                    )
                )
            } + (0 until width).flatMap { columnIndex ->
                listOf(
                    BeamTip(
                        position = Position(0, columnIndex),
                        direction = 'D',
                        map = map,
                    ),
                    BeamTip(
                        position = Position(height - 1, columnIndex),
                        direction = 'U',
                        map = map,
                    )
                )
            }

        return@runBlocking startTips
            .map { startBeamTip ->
                async(Dispatchers.Default) {
                    getEnergizedPositionCount(startBeamTip)
                }
            }
            .awaitAll()
            .max()
    }

    val input =
//        readInput("Day16_test")
        readInput("Day16")

    part1(input).println()
    part2(input).println()
}

private fun getEnergizedPositionCount(
    startBeamTip: BeamTip,
): Int {
    val visitedPositions = mutableSetOf<Pair<Position, Char>>()
    val tips = LinkedList<BeamTip>()
    tips += startBeamTip
    do {
        val tip = tips.pop()
        val nextTips = tip.next
        // Gotta keep track of visited mirrors and splitters to avoid infinite loops.
        // If a position has been visited with the same direction, do not proceed.
        if (visitedPositions.add(tip.position to tip.direction)) {
            nextTips.forEach(tips::push)
        }
    } while (tips.isNotEmpty())

    return visitedPositions
        .mapTo(mutableSetOf(), Pair<Position, *>::first)
        .size
}

private data class BeamTip(
    val position: Position,
    val direction: Char,
    val map: List<List<Char>>,
) {
    val next: List<BeamTip>
        get() = when (map[position]) {
            '\\' if direction == 'U' ->
                listOf(
                    this.copy(
                        position = position.next('L'),
                        direction = 'L',
                    )
                )

            '\\' if direction == 'R' ->
                listOf(
                    this.copy(
                        position = position.next('D'),
                        direction = 'D',
                    )
                )

            '\\' if direction == 'D' ->
                listOf(
                    this.copy(
                        position = position.next('R'),
                        direction = 'R',
                    )
                )

            '\\' if direction == 'L' ->
                listOf(
                    this.copy(
                        position = position.next('U'),
                        direction = 'U',
                    )
                )

            '/' if direction == 'U' ->
                listOf(
                    this.copy(
                        position = position.next('R'),
                        direction = 'R',
                    )
                )

            '/' if direction == 'R' ->
                listOf(
                    this.copy(
                        position = position.next('U'),
                        direction = 'U',
                    )
                )

            '/' if direction == 'D' ->
                listOf(
                    this.copy(
                        position = position.next('L'),
                        direction = 'L',
                    )
                )

            '/' if direction == 'L' ->
                listOf(
                    this.copy(
                        position = position.next('D'),
                        direction = 'D',
                    )
                )

            '|' if (direction == 'R' || direction == 'L') ->
                listOf(
                    this.copy(
                        position = position.next('U'),
                        direction = 'U',
                    ),
                    this.copy(
                        position = position.next('D'),
                        direction = 'D',
                    ),
                )

            '-' if (direction == 'U' || direction == 'D') ->
                listOf(
                    this.copy(
                        position = position.next('L'),
                        direction = 'L',
                    ),
                    this.copy(
                        position = position.next('R'),
                        direction = 'R',
                    ),
                )

            else ->
                listOf(
                    this.copy(
                        position = position.next(direction)
                    )
                )
        }.filterNot { map[it.position] == 'X' }

    override fun toString(): String {
        return "BeamTip(direction=$direction, position=$position)"
    }
}
