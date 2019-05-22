package set1

import java.io.File
import java.util.*


fun main() {
    println("This should say 37: ${hamming("this is a test","wokka wokka!!!")}")
    breakRepeatingKeyXOR(File("src/set1/challenge6.txt").readBytes())
}
fun hamming(a: String, b: String) : Int {
    return hamming(a.map { char -> char.toInt() }, b.map { char -> char.toInt() })
}
fun hamming(a: List<Int>, b: List<Int>) : Int {
    var diff = 0
    a.forEachIndexed { index, c ->
        diff += Integer.toBinaryString(c.xor(b[index])).count { char -> char == '1' }
    }
    return diff
}

fun breakRepeatingKeyXOR(encodedCipherText: ByteArray) {
    val cipherText = Base64.getMimeDecoder().decode(encodedCipherText).map { it.toInt() }

    val keySize = (2..40).map { possibleKeySize ->
        val blocks = cipherText.chunked(possibleKeySize)

        possibleKeySize to blocks.mapIndexed { index, _ ->
            if((index + 2) < blocks.size) {
                hamming(blocks[index], blocks[index + 1]).div(possibleKeySize.toDouble())
            } else {
                0.0
            }
        }.average()
    }.minBy { it.second }!!.first

    println("Most likely key size is $keySize")

    val blocks = cipherText.chunked(keySize)
    val transposed = MutableList(blocks[0].size) { MutableList(blocks.size) {0} }
    blocks.forEachIndexed { outerIndex, byteArray ->
        byteArray.forEachIndexed {innerIndex, _ ->
            transposed[innerIndex][outerIndex] = blocks[outerIndex][innerIndex]
        }
    }

    val key = transposed.map {block ->
        findSingleByteXORCipher(block)
    }

    println("The key is: \"${String(key.toCharArray())}\"")

    val plaintext= repeatingKeyXOR(cipherText, key.map { it.toInt() })

    println("The plaintext message was:")
    println(plaintext)

}

fun findSingleByteXORCipher(bytes: List<Int>): Char {
    val solutions = (0..255)
            .map { cypher -> cypher to String(singleCharXOR(bytes, cypher)
                    .map { it.toChar() }
                    .toCharArray())
            }
    val bestMatch = solutions
            .maxBy { solution -> solution.second.map { letter -> englishLetterScore(letter) }.sum() }!!.first

    return bestMatch.toChar()
}

fun repeatingKeyXOR(input: List<Int>, key: List<Int>) : String {
    return input.mapIndexed { index, c ->
        val cipher = key[index % key.size]
         c.xor(cipher).toChar()
    }.joinToString("")
}