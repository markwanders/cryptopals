package set2

import set1.detectAESinECBMode
import java.io.File
import java.util.*

val key = "YELLOW SUBMARINE".toByteArray()
val unknownString: ByteArray = Base64.getMimeDecoder().decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg\n" +
        "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq\n" +
        "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg\n" +
        "YnkK")

fun main() {
    println(String(byteAtATimeECBDecryption(File("src/set2/challenge11.txt").readText().toByteArray())))
}

fun byteAtATimeECBDecryption(input: ByteArray) : ByteArray {
    val keySize = findKeySize(input)
    val cipherText = notSoRandomCipherText(input)
    val usesECB = detectAESinECBMode(cipherText)
    var plaintext = ByteArray(0)
    println("Uses ECB: $usesECB")

    (0..unknownString.size/keySize).forEach { blockNumber ->
        (1..keySize).forEach { byteNumber ->
            val placeHolderNumber = keySize - byteNumber
            val oneByteShort = ByteArray(placeHolderNumber) { 'A'.toByte() }
            val dictionary = (0..255).map { i ->
                i.toChar() to notSoRandomCipherText(oneByteShort.plus(plaintext).plus(i.toChar().toByte())).copyOfRange(keySize * blockNumber, keySize + keySize * blockNumber)
            }
            val oneByteShortOutput = notSoRandomCipherText(oneByteShort)
            val byte = dictionary.find { oneByteShortOutput.copyOfRange(keySize * blockNumber, keySize + keySize * blockNumber).contentEquals(it.second) }?.first?.toByte()
            plaintext = plaintext.plus(byte ?: ' '.toByte())
        }

    }
    return plaintext
}

fun notSoRandomCipherText(input: ByteArray) : ByteArray {
    val plaintext = pad(input + unknownString, key.size)
    return encryptAESinECBMode(plaintext, key)
}

//As soon as we get a repeating first block, we know the key length
fun findKeySize(input: ByteArray) : Int {
    var keySizeFound = false
    var n = 0
    var value = ByteArray(0)
    while (!keySizeFound) {
        if(n == 0) {
            value = notSoRandomCipherText(input.copyOfRange(0, n))
            n++
        }
        val next = notSoRandomCipherText(input.copyOfRange(0, n + 1))
        if (next.copyOfRange(0, n).contentEquals(value.copyOfRange(0, n))) {
            println("Found key with size $n")
            keySizeFound = true
        } else {
            value = next
            n++
        }
    }

    return n
}