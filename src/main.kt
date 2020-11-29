data class Pad(val position: String, val note: String, val state: String, val color: String) {
    override fun toString(): String {
        return "$position ${format(note)} $state $color "
    }
}

val prefix = "f000 2029 020d 0501 7f49 534f 5f32 0000\n7f00 0015 "
val postfix = "f7"

val positions = arrayListOf(
    arrayListOf("5100", "5200", "5300", "5400", "5500", "5600", "5700", "5800"),
    arrayListOf("3d00", "3e00", "3f00", "4000", "4100", "4200", "4300", "4400"),
    arrayListOf("4700", "4800", "4900", "4a00", "4b00", "4c00", "4d00", "4e00"),
    arrayListOf("3300", "3400", "3500", "3600", "3700", "3800", "3900", "3a00"),
    arrayListOf("2900", "2a00", "2b00", "2c00", "2d00", "2e00", "2f00", "3000"),
    arrayListOf("1f00", "2000", "2100", "2200", "2300", "2400", "2500", "2600"),
    arrayListOf("1500", "1600", "1700", "1800", "1900", "1a00", "1b00", "1c00"),
    arrayListOf("0b00", "0c00", "0d00", "0e00", "0f00", "1000", "1100", "1200")
)
fun main() {
    println("What's root note?")
    val rootNote = readLine()
    var rootNoteInt = rootNote?.toInt(radix = 16) ?: 0

    val rootNoteString = rootNoteInt.toString(radix = 16)
    val formattedRootNoteString = format(rootNoteString)
    println("Root note is $formattedRootNoteString ")


    var padsGrid = mutableListOf<MutableList<Pad>>()

    val maxRow = 7
    val maxColumn = 7
    for (i in maxRow downTo 0 ) {
        var padsRow = mutableListOf<Pad>()
        for (j in 0..maxColumn) {
            padsRow.add(Pad(positions[i][j], rootNoteInt.toString(radix = 16), "0000", "0034"))
            rootNoteInt += 1
        }
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