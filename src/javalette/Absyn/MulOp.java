// File generated by the BNF Converter (bnfc 2.9.4.1).

package javalette.Absyn;

public abstract class MulOp implements java.io.Serializable {
  public abstract <R,A> R accept(MulOp.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.Times p, A arg);
    public R visit(javalette.Absyn.Div p, A arg);
    public R visit(javalette.Absyn.Mod p, A arg);

  }

}