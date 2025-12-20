fun main() {
    fun part1(input: InputStrings): Any {
        val bag = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )

        return input
            .toGames()
            .filter { game ->
                game.sets.all { set ->
                    set.all { (color, count) ->
                        count <= bag.getValue(color)
                    }
                }
            }
            .sumOf(Game::id)
    }

    fun part2(input: InputStrings): Any {
        return input
            .toGames()
            .sumOf { game ->
                val requiredBag =
                    game
                        .sets
                        .fold(mutableMapOf<String, Int>()) { bag, set ->
                            set.forEach { (color, count) ->
                                if (count > bag.getOrDefault(color, 0)) {
                                    bag[color] = count
                                }
                            }
                            bag
                        }

                return@sumOf requiredBag
                    .values
                    .fold(1L) { power, count -> power * count }
            }
    }

    val input =
//        readInput("Day2302_test")
        readInput("Day02")

    part1(input).println()
    part2(input).println()
}

private fun InputStrings.toGames(): List<Game> =
    this
        .filterNotEmpty()
        .map { line ->
            val id =
                line
                    .substringBefore(':')
                    .substringAfter("Game ")
                    .toInt()

            val sets =
                line
                    .substringAfter(": ")
                    .split("; ")
                    .map { setLine ->
                        setLine
                            .split(", ")
                            .associate { setColorCountLine ->
                                val (count, color) =
                                    setColorCountLine
                                        .split(' ')
                                Pair(
                                    color,
                                    count.toInt(),
                                )
                            }
                    }

            Game(id, sets)
        }

private data class Game(
    val id: Int,
    val sets: List<Map<String, Int>>,
)
