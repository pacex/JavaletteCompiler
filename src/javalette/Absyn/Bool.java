// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class Bool  extends Type {
  public Bool() { }

  public <R,A> R accept(javalette.Absyn.Type.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Bool) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}
