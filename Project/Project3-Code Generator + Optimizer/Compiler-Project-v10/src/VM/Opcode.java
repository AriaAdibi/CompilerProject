package VM;

public enum Opcode {
	PLUS,
	MINUS,
	MUL,
	DIV,
	MOD,
	AND,
	OR,
	L,
	G,
	LE,
	GE,
	E,
	NE,
	NOT,
	ASSIGN,
	JT,
	JMP,
	WI,
	WF,
	WB,
	WC,
	RI,
	RF,
	RB,
	RC,
	PC,
	SP,
	ASP,
	RET;
	public static String [] strings={"+","-","*","/","%","&&","||","<",">","<=",">=","==","!=","!",":=","jt","jmp","wi","wf","wb","wc","ri","rf","rb","rc",":=pc",":=sp","sp:=","ret"};
}
