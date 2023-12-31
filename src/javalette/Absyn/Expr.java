// File generated by the BNF Converter (bnfc 2.9.4).

package javalette.Absyn;

public abstract class Expr implements java.io.Serializable {
  public abstract <R,A> R accept(Expr.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(javalette.Absyn.EVar p, A arg);
    public R visit(javalette.Absyn.EIndex p, A arg);
    public R visit(javalette.Absyn.ELength p, A arg);
    public R visit(javalette.Absyn.ELitArr p, A arg);
    public R visit(javalette.Absyn.ELitInt p, A arg);
    public R visit(javalette.Absyn.ELitDoub p, A arg);
    public R visit(javalette.Absyn.ELitTrue p, A arg);
    public R visit(javalette.Absyn.ELitFalse p, A arg);
    public R visit(javalette.Absyn.EApp p, A arg);
    public R visit(javalette.Absyn.EString p, A arg);
    public R visit(javalette.Absyn.Neg p, A arg);
    public R visit(javalette.Absyn.Not p, A arg);
    public R visit(javalette.Absyn.EMul p, A arg);
    public R visit(javalette.Absyn.EAdd p, A arg);
    public R visit(javalette.Absyn.ERel p, A arg);
    public R visit(javalette.Absyn.EAnd p, A arg);
    public R visit(javalette.Absyn.EOr p, A arg);

  }

}
