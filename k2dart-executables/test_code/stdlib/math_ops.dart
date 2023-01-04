import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.abs
import kotlin.math.roundToInt

void main() {
    final a1= (1.2).round();
    final a2 = (1.2).round();
    final a3 = a2.roundToInt();
    final a4 = (1.2+a2).roundToInt();
    final a5 = (1.0+(1.2+a2).roundToInt()).abs();
    final a6 = (1.0+(1.2+a2).roundToInt())/2.0;

    final b1= (2.3).ceil();
    final b2= (2.3).ceil();

    final c1= (3.4).abs();
    final c2=(3.4).abs();
}
