// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class Ret  extends Stmt {
  public final Expr expr_;
  public Ret(Expr p1) { expr_ = p1; }

  public <R,A> R accept(javalette.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Ret) {
      javalette.Absyn.Ret x = (javalette.Absyn.Ret)o;
      return this.expr_.equals(x.expr_);
    }
    return false;
  }

  public int hashCode() {
    return this.expr_.hashCode();
  }


}
