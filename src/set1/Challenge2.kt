package set1

fun main() {
    println(xor("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"))
}

fun xor(a: String, b: String) : String {
    val binaryA = base16Decode(a)
    val binaryB = base16Decode(b)

    var result = ""
    binaryA.forEachIndexed { i, it ->
        result += base16Encode(it.xor(binaryB[i]))
    }

    return result
}

fun base16Encode(input: Int) : Char {
    return when(input) {
        0 -> '0'
        1 -> '1'
        2 -> '2'
        3 -> '3'
        4 -> '4'
        5 -> '5'
        6 -> '6'
        7 -> '7'
        8 -> '8'
        9 -> '9'
        10 -> 'a'
        11 -> 'b'
        12 -> 'c'
        13 -> 'd'
        14 -> 'e'
        15 -> 'f'
        else -> 'X'
    }
}
