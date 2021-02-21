import java.io.File

data class Pad(val position: String, val note: String, val state: String, val color: String) {
    override fun toString(): String {
        return "$position $note $state $color "
    }
}

const val prefix = "f000 2029 020d 0501 7f49 534f 5f33 0000 7f00 0015 "
const val postfix = "f7"
const val lowestPossibleNote = 0x0100

fun main() {
    println("What's root note? [C, C#, D, D#, E, F, F#, G, G#, A, A#, B]")
    val rootNote = readLine()?.toUpperCase()
    val rootNoteFactor = noteMapping[rootNote] ?: 0

    println("What's octave? [-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8]")
    val octave = readLine()
    val octaveFactor = octaveMapping[octave] ?: 0

    println("What's horizontal grid offset? [-2, -1 , 0]")
    val horizontalGridOffset = readLine()?.toInt() ?: 0

    println("Color scheme:")
    colorMapping.entries.forEach {
        println("${it.key}:${it.value.title}")
    }

    println("What's root note color:")
    val rootNoteColorIndex = readLine()?.toInt() ?: 0
    val rootNoteColor = colorMapping[rootNoteColorIndex]?.value ?: "0005"

    println("What's regular note color:")
    val regularNoteColorIndex = readLine()?.toInt() ?: 26
    val regularNoteColor = colorMapping[regularNoteColorIndex]?.value ?: "0001"

    println("What's output file name:")
    val outputFileName = readLine() ?: "default"

    var startNote = lowestPossibleNote + rootNoteFactor + octaveFactor*12 - horizontalGridOffset
    // TODO: Check that startNote is not lower than lowestPossibleNote

    val initialStartNote = startNote + horizontalGridOffset

    val formattedRootNoteString = format(startNote.toString(16))
    println("Root note is $formattedRootNoteString ")

    val padsGrid = mutableListOf<MutableList<Pad>>()
    val maxRow = 7
    val maxColumn = 7
    var offset = 0

    for (i in maxRow downTo 0 ) {
        val padsRow = mutableListOf<Pad>()
        for (j in 0..maxColumn) {
            val currentNote: Int = startNote - offset
            val color = if ((currentNote - initialStartNote) % 12 == 0) rootNoteColor else regularNoteColor
            padsRow.add(Pad(positions[i][j], format(currentNote.toString(radix = 16)), "0000", color))
            startNote += 1
        }
        offset += 3
        padsGrid.add(0, padsRow)
    }

    var finalString = prefix
    for (i in 0..maxRow) {
        for (j in 0..maxColumn) {
            print("${padsGrid[i][j]}")
            finalString += padsGrid[i][j].toString()
        }
        println()
    }
    finalString += postfix

    val finalFileName = "$outputFileName.syx"
    println("Will write to: $finalFileName")
    println(finalString)

    val bytes = finalString.split(" ").map {
        it.toInt(16).toByteArray()
    }.flatMap { it.asList() }.toByteArray()

    val mutableBytes = bytes.toMutableList()
    mutableBytes.removeAt(bytes.size-2)

    File(finalFileName).writeBytes(mutableBytes.toByteArray())
}

fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
            ((this and 0xFF00) shr 8).toByte(),
            ((this and 0x00FF) shr 0).toByte()
    )
}

fun format(string: String): String {
    when {
        string.isEmpty() -> {
            return ""
        }
        string.length == 1 -> {
            return "000$string"
        }
        string.length == 2 -> {
            return "00$string"
        }
        string.length == 3 -> {
            return "0$string"
        }
    }
    return string
}