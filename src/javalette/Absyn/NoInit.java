// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public class NoInit  extends Item {
  public final String ident_;
  public NoInit(String p1) { ident_ = p1; }

  public <R,A> R accept(javalette.Absyn.Item.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof javalette.Absyn.NoInit) {
      javalette.Absyn.NoInit x = (javalette.Absyn.NoInit)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
