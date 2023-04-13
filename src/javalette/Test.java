// File generated by the BNF Converter (bnfc 2.9.4).

package javalette;

import javalette.*;

import java.io.*;
import java.lang.ProcessBuilder.Redirect.Type;

import java_cup.runtime.*;


public class Test
{
  Yylex l;
  parser p;

  public Test(String[] args)
  {
    try
    {
      Reader input;
      if (args.length == 0) input = new InputStreamReader(System.in);
      else input = new FileReader(args[0]);
      l = new Yylex(input);
    }
    catch(IOException e)
    {
      System.err.println("ERROR");
      System.err.println("Error: File not found: " + args[0]);
      System.exit(1);
    }
    p = new parser(l, l.getSymbolFactory());
  }

  public javalette.Absyn.Prog parse(boolean printast) throws Exception
  {
    /* The default parser is the first-defined entry point. */
    javalette.Absyn.Prog ast = p.pProg();

    if (printast){
      System.out.println();
      System.out.println("Parse Succesful!");
      System.out.println();
      System.out.println("[Abstract Syntax]");
      System.out.println();
      System.out.println(PrettyPrinter.show(ast));
      System.out.println();
      System.out.println("[Linearized Tree]");
      System.out.println();
      System.out.println(PrettyPrinter.print(ast));
    }
    return ast;
  }

  public static void main(String args[]) throws Exception
  {
    boolean printast = false;
    boolean generateLLVM = true;

    for(int i = 1; i < args.length; i++){
      String arg = args[i];
      if (arg.equals("--printast")) printast = true;
      else if (arg.equals("--nooutput")) generateLLVM = false;
      else{
        System.err.println("ERROR\nUnkown argument: '" + arg + "'!");
      }
    }

    // Lexer
    Test t = new Test(args);

    // Parser
    javalette.Absyn.Prog ast = null;

    try
    {
      ast = t.parse(printast);
    }
    catch(Throwable e)
    {
      System.err.println("ERROR");
      System.err.println("At line " + String.valueOf(t.l.line_num()) + ", near \"" + t.l.buff() + "\" :");
      System.err.println("     " + e.getMessage());
      System.exit(1);
    }

    // Type Checker
    TypeChecker tc = new TypeChecker(ast);
    tc.typeCheck();

    // Code Generator
    if (generateLLVM){
      CodeGenerator cg = new CodeGenerator(ast, tc.getFunctions(), tc.getExpressions(), tc.getStringLiterals());
      PrintStream code = cg.generateCode();
      code.close();
    }

    // AST Valid
    System.err.println("OK");
  }
}
