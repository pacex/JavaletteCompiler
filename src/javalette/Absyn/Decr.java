// File generated by the BNF Converter (bnfc 2.9.4.1).

package javalette.Absyn;

public class Decr  extends Stmt {
  public final String ident_;
  public Decr(String p1) { ident_ = p1; }

  public <R,A> R accept(javalette.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.Decr) {
      javalette.Absyn.Decr x = (javalette.Absyn.Decr)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
