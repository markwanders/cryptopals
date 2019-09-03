package set1

import java.io.File

fun main() {
    detectAESinECBMode(File("src/set1/challenge8.txt").readLines())
}

fun detectAESinECBMode(input: List<String>) {
    val detectedLine = input.first {
        detectAESinECBMode(it.toByteArray())
    }
    println("Probable line $detectedLine:")
}

fun detectAESinECBMode(input: ByteArray) : Boolean {
    return input
            .toList()
            .chunked(16)
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }
            .values
            .sum() > 0
}