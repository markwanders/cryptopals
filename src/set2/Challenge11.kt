package set2

import set1.detectAESinECBMode
import java.io.File
import java.util.*
import kotlin.random.Random


/**
 * Now that you have ECB and CBC working:

Write a function to generate a random AES key; that's just 16 random bytes.

Write a function that encrypts data under an unknown key --- that is, a function that generates a random key and encrypts under it.

The function should look like:

encryption_oracle(your-input)
=> [MEANINGLESS JIBBER JABBER]
Under the hood, have the function append 5-10 bytes (count chosen randomly) before the plaintext and 5-10 bytes after the plaintext.

Now, have the function choose to encrypt under ECB 1/2 the time, and under CBC the other half (just use random IVs each time for CBC). Use rand(2) to decide which to use.

Detect the block cipher mode the function is using each time. You should end up with a piece of code that, pointed at a block box that might be encrypting ECB or CBC, tells you which one is happening.
 */
fun main() {
    println(String(encryptionOracle(File("src/set2/challenge11.txt").readText().toByteArray())))
}

fun encryptionOracle(input: ByteArray) : ByteArray {
    val cipherText = randomCipherText(input)
    val ecb = detectAESinECBMode(cipherText)
    println("Detected use of ${if(ecb) "ECB" else "CBC"}")

    return cipherText
}

fun randomKey() : ByteArray {
    return Random.nextBytes(16)
}

fun randomCipherText(input: ByteArray) : ByteArray {
    val key = randomKey()
    val appended = Random.nextBytes(Random.nextInt(5, 11))
    val prepended = Random.nextBytes(Random.nextInt(5, 11))
    val plaintext = pad(prepended + input + appended, key.size)

    val useECB = Random.nextBoolean()
    println("Encrypting under ${if(useECB) "ECB" else "CBC"}")

    return if(useECB) {
        encryptAESinECBMode(plaintext, key)
    } else {
        val iv = Random.nextBytes(key.size)
        encryptAESinCBCMode(plaintext, key, iv)
    }
}