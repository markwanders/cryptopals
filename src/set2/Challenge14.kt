package set2

import kotlin.random.Random

/**
 * Byte-at-a-time ECB decryption (Harder)
 * Take your oracle function from #12. Now generate a random count of random bytes and prepend this string to every plaintext. You are now doing:
 *
 * AES-128-ECB(random-prefix || attacker-controlled || target-bytes, random-key)
 *
 * Same goal: decrypt the target-bytes.
 */
val randomPrefix = Random.nextBytes(Random.nextInt(0, 32))
var prefixPlusTargetLength = 0

fun main() {
    val blockSize = findBlockSize(::oracle)
    //We can find the length of the random prefix by adding increasing padding until we see a change in block repetition in the ciphertext. Then we know (padding + prefix) % keysize == 0
    val foundPrefixLength = findPrefixLength()
    println("Random prefix length: ${randomPrefix.size}, deduced: $foundPrefixLength")
    println(String(byteAtATimeECBDecryptionWithPrefix(::oracle, foundPrefixLength, blockSize)))
}

fun byteAtATimeECBDecryptionWithPrefix(oracle: (b: ByteArray) -> ByteArray, prefixLength: Int, blockSize: Int): ByteArray {
    var plaintext = ByteArray(0)
    val targetSize = prefixPlusTargetLength - prefixLength
    (0..targetSize).forEach { _ ->
        val paddingLength = (blockSize - ((plaintext.size + 1 + prefixLength) % blockSize)) % blockSize
        val padding = ByteArray(paddingLength) { 'A'.toByte() }
        val targetBlockNumber = (plaintext.size + prefixLength) / blockSize
        val targetRange = ((targetBlockNumber * blockSize) until ((targetBlockNumber + 1) * blockSize))
        val targetBlock = oracle(padding).slice(targetRange)
        for (byte in 0..256) {
            val attackerControlled = padding + plaintext + byte.toChar().toByte()
            val encryptedBlock = oracle(attackerControlled).slice(targetRange)
            if (encryptedBlock == targetBlock) {
                plaintext += byte.toChar().toByte()
                break
            }
        }
    }

    return plaintext
}

fun oracle(attackerControlled: ByteArray): ByteArray {
    val plaintext = pad(randomPrefix + attackerControlled + unknownString, key.size)
    return encryptAESinECBMode(plaintext, key)
}

fun toHex(bytes: ByteArray): String {
    var output = ""
    for (b in bytes) {
        output += String.format("%02X", b)
    }
    return output
}

fun findPrefixLength(): Int {
    var previousCipherText = ByteArray(0)
    var previousIntersect = 0
    (0..16).forEach { n ->
        val cipherText = oracle(ByteArray(n) { 'A'.toByte() })
        val intersect = cipherText.toList().chunked(key.size).intersect(previousCipherText.toList().chunked(key.size).asIterable())
//        println("${intersect.size} $n : ${toHex(cipherText)}")
        if (intersect.isNotEmpty() && n == 1) {
            //this means prefix >= 16
            previousIntersect = intersect.size
        }
        if (intersect.isNotEmpty() && intersect.size > previousIntersect) {
            val prefixLength = ((intersect.size) * key.size) - (n - 1) % key.size
//            println("intersects: ${intersect.size} Padding size = ${n - 1}, prefix size $prefixLength")
            return prefixLength
        }
        previousCipherText = cipherText
    }
    //this means prefix%16 == 0
    return previousIntersect * key.size
}

fun findBlockSize(oracle: (b: ByteArray) -> ByteArray): Int {
    val input = ByteArray(32) { 'A'.toByte() }
    var n = 0
    var value = 0
    var keySize = 0
    while (keySize == 0) {
        if (n == 0) {
            value = oracle(input.copyOfRange(0, n)).size
            n++
        }
        val next = oracle(input.copyOfRange(0, n + 1)).size
        if (next > value) {
            println("Found key with size ${next - value}")
            keySize = next - value
            prefixPlusTargetLength = value - n - 1
        } else {
            value = next
            n++
        }
    }

    return keySize
}