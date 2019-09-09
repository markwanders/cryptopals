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
val randomPrefix = Random.nextBytes(96)

fun main() {
    //We can find the length of the random prefix by adding increasing padding until we see a change in block repetition in the ciphertext. Then we know (padding + prefix) % keysize == 0
    println(randomPrefix.size)
    println(findPrefixLength())
}

fun oracle(attackerControlled: ByteArray): ByteArray {
    val plaintext = pad(randomPrefix + attackerControlled + unknownString, key.size)
    return encryptAESinECBMode(plaintext, key)
}

fun toHex(bytes: ByteArray) : String {
    var output = ""
    for (b in bytes) {
        output += String.format("%02X", b)
    }
    return output
}

fun findPrefixLength() :Int {
    var previousCipherText = ByteArray(0)
    var previousIntersect = 0
    (0..16).forEach { n ->
        val cipherText = oracle(ByteArray(n){'A'.toByte()})
        val intersect = cipherText.toList().chunked(key.size).intersect(previousCipherText.toList().chunked(key.size).asIterable())
//        println("${intersect.size} $n : ${toHex(cipherText)}")
        if(intersect.isNotEmpty() && n == 1) {
            //this means prefix >= 16
            previousIntersect = intersect.size
        }
        if(intersect.isNotEmpty() && intersect.size > previousIntersect) {
            val prefixLength = ((intersect.size) * key.size) - (n - 1)%key.size
//            println("intersects: ${intersect.size} Padding size = ${n - 1}, prefix size $prefixLength")
            return prefixLength
        }
        previousCipherText = cipherText
    }
    //this means prefix%16 == 0
    return previousIntersect * key.size
}