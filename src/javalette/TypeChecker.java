// File generated by the BNF Converter (bnfc 2.9.4).
package javalette;

import java.util.*;

import javalette.Absyn.*;
import javalette.Absyn.Void;

/*** Type Checker. ***/

public class TypeChecker
{
  public class FuncType{
    public Type ret_;
    public ListType args_;

    public FuncType(Type ret, ListType args){
      ret_ = ret;
      args_ = args;
    }

    public boolean equals(Object o){
      if (this == o) return true;
      if (o instanceof FuncType) {
        FuncType o_ = (FuncType)o;
        if (!this.ret_.equals(o_.ret_)) return false;
        if (this.args_.size() != o_.args_.size()) return false;
        for (int i = 0; i < this.args_.size(); i++){
          if (!this.args_.get(i).equals(o_.args_.get(i))) return false;
        }
        return true;
      }
      return false;
    }
  }

  private Prog ast;

  private HashMap<String, FuncType> functions;
  private LinkedList<HashMap<String, Type>> stack;

  // region Stack Frame Methods
  private void createStackFrame(){
    stack.add(new HashMap<String,Type>());
  }

  private void removeTopStackFrame(){
    stack.removeLast();
  }

  private void checkAndAddVarToStackFrame(String identifier, Type type){
    if (stack.getLast().containsKey(identifier)) abort("Variable with identifier " + identifier + " already defined!");
    stack.getLast().put(identifier, type);
  }

  private Type getVarType(String identifier){
    for(int i = stack.size() - 1; i >= 0; i--){
      HashMap<String,Type> frame = stack.get(i);
      if (frame.containsKey(identifier)){
        return frame.get(identifier);
      }
    }
    return null;
  }
  // endregion

  public TypeChecker(Prog ast){
    this.ast = ast;
    this.functions = new HashMap<String,FuncType>();
    this.stack = new LinkedList<HashMap<String,Type>>();
  }
  
  public void typeCheck(){

    // Build Function List and check for duplicate function identifiers
    ProgVisitorFP fl = new ProgVisitorFP();
    fl.visit((Program)ast, null);

    // Type Check Pass
    ProgVisitor tc = new ProgVisitor();
    tc.visit((Program)ast, null);

  }

  private void abort(String message){
    System.err.println("ERROR");
    System.err.println(message);
    System.exit(1);
  }

  //region Function List Pass
  public class ProgVisitorFP implements javalette.Absyn.Prog.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.Program p, java.lang.Void arg)
    { /* Code for Program goes here */
      for (javalette.Absyn.TopDef x: p.listtopdef_) {
        x.accept(new TopDefVisitorFP(), arg);
      }
      return null;
    }
  }

  public class TopDefVisitorFP implements javalette.Absyn.TopDef.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.FnDef p, java.lang.Void arg)
    { 
      if (functions.containsKey(p.ident_)){
        abort("Function with identifier " + p.ident_ + " already defined.");
      }

      ListType argTypes = new ListType();

      for (javalette.Absyn.Arg x: p.listarg_) {
        argTypes.add(x.accept(new ArgVisitor(), null).type_);
      }

      FuncType fType = new FuncType(p.type_, argTypes);
      functions.put(p.ident_, fType);
      
      return null;
    }
  }
  //endregion

  // Type Check Pass
  public class ProgVisitor implements javalette.Absyn.Prog.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.Program p, java.lang.Void arg)
    { /* Code for Program goes here */
      for (javalette.Absyn.TopDef x: p.listtopdef_) {
        x.accept(new TopDefVisitor(), arg);
      }
      return null;
    }
  }
  public class TopDefVisitor implements javalette.Absyn.TopDef.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.FnDef p, java.lang.Void arg)
    { /* Code for FnDef goes here */

      // Function stack frame is created here including arguments
      createStackFrame();

      //p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      for (javalette.Absyn.Arg x: p.listarg_) {
        Argument a = x.accept(new ArgVisitor(), arg);
        checkAndAddVarToStackFrame(a.ident_, a.type_);
      }
      p.blk_.accept(new BlkVisitor(), false);

      removeTopStackFrame();
      return null;
    }
  }
  public class ArgVisitor implements javalette.Absyn.Arg.Visitor<Argument,java.lang.Void>
  {
    public Argument visit(javalette.Absyn.Argument p, java.lang.Void arg)
    { /* Code for Argument goes here */
      //p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      return p;
    }
  }
  public class BlkVisitor implements javalette.Absyn.Blk.Visitor<java.lang.Void,Boolean>
  {
    public java.lang.Void visit(javalette.Absyn.Block p, Boolean createStackFrame)
    { /* Code for Block goes here */
      if (createStackFrame) createStackFrame();
      
      for (javalette.Absyn.Stmt x: p.liststmt_) {
        x.accept(new StmtVisitor(), null);
      }

      if (createStackFrame) removeTopStackFrame();
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
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.VRet p, java.lang.Void arg)
    { /* Code for VRet goes here */
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
      //p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ItemVisitor implements javalette.Absyn.Item.Visitor<java.lang.Void,Type>
  {
    public java.lang.Void visit(javalette.Absyn.NoInit p, Type t)
    { /* Code for NoInit goes here */
      checkAndAddVarToStackFrame(p.ident_, t);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Init p, Type t)
    { /* Code for Init goes here */
      //p.ident_;
      p.expr_.accept(new ExprVisitor(), t);
      checkAndAddVarToStackFrame(p.ident_, t);
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
  public class ExprVisitor implements javalette.Absyn.Expr.Visitor<java.lang.Void,Type>
  {
    public java.lang.Void visit(javalette.Absyn.EVar p, Type arg)
    { /* Code for EVar goes here */
      //p.ident_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.ELitInt p, Type arg)
    { /* Code for ELitInt goes here */
      //p.integer_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.ELitDoub p, Type arg)
    { /* Code for ELitDoub goes here */
      //p.double_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.ELitTrue p, Type arg)
    { /* Code for ELitTrue goes here */
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.ELitFalse p, Type arg)
    { /* Code for ELitFalse goes here */
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EApp p, Type arg)
    { /* Code for EApp goes here */
      //p.ident_;
      for (javalette.Absyn.Expr x: p.listexpr_) {
        //x.accept(new ExprVisitor(), arg);
      }
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EString p, Type arg)
    { /* Code for EString goes here */
      //p.string_;
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Neg p, Type arg)
    { /* Code for Neg goes here */
      //p.expr_.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Not p, Type arg)
    { /* Code for Not goes here */
      //p.expr_.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EMul p, Type arg)
    { /* Code for EMul goes here */
      //p.expr_1.accept(new ExprVisitor(), arg);
      //p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EAdd p, Type arg)
    { /* Code for EAdd goes here */
      //p.expr_1.accept(new ExprVisitor(), arg);
      //p.addop_.accept(new AddOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.ERel p, Type arg)
    { /* Code for ERel goes here */
      //p.expr_1.accept(new ExprVisitor(), arg);
      //p.relop_.accept(new RelOpVisitor<R,A>(), arg);
      //p.expr_2.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EAnd p, Type arg)
    { /* Code for EAnd goes here */
      //p.expr_1.accept(new ExprVisitor(), arg);
      //p.expr_2.accept(new ExprVisitor(), arg);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.EOr p, Type arg)
    { /* Code for EOr goes here */
      //p.expr_1.accept(new ExprVisitor(), arg);
      //p.expr_2.accept(new ExprVisitor(), arg);
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
