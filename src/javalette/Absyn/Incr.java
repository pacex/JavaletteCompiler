// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class Incr  extends Stmt {
  public final Lhs lhs_;
  public Incr(Lhs p1) { lhs_ = p1; }

  public <R,A> R accept(javalette.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Incr) {
      javalette.Absyn.Incr x = (javalette.Absyn.Incr)o;
      return this.lhs_.equals(x.lhs_);
    }
    return false;
  }

  public int hashCode() {
    return this.lhs_.hashCode();
  }


}
