## File generated by the BNF Converter (bnfc 2.9.4).

JAVAC=javac
JAVAC_FLAGS=-sourcepath .
JAVA=java
JAVA_FLAGS=
PARSER=${JAVA} ${JAVA_FLAGS} java_cup.Main
PARSER_FLAGS=-nopositions -expect 100
LEXER=${JAVA} ${JAVA_FLAGS} JLex.Main
LEXER_FLAGS=
all : test

test : absyn javalette/Reg.class javalette/CodeGenerator.class javalette/TypeChecker.class javalette/Yylex.class javalette/PrettyPrinter.class javalette/Test.class javalette/VisitSkel.class javalette/ComposVisitor.class javalette/AbstractVisitor.class javalette/FoldVisitor.class javalette/AllVisitor.class javalette/parser.class javalette/sym.class javalette/Test.class

.PHONY : absyn

%.class : %.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn : javalette/Absyn/Prog.java javalette/Absyn/Program.java javalette/Absyn/TopDef.java javalette/Absyn/FnDef.java javalette/Absyn/ListTopDef.java javalette/Absyn/Arg.java javalette/Absyn/Argument.java javalette/Absyn/ListArg.java javalette/Absyn/Blk.java javalette/Absyn/Block.java javalette/Absyn/ListStmt.java javalette/Absyn/Stmt.java javalette/Absyn/Empty.java javalette/Absyn/BStmt.java javalette/Absyn/Decl.java javalette/Absyn/Ass.java javalette/Absyn/Incr.java javalette/Absyn/Decr.java javalette/Absyn/Ret.java javalette/Absyn/VRet.java javalette/Absyn/Cond.java javalette/Absyn/CondElse.java javalette/Absyn/While.java javalette/Absyn/For.java javalette/Absyn/SExp.java javalette/Absyn/Item.java javalette/Absyn/NoInit.java javalette/Absyn/Init.java javalette/Absyn/ListItem.java javalette/Absyn/Type.java javalette/Absyn/Int.java javalette/Absyn/Doub.java javalette/Absyn/Bool.java javalette/Absyn/Void.java javalette/Absyn/ArrType.java javalette/Absyn/Fun.java javalette/Absyn/ListType.java javalette/Absyn/Expr.java javalette/Absyn/EVar.java javalette/Absyn/EIndex.java javalette/Absyn/ELength.java javalette/Absyn/ELitArr.java javalette/Absyn/ELitInt.java javalette/Absyn/ELitDoub.java javalette/Absyn/ELitTrue.java javalette/Absyn/ELitFalse.java javalette/Absyn/EApp.java javalette/Absyn/EString.java javalette/Absyn/Neg.java javalette/Absyn/Not.java javalette/Absyn/EMul.java javalette/Absyn/EAdd.java javalette/Absyn/ERel.java javalette/Absyn/EAnd.java javalette/Absyn/EOr.java javalette/Absyn/Dim.java javalette/Absyn/ArrDim.java javalette/Absyn/ListDim.java javalette/Absyn/ListExpr.java javalette/Absyn/Lhs.java javalette/Absyn/LhsVar.java javalette/Absyn/LhsArray.java javalette/Absyn/Index.java javalette/Absyn/ArrInd.java javalette/Absyn/AddOp.java javalette/Absyn/Plus.java javalette/Absyn/Minus.java javalette/Absyn/MulOp.java javalette/Absyn/Times.java javalette/Absyn/Div.java javalette/Absyn/Mod.java javalette/Absyn/RelOp.java javalette/Absyn/LTH.java javalette/Absyn/LE.java javalette/Absyn/GTH.java javalette/Absyn/GE.java javalette/Absyn/EQU.java javalette/Absyn/NE.java
	${JAVAC} ${JAVAC_FLAGS} $^

javalette/Yylex.java : javalette/Yylex
	${LEXER} ${LEXER_FLAGS} javalette/Yylex

javalette/parser.java javalette/sym.java : javalette/_cup.cup
	${PARSER} ${PARSER_FLAGS} javalette/_cup.cup
	mv parser.java sym.java javalette/

javalette/Yylex.class : javalette/Yylex.java javalette/parser.java javalette/sym.java

javalette/sym.class : javalette/sym.java

javalette/parser.class : javalette/parser.java javalette/sym.java

javalette/PrettyPrinter.class : javalette/PrettyPrinter.java

clean :
	rm -f javalette/Absyn/*.class javalette/*.class

distclean : vclean

vclean :
	 rm -f javalette/Absyn/Prog.java javalette/Absyn/Program.java javalette/Absyn/TopDef.java javalette/Absyn/FnDef.java javalette/Absyn/ListTopDef.java javalette/Absyn/Arg.java javalette/Absyn/Argument.java javalette/Absyn/ListArg.java javalette/Absyn/Blk.java javalette/Absyn/Block.java javalette/Absyn/ListStmt.java javalette/Absyn/Stmt.java javalette/Absyn/Empty.java javalette/Absyn/BStmt.java javalette/Absyn/Decl.java javalette/Absyn/Ass.java javalette/Absyn/Incr.java javalette/Absyn/Decr.java javalette/Absyn/Ret.java javalette/Absyn/VRet.java javalette/Absyn/Cond.java javalette/Absyn/CondElse.java javalette/Absyn/While.java javalette/Absyn/For.java javalette/Absyn/SExp.java javalette/Absyn/Item.java javalette/Absyn/NoInit.java javalette/Absyn/Init.java javalette/Absyn/ListItem.java javalette/Absyn/Type.java javalette/Absyn/Int.java javalette/Absyn/Doub.java javalette/Absyn/Bool.java javalette/Absyn/Void.java javalette/Absyn/ArrType.java javalette/Absyn/Fun.java javalette/Absyn/ListType.java javalette/Absyn/Expr.java javalette/Absyn/EVar.java javalette/Absyn/EIndex.java javalette/Absyn/ELength.java javalette/Absyn/ELitArr.java javalette/Absyn/ELitInt.java javalette/Absyn/ELitDoub.java javalette/Absyn/ELitTrue.java javalette/Absyn/ELitFalse.java javalette/Absyn/EApp.java javalette/Absyn/EString.java javalette/Absyn/Neg.java javalette/Absyn/Not.java javalette/Absyn/EMul.java javalette/Absyn/EAdd.java javalette/Absyn/ERel.java javalette/Absyn/EAnd.java javalette/Absyn/EOr.java javalette/Absyn/Dim.java javalette/Absyn/ArrDim.java javalette/Absyn/ListDim.java javalette/Absyn/ListExpr.java javalette/Absyn/Lhs.java javalette/Absyn/LhsVar.java javalette/Absyn/LhsArray.java javalette/Absyn/Index.java javalette/Absyn/ArrInd.java javalette/Absyn/AddOp.java javalette/Absyn/Plus.java javalette/Absyn/Minus.java javalette/Absyn/MulOp.java javalette/Absyn/Times.java javalette/Absyn/Div.java javalette/Absyn/Mod.java javalette/Absyn/RelOp.java javalette/Absyn/LTH.java javalette/Absyn/LE.java javalette/Absyn/GTH.java javalette/Absyn/GE.java javalette/Absyn/EQU.java javalette/Absyn/NE.java javalette/Absyn/Prog.class javalette/Absyn/Program.class javalette/Absyn/TopDef.class javalette/Absyn/FnDef.class javalette/Absyn/ListTopDef.class javalette/Absyn/Arg.class javalette/Absyn/Argument.class javalette/Absyn/ListArg.class javalette/Absyn/Blk.class javalette/Absyn/Block.class javalette/Absyn/ListStmt.class javalette/Absyn/Stmt.class javalette/Absyn/Empty.class javalette/Absyn/BStmt.class javalette/Absyn/Decl.class javalette/Absyn/Ass.class javalette/Absyn/Incr.class javalette/Absyn/Decr.class javalette/Absyn/Ret.class javalette/Absyn/VRet.class javalette/Absyn/Cond.class javalette/Absyn/CondElse.class javalette/Absyn/While.class javalette/Absyn/For.class javalette/Absyn/SExp.class javalette/Absyn/Item.class javalette/Absyn/NoInit.class javalette/Absyn/Init.class javalette/Absyn/ListItem.class javalette/Absyn/Type.class javalette/Absyn/Int.class javalette/Absyn/Doub.class javalette/Absyn/Bool.class javalette/Absyn/Void.class javalette/Absyn/ArrType.class javalette/Absyn/Fun.class javalette/Absyn/ListType.class javalette/Absyn/Expr.class javalette/Absyn/EVar.class javalette/Absyn/EIndex.class javalette/Absyn/ELength.class javalette/Absyn/ELitArr.class javalette/Absyn/ELitInt.class javalette/Absyn/ELitDoub.class javalette/Absyn/ELitTrue.class javalette/Absyn/ELitFalse.class javalette/Absyn/EApp.class javalette/Absyn/EString.class javalette/Absyn/Neg.class javalette/Absyn/Not.class javalette/Absyn/EMul.class javalette/Absyn/EAdd.class javalette/Absyn/ERel.class javalette/Absyn/EAnd.class javalette/Absyn/EOr.class javalette/Absyn/Dim.class javalette/Absyn/ArrDim.class javalette/Absyn/ListDim.class javalette/Absyn/ListExpr.class javalette/Absyn/Lhs.class javalette/Absyn/LhsVar.class javalette/Absyn/LhsArray.class javalette/Absyn/Index.class javalette/Absyn/ArrInd.class javalette/Absyn/AddOp.class javalette/Absyn/Plus.class javalette/Absyn/Minus.class javalette/Absyn/MulOp.class javalette/Absyn/Times.class javalette/Absyn/Div.class javalette/Absyn/Mod.class javalette/Absyn/RelOp.class javalette/Absyn/LTH.class javalette/Absyn/LE.class javalette/Absyn/GTH.class javalette/Absyn/GE.class javalette/Absyn/EQU.class javalette/Absyn/NE.class
	 rm -f javalette/Absyn/*.class
	 rmdir javalette/Absyn
	 rm -f javalette/Yylex javalette/_cup.cup javalette/Yylex.java javalette/VisitSkel.java javalette/ComposVisitor.java javalette/AbstractVisitor.java javalette/FoldVisitor.java javalette/AllVisitor.java javalette/PrettyPrinter.java javalette/Skeleton.java javalette/Test.java javalette/parser.java javalette/sym.java javalette/*.class
	 rm -f Makefile
	 rmdir -p javalette
