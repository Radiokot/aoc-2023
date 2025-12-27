fun main() {
    fun part1(input: InputStrings): Any {
        val map = input.toRocksMap()
        map.rollRocksNorth()
        return map.totalLoad
    }

    fun part2(input: InputStrings): Any {
        var map = input.toRocksMap()

        fun doCycle() {
            repeat(4) {
                map.rollRocksNorth()
                map = map.rotatedClockwiseMutable()
            }
        }

        val observedTotals = MutableList(10000) { 0 }
        observedTotals.indices.forEach { i ->
            doCycle()
            observedTotals[i] = map.totalLoad
        }

        val repeatingChunkSize = (2..1000).minBy { chunkSizeToCheck ->
            observedTotals
                .chunked(chunkSizeToCheck)
                .distinct()
                .size
        }
        val repeatingChunk = observedTotals.takeLast(repeatingChunkSize)

        return repeatingChunk[(1000000000 - observedTotals.size - 1) % repeatingChunkSize]
    }

    val input =
        readInput("Day14_test")
//        readInput("Day14")

    part1(input).println()
    part2(input).println()
}

private typealias RocksMap = List<MutableList<Char>>

private fun InputStrings.toRocksMap(): RocksMap =
    this
        .filterNotEmpty()
        .map(String::toMutableList)

private fun RocksMap.rollRocksNorth() {
    val map = this
    map[0].indices.forEach columnLoop@{ columnIndex ->
        map.forEachIndexed rowLoop@{ rowIndex, row ->
            if (row[columnIndex] != 'O') {
                return@rowLoop
            }
            var newRowIndex = -1
            for (checkRowIndex in (rowIndex - 1) downTo 0) {
                if (map[checkRowIndex][columnIndex] == '.') {
                    newRowIndex = checkRowIndex
                } else {
                    break
                }
            }
            if (newRowIndex != -1) {
                map[newRowIndex][columnIndex] = 'O'
                map[rowIndex][columnIndex] = '.'
            }
        }
    }
}

private val RocksMap.totalLoad: Int
    get() = foldIndexed(0) { rowIndex, sum, row ->
        sum + (size - rowIndex) * row.count { it == 'O' }
    }
