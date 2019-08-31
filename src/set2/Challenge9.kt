package set2

/**
 * Implement PKCS#7 padding
 * A block cipher transforms a fixed-sized block (usually 8 or 16 bytes) of plaintext into ciphertext. But we almost never want to transform a single block; we encrypt irregularly-sized messages.
 *
 * One way we account for irregularly-sized messages is by padding, creating a plaintext that is an even multiple of the blocksize. The most popular padding scheme is called PKCS#7.
 * So: pad any block to a specific block length, by appending the number of bytes of padding to the end of the block. For instance,
 * "YELLOW SUBMARINE"
 * ... padded to 20 bytes would be:
 * "YELLOW SUBMARINE\x04\x04\x04\x04"
 */
fun main() {
    println(String(pad("YELLOW SUBMARINE".toByteArray(), 20)))
}

fun pad(input: ByteArray, paddingLength: Int): ByteArray {
    val paddingSize = (paddingLength - (input.size % paddingLength)) // PKCS#7 padding mean the value of each added byte is the number of bytes that are added, i.e. N bytes, each of value N are added.
    val padding = ByteArray(paddingSize) {paddingSize.toByte()}
    return input.plus(padding)
}
