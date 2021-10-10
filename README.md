This project is a java implementation of a compiler translating *Decaf* language to a certain target (virtual) machine code. The details of the definitions and objectives can be found in [*DecafProject.pdf*](https://github.com/AriaAdibi/CompilerProject/blob/modifyingTheREADME/DecafProject.pdf). The execution of the virtual machine is also implemented.

*No special tool is used in this project and all parts are implemented.*

## Features
The program may be configured to generate the machine code in **one-pass**, instead of the default which generate an *Intermediate Representation (IR)* first. The configuration is an easy code modification.

### Lexer

- Handles `#include` command.
- Identifies any possible error and does the following:
	- Reports the exact file name and line number where the error occurred.
	- Guesses what went wrong and suggests some corrective measures.
	- By default uses the `panic-mode` error recovery. The user can disable this feature.

### Parser, Semantic Analyzer and Abstract Sytanx Tree
- The grammar is converted to a L-attributed and LL(1) grammar.
- On-demand error recovery, which uses `panic-mode` and a simple `phrase-level` strategies.
- A Syntax-directed translation is implemented in a way that IR generation (or the taget machine code depending on the user's choice) is done in one-pass.

### IR to machine code module
- Translates the IR code to the target machine code.

## TODO
- Optimizations for IR code
- Making a better interface for choosing the options.

## Screenshots

![sort](/Overview/Screenshots-IRCode/sort.png)
