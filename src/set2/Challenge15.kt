package set2

import javax.crypto.BadPaddingException

fun main() {
    println(String(stripPKCS7Padding("ICE ICE BABY".toByteArray().plus(ByteArray(4) {4.toByte()}))))
    println(String(stripPKCS7Padding("ICE ICE BABY".toByteArray().plus(ByteArray(4) {5.toByte()}))))
    println(String(stripPKCS7Padding("ICE ICE BABY".toByteArray().plus(1.toByte()).plus(2.toByte()).plus(3.toByte()).plus(4.toByte()))))
}

fun stripPKCS7Padding(input: ByteArray) : ByteArray {
    for(n in 1 until input.size) {
        if (input.sliceArray((input.size - n) until (input.size)).contentEquals( ByteArray(n) {n.toChar().toByte()})) {
            return input.sliceArray(0 until input.size - n)
        }
    }
    throw BadPaddingException()
}