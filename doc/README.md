# Documentation

HOW TO RUN
==========

1) Make in root directory.
2) Execute jlc in root directory. jlc takes one argument: ./jlc <path to javalette source file>
    Alternatively, if jlc is run without any argument, it expects javalette source code in stdin.

PROGRAM BEHAVIOUR
=================

In its current state the compiler only performs lexing, parsing and type/return checking.
If all steps were successful, the program will write 'OK' to stderr and terminate with exit code 0.
If any of the steps were unsuccessful, the program will write 'ERROR' to stderr followed by an error message and terminate with exit code 1.

JAVALETTE LANGUAGE AND EXTENSIONS
=================================

The specification of the Javalette language the compiler uses is found in src/Javalette.cf.
The grammar is in its original state as provided. Thus there are no additional shift/reduce conflicts.
No extensions have been implemented at this point for submission A.
