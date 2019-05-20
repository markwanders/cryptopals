package set1

import java.math.BigInteger

fun main() {
        println(repeatingKeyXOR("Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal", "ICE"))
}

fun repeatingKeyXOR(input: String, key: String) : String {
    val byteString = input.mapIndexed { index, c ->
            val cipher = key[index % key.length]
            val xor = c.toInt().xor(cipher.toInt())
            val bytes = xor.toString(2).padStart(8, '0')
            println("$index: ${input[index]} XOR $cipher = $xor, $bytes")
            bytes
    }.joinToString("")
    return BigInteger(byteString, 2).toString(16)
}