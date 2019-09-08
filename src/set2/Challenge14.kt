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

fun main() {
    println(randomPrefix.size)
    (1..32).forEach {
        val cipherText = toHex(oracle(ByteArray(it){'A'.toByte()}))
        println("$it : $cipherText")
    }
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