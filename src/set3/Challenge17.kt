package set3

import set2.decryptAESinCBCMode
import set2.encryptAESinCBCMode
import set2.pad
import set2.stripPKCS7Padding
import java.io.File
import java.util.*
import javax.crypto.BadPaddingException
import kotlin.random.Random

val randomKey = set2.randomKey()
val iv = set2.randomKey()

fun main() {
    val inputs = File("src/set3/challenge17.txt").readLines().map { Base64.getDecoder().decode(it) }
    val encryptedRandomString = encryptRandomInput(inputs)
    println(cbcPaddingOracle(encryptedRandomString))

}

fun cbcPaddingOracle(input: ByteArray) : Boolean {
    val decrypted = decryptAESinCBCMode(Base64.getEncoder().encode(input), randomKey, iv)
    return try {
        stripPKCS7Padding(decrypted)
        true
    } catch (e: BadPaddingException) {
        false
    }
}

fun encryptRandomInput(inputs: List<ByteArray>) : ByteArray {
    val selectedInput = inputs[Random.nextInt(inputs.size - 1)]
    return encryptAESinCBCMode(pad(selectedInput, 16), randomKey, iv)
}