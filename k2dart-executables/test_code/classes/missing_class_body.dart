class A
{
}


class B/* (val a:int,var b:int) */
{
final int a;
int b;

B(this.a,this.b,);
}


data class C/* (var a:int,val b:String) */
{
int a;
final String b;

C(this.a,this.b,);
}


class C
{

}
