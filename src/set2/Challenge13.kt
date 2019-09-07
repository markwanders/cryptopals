package set2

import set1.decryptAESinECBMode
import kotlin.random.Random

val encryptionKey = Random.nextBytes(16)

/**
 * ECB cut-and-paste
 */
fun main() {
    val encrypted = encryptAESinECBMode(pad(profileFor("foo@bar.com").toByteArray(), encryptionKey.size), encryptionKey)
    parseKeyValue(String(decryptAESinECBMode(encrypted, encryptionKey, true)))
    //Since we know the plaintext corresponding to the ciphertext, we can manipulate the plaintext and send 16 byte blocks such that the pieces of ciphertext we want come out nicely
    //                                                      block:     1111111111222222222222222233333333333333334444444444444444555
    val exploit = encryptAESinECBMode(pad(profileFor("thisisanevilguysemail@aol.admin           AAAAAAAAAAAAAAA.com").toByteArray(), encryptionKey.size), encryptionKey)
    val exploitBlocks = exploit.toList().chunked(16).map { it.toByteArray() }
    val exploitCipherText = exploitBlocks[0].plus(exploitBlocks[1]).plus(exploitBlocks[4]).plus(exploitBlocks[2])
    parseKeyValue(String(decryptAESinECBMode(exploitCipherText, encryptionKey, false)))
}
fun parseKeyValue(input: String) {
    val map = input.split("&")
            .associate {
                val (k, v) = it.split("=")
                k to v
            }
    println(map)
}

fun profileFor(email: String) : String {
    val map = mapOf("email" to email.replace("&","").replace("=", ""), "uid" to Random.nextInt(10, 99), "role" to "user")
    return map.map { (k, v) -> "$k=$v" }.joinToString("&")
}