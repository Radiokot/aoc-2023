import java.util.*

fun main() {
    fun part1(input: InputStrings): Any {
        val map = input
            .filterNotEmpty()
            .map(String::toList)

        return findMinCrucibleHeatLoss(
            startState = CrucibleState(
                position = Position(0, 0),
                direction = 'R',
                straightStepCount = 0,
            ),
            isPart2 = false,
            map = map,
        )
    }

    fun part2(input: InputStrings): Any {
        val map = input
            .filterNotEmpty()
            .map(String::toList)

        return findMinCrucibleHeatLoss(
            startState = CrucibleState(
                position = Position(0, 0),
                direction = 'R',
                straightStepCount = 0,
            ),
            isPart2 = true,
            map = map,
        )
    }

    val input =
//        readInput("Day17_test")
        readInput("Day17")

    part1(input).println()
    part2(input).println()
}

private data class CrucibleState(
    val position: Position,
    val direction: Char,
    val straightStepCount: Int,
) {
    val next: List<CrucibleState>
        get() = buildList {
            if (straightStepCount < 3) {
                add(
                    copy(
                        position = position.next(direction),
                        straightStepCount = straightStepCount + 1,
                    )
                )
            }
            add(
                copy(
                    position = position.next(turnClockwise(direction)),
                    direction = turnClockwise(direction),
                    straightStepCount = 1,
                )
            )
            add(
                copy(
                    position = position.next(turnCounterClockwise(direction)),
                    direction = turnCounterClockwise(direction),
                    straightStepCount = 1,
                )
            )
        }

    val nextPart2: List<CrucibleState>
        get() = buildList {
            if (straightStepCount < 10) {
                add(
                    copy(
                        position = position.next(direction),
                        straightStepCount = straightStepCount + 1,
                    )
                )
            }
            if (straightStepCount >= 4) {
                add(
                    copy(
                        position = position.next(turnClockwise(direction)),
                        direction = turnClockwise(direction),
                        straightStepCount = 1,
                    )
                )
                add(
                    copy(
                        position = position.next(turnCounterClockwise(direction)),
                        direction = turnCounterClockwise(direction),
                        straightStepCount = 1,
                    )
                )
            }
        }
}

private fun findMinCrucibleHeatLoss(
    startState: CrucibleState,
    isPart2: Boolean,
    map: List<List<Char>>,
): Int {
    val finishPosition = Position(
        row = map.indices.last,
        column = map[0].indices.last,
    )

    // Run Dijkstra algorithm where a node is a crucible state
    // and a weight is a heat loss.
    val dijkstraQueue = PriorityQueue<Pair<CrucibleState, Int>>(
        compareBy { (_, heatLoss) -> heatLoss }
    )
    dijkstraQueue += Pair(
        startState,
        // Because you already start in the top-left block,
        // you don't incur that block's heat loss
        // unless you leave that block and then return to it.
        0
    )
    val minHeatLossByCrucibleState = mutableMapOf<CrucibleState, Int>()
    minHeatLossByCrucibleState[startState] = 0
    while (dijkstraQueue.isNotEmpty()) {
        val (crucibleState, currentHeatLoss) = dijkstraQueue.poll()

        if (crucibleState.position == finishPosition
            && (!isPart2 || crucibleState.straightStepCount >= 4)
        ) {
            return currentHeatLoss
        }

        val nextCrucibleStates =
            if (isPart2)
                crucibleState.nextPart2
            else
                crucibleState.next

        nextCrucibleStates.forEach { nextCrucibleState ->
            if (map[nextCrucibleState.position] == 'X') {
                return@forEach
            }

            val nextStateHeatLoss = currentHeatLoss + map[nextCrucibleState.position].digitToInt()
            if (nextStateHeatLoss < (minHeatLossByCrucibleState[nextCrucibleState] ?: Int.MAX_VALUE)) {
                minHeatLossByCrucibleState[nextCrucibleState] = nextStateHeatLoss
                dijkstraQueue += Pair(
                    nextCrucibleState,
                    nextStateHeatLoss,
                )
            }
        }
    }

    error("It failed to reach the finish position")
}
