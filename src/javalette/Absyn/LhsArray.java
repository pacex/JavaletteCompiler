// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class LhsArray  extends Lhs {
  public final Index index_;
  public LhsArray(Index p1) { index_ = p1; }

  public <R,A> R accept(javalette.Absyn.Lhs.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.LhsArray) {
      javalette.Absyn.LhsArray x = (javalette.Absyn.LhsArray)o;
      return this.index_.equals(x.index_);
    }
    return false;
  }

  public int hashCode() {
    return this.index_.hashCode();
  }


}
