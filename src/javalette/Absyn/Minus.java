// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class Minus  extends AddOp {
  public Minus() { }

  public <R,A> R accept(javalette.Absyn.AddOp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Minus) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}
