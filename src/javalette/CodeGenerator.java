package javalette;

import java.io.*;
import java.util.*;

import javalette.Absyn.*;
import javalette.TypeChecker.FuncType;

public class CodeGenerator {

  public class Var{
    public Reg memPtrReg_;
    public Type type_;
  }

  private Prog ast;
  private PrintStream code;
  private HashMap<String, FuncType> functions;
  private LinkedList<HashMap<String,Var>> stack;

  public CodeGenerator(Prog ast, HashMap<String,FuncType> functions){
      this.ast = ast;
      this.functions = functions;
      this.code = new PrintStream(System.out, false);
      this.stack = new LinkedList<HashMap<String,Var>>();
  }

  public PrintStream generateCode(){
    
    // Overhead  
    code.println("declare void @printInt(i32)");
    code.println("declare void @printDouble(double)");
    code.println("declare void @printString(i8*)");
    code.println("declare i32 @readInt()");
    code.println("declare double @readDouble()");

    // Function definitions

    return code;
  }



  public class ProgVisitor<R,A> implements javalette.Absyn.Prog.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Program p, A arg)
    { /* Code for Program goes here */
      for (javalette.Absyn.TopDef x: p.listtopdef_) {
        x.accept(new TopDefVisitor<R,A>(), arg);
      }
      return null;
    }
  }
  public class TopDefVisitor<R,A> implements javalette.Absyn.TopDef.Visitor<R,A>
  {
    public R visit(javalette.Absyn.FnDef p, A arg)
    { /* Code for FnDef goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      for (javalette.Absyn.Arg x: p.listarg_) {
        x.accept(new ArgVisitor<R,A>(), arg);
      }
      p.blk_.accept(new BlkVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ArgVisitor<R,A> implements javalette.Absyn.Arg.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Argument p, A arg)
    { /* Code for Argument goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      return null;
    }
  }
  public class BlkVisitor<R,A> implements javalette.Absyn.Blk.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Block p, A arg)
    { /* Code for Block goes here */
      for (javalette.Absyn.Stmt x: p.liststmt_) {
        x.accept(new StmtVisitor<R,A>(), arg);
      }
      return null;
    }
  }
  public class StmtVisitor<R,A> implements javalette.Absyn.Stmt.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Empty p, A arg)
    { /* Code for Empty goes here */
      return null;
    }
    public R visit(javalette.Absyn.BStmt p, A arg)
    { /* Code for BStmt goes here */
      p.blk_.accept(new BlkVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.Decl p, A arg)
    { /* Code for Decl goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      for (javalette.Absyn.Item x: p.listitem_) {
        x.accept(new ItemVisitor<R,A>(), arg);
      }
      return null;
    }
    public R visit(javalette.Absyn.Ass p, A arg)
    { /* Code for Ass goes here */
      //p.ident_;
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.Incr p, A arg)
    { /* Code for Incr goes here */
      //p.ident_;
      return null;
    }
    public R visit(javalette.Absyn.Decr p, A arg)
    { /* Code for Decr goes here */
      //p.ident_;
      return null;
    }
    public R visit(javalette.Absyn.Ret p, A arg)
    { /* Code for Ret goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.VRet p, A arg)
    { /* Code for VRet goes here */
      return null;
    }
    public R visit(javalette.Absyn.Cond p, A arg)
    { /* Code for Cond goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.CondElse p, A arg)
    { /* Code for CondElse goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_1.accept(new StmtVisitor<R,A>(), arg);
      p.stmt_2.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.While p, A arg)
    { /* Code for While goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.SExp p, A arg)
    { /* Code for SExp goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ItemVisitor<R,A> implements javalette.Absyn.Item.Visitor<R,A>
  {
    public R visit(javalette.Absyn.NoInit p, A arg)
    { /* Code for NoInit goes here */
      //p.ident_;
      return null;
    }
    public R visit(javalette.Absyn.Init p, A arg)
    { /* Code for Init goes here */
      //p.ident_;
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class TypeVisitor<R,A> implements javalette.Absyn.Type.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Int p, A arg)
    { /* Code for Int goes here */
      return null;
    }
    public R visit(javalette.Absyn.Doub p, A arg)
    { /* Code for Doub goes here */
      return null;
    }
    public R visit(javalette.Absyn.Bool p, A arg)
    { /* Code for Bool goes here */
      return null;
    }
    public R visit(javalette.Absyn.Void p, A arg)
    { /* Code for Void goes here */
      return null;
    }
    public R visit(javalette.Absyn.Fun p, A arg)
    { /* Code for Fun goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      for (javalette.Absyn.Type x: p.listtype_) {
        x.accept(new TypeVisitor<R,A>(), arg);
      }
      return null;
    }
  }
  public class ExprVisitor<R,A> implements javalette.Absyn.Expr.Visitor<R,A>
  {
    public R visit(javalette.Absyn.EVar p, A arg)
    { /* Code for EVar goes here */
      //p.ident_;
      return null;
    }
    public R visit(javalette.Absyn.ELitInt p, A arg)
    { /* Code for ELitInt goes here */
      //p.integer_;
      return null;
    }
    public R visit(javalette.Absyn.ELitDoub p, A arg)
    { /* Code for ELitDoub goes here */
      //p.double_;
      return null;
    }
    public R visit(javalette.Absyn.ELitTrue p, A arg)
    { /* Code for ELitTrue goes here */
      return null;
    }
    public R visit(javalette.Absyn.ELitFalse p, A arg)
    { /* Code for ELitFalse goes here */
      return null;
    }
    public R visit(javalette.Absyn.EApp p, A arg)
    { /* Code for EApp goes here */
      //p.ident_;
      for (javalette.Absyn.Expr x: p.listexpr_) {
        x.accept(new ExprVisitor<R,A>(), arg);
      }
      return null;
    }
    public R visit(javalette.Absyn.EString p, A arg)
    { /* Code for EString goes here */
      //p.string_;
      return null;
    }
    public R visit(javalette.Absyn.Neg p, A arg)
    { /* Code for Neg goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.Not p, A arg)
    { /* Code for Not goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.EMul p, A arg)
    { /* Code for EMul goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.EAdd p, A arg)
    { /* Code for EAdd goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.addop_.accept(new AddOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.ERel p, A arg)
    { /* Code for ERel goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.relop_.accept(new RelOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.EAnd p, A arg)
    { /* Code for EAnd goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public R visit(javalette.Absyn.EOr p, A arg)
    { /* Code for EOr goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AddOpVisitor<R,A> implements javalette.Absyn.AddOp.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Plus p, A arg)
    { /* Code for Plus goes here */
      return null;
    }
    public R visit(javalette.Absyn.Minus p, A arg)
    { /* Code for Minus goes here */
      return null;
    }
  }
  public class MulOpVisitor<R,A> implements javalette.Absyn.MulOp.Visitor<R,A>
  {
    public R visit(javalette.Absyn.Times p, A arg)
    { /* Code for Times goes here */
      return null;
    }
    public R visit(javalette.Absyn.Div p, A arg)
    { /* Code for Div goes here */
      return null;
    }
    public R visit(javalette.Absyn.Mod p, A arg)
    { /* Code for Mod goes here */
      return null;
    }
  }
  public class RelOpVisitor<R,A> implements javalette.Absyn.RelOp.Visitor<R,A>
  {
    public R visit(javalette.Absyn.LTH p, A arg)
    { /* Code for LTH goes here */
      return null;
    }
    public R visit(javalette.Absyn.LE p, A arg)
    { /* Code for LE goes here */
      return null;
    }
    public R visit(javalette.Absyn.GTH p, A arg)
    { /* Code for GTH goes here */
      return null;
    }
    public R visit(javalette.Absyn.GE p, A arg)
    { /* Code for GE goes here */
      return null;
    }
    public R visit(javalette.Absyn.EQU p, A arg)
    { /* Code for EQU goes here */
      return null;
    }
    public R visit(javalette.Absyn.NE p, A arg)
    { /* Code for NE goes here */
      return null;
    }
  }
}
