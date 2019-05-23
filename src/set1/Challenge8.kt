package set1

import java.io.File

fun main() {
    detectAESinECBMode(File("src/set1/challenge8.txt").readLines())
}

fun detectAESinECBMode(input: List<String>) {
    val detectedLine = input.mapIndexed { line, s ->
        val blocks = s.chunked(16) // Split into 16 byte blocks
        val repeatedBlocks = blocks.groupingBy { it }.eachCount().filter { it.value > 1 }.values.sum() // Count the repeated blocks
        line to repeatedBlocks
    }.maxBy { it.second }!!.first // Get the line with the most repeats
    println("Probable line #$detectedLine:")
    println(input[detectedLine])
}