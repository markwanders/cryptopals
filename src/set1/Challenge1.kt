package set1

fun main() {
    println(base64Encode(base16Decode("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")))
}

val chars = arrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, '+', '/')

fun base64Encode(input: List<Int>): String {
    var buffer = ""
    var output = ""
    input.forEach {
        val binary = String.format("%4s", Integer.toBinaryString(it)).replace(' ', '0')
        buffer += binary
    }
    buffer.chunked(6).forEach {
        output += chars[it.toInt(2)]
    }

    return output
}

fun base16Decode(input: String): List<Int> {
    return input.map { char ->
        base16Decode(char)
    }
}

fun base16Decode(input: Char): Int {
    return when (input) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'a' -> 10
        'b' -> 11
        'c' -> 12
        'd' -> 13
        'e' -> 14
        'f' -> 15
        else -> 0
    }
}