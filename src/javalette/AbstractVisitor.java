// File generated by the BNF Converter (bnfc 2.9.4).

package javalette;

/** Abstract Visitor */

public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
    /* Prog */
    public R visit(javalette.Absyn.Program p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Prog p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* TopDef */
    public R visit(javalette.Absyn.FnDef p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.TopDef p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Arg */
    public R visit(javalette.Absyn.Argument p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Arg p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Blk */
    public R visit(javalette.Absyn.Block p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Blk p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Stmt */
    public R visit(javalette.Absyn.Empty p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.BStmt p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Decl p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Ass p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Incr p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Decr p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Ret p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.VRet p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Cond p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.CondElse p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.While p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.SExp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Stmt p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Item */
    public R visit(javalette.Absyn.NoInit p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Init p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Item p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Type */
    public R visit(javalette.Absyn.Int p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Doub p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Bool p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Void p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Fun p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Type p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* Expr */
    public R visit(javalette.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.ELitInt p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.ELitDoub p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.ELitTrue p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.ELitFalse p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EApp p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EString p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Neg p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Not p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EMul p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.ERel p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EAnd p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EOr p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.Expr p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* AddOp */
    public R visit(javalette.Absyn.Plus p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Minus p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.AddOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* MulOp */
    public R visit(javalette.Absyn.Times p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Div p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.Mod p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.MulOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
    /* RelOp */
    public R visit(javalette.Absyn.LTH p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.LE p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.GTH p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.GE p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.EQU p, A arg) { return visitDefault(p, arg); }
    public R visit(javalette.Absyn.NE p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(javalette.Absyn.RelOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
