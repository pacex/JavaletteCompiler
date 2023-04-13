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

  private LinkedList<EString> stringLiterals;
  private HashMap<EString, String> stringLiteralIdentifiers;

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

  public CodeGenerator(Prog ast, HashMap<String,FuncType> functions, HashMap<Expr,Type> expressions, LinkedList<EString> stringLiterals){
      this.ast = ast;
      this.functions = functions;
      this.expressions = expressions;
      this.stringLiterals = stringLiterals;
      this.stringLiteralIdentifiers = new HashMap<EString, String>();
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

      // Constants
      code.println("@zeroInt = internal constant i32 0");
      code.println("@zeroDouble = internal constant double 0.0");
      code.println("@true = internal constant i1 true");
      code.println("@false = internal constant i1 false");

      // String literals
      int strCnt = 0;
      for (EString estr : stringLiterals){
        String ident = "@hw" + Integer.valueOf(strCnt);
        code.println(ident + " = internal constant [" + 
          Integer.valueOf(estr.string_.length() + 2) + " x i8] c\"" + estr.string_ + "\\0A\\00\"");
        /*code.println(ident + " = internal constant [" + 
          Integer.valueOf(estr.string_.length()) + " x i8] c\"" + estr.string_ + "\"");*/
        stringLiteralIdentifiers.put(estr, ident);
      }

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
      
      // Treat arguments as variables on stack
      for (Arg a : args){
        Reg memPtr = new Reg(TypeToString(a.type_) + "*"); // Create handle for memPtr register
        code.println(memPtr.Ident_ + " = alloca " + TypeToString(a.type_)); // Memory allocation
        
        Var v = new Var(memPtr, a.type_); // Add variable to stackframe
        addVarToStackFrame(a.ident_, v);

        code.println("store " + a.reg_.TypeAndIdent() + " , " + memPtr.TypeAndIdent()); // Store value from argument register
      }

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
        x.accept(new StmtVisitor(), null);
      }
      if (createStackframe) removeTopStackFrame();
      return null;
    }
  }
  public class StmtVisitor implements javalette.Absyn.Stmt.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.Empty p, java.lang.Void arg)
    { /* Code for Empty goes here */
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.BStmt p, java.lang.Void arg)
    { /* Code for BStmt goes here */
      p.blk_.accept(new BlkVisitor(), true);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Decl p, java.lang.Void arg)
    { /* Code for Decl goes here */
      //p.type_.accept(new TypeVisitor<R,A>(), arg);
      for (javalette.Absyn.Item x: p.listitem_) {
        x.accept(new ItemVisitor(), p.type_);
      }
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Ass p, java.lang.Void arg)
    { /* Code for Ass goes here */
      //p.ident_;
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Incr p, java.lang.Void arg)
    { /* Code for Incr goes here */
      //p.ident_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Decr p, java.lang.Void arg)
    { /* Code for Decr goes here */
      //p.ident_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Ret p, java.lang.Void arg)
    { /* Code for Ret goes here */
      Reg r = p.expr_.accept(new ExprVisitor(), null);
      code.println("ret " + r.TypeAndIdent());
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.VRet p, java.lang.Void arg)
    { /* Code for VRet goes here */
      code.println("ret void");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Cond p, java.lang.Void arg)
    { /* Code for Cond goes here */
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      //p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.CondElse p, java.lang.Void arg)
    { /* Code for CondElse goes here */
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      //p.stmt_1.accept(new StmtVisitor<R,A>(), arg);
      //p.stmt_2.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.While p, java.lang.Void arg)
    { /* Code for While goes here */
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      //p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.SExp p, java.lang.Void arg)
    { /* Code for SExp goes here */
      EApp f = (EApp)p.expr_;
      LinkedList<Reg> argRegs = new LinkedList<Reg>();

      // Get all arguments' registers
      for(Expr e : f.listexpr_){
        argRegs.add(e.accept(new ExprVisitor(), null));
      }

      code.print("call void @" + f.ident_ + "(");

      for(int i = 0; i < argRegs.size(); i++){
        code.print(argRegs.get(i).TypeAndIdent());
        if (i < argRegs.size() - 1) code.print(", ");
      }
      code.print(")\n");

      return null;
    }
  }
  public class ItemVisitor implements javalette.Absyn.Item.Visitor<java.lang.Void,Type>
  {
    public java.lang.Void visit(javalette.Absyn.NoInit p, Type t)
    { /* Code for NoInit goes here */
      //p.ident_;
      Reg memPtr = new Reg(TypeToString(t) + "*"); // Create handle for memPtr register
      code.println(memPtr.Ident_ + " = alloca " + TypeToString(t)); // Memory allocation
      
      Var v = new Var(memPtr, t); // Add variable to stackframe
      addVarToStackFrame(p.ident_, v);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Init p, Type t)
    { /* Code for Init goes here */
      //p.ident_;
      Reg memPtr = new Reg(TypeToString(t) + "*"); // Create handle for memPtr register
      code.println(memPtr.Ident_ + " = alloca " + TypeToString(t)); // Memory allocation
      
      Var v = new Var(memPtr, t); // Add variable to stackframe
      addVarToStackFrame(p.ident_, v);
      
      Reg e = p.expr_.accept(new ExprVisitor(), null);

      code.println("store " + e.TypeAndIdent() + " , " + memPtr.TypeAndIdent()); // Store value from expression register
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
  public class ExprVisitor implements javalette.Absyn.Expr.Visitor<Reg,java.lang.Void>
  {
    public Reg visit(javalette.Absyn.EVar p, java.lang.Void arg)
    { /* Code for EVar goes here */
      //p.ident_;
      return null;
    }
    public Reg visit(javalette.Absyn.ELitInt p, java.lang.Void arg)
    { /* Code for ELitInt goes here */
      //p.integer_;
      Reg z = new Reg("i32");
      Reg r = new Reg("i32");
      code.println(z.Ident_ + " = load i32, i32* @zeroInt");
      code.println(r.Ident_ + " = add " + z.TypeAndIdent() + ", " + Integer.valueOf(p.integer_));
      return r;
    }
    public Reg visit(javalette.Absyn.ELitDoub p, java.lang.Void arg)
    { /* Code for ELitDoub goes here */
      //p.double_;
      return null;
    }
    public Reg visit(javalette.Absyn.ELitTrue p, java.lang.Void arg)
    { /* Code for ELitTrue goes here */
      return null;
    }
    public Reg visit(javalette.Absyn.ELitFalse p, java.lang.Void arg)
    { /* Code for ELitFalse goes here */
      return null;
    }
    public Reg visit(javalette.Absyn.EApp p, java.lang.Void arg)
    { /* Code for EApp goes here */
      //p.ident_;
      for (javalette.Absyn.Expr x: p.listexpr_) {
        //x.accept(new ExprVisitor<R,A>(), arg);
      }
      return null;
    }
    public Reg visit(javalette.Absyn.EString p, java.lang.Void arg)
    { /* Code for EString goes here */
      Reg r = new Reg("i8*", stringLiteralIdentifiers.get(p));
      return r;
    }
    public Reg visit(javalette.Absyn.Neg p, java.lang.Void arg)
    { /* Code for Neg goes here */
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.Not p, java.lang.Void arg)
    { /* Code for Not goes here */
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.EMul p, java.lang.Void arg)
    { /* Code for EMul goes here */
      //p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      //p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.EAdd p, java.lang.Void arg)
    { /* Code for EAdd goes here */
      //p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      //p.addop_.accept(new AddOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.ERel p, java.lang.Void arg)
    { /* Code for ERel goes here */
      //p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      //p.relop_.accept(new RelOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.EAnd p, java.lang.Void arg)
    { /* Code for EAnd goes here */
      //p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public Reg visit(javalette.Absyn.EOr p, java.lang.Void arg)
    { /* Code for EOr goes here */
      //p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor<R,A>(), arg);
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
