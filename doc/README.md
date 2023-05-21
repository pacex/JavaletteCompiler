# Documentation

HOW TO RUN
==========

1) Make in root directory.
2) Execute jlc in root directory. jlc takes one argument: ./jlc <path to javalette source file>
    Alternatively, if jlc is run without any argument, it expects javalette source code in stdin.


For debugging purposes, optional arguments are available:

    --nooutput      if set, the compiler will not output LLVM code
    --printast      if set, the compiler will output a visualization of the AST

Note: these options only work if a file path has been specified!

PROGRAM BEHAVIOUR
=================

The frontend stage of the compiler performs lexing, parsing and type/return checking.
If all steps were successful, the program will write 'OK' to stderr.
If any of the steps were unsuccessful, the program will write 'ERROR' to stderr followed by an error message and terminate with exit code 1.

Assuming the input is a correct javalette program, the compiler will proceed to the backend stage and generate corresponding LLVM code and write it to std out.
Afterwards it will terminate with exit code 0.

JAVALETTE LANGUAGE AND EXTENSIONS
=================================

The specification of the Javalette language the compiler uses is found in the grammar file src/Javalette.cf.
This submission is a preliminary one for assignment C. It implements the following extensions:

 - Arrays1
 - Arrays2

The grammar file has been extended accordingly. The updated grammar contains ONE additional shift/reduce conflict other than those already present in the given grammar.

It occurs between the rules "Expr6 ::= "new" Type [Dim] ;" and "Dim ::= "[" Expr "]" ;" under the symbol "[". This is because "[" could also be the begin of the index operator, indexing the newly allocated array. Since the conflict is resolved in favor of shifting, subsequent "[" symbols will never be seen as part of an index operator, preserving the intended behaviour. The consequence is that directly indexing an array at the "new" keyword is impossible without specifying precedence through additional brackets. This is neither severe nor avoidable with the given syntax.
