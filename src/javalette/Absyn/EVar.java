// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class EVar  extends Expr {
  public final String ident_;
  public EVar(String p1) { ident_ = p1; }

  public <R,A> R accept(javalette.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.EVar) {
      javalette.Absyn.EVar x = (javalette.Absyn.EVar)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
