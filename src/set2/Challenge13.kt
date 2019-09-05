package set2

import set1.decryptAESinECBMode
import kotlin.random.Random

val encryptionKey = Random.nextBytes(16)

fun main() {
    val encrypted = encryptAESinECBMode(pad(profileFor("foo@bar.com").toByteArray(), encryptionKey.size), encryptionKey)
    parseKeyValue(String(decryptAESinECBMode(encrypted, encryptionKey)))

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
    val map = mapOf("email" to email.replace("&","").replace("=", ""), "uid" to 10, "role" to "user")
    return map.map { (k, v) -> "$k=$v" }.joinToString("&")
}
