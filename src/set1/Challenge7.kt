package set1

import java.io.File
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun main() {
    println(String(decryptAESinECBMode(Base64.getMimeDecoder().decode(File("src/set1/challenge7.txt").readBytes()), "YELLOW SUBMARINE".toByteArray())))
}

fun decryptAESinECBMode(readBytes: ByteArray, key: ByteArray) : ByteArray {
    val secretKey = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return try {
        cipher.doFinal(readBytes)
    } catch (e: BadPaddingException) {
        cipher.update(readBytes)
    }
}
