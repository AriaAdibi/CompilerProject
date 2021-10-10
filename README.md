This project is a java implementation of a compiler translating *Decaf* language to a certain target (virtual) machine code. The details of the definitions and objectives can be found in [*DecafProject.pdf*](https://github.com/AriaAdibi/CompilerProject/blob/modifyingTheREADME/DecafProject.pdf). The execution of the virtual machine is also implemented.

*No special tool is used in this project and all parts are implemented.*

## Features
The program may be configured to generate the machine code in **one-pass**, instead of the default which generate an *Intermediate Representation (IR)* first. The configuration is an easy code modification.

### Lexer
Two implementation:
- Raw Java
- Using jFlex

Capabilities:
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

## Screenshots
The screenshots can be found in the *Overview* directory.

#### To IR Code

###### Sort
![sort](/Overview/Screenshots-IRCode/sort.png)
This error is due to the fact that the grammar doe not support simultaneous variable definition and assignment.

###### Semantic
![semantic](/Overview/Screenshots-IRCode/semantic.png)

###### Semantic 2
![semantic2](/Overview/Screenshots-IRCode/semantic2.png)

###### Scope
![scope](/Overview/Screenshots-IRCode/scope.png)

###### Prime
![prime](/Overview/Screenshots-IRCode/prime.png)

###### Op
![op](/Overview/Screenshots-IRCode/op.png)

###### Multi-dimensional Array
![multDimArray](/Overview/Screenshots-IRCode/multDimArray.png)

###### Recursive Factorial
![fac](/Overview/Screenshots-IRCode/fac.png)

###### Exp
![exp](/Overview/Screenshots-IRCode/exp.png)

###### Bad Array
![badarray](/Overview/Screenshots-IRCode/badarray.png)

###### Array
![array](/Overview/Screenshots-IRCode/array.png)


#### To The Machine Code and then Evaluated

###### Array
![array](/Overview/Screenshots-CodeGenerator+Evaluation/array.png)

###### Array Output
![array-output](/Overview/Screenshots-CodeGenerator+Evaluation/array-output.png)

###### Exp
![exp](/Overview/Screenshots-CodeGenerator+Evaluation/exp.png)

###### Index Out of Bound
![indexOutOfBound](/Overview/Screenshots-CodeGenerator+Evaluation/indexOutOfBound.png)

###### Op
![op](/Overview/Screenshots-CodeGenerator+Evaluation/op.png)
