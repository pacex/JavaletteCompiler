// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public abstract class RelOp implements java.io.Serializable {
  public abstract <R,A> R accept(RelOp.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.LTH p, A arg);
    public R visit(javalette.Absyn.LE p, A arg);
    public R visit(javalette.Absyn.GTH p, A arg);
    public R visit(javalette.Absyn.GE p, A arg);
    public R visit(javalette.Absyn.EQU p, A arg);
    public R visit(javalette.Absyn.NE p, A arg);

  }

}
