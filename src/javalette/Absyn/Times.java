// File generated by the BNF Converter (bnfc 2.9.4.1).

package javalette.Absyn;

public class Times  extends MulOp {
  public Times() { }

  public <R,A> R accept(javalette.Absyn.MulOp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Times) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}
