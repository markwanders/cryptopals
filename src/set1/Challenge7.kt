package set1

import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun main() {
    println(String(decryptAESinECBMode(Base64.getMimeDecoder().decode(File("src/set1/challenge7.txt").readBytes()), "YELLOW SUBMARINE")))
}

fun decryptAESinECBMode(readBytes: ByteArray, key: String) : ByteArray {
    val secretKey = SecretKeySpec(key.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return cipher.doFinal(readBytes)
}
