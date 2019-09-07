package set1

import java.io.File
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun main() {
    println(String(decryptAESinECBMode(Base64.getMimeDecoder().decode(File("src/set1/challenge7.txt").readBytes()), "YELLOW SUBMARINE".toByteArray(), true)))
}

fun decryptAESinECBMode(readBytes: ByteArray, key: ByteArray, usePadding: Boolean) : ByteArray {
    val secretKey = SecretKeySpec(key, "AES")

    val cipher = if(usePadding) {
        Cipher.getInstance("AES/ECB/PKCS5Padding")
    } else {
        Cipher.getInstance("AES/ECB/NoPadding")
    }
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return cipher.doFinal(readBytes)
}
