package set1

import java.io.File

fun main() {
    val input = readFileLineByLineUsingForEachLine("src/set1/challenge4.txt")
    val solution = input
            .map{ findSingleByteXORCipher(it) }
            .maxBy { solution -> solution.map { letter -> englishLetterScore(letter) }.sum() }!!
    println(solution)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}