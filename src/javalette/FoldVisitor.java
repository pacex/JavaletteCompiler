// File generated by the BNF Converter (bnfc 2.9.4).

package javalette;

/** Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Prog */
    public R visit(javalette.Absyn.Program p, A arg) {
      R r = leaf(arg);
      for (javalette.Absyn.TopDef x : p.listtopdef_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* TopDef */
    public R visit(javalette.Absyn.FnDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (javalette.Absyn.Arg x : p.listarg_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }

/* Arg */
    public R visit(javalette.Absyn.Argument p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Blk */
    public R visit(javalette.Absyn.Block p, A arg) {
      R r = leaf(arg);
      for (javalette.Absyn.Stmt x : p.liststmt_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Stmt */
    public R visit(javalette.Absyn.Empty p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.BStmt p, A arg) {
      R r = leaf(arg);
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.Decl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (javalette.Absyn.Item x : p.listitem_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(javalette.Absyn.Ass p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.AssArray p, A arg) {
      R r = leaf(arg);
      r = combine(p.index_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.Incr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Decr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Ret p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.VRet p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Cond p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.CondElse p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_1.accept(this, arg), r, arg);
      r = combine(p.stmt_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.While p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.For p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Item */
    public R visit(javalette.Absyn.NoInit p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Init p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(javalette.Absyn.Int p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Doub p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Bool p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Void p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.ArrType p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.Fun p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (javalette.Absyn.Type x : p.listtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Expr */
    public R visit(javalette.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.EIndex p, A arg) {
      R r = leaf(arg);
      r = combine(p.index_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.ELength p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.ELitArr p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.ELitInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.ELitDoub p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.ELitTrue p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.ELitFalse p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.EApp p, A arg) {
      R r = leaf(arg);
      for (javalette.Absyn.Expr x : p.listexpr_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(javalette.Absyn.EString p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Neg p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.Not p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.EMul p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.mulop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.addop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.ERel p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.relop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.EAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(javalette.Absyn.EOr p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* Index */
    public R visit(javalette.Absyn.ArrInd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* AddOp */
    public R visit(javalette.Absyn.Plus p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Minus p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MulOp */
    public R visit(javalette.Absyn.Times p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Div p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* RelOp */
    public R visit(javalette.Absyn.LTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.LE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.GTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.GE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.EQU p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(javalette.Absyn.NE p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
