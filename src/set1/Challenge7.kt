package set1

import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun main() {
    decryptAESinECBMode(File("src/set1/challenge7.txt").readBytes())
}

fun decryptAESinECBMode(readBytes: ByteArray) {
    val decoded = Base64.getMimeDecoder().decode(readBytes)
    val secretKey = SecretKeySpec("YELLOW SUBMARINE".toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    println(String(cipher.doFinal(decoded)))
}
