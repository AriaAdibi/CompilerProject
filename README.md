This project is a java implementation of a compiler for *Decaf* language to a certain (virtual) machine code. The details of the objective and definition can be found in *DecafProject.pdf*.

*No special tool is used. All parts are implemented.

*Generates the "Intermediate Representation" in one pass.

*Lexer
	--Handles "#include" command.
	--Identifies any possible error and does the following:
		-Reports the exact file name and line number where the error occurred.
		-Guesses what went wrong and suggests some corrective measures.
		-By default uses the ``panic-mode'' error recovery. The user can disable this feature.

*Parser, semantic analyzer and abstract sytanx tree
	--The grammar is converted to a L-attributed and LL(1) grammar.
	--On-demand error recovery, which uses "panic-mode" and a simple "phrase-level" strategies.
	--A "Syntax-directed translation" is implemented in a way that IR generation is done in one pass.

*IR to machine code module
