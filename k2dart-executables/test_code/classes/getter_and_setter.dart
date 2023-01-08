class A {
    final String get a  { return "Hi";}
    final String get b  => "Hello"

    int get c=0
         => field
        set(value) {
            field=value
        }
     /* private */

     int _d=0;
    var get d
         => _d
set d (value) {
            _d=value
        }
        

}



void main()
{

    final a=A();
    println(a.a);
    println(a.b);
    a.c=1
    println(a.c);

    a.d=3
    println(a.d);

}
