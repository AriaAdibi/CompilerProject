This project is a java implementation of a compiler for *Decaf* language to a certain (virtual) machine code. The details of the definition and objectives can be found in *DecafProject.pdf*.

No special tool is used in this project and all parts are implemented.

## Features of the moduels
The program may be configured to generate the machine code in **one-pass**, instead of the default which generate an *Intermediate Representation (IR)* first. The configuration is an easy code modification.

### Lexer
	* Handles "#include" command.
	* Identifies any possible error and does the following:
		-Reports the exact file name and line number where the error occurred.
		-Guesses what went wrong and suggests some corrective measures.
		-By default uses the ``panic-mode'' error recovery. The user can disable this feature.

### Parser, semantic analyzer and abstract sytanx tree
	--The grammar is converted to a L-attributed and LL(1) grammar.
	--On-demand error recovery, which uses "panic-mode" and a simple "phrase-level" strategies.
	--A "Syntax-directed translation" is implemented in a way that IR generation is done in one pass.

### IR to machine code module

## TODO
