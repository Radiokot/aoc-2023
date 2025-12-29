import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.readText

typealias InputStrings = List<String>

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): InputStrings =
    Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun InputStrings.filterNotEmpty(): InputStrings =
    filter(String::isNotEmpty)

fun createVisualisationImage(width: Int, height: Int): BufferedImage =
    BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

fun BufferedImage.draw(doDraw: Graphics2D.() -> Any) = with(createGraphics()) {
    try {
        doDraw()
    } finally {
        dispose()
    }
}

fun saveVisualisation(image: BufferedImage) {
    ImageIO.write(image, "png", File("visualisation.png"))
}

data class Position(
    val row: Int,
    val column: Int,
) {
    val left: Position
        get() = Position(row, column - 1)

    val right: Position
        get() = Position(row, column + 1)

    val up: Position
        get() = Position(row - 1, column)

    val down: Position
        get() = Position(row + 1, column)

    val x3: Position
        get() = Position(1 + row * 3, 1 + column * 3)

    fun next(direction: Char): Position =
        when (direction) {
            'L' -> left
            'R' -> right
            'U' -> up
            'D' -> down
            else -> error("Unknown direction")
        }
}

fun turnClockwise(direction: Char): Char =
    when (direction) {
        'U' -> 'R'
        'R' -> 'D'
        'D' -> 'L'
        'L' -> 'U'
        else -> error("Unknown direction")
    }

fun turnCounterClockwise(direction: Char): Char =
    when (direction) {
        'U' -> 'L'
        'L' -> 'D'
        'D' -> 'R'
        'R' -> 'U'
        else -> error("Unknown direction")
    }

operator fun List<List<Char>>.get(position: Position): Char {
    return this
        .getOrNull(position.row)
        ?.getOrNull(position.column)
        ?: 'X' // Out of bounds
}

fun <T> List<List<T>>.transpose(): List<List<T>> =
    this[0]
        .indices
        .map { cellIndex ->
            this.indices.map { rowIndex ->
                this[rowIndex][cellIndex]
            }
        }

fun <T> List<MutableList<T>>.rotatedClockwiseMutable(): List<MutableList<T>> =
    indices.map { newRowIndex ->
        this[0].indices.mapTo(mutableListOf()) { newColumnIndex ->
            this[size - 1 - newColumnIndex][newRowIndex]
        }
    }
