data class Pad(val position: String, val note: String, val state: String, val color: String) {
    override fun toString(): String {
        return "$position ${format(note)} $state $color "
    }
}

val prefix = "f000 2029 020d 0501 7f49 534f 5f32 0000\n7f00 0015 "
val postfix = "f7"

fun main() {
    println("What's root note?")
    val rootNote = readLine()
    var rootNoteInt = rootNote?.toInt(radix = 16) ?: 0
    val initialRootNoteInt = rootNoteInt

    val rootNoteString = rootNoteInt.toString(radix = 16)
    val formattedRootNoteString = format(rootNoteString)
    println("Root note is $formattedRootNoteString ")


    val padsGrid = mutableListOf<MutableList<Pad>>()

    val maxRow = 7
    val maxColumn = 7
    var offset = 0
    for (i in maxRow downTo 0 ) {
        val padsRow = mutableListOf<Pad>()
        for (j in 0..maxColumn) {
            val currentNote: Int = rootNoteInt - offset

            val color = if ((currentNote - initialRootNoteInt) % 12 == 0) "0031" else "0034"
            padsRow.add(Pad(positions[i][j], currentNote.toString(radix = 16), "0000", color))
            rootNoteInt += 1
        }
        offset += 3
        padsGrid.add(0, padsRow)
    }

    for (i in 0..maxRow) {
        for (j in 0..maxColumn) {
            print("${padsGrid[i][j]}")
        }
        println()
    }
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