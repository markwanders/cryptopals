package set2

import set1.decryptAESinECBMode
import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

/**
 * Implement CBC mode
 * CBC mode is a block cipher mode that allows us to encrypt irregularly-sized messages, despite the fact that a block cipher natively only transforms individual blocks.
 * In CBC mode, each ciphertext block is added to the next plaintext block before the next call to the cipher core.
 *
 * The first plaintext block, which has no associated previous ciphertext block, is added to a "fake 0th ciphertext block" called the initialization vector, or IV.
 * Implement CBC mode by hand by taking the ECB function you wrote earlier, making it encrypt instead of decrypt (verify this by decrypting whatever you encrypt to test), and using your XOR function from the previous exercise to combine them.
 *
 * The file here is intelligible (somewhat) when CBC decrypted against "YELLOW SUBMARINE" with an IV of all ASCII 0 (\x00\x00\x00 &c)
 */

fun main() {
    val key = "YELLOW SUBMARINE"
    val iv = ByteArray(key.length){"0".toByte()}
    val encrypted = Base64.getMimeEncoder().encode(encryptAESinCBCMode("some random input i don't care about".toByteArray(), key.toByteArray(), iv))
    println(String(encrypted))
    println(String(decryptAESinCBCMode(encrypted, key, iv)))
    println(String(decryptAESinCBCMode(File("src/set2/challenge10.txt").readBytes(), key, iv)))
}

fun decryptAESinCBCMode(readBytes: ByteArray, key: String, iv: ByteArray): ByteArray {
    val decoded = Base64.getMimeDecoder().decode(readBytes)
    val ciphertextBlocks = decoded.toList().chunked(key.length).map { it.toByteArray() }
    val plaintextBlocks = mutableListOf<ByteArray>()
    ciphertextBlocks.mapIndexedTo( plaintextBlocks, { index, ciphertextBlock ->
        val previousBlock = if(index == 0) {
            iv
        } else {
            ciphertextBlocks[index - 1]
        }
        val ecbDecrypted = decryptAESinECBMode(ciphertextBlock, key)
        ecbDecrypted.mapIndexed { byteIndex, byte -> byte.xor(previousBlock[byteIndex])}.toByteArray()
    })
    return plaintextBlocks.reduce(ByteArray::plus)
}

fun encryptAESinCBCMode(input: ByteArray, key: ByteArray, iv: ByteArray) : ByteArray {
    val padded = pad(input, key.size)
    val plaintextBlocks = padded.toList().chunked(key.size).map { it.toByteArray() }
    val encryptedBlocks = mutableListOf<ByteArray>()
    // In CBC mode, each block of plaintext is XORed with the previous ciphertext block before being encrypted.
    // This way, each ciphertext block depends on all plaintext blocks processed up to that point.
    // To make each message unique, an initialization vector must be used in the first block.
    plaintextBlocks.mapIndexedTo( encryptedBlocks, { index, plaintextBlock ->
        val previousBlock = if(index == 0) {
            iv
        } else {
            encryptedBlocks[index -1]
        }
        encryptAESinECBMode(plaintextBlock.mapIndexed { byteIndex, byte -> byte.xor(previousBlock[byteIndex]) }.toByteArray(), key)
    })
    return encryptedBlocks.reduce(ByteArray::plus)
}

fun encryptAESinECBMode(input: ByteArray, key: ByteArray) : ByteArray {
    val secretKey = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return cipher.doFinal(input)
}