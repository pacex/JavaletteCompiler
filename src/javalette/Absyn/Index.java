// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public abstract class Index implements java.io.Serializable {
  public abstract <R,A> R accept(Index.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.ArrInd p, A arg);

  }

}
