/**
 * Aaaaa
 * bbbbb
 * ccccc
 */
open class A(
    /**
     * sssss
     */
    val s: Float=0f,
    /**
     * eeee
     */
    val e: Float=0f
) {
    fun contains(value: Float): Boolean = value >= s && value <= e
}
