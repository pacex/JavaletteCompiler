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

  public HashMap<String, FuncType> getFunctions(){
    return functions;
  }

  // Type checker state variables
  private String currentFunction;

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

    this.currentFunction = "";
  }
  
  public void typeCheck(){

    // Build Function List and check for duplicate function identifiers

      // add default functions
    ListType ltPrintInt = new ListType();
    ltPrintInt.add(new Int());
    functions.put("printInt", new FuncType(new Void(), ltPrintInt));

    ListType ltPrintDouble = new ListType();
    ltPrintDouble.add(new Doub());
    functions.put("printDouble", new FuncType(new Void(), ltPrintDouble));

    /* printString checked for explicitly due to string input type */

    ListType ltReadInt = new ListType();
    functions.put("readInt", new FuncType(new Int(), ltReadInt));

    ListType ltReadDouble = new ListType();
    functions.put("readDouble", new FuncType(new Doub(), ltReadDouble));

    ProgVisitorFP fl = new ProgVisitorFP();
    fl.visit((Program)ast, null);

    if (!functions.containsKey("main")) abort("No main function!");

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

      if (p.ident_.equals("main") && !fType.ret_.equals(new Int())) abort("Function 'main' must return a value of type 'int'!");

      functions.put(p.ident_, fType);
      
      return null;
    }
  }
  //endregion

  // region Return Check
  public class StmtRCVisitor implements javalette.Absyn.Stmt.Visitor<Boolean,java.lang.Void>
  {
    public Boolean visit(javalette.Absyn.Empty p, java.lang.Void arg)
    { /* Code for Empty goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.BStmt p, java.lang.Void arg)
    { /* Code for BStmt goes here */
      return p.blk_.accept(new BlkRCVisitor(), null);
    }
    public Boolean visit(javalette.Absyn.Decl p, java.lang.Void arg)
    { /* Code for Decl goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.Ass p, java.lang.Void arg)
    { /* Code for Ass goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.Incr p, java.lang.Void arg)
    { /* Code for Incr goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.Decr p, java.lang.Void arg)
    { /* Code for Decr goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.Ret p, java.lang.Void arg)
    { /* Code for Ret goes here */
      return true;
    }
    public Boolean visit(javalette.Absyn.VRet p, java.lang.Void arg)
    { /* Code for VRet goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.Cond p, java.lang.Void arg)
    { /* Code for Cond goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.CondElse p, java.lang.Void arg)
    { /* Code for CondElse goes here */
      Boolean s1, s2;
      s1 = p.stmt_1.accept(new StmtRCVisitor(), null);
      s2 = p.stmt_2.accept(new StmtRCVisitor(), null);
      return s1 && s2;
    }
    public Boolean visit(javalette.Absyn.While p, java.lang.Void arg)
    { /* Code for While goes here */
      return false;
    }
    public Boolean visit(javalette.Absyn.SExp p, java.lang.Void arg)
    { /* Code for SExp goes here */
      return false;
    }
  }
  public class BlkRCVisitor implements javalette.Absyn.Blk.Visitor<Boolean,java.lang.Void>
  {
    public Boolean visit(javalette.Absyn.Block p, java.lang.Void arg)
    { /* Code for Block goes here */
      for (javalette.Absyn.Stmt x: p.liststmt_) {
         if (x.accept(new StmtRCVisitor(), null)) return true;
      }
      return false;
    }
  }

  // endregion



  // region Type Check Pass
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
      currentFunction = p.ident_;

      for (javalette.Absyn.Arg x: p.listarg_) {
        Argument a = x.accept(new ArgVisitor(), arg);
        checkAndAddVarToStackFrame(a.ident_, a.type_);
      }
      p.blk_.accept(new BlkVisitor(), false);

      if (!p.type_.equals(new Void()) && !p.blk_.accept(new BlkRCVisitor(), null)) abort("Function '" + p.ident_ + "' has to return a value!");

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
      if (p.type_.equals(new Void())) abort("Arguments of type 'void' not allowed!");
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
      if (p.type_.equals(new Void())) abort("Variable declarations of type 'void' not allowed!");
      for (javalette.Absyn.Item x: p.listitem_) {
        x.accept(new ItemVisitor(), p.type_);
      }
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Ass p, java.lang.Void arg)
    { /* Code for Ass goes here */
      Type expectedType = getVarType(p.ident_);
      if (expectedType == null) abort("Variable '" + p.ident_ + "'' is undeclared!");
      //p.expr_.accept(new ExprVisitor(), expectedType);
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(expectedType)) abort("Expression does not match expected type!");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Incr p, java.lang.Void arg)
    { /* Code for Incr goes here */
      Type t = getVarType(p.ident_);
      if (t == null) abort("Variable '" + p.ident_ + "'' is undeclared!");
      if (!(t instanceof javalette.Absyn.Int)) abort("'Increment' operation only valid for variables of type int!");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Decr p, java.lang.Void arg)
    { /* Code for Decr goes here */
      Type t = getVarType(p.ident_);
      if (t == null) abort("Variable '" + p.ident_ + "'' is undeclared!");
      if (!(t instanceof javalette.Absyn.Int)) abort("'Decrement' operation only valid for variables of type int!");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Ret p, java.lang.Void arg)
    { /* Code for Ret goes here */
      //p.expr_.accept(new ExprVisitor(), functions.get(currentFunction).ret_);
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(functions.get(currentFunction).ret_)) abort("Expression does not match expected type!");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.VRet p, java.lang.Void arg)
    { /* Code for VRet goes here */
      if (!functions.get(currentFunction).ret_.equals(new Void())) abort("Function '" + currentFunction + "' has to return a value!");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Cond p, java.lang.Void arg)
    { /* Code for Cond goes here */
      
      //p.expr_.accept(new ExprVisitor(), new Bool());
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(new Bool())) abort("Expression does not match expected type!");

      p.stmt_.accept(new StmtVisitor(), null);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.CondElse p, java.lang.Void arg)
    { /* Code for CondElse goes here */
      //p.expr_.accept(new ExprVisitor(), new Bool());
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(new Bool())) abort("Expression does not match expected type!");

      p.stmt_1.accept(new StmtVisitor(), null);
      p.stmt_2.accept(new StmtVisitor(), null);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.While p, java.lang.Void arg)
    { /* Code for While goes here */
      //p.expr_.accept(new ExprVisitor(), new Bool());
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(new Bool())) abort("Expression does not match expected type!");

      p.stmt_.accept(new StmtVisitor(), null);
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.SExp p, java.lang.Void arg)
    { /* Code for SExp goes here */
      //p.expr_.accept(new ExprVisitor(), new Void());
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(new Void())) abort("Expression does not match expected type!");
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
      if (!p.expr_.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
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
  public class AddOpVisitor implements javalette.Absyn.AddOp.Visitor<Type,Type>
  {
    public Type visit(javalette.Absyn.Plus p, Type operand)
    { /* Code for Plus goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'add' operation expects operand of type 'int' or 'double'!");
      return operand;
    }
    public Type visit(javalette.Absyn.Minus p, Type operand)
    { /* Code for Minus goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'subtract' operation expects operand of type 'int' or 'double'!");
      return operand;
    }
  }
  public class MulOpVisitor implements javalette.Absyn.MulOp.Visitor<Type,Type>
  {
    public Type visit(javalette.Absyn.Times p, Type operand)
    { /* Code for Times goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'multiply' operation expects operand of type 'int' or 'double'!");
      return operand;
    }
    public Type visit(javalette.Absyn.Div p, Type operand)
    { /* Code for Div goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'divide' operation expects operand of type 'int' or 'double'!");
      return operand;
    }
    public Type visit(javalette.Absyn.Mod p, Type operand)
    { /* Code for Mod goes here */
      if (!(operand instanceof Int)) abort("'modulo' operation expects operand of type 'int'!");
      return operand;
    }
  }
  public class RelOpVisitor implements javalette.Absyn.RelOp.Visitor<Type,Type>
  {
    public Type visit(javalette.Absyn.LTH p, Type operand)
    { /* Code for LTH goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'less than' operation expects operand of type 'int' or 'double'!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.LE p, Type operand)
    { /* Code for LE goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'less than or equal' operation expects operand of type 'int' or 'double'!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.GTH p, Type operand)
    { /* Code for GTH goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'greater than' operation expects operand of type 'int' or 'double'!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.GE p, Type operand)
    { /* Code for GE goes here */
      if (!(operand instanceof Int || operand instanceof Doub)) abort("'greater than or equal' operation expects operand of type 'int' or 'double'!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.EQU p, Type operand)
    { /* Code for EQU goes here */
      if (!(operand instanceof Bool || operand instanceof Int || operand instanceof Doub)) abort("'equal' operation expects operand of type 'bool', 'int' or 'double'!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.NE p, Type operand)
    { /* Code for NE goes here */
      if (!(operand instanceof Bool || operand instanceof Int || operand instanceof Doub)) abort("'not equal' operation expects operand of type 'bool', 'int' or 'double'!");
      return new Bool();
    }
  }

  public class ExprInferVisitor implements javalette.Absyn.Expr.Visitor<Type,java.lang.Void>
  {
    public Type visit(javalette.Absyn.EVar p, java.lang.Void arg)
    { /* Code for EVar goes here */
      Type varType = getVarType(p.ident_);
      if (varType == null) abort("Variable '" + p.ident_ + "'' is undefined!");
      return varType;
    }
    public Type visit(javalette.Absyn.ELitInt p, java.lang.Void arg)
    { /* Code for ELitInt goes here */
      return new Int();
    }
    public Type visit(javalette.Absyn.ELitDoub p, java.lang.Void arg)
    { /* Code for ELitDoub goes here */
      return new Doub();
    }
    public Type visit(javalette.Absyn.ELitTrue p, java.lang.Void arg)
    { /* Code for ELitTrue goes here */
      return new Bool();
    }
    public Type visit(javalette.Absyn.ELitFalse p, java.lang.Void arg)
    { /* Code for ELitFalse goes here */
      return new Bool();
    }
    public Type visit(javalette.Absyn.EApp p, java.lang.Void arg)
    { /* Code for EApp goes here */
      
      // Exception for printString
      if (p.ident_.equals("printString")){
        if (p.listexpr_.size() != 1) abort("Call to function 'printString' expected 1 argument(s). " + p.listexpr_.size() + " arguments given.");
        if (!(p.listexpr_.get(0) instanceof EString)) abort("Function 'printString' expects string literal!");
        return new Void();
      }

      if (!functions.containsKey(p.ident_)) abort("Function '" + p.ident_ + "'' is undefined!");

      FuncType ft = functions.get(p.ident_);

      if (ft.args_.size() != p.listexpr_.size()) abort("Call to function '" + p.ident_ + "'' expected " + ft.args_.size() + " argument(s). " + p.listexpr_.size() + " arguments given.");

      for (int i = 0; i < p.listexpr_.size(); i++) {
        Type expectedType = ft.args_.get(i);
        Expr x = p.listexpr_.get(i);
        //x.accept(new ExprVisitor(), expectedType);
        if (!x.accept(new ExprInferVisitor(), null).equals(expectedType)) abort("Expression does not match expected type!");
      }
      return ft.ret_;
    }
    public Type visit(javalette.Absyn.EString p, java.lang.Void arg)
    { /* Code for EString goes here */
      abort("String literal only allowed in call to function printString!");
      return null;
    }
    public Type visit(javalette.Absyn.Neg p, java.lang.Void arg)
    { /* Code for Neg goes here */
      Type t = p.expr_.accept(new ExprInferVisitor(), null);
      if (!(t instanceof Int || t instanceof Doub)) abort("'negation' operation expects operand of type 'int' or 'double'!");
      return t;
    }
    public Type visit(javalette.Absyn.Not p, java.lang.Void arg)
    { /* Code for Not goes here */
      Type t = p.expr_.accept(new ExprInferVisitor(), null);
      if (!(t instanceof Bool)) abort("'negation' operation expects operand of type 'boolean'!");
      return t;
    }
    public Type visit(javalette.Absyn.EMul p, java.lang.Void arg)
    { /* Code for EMul goes here */
      Type t = p.expr_1.accept(new ExprInferVisitor(), null);
      if (!p.expr_2.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
      return p.mulop_.accept(new MulOpVisitor(), t);
    }
    public Type visit(javalette.Absyn.EAdd p, java.lang.Void arg)
    { /* Code for EAdd goes here */
      Type t = p.expr_1.accept(new ExprInferVisitor(), null);
      if (!p.expr_2.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
      return p.addop_.accept(new AddOpVisitor(), t);
    }
    public Type visit(javalette.Absyn.ERel p, java.lang.Void arg)
    { /* Code for ERel goes here */
      Type t = p.expr_1.accept(new ExprInferVisitor(), null);
      if (!p.expr_2.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
      return p.relop_.accept(new RelOpVisitor(), t);
    }
    public Type visit(javalette.Absyn.EAnd p, java.lang.Void arg)
    { /* Code for EAnd goes here */
      Type t = p.expr_1.accept(new ExprInferVisitor(), null);
      if (!p.expr_2.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
      if (!(t instanceof Bool)) abort("'and' operation expects operand of type boolean!");
      return new Bool();
    }
    public Type visit(javalette.Absyn.EOr p, java.lang.Void arg)
    { /* Code for EOr goes here */
      Type t = p.expr_1.accept(new ExprInferVisitor(), null);
      if (!p.expr_2.accept(new ExprInferVisitor(), null).equals(t)) abort("Expression does not match expected type!");
      if (!(t instanceof Bool)) abort("'or' operation expects operand of type boolean!");
      return new Bool();
    }
    //endregion
  }
  
}
