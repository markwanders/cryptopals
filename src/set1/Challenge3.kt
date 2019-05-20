package set1

import java.math.BigInteger


fun main() {
    println(findSingleByteXORCipher("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"))
}

fun findSingleByteXORCipher(input: String): String {
    val decoded = BigInteger(input, 16).toString(2)
    var padded = decoded
    while (padded.length % 8 != 0) {
        padded = String.format("0$padded")
    }
    val bytes = padded
            .chunked(8)
            .map { byte -> Integer.parseInt(byte, 2) }
    val solutions = (0..255)
            .map { cypher -> String(singleCharXOR(bytes, cypher)
                    .map { it.toChar() }
                    .toCharArray())
    }
    return solutions
            .maxBy { solution -> solution.map { letter -> englishLetterScore(letter) }.sum() }!!
}

fun singleCharXOR(input: List<Int>, cipher: Int): List<Int> {
    return input.map { it.xor(cipher) }
}

fun englishLetterScore(letter: Char) : Int {
    return when(letter) {
        'e' , 'E' -> 12
        't', 'T' -> 9
        'a', 'A' -> 8
        'o', 'O'-> 7
        'i', 'I' -> 7
        'n', 'N' -> 7
        's', 'S', ' ' -> 6
        'h', 'H' -> 6
        'r', 'R' -> 6
        'd', 'D' -> 4
        'l', 'L' -> 4
        else -> 0
    }
}