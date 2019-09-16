package set2

import java.util.*
import kotlin.experimental.xor

val iv = randomKey()
val randomKey = randomKey()

/**
 * CBC bitflipping attacks
Generate a random AES key.

Combine your padding code and CBC code to write two functions.

The first function should take an arbitrary input string, prepend the string:

"comment1=cooking%20MCs;userdata="
.. and append the string:

";comment2=%20like%20a%20pound%20of%20bacon"
The function should quote out the ";" and "=" characters.

The function should then pad out the input to the 16-byte AES block length and encrypt it under the random AES key.

The second function should decrypt the string and look for the characters ";admin=true;" (or, equivalently, decrypt, split the string on ";", convert each resulting string into 2-tuples, and look for the "admin" tuple).

Return true or false based on whether the string exists.

If you've written the first function properly, it should not be possible to provide user input to it that will generate the string the second function is looking for. We'll have to break the crypto to do that.

Instead, modify the ciphertext (without knowledge of the AES key) to accomplish this.

You're relying on the fact that in CBC mode, a 1-bit error in a ciphertext block:

Completely scrambles the block the error occurs in
Produces the identical 1-bit error(/edit) in the next ciphertext block.
 */
fun main() {
    val encrypted = padAndEncrypt(";admin=true")
    println("Before attack; admin = ${decryptAndSearch(encrypted)}")
    encrypted[16] = encrypted[16].xor('?'.toByte()).xor(';'.toByte())
    encrypted[16 + 6] = encrypted[16 + 6].xor('?'.toByte()).xor('='.toByte())
    println("After attack; admin = ${decryptAndSearch(encrypted)}")
}

fun padAndEncrypt(input: String): ByteArray {
    val sanitizedInput = input.replace(";", "?").replace("=", "?").toByteArray()
    val encryptionInput = pad("comment1=cooking%20MCs;userdata=".toByteArray().plus(sanitizedInput).plus(";comment2=%20like%20a%20pound%20of%20bacon".toByteArray()), 16)
    return encryptAESinCBCMode(encryptionInput, randomKey, iv)
}

fun decryptAndSearch(input: ByteArray): Boolean {
    val decryptionOutput = decryptAESinCBCMode(Base64.getEncoder().encode(input), randomKey, iv)
    return parseKeyValue(String(decryptionOutput))["admin"]?.equals("true") ?: false
}