// File generated by the BNF Converter (bnfc 2.9.4.1).

package javalette.Absyn;

public abstract class Blk implements java.io.Serializable {
  public abstract <R,A> R accept(Blk.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.Block p, A arg);

  }

}
