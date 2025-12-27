fun main() {
    fun part1(input: InputStrings): Any {
        return input[0]
            .split(',')
            .sumOf(String::day15Hash)
    }

    fun part2(input: InputStrings): Any {
        val boxes = List<MutableList<Lens>>(256) { mutableListOf() }
        input[0]
            .split(',')
            .forEach { command ->
                val action = if (command.contains('=')) '=' else '-'
                val label = command.substringBefore(action)
                val box = boxes[label.day15Hash]

                if (action == '-') {
                    box.removeIf { it.label == label }
                } else {
                    val lensIndex = box.indexOfFirst { it.label == label }
                    val focalLength = command.substringAfter(action).toInt()
                    if (lensIndex != -1) {
                        box[lensIndex] = Lens(label, focalLength)
                    } else {
                        box.add(Lens(label, focalLength))
                    }
                }
            }

        return boxes.foldIndexed(0) { boxIndex, totalPower, box ->
            totalPower +
                    box.foldIndexed(0) { lensIndex, boxPower, lens ->
                        boxPower + (boxIndex + 1) * (lensIndex + 1) * lens.focalLength
                    }
        }
    }

    val input =
//        readInput("Day15_test")
        readInput("Day15")

    part1(input).println()
    part2(input).println()
}

private typealias Lens = Pair<String, Int>

private val Lens.label: String
    get() = first

private val Lens.focalLength: Int
    get() = second

private val String.day15Hash: Int
    get() = fold(0) { hash, char ->
        ((hash + char.code) * 17) % 256
    }
