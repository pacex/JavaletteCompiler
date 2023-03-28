// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public abstract class Stmt implements java.io.Serializable {
  public abstract <R,A> R accept(Stmt.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.Empty p, A arg);
    public R visit(javalette.Absyn.BStmt p, A arg);
    public R visit(javalette.Absyn.Decl p, A arg);
    public R visit(javalette.Absyn.Ass p, A arg);
    public R visit(javalette.Absyn.Incr p, A arg);
    public R visit(javalette.Absyn.Decr p, A arg);
    public R visit(javalette.Absyn.Ret p, A arg);
    public R visit(javalette.Absyn.VRet p, A arg);
    public R visit(javalette.Absyn.Cond p, A arg);
    public R visit(javalette.Absyn.CondElse p, A arg);
    public R visit(javalette.Absyn.While p, A arg);
    public R visit(javalette.Absyn.SExp p, A arg);

  }

}
