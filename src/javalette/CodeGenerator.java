package javalette;

import java.io.*;
import java.util.*;

import javalette.Absyn.*;
import javalette.TypeChecker.FuncType;

public class CodeGenerator {

  public class Var{
    public Var(Reg r, Type t) {
      memPtrReg_ = r;
      type_ = t;
    }
    public Reg memPtrReg_;
    public Type type_;
  }

  public class Arg{
    public Arg(Reg r, Type t, String i){
      type_ = t;
      reg_ = r;
      ident_ = i;
    }
    public Type type_;
    public String ident_;
    public Reg reg_;
  }

  private String TypeToString(Type t){
    if (t instanceof Int) return "i32";
    else if (t instanceof Doub) return "double";
    else if (t instanceof Bool) return "i1";
    else if (t instanceof javalette.Absyn.Void) return "void";
    else return "<unknown type>";
  }

  private Prog ast;
  private PrintStream code;
  private HashMap<String, FuncType> functions;
  private HashMap<Expr,Type> expressions;
  private LinkedList<HashMap<String,Var>> stack;

  private String currentFunction;

  // region Stack Frame Methods
  private void createStackFrame(){
    stack.add(new HashMap<String,Var>());
  }

  private void removeTopStackFrame(){
    stack.removeLast();
  }

  private void addVarToStackFrame(String identifier, Var var){
    stack.getLast().put(identifier, var);
  }

  private Var getVar(String identifier){
    for(int i = stack.size() - 1; i >= 0; i--){
      HashMap<String,Var> frame = stack.get(i);
      if (frame.containsKey(identifier)){
        return frame.get(identifier);
      }
    }
    return null;
  }
  // endregion

  public CodeGenerator(Prog ast, HashMap<String,FuncType> functions, HashMap<Expr,Type> expressions){
      this.ast = ast;
      this.functions = functions;
      this.expressions = expressions;
      this.code = new PrintStream(System.out, false);
      this.stack = new LinkedList<HashMap<String,Var>>();
      this.currentFunction = "";
  }

  public PrintStream generateCode(){
    
    

    ProgVisitor cg = new ProgVisitor();
    cg.visit((Program)ast, null);

    return code;
  }



  public class ProgVisitor implements javalette.Absyn.Prog.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.Program p, java.lang.Void arg)
    { 
      currentFunction = "";

      // Overhead  
      code.println("declare void @printInt(i32)");
      code.println("declare void @printDouble(double)");
      code.println("declare void @printString(i8*)");
      code.println("declare i32 @readInt()");
      code.println("declare double @readDouble()");

      for (javalette.Absyn.TopDef x: p.listtopdef_) {
        x.accept(new TopDefVisitor(), null);
      }
      return null;
    }
  }
  public class TopDefVisitor implements javalette.Absyn.TopDef.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.FnDef p, java.lang.Void arg)
    { /* Code for FnDef goes here */
      //p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      createStackFrame();
      LinkedList<Arg> args = new LinkedList<Arg>();

      currentFunction = p.ident_;

      code.print("define " + TypeToString(p.type_) + " @" + p.ident_ + "(");

      for (int i = 0; i < p.listarg_.size(); i++) {
        javalette.Absyn.Arg x = p.listarg_.get(i);
        Arg a = x.accept(new ArgVisitor(), arg);
        args.add(a);
        if (i < p.listarg_.size() - 1) code.print(", ");
      }
      code.print(") {\n");
      code.print("entry: ");
      /* TODO: initialize arguments as variables */

      p.blk_.accept(new BlkVisitor(), false);

      code.print("}\n");

      removeTopStackFrame();
      return null;
    }
  }
  public class ArgVisitor implements javalette.Absyn.Arg.Visitor<Arg,java.lang.Void>
  {
    public Arg visit(javalette.Absyn.Argument p, java.lang.Void arg)
    { /* Code for Argument goes here */
      Arg a = new Arg(new Reg(TypeToString(p.type_)), p.type_, p.ident_);
      code.print(TypeToString(a.type_) + " " + a.reg_.Ident_);
      return a;
    }
  }
  public class BlkVisitor implements javalette.Absyn.Blk.Visitor<java.lang.Void,Boolean>
  {
    public java.lang.Void visit(javalette.Absyn.Block p, Boolean createStackframe)
    { /* Code for Block goes here */
      if (createStackframe) createStackFrame();
      for (javalette.Absyn.Stmt x: p.liststmt_) {
        //x.accept(new StmtVisitor<R,A>(), arg);
      }
      if (createStackframe) removeTopStackFrame();
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
      p.blk_.accept(new BlkVisitor(), true);
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
