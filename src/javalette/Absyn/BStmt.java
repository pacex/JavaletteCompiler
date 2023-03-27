// File generated by the BNF Converter (bnfc 2.9.4.1).

package javalette.Absyn;

public class BStmt  extends Stmt {
  public final Blk blk_;
  public BStmt(Blk p1) { blk_ = p1; }

  public <R,A> R accept(javalette.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.BStmt) {
      javalette.Absyn.BStmt x = (javalette.Absyn.BStmt)o;
      return this.blk_.equals(x.blk_);
    }
    return false;
  }

  public int hashCode() {
    return this.blk_.hashCode();
  }


}