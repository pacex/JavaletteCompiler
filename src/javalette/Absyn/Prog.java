// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public abstract class Prog implements java.io.Serializable {
  public abstract <R,A> R accept(Prog.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.Program p, A arg);

  }

}
