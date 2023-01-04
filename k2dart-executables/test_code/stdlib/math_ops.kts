import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {
    val a1= Math.round(1.2)
    val a2 = round(1.2)
    val a3 = a2.roundToInt()
    val a4 = (1.2+a2).roundToInt()
    val a5 = Math.abs(1.0+(1.2+a2).roundToInt())
    val a6 = (1.0+(1.2+a2).roundToInt())/2.0

    val b1= Math.ceil(2.3)
    val b2= ceil(2.3)

    val c1= Math.abs(3.4)
    val c2=abs(3.4)
}
