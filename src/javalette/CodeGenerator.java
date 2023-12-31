package javalette;

import java.io.*;
import java.lang.Void;
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
    return TypeToString(t, true);
  }

  private String TypeToString(Type t, boolean arrPtr){
    if (t instanceof Int) return "i32";
    else if (t instanceof Doub) return "double";
    else if (t instanceof Bool) return "i1";
    else if (t instanceof ArrType) {
      ArrType arrT = (ArrType)t;
      if (arrT.type_ instanceof Int) return arrPtr ? "%arrayPtrInt" : "%arrayInt";
      else if (arrT.type_ instanceof Doub) return arrPtr ? "%arrayPtrDouble" : "%arrayDouble";
      else if (arrT.type_ instanceof Bool) return arrPtr ? "%arrayPtrBool" : "%arrayBool";
      else return arrPtr ? "%arrayPtrArray" : "%arrayArray";
    }
    else if (t instanceof javalette.Absyn.Void) return "void";
    else return "<unknown type>";
  }

  private Prog ast;
  private PrintStream code;
  private HashMap<String, FuncType> functions;
  private HashMap<Expr,Type> expressions;
  private HashMap<Index, ArrType> indexAnnotations;
  private LinkedList<HashMap<String,Var>> stack;

  private LinkedList<EString> stringLiterals;
  private HashMap<EString, String> stringLiteralIdentifiers;

  private int labelCounter;

  private String getNewLabel(){
    String l = "lab" + Integer.valueOf(labelCounter);
    labelCounter++;
    return l;
  }

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

  public CodeGenerator(Prog ast, HashMap<String,FuncType> functions, HashMap<Expr,Type> expressions, LinkedList<EString> stringLiterals, HashMap<Index, ArrType> indexAnnotations){
      this.ast = ast;
      this.functions = functions;
      this.expressions = expressions;
      this.indexAnnotations = indexAnnotations;
      this.stringLiterals = stringLiterals;
      this.stringLiteralIdentifiers = new HashMap<EString, String>();
      this.code = new PrintStream(System.out, false);
      this.stack = new LinkedList<HashMap<String,Var>>();
      this.labelCounter = 0;
  }

  public PrintStream generateCode(){
    
    

    ProgVisitor cg = new ProgVisitor();
    cg.visit((Program)ast, null);

    return code;
  }

  private Reg allocMultiArray(ArrType t, LinkedList<Reg> cnts, int dim){
    Reg cnt = cnts.get(cnts.size() - dim);

    Reg arrPtr = allocArray(t, cnt);

    if (dim > 1){
      Reg i = new Reg("i32*");
      code.println(i.Ident_ + " = alloca i32");
      code.println("store i32 0, " + i.TypeAndIdent());
      String labCond = getNewLabel();
      String labBody = getNewLabel();
      String labEnd = getNewLabel();

      code.println("br label %" + labCond);
      
      // Condition
      code.print(labCond + ": ");
      Reg jmp = new Reg("i1");
      Reg currentIndex = new Reg("i32");
      code.println(currentIndex.Ident_ + " = load i32, " + i.TypeAndIdent());
      code.println(jmp.Ident_ + " = icmp ult i32 " + currentIndex.Ident_ + ", " + cnt.Ident_);
      code.println("br i1 " + jmp.Ident_ + ", label %" + labBody + ", label %" + labEnd);

      // Body
      code.print(labBody + ": ");
      Reg valPtr = indexArray(arrPtr, currentIndex, t);
      Reg subArrPtr = allocMultiArray((ArrType)t.type_, cnts, dim - 1);
      code.println("store " + subArrPtr.TypeAndIdent() + ", " + valPtr.TypeAndIdent());

      Reg newIndex = new Reg("i32");
      code.println(newIndex.Ident_ + " = add i32 " + currentIndex.Ident_ + ", 1");
      code.println("store " + newIndex.TypeAndIdent() + ", " + i.TypeAndIdent());

      code.println("br label %" + labCond);

      // End
      code.print(labEnd + ": ");
    }

    return arrPtr;
  }

  private Reg allocArray(ArrType t, Reg cnt){
    Reg arrPtr = new Reg(TypeToString(t, true));
    String arrTypeStr = TypeToString(t, false);

    // Compute element size in bytes
    Type elementType = t.type_;
    String elemTypeStr = TypeToString(elementType, true);
    Reg elementSizePtr = new Reg("ptr");
    Reg elementSize = new Reg("i32");
    code.println(elementSizePtr.Ident_ + " = getelementptr " + elemTypeStr + ", ptr null, i32 1");
    code.println(elementSize.Ident_ + " = ptrtoint " + elementSizePtr.TypeAndIdent() + " to i32");

    // Allocate memory on heap and write array length
    code.println(arrPtr.Ident_ + " = call ptr @calloc(i32 12, i32 1)");

    Reg cntPtr = new Reg("ptr");
    code.println(cntPtr.Ident_ + " = getelementptr " + arrTypeStr + ", " + arrPtr.TypeAndIdent() + ", i32 0, i32 0");
    code.println("store " + cnt.TypeAndIdent() + ", " + cntPtr.TypeAndIdent());

    Reg dataPtrPtr = new Reg("ptr");
    code.println(dataPtrPtr.Ident_ + " = getelementptr " + arrTypeStr + ", " + arrPtr.TypeAndIdent() + ", i32 0, i32 1");
    Reg dataPtr = new Reg("ptr");
    code.println(dataPtr.Ident_ + " = call ptr @calloc(" + cnt.TypeAndIdent() + ", " + elementSize.TypeAndIdent() + ")");
    code.println("store " + dataPtr.TypeAndIdent() + ", " + dataPtrPtr.TypeAndIdent());

    return arrPtr;
  }

  private Reg indexArray(Reg arrPtr, Reg index, ArrType arrType){
    String arrTypeStr = TypeToString(arrType, false);

    Reg dataPtrPtr = new Reg("ptr");
    code.println(dataPtrPtr.Ident_ + " = getelementptr " + arrTypeStr + ", " + arrPtr.TypeAndIdent() + ", i32 0, i32 1");
    Reg dataPtr = new Reg("ptr");
    code.println(dataPtr.Ident_ + " = load ptr, " + dataPtrPtr.TypeAndIdent());

    String dataTypeStr = "[0 x " + TypeToString(arrType.type_) + "]";
    Reg valuePtr = new Reg("ptr");
    code.println(valuePtr.Ident_ + " = getelementptr " + dataTypeStr + ", " + dataPtr.TypeAndIdent() + ", i32 0, " + index.TypeAndIdent());

    return valuePtr;
  }

  public class ProgVisitor implements javalette.Absyn.Prog.Visitor<java.lang.Void,java.lang.Void>
  {
    public java.lang.Void visit(javalette.Absyn.Program p, java.lang.Void arg)
    { 
      // Overhead  
      code.println("declare void @printInt(i32)");
      code.println("declare void @printDouble(double)");
      code.println("declare void @printString(i8*)");
      code.println("declare i32 @readInt()");
      code.println("declare double @readDouble()");

      // Array Overhead
      code.println("declare ptr @calloc(i32, i32)");
      code.println("declare ptr @malloc(i32, i32)");

      code.println("%arrayPtrInt = type %arrayInt*");
      code.println("%arrayPtrDouble = type %arrayDouble*");
      code.println("%arrayPtrBool = type %arrayBool*");
      code.println("%arrayPtrArray = type %arrayArray*");

      code.println("%arrayInt = type {i32, [0 x i32]*}");
      code.println("%arrayDouble = type {i32, [0 x double]*}");
      code.println("%arrayBool = type {i32, [0 x i1]*}");
      code.println("%arrayArray = type {i32, [0 x ptr]*}");
      

      // String literals
      int strCnt = 0;
      for (EString estr : stringLiterals){
        String ident = "@hw" + Integer.valueOf(strCnt);
        code.println(ident + " = internal constant [" + 
          Integer.valueOf(estr.string_.length() + 1) + " x i8] c\"" + estr.string_ + "\\00\"");
        /*code.println(ident + " = internal constant [" + 
          Integer.valueOf(estr.string_.length()) + " x i8] c\"" + estr.string_ + "\"");*/
        stringLiteralIdentifiers.put(estr, ident);
        strCnt++;
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
        Reg memPtr = new Reg("ptr"); // Create handle for memPtr register
        code.println(memPtr.Ident_ + " = alloca " + TypeToString(a.type_)); // Memory allocation
        
        Var v = new Var(memPtr, a.type_); // Add variable to stackframe
        addVarToStackFrame(a.ident_, v);

        code.println("store " + a.reg_.TypeAndIdent() + " , " + memPtr.TypeAndIdent()); // Store value from argument register
      }

      p.blk_.accept(new BlkVisitor(), false);

      if (p.type_ instanceof javalette.Absyn.Void) code.println("ret void");
      else if (p.type_ instanceof Int) code.println("ret i32 0");
      else if (p.type_ instanceof Bool) code.println("ret i1 false");
      else if (p.type_ instanceof Doub) code.println("ret double 0.0");
        
      

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
    { 
      // Get assignment expression register
      Reg rhs = p.expr_.accept(new ExprVisitor(), null);
      Reg memPtrReg = p.lhs_.accept(new LhsVisitor(), null);
      code.println("store " + rhs.TypeAndIdent() + ", " + memPtrReg.TypeAndIdent());
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Incr p, java.lang.Void arg)
    { /* Code for Incr goes here */
      Reg memPtrReg = p.lhs_.accept(new LhsVisitor(), null);
      Reg prior = new Reg("i32");
      code.println(prior.Ident_ + " = load i32, " + memPtrReg.TypeAndIdent());
      Reg posterior = new Reg("i32");
      code.println(posterior.Ident_ + " = add " + prior.TypeAndIdent() + ", 1");
      code.println("store " + posterior.TypeAndIdent() + ", " + memPtrReg.TypeAndIdent());
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Decr p, java.lang.Void arg)
    { /* Code for Decr goes here */
      Reg memPtrReg = p.lhs_.accept(new LhsVisitor(), null);
      Reg prior = new Reg("i32");
      code.println(prior.Ident_ + " = load i32, " + memPtrReg.TypeAndIdent());
      Reg posterior = new Reg("i32");
      code.println(posterior.Ident_ + " = sub " + prior.TypeAndIdent() + ", 1");
      code.println("store " + posterior.TypeAndIdent() + ", " + memPtrReg.TypeAndIdent());
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
      Reg expr = p.expr_.accept(new ExprVisitor(), null);
      String labTrue = getNewLabel();
      String labEnd = getNewLabel();

      code.println("br " + expr.TypeAndIdent() + ", label %" + labTrue + ", label %" + labEnd);

      // If block
      code.print(labTrue + ": ");
      p.stmt_.accept(new StmtVisitor(), null);
      code.println("br label %" + labEnd);

      // End
      code.print(labEnd + ": ");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.CondElse p, java.lang.Void arg)
    { /* Code for CondElse goes here */
      Reg expr = p.expr_.accept(new ExprVisitor(), null);
      String labTrue = getNewLabel();
      String labFalse = getNewLabel();
      String labEnd = getNewLabel();

      code.println("br " + expr.TypeAndIdent() + ", label %" + labTrue + ", label %" + labFalse);

      // If block
      code.print(labTrue + ": ");
      p.stmt_1.accept(new StmtVisitor(), null);
      code.println("br label %" + labEnd);

      // Else block
      code.print(labFalse + ": ");
      p.stmt_2.accept(new StmtVisitor(), null);
      code.println("br label %" + labEnd);

      // End
      code.print(labEnd + ": ");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.While p, java.lang.Void arg)
    { /* Code for While goes here */
      String labStart = getNewLabel();
      String labBlock = getNewLabel();
      String labEnd = getNewLabel();
      code.println("br label %" + labStart);

      // Check condition
      code.print(labStart + ": ");
      Reg expr = p.expr_.accept(new ExprVisitor(), null);
      code.println("br " + expr.TypeAndIdent() + ", label %" + labBlock + ", label %" + labEnd);

      // Block
      code.print(labBlock + ": ");
      p.stmt_.accept(new StmtVisitor(), arg);
      code.println("br label %" + labStart);
      
      // End loop
      code.println(labEnd + ": ");
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.SExp p, java.lang.Void arg)
    { /* Code for SExp goes here */
      EApp f = (EApp)p.expr_;
      LinkedList<Reg> argRegs = new LinkedList<Reg>();

      if (f.ident_.equals("printString")){
        // Exception for printString
        EString estr = (EString)f.listexpr_.get(0);
        Reg strConst = estr.accept(new ExprVisitor(), null);
        Reg ptr = new Reg("i8*");
        code.println(ptr.Ident_ + " = getelementptr " + strConst.Type_ + ", " + strConst.Type_ + "* " + strConst.Ident_ + ", i32 0, i32 0");
        code.println("call void @printString(" + ptr.TypeAndIdent() + ")");
        return null;
      }

      // Any other function

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

    public java.lang.Void visit(For p, Void arg) {
      // Labels
      String labCond = getNewLabel();
      String labLoop = getNewLabel();
      String labEnd = getNewLabel();

      // Array
      Reg arrPtr = p.expr_.accept(new ExprVisitor(), null);
      ArrType arrType = new ArrType(p.type_);
      String arrTypeStr = TypeToString(arrType, false);

      // Running variable
      createStackFrame();
      Reg rvMemPtr = new Reg("ptr");
      code.println(rvMemPtr.Ident_ + " = alloca " + TypeToString(p.type_));
      Var runningVar = new Var(rvMemPtr, p.type_);
      addVarToStackFrame(p.ident_, runningVar);


      // Get array length + init
      Reg lengthPtr = new Reg("ptr");
      Reg length = new Reg("i32");
      code.println(lengthPtr.Ident_ + " = getelementptr " + arrTypeStr + ", " + arrPtr.TypeAndIdent() + ", i32 0, i32 0");
      code.println(length.Ident_ + " = load i32, " + lengthPtr.TypeAndIdent());

      Reg indexPtr = new Reg("ptr");
      code.println(indexPtr.Ident_ + " = alloca i32");
      code.println("store i32 0, " + indexPtr.TypeAndIdent());

      code.println("br label %" + labCond);


      // Condition
      code.print(labCond + ": ");
      Reg index = new Reg("i32");
      code.println(index.Ident_ + " = load i32, " + indexPtr.TypeAndIdent());
      Reg comp = new Reg("i1");
      code.println(comp.Ident_ + " = icmp ult i32 " + index.Ident_ + ", " + length.Ident_);
      code.println("br i1 " + comp.Ident_ + ", label %" + labLoop + ", label %" + labEnd);


      // Loop
      code.print(labLoop + ": ");
      Reg valuePtr = new Reg("ptr");
      valuePtr = indexArray(arrPtr, index, arrType);
      Reg value = new Reg(TypeToString(p.type_));
      code.println(value.Ident_ + " = load " + TypeToString(p.type_) + ", " + valuePtr.TypeAndIdent());
      code.println("store " + value.TypeAndIdent() + ", " + runningVar.memPtrReg_.TypeAndIdent());

      p.stmt_.accept(new StmtVisitor(), null); // Loop body

      Reg newIndex = new Reg("i32");
      code.println(newIndex.Ident_ + " = add i32 " + index.Ident_ + ", 1");
      code.println("store " + newIndex.TypeAndIdent() + ", " + indexPtr.TypeAndIdent());
      code.println("br label %" + labCond);


      // End
      code.print(labEnd + ": ");

      removeTopStackFrame();
      return null;
    }
  }
  public class ItemVisitor implements javalette.Absyn.Item.Visitor<java.lang.Void,Type>
  {
    public java.lang.Void visit(javalette.Absyn.NoInit p, Type t)
    { /* Code for NoInit goes here */
      Reg memPtr = new Reg("ptr"); // Create handle for memPtr register
      code.println(memPtr.Ident_ + " = alloca " + TypeToString(t, true)); // Memory allocation
      
      Var v = new Var(memPtr, t); // Add variable to stackframe
      addVarToStackFrame(p.ident_, v);

      // Initialize variable to 0, 0.0 or false

      if (!(t instanceof ArrType)){
        Reg e = new Reg(TypeToString(t));
        if (t instanceof Int) code.println(e.Ident_ + " = add i32 0, 0");
        else if (t instanceof Doub) code.println(e.Ident_ + " = fadd double 0.0, 0.0");
        else if (t instanceof Bool) code.println(e.Ident_ + " = add i1 false, false");
        
        code.println("store " + e.TypeAndIdent() + ", " + memPtr.TypeAndIdent()); // Store value from expression register
      }else{
        Reg cnt = new Reg("i32");
        code.println(cnt.Ident_ + " = add i32 0, 0");

        Reg arrPtr = allocArray((ArrType)t, cnt);
        code.println("store " + arrPtr.TypeAndIdent() + ", " + memPtr.TypeAndIdent());
      }
      
      return null;
    }
    public java.lang.Void visit(javalette.Absyn.Init p, Type t)
    { /* Code for Init goes here */
      Reg memPtr = new Reg("ptr"); // Create handle for memPtr register
      code.println(memPtr.Ident_ + " = alloca " + TypeToString(t, true)); // Memory allocation
      
      Reg e = p.expr_.accept(new ExprVisitor(), null);

      Var v = new Var(memPtr, t); // Add variable to stackframe
      addVarToStackFrame(p.ident_, v);
      
      code.println("store " + e.TypeAndIdent() + ", " + memPtr.TypeAndIdent()); // Store value from expression register
      return null;
    }
  }
  
  public class ExprVisitor implements javalette.Absyn.Expr.Visitor<Reg,java.lang.Void>
  {
    public Reg visit(javalette.Absyn.EVar p, java.lang.Void arg)
    { /* Code for EVar goes here */
      Var v = getVar(p.ident_);
      Reg r = new Reg(TypeToString(v.type_));
      code.println(r.Ident_ + " = load " + TypeToString(v.type_) + ", " + v.memPtrReg_.TypeAndIdent());
      return r;
    }

    public Reg visit(javalette.Absyn.EIndex p, java.lang.Void arg)
    { /* Code for EIndex goes here */
      Reg valuePtr = p.index_.accept(new IndexVisitor(), null);
      Type t = expressions.get(p);
      String tStr = TypeToString(t);

      Reg value = new Reg(tStr);
      code.println(value.Ident_ + " = load " + tStr + ", " + valuePtr.TypeAndIdent());
      return value;
    }
    public Reg visit(javalette.Absyn.ELength p, java.lang.Void arg)
    { /* Code for ELength goes here */
      Reg arrPtr = p.expr_.accept(new ExprVisitor(), arg);
      ArrType arrType = (ArrType)expressions.get(p.expr_);
      String arrTypeStr = TypeToString(arrType, false);

      Reg lengthPtr = new Reg("i32*");
      code.println(lengthPtr.Ident_ + " = getelementptr " + arrTypeStr + ", " + arrPtr.TypeAndIdent() + ", i32 0, i32 0");
      Reg length = new Reg("i32");
      code.println(length.Ident_ + " = load i32, i32* " + lengthPtr.Ident_);
      return length;
    }
    public Reg visit(javalette.Absyn.ELitArr p, java.lang.Void arg)
    { /* Code for ELitArr goes here */
      Type t = p.type_;
      LinkedList<Reg> cnts = new LinkedList<Reg>();
      for (int i = 0; i < p.listdim_.size(); i++){
        cnts.add(p.listdim_.get(i).accept(new DimVisitor(), null));
        t = new ArrType(t);
      }
      Reg arrPtr = allocMultiArray((ArrType)t, cnts, p.listdim_.size());

      return arrPtr;
    }

    public Reg visit(javalette.Absyn.ELitInt p, java.lang.Void arg)
    { /* Code for ELitInt goes here */
      Reg r = new Reg("i32");
      code.println(r.Ident_ + " = add i32 0, " + Integer.valueOf(p.integer_));
      return r;
    }
    public Reg visit(javalette.Absyn.ELitDoub p, java.lang.Void arg)
    { /* Code for ELitDoub goes here */
      Reg r = new Reg("double");
      code.println(r.Ident_ + " = fadd double 0.0, " + Double.valueOf(p.double_));
      return r;
    }
    public Reg visit(javalette.Absyn.ELitTrue p, java.lang.Void arg)
    { /* Code for ELitTrue goes here */
      Reg r = new Reg("i1");
      code.println(r.Ident_ + " = and i1 true, true");
      return r;
    }
    public Reg visit(javalette.Absyn.ELitFalse p, java.lang.Void arg)
    { /* Code for ELitFalse goes here */
      Reg r = new Reg("i1");
      code.println(r.Ident_ + " = and i1 false, false");
      return r;
    }
    public Reg visit(javalette.Absyn.EApp p, java.lang.Void arg)
    { /* Code for EApp goes here */
      LinkedList<Reg> argRegs = new LinkedList<Reg>();
      Type t = functions.get(p.ident_).ret_;

      // Get all arguments' registers
      for(Expr e : p.listexpr_){
        argRegs.add(e.accept(new ExprVisitor(), null));
      }
      Reg r = new Reg(TypeToString(t));
      code.print(r.Ident_ + " = call " + TypeToString(t) + " @" + p.ident_ + "(");

      for(int i = 0; i < argRegs.size(); i++){
        code.print(argRegs.get(i).TypeAndIdent());
        if (i < argRegs.size() - 1) code.print(", ");
      }
      code.print(")\n");

      return r;
    }
    public Reg visit(javalette.Absyn.EString p, java.lang.Void arg)
    { /* Code for EString goes here */
      Reg r = new Reg("[" + Integer.valueOf(p.string_.length() + 1) + " x i8]", stringLiteralIdentifiers.get(p));
      return r;
    }
    public Reg visit(javalette.Absyn.Neg p, java.lang.Void arg)
    { /* Code for Neg goes here */
      Reg op = p.expr_.accept(new ExprVisitor(), null);
      Type t = op.GetType();
      Reg r = new Reg(TypeToString(t));
      
      if (t instanceof Int) code.println(r.Ident_ + " = mul i32 -1, " + op.Ident_);
      else code.println(r.Ident_ + " = fmul double -1.0, " + op.Ident_);
      return r;
    }
    public Reg visit(javalette.Absyn.Not p, java.lang.Void arg)
    { /* Code for Not goes here */
      Reg op = p.expr_.accept(new ExprVisitor(), null);
      Reg r = new Reg(op.Type_);
      code.println(r.Ident_ + " = xor i1 true, " + op.Ident_);
      return r;
    }
    public Reg visit(javalette.Absyn.EMul p, java.lang.Void arg)
    { /* Code for EMul goes here */
      Reg op1 = p.expr_1.accept(new ExprVisitor(), null);
      Type t = op1.GetType();
      String operator = p.mulop_.accept(new MulOpVisitor(), arg);
      Reg op2 = p.expr_2.accept(new ExprVisitor(), null);
      Reg r = new Reg(TypeToString(t));

      if (t instanceof Int){
        if (operator.equals("div")) operator = "sdiv";
        code.println(r.Ident_ + " = " + operator + " i32 " + op1.Ident_ + ", " + op2.Ident_);
      } 
      else code.println(r.Ident_ + " = f" + operator + " double " + op1.Ident_ + ", " + op2.Ident_);
      return r;
    }
    public Reg visit(javalette.Absyn.EAdd p, java.lang.Void arg)
    { /* Code for EAdd goes here */
      Reg op1 = p.expr_1.accept(new ExprVisitor(), null);
      Type t = op1.GetType();
      String operator = p.addop_.accept(new AddOpVisitor(), arg);
      Reg op2 = p.expr_2.accept(new ExprVisitor(), null);
      Reg r = new Reg(TypeToString(t));

      if (t instanceof Int) {
        code.println(r.Ident_ + " = " + operator + " i32 " + op1.Ident_ + ", " + op2.Ident_);
      }
      else code.println(r.Ident_ + " = f" + operator + " double " + op1.Ident_ + ", " + op2.Ident_);
      return r;
    }
    public Reg visit(javalette.Absyn.ERel p, java.lang.Void arg)
    { /* Code for ERel goes here */
      Reg op1 = p.expr_1.accept(new ExprVisitor(), arg);
      Reg op2 = p.expr_2.accept(new ExprVisitor(), arg);
      Type t = op1.GetType();

      String opCode = (t instanceof Doub) ? "fcmp" : "icmp";
      String condCode = p.relop_.accept(new RelOpVisitor(), t);
      Reg r = new Reg("i1");

      code.println(r.Ident_ + " = " + opCode + " " + condCode + " " + op1.TypeAndIdent() + ", " + op2.Ident_);
      return r;
    }
    public Reg visit(javalette.Absyn.EAnd p, java.lang.Void arg)
    { /* Code for EAnd goes here */
      Reg op1 = p.expr_1.accept(new ExprVisitor(), null);
      String labTrue = getNewLabel();
      String labEnd = getNewLabel();
      Reg memPtr = new Reg("i1*");
      code.println(memPtr.Ident_ + " = alloca i1");
      code.println("store i1 false, " + memPtr.TypeAndIdent());
      code.println("br i1 " + op1.Ident_ + ", label %" + labTrue + ", label %" + labEnd);

      // True
      code.print(labTrue + ": ");
      Reg op2 = p.expr_2.accept(new ExprVisitor(), null);
      code.println("store i1 " + op2.Ident_ + ", " + memPtr.TypeAndIdent());
      code.println("br label %"+labEnd);

      // End
      Reg r = new Reg("i1");
      code.print(labEnd + ": ");
      code.println(r.Ident_ + " = load i1, " + memPtr.TypeAndIdent());
      return r;
    }
    public Reg visit(javalette.Absyn.EOr p, java.lang.Void arg)
    { /* Code for EOr goes here */
      Reg op1 = p.expr_1.accept(new ExprVisitor(), null);
      String labFalse = getNewLabel();
      String labEnd = getNewLabel();
      Reg memPtr = new Reg("i1*");
      code.println(memPtr.Ident_ + " = alloca i1");
      code.println("store i1 true, " + memPtr.TypeAndIdent());
      code.println("br i1 " + op1.Ident_ + ", label %" + labEnd + ", label %" + labFalse);

      // False
      code.print(labFalse + ": ");
      Reg op2 = p.expr_2.accept(new ExprVisitor(), null);
      code.println("store i1 " + op2.Ident_ + ", " + memPtr.TypeAndIdent());
      code.println("br label %"+labEnd);

      // End
      Reg r = new Reg("i1");
      code.print(labEnd + ": ");
      code.println(r.Ident_ + " = load i1, " + memPtr.TypeAndIdent());
      return r;
    }
  }

  public class DimVisitor implements javalette.Absyn.Dim.Visitor<Reg,java.lang.Void>
  {
    public Reg visit(javalette.Absyn.ArrDim p, java.lang.Void arg)
    { /* Code for ArrDim goes here */
      return p.expr_.accept(new ExprVisitor(), arg);
    }
  }

  public class LhsVisitor implements javalette.Absyn.Lhs.Visitor<Reg,java.lang.Void>
  {
    public Reg visit(javalette.Absyn.LhsVar p, java.lang.Void arg)
    { /* Code for LhsVar goes here */
      return getVar(p.ident_).memPtrReg_;
    }
    public Reg visit(javalette.Absyn.LhsArray p, java.lang.Void arg)
    { /* Code for LhsArray goes here */
      return p.index_.accept(new IndexVisitor(), null);
    }
  }
  public class IndexVisitor implements javalette.Absyn.Index.Visitor<Reg,java.lang.Void>
  {
    public Reg visit(javalette.Absyn.ArrInd p, java.lang.Void arg)
    { /* Code for ArrInd goes here */

      Reg arrPtr = p.expr_1.accept(new ExprVisitor(), arg);
      //ArrType arrType = (ArrType)expressions.get(p.expr_1);
      ArrType arrType = indexAnnotations.get(p);
      Reg index = p.expr_2.accept(new ExprVisitor(), arg);

      Reg valuePtr = indexArray(arrPtr, index, arrType);
      return valuePtr;
    }
  }

  public class AddOpVisitor implements javalette.Absyn.AddOp.Visitor<String,java.lang.Void>
  {
    public String visit(javalette.Absyn.Plus p, java.lang.Void arg)
    { /* Code for Plus goes here */
      return "add";
    }
    public String visit(javalette.Absyn.Minus p, java.lang.Void arg)
    { /* Code for Minus goes here */
      return "sub";
    }
  }
  public class MulOpVisitor implements javalette.Absyn.MulOp.Visitor<String,java.lang.Void>
  {
    public String visit(javalette.Absyn.Times p, java.lang.Void arg)
    { /* Code for Times goes here */
      return "mul";
    }
    public String visit(javalette.Absyn.Div p, java.lang.Void arg)
    { /* Code for Div goes here */
      return "div";
    }
    public String visit(javalette.Absyn.Mod p, java.lang.Void arg)
    { /* Code for Mod goes here */
      return "srem";
    }
  }
  public class RelOpVisitor implements javalette.Absyn.RelOp.Visitor<String,Type>
  {
    public String visit(javalette.Absyn.LTH p, Type arg)
    { /* Code for LTH goes here */
      if (arg instanceof Int) return "slt";
      return "olt";
    }
    public String visit(javalette.Absyn.LE p, Type arg)
    { /* Code for LE goes here */
      if (arg instanceof Int) return "sle";
      return "ole";
    }
    public String visit(javalette.Absyn.GTH p, Type arg)
    { /* Code for GTH goes here */
      if (arg instanceof Int) return "sgt";
      return "ogt";
    }
    public String visit(javalette.Absyn.GE p, Type arg)
    { /* Code for GE goes here */
      if (arg instanceof Int) return "sge";
      return "oge";
    }
    public String visit(javalette.Absyn.EQU p, Type arg)
    { /* Code for EQU goes here */
      if (arg instanceof Int || arg instanceof Bool) return "eq";
      return "oeq";
    }
    public String visit(javalette.Absyn.NE p, Type arg)
    { /* Code for NE goes here */
      if (arg instanceof Int || arg instanceof Bool) return "ne";
      return "one";
    }
  }
}
