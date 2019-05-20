package set1


fun main() {
    println(hamming("this is a test","wokka wokka!!!"))
    breakRepeatingKeyXOR(readFileLineByLineUsingForEachLine("src/set1/challenge6.txt").joinToString("\n"))
}

fun hamming(a: String, b: String) : Int {
    val bytesA = a.map { char -> char.toInt() }.map { int -> int.toString(2).padStart(8, '0') }.joinToString("")
    val bytesB = b.map { char -> char.toInt() }.map { int -> int.toString(2).padStart(8, '0') }.joinToString("")

    var diff = 0
    bytesA.forEachIndexed { index, c ->
        if(c != bytesB[index])
            diff++
    }

    return diff
}

fun breakRepeatingKeyXOR(input: String) : String {
    println(input)
    val hammingDistances = (2..40).map {
        keysize -> keysize to hamming(input.substring(0, keysize), input.substring(keysize, 2* keysize)).div(keysize.toDouble())
    }.toMap()
    println(hammingDistances)
    val minimumHammingDistance = hammingDistances.minBy { it.value }!!.value
    println(minimumHammingDistance)
    val probableKeysizes = hammingDistances.filter { it.value == minimumHammingDistance }.keys

    println(probableKeysizes)

    return input
}