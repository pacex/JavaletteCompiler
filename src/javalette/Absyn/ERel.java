// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class ERel  extends Expr {
  public final Expr expr_1, expr_2;
  public final RelOp relop_;
  public ERel(Expr p1, RelOp p2, Expr p3) { expr_1 = p1; relop_ = p2; expr_2 = p3; }

  public <R,A> R accept(javalette.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.ERel) {
      javalette.Absyn.ERel x = (javalette.Absyn.ERel)o;
      return this.expr_1.equals(x.expr_1) && this.relop_.equals(x.relop_) && this.expr_2.equals(x.expr_2);
    }
    return false;
  }

  public int hashCode() {
    return 37*(37*(this.expr_1.hashCode())+this.relop_.hashCode())+this.expr_2.hashCode();
  }


}
