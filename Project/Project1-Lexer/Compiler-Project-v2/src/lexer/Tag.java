package lexer;

public class Tag{
	public static final int
		//Keywords
		BASIC= 127,	/*int, float, char, boolean*/ /*Grouped for parser*/	
		BREAK= 128,		CONTINUE= 129,		ELSE= 130,			FALSE= 131,
		FOR= 132,		IF= 133,			READCHAR= 134,		READFLOAT= 135,
		READINT= 134,	RETURN= 135,		TRUE= 136,			VOID= 137,
		WHILE= 138,		WRITECHAR= 139,		WRITEFLOAT= 140,	WRITEINT= 141,
	
		//Compound operators
		EQ= 142,		NE= 143,	GE= 144,	LE= 145,
		ANDAND= 146,	OR= 147,
		
		//Literals
		NUM= 148,	REAL= 149,	CHAR= 150,		//BOOL= 151,//don't need bool, we have Tag.TRUE & Tag.FALSE
															//Bool literals are considered to be a Word(keywords)
		
		//ID
		ID= 152,
		
		//Used in building syntax-tree and not in the lexer
		INDEX= 153, MINUS= 154, TEMP= 155,
	
		//EOF
		EOF= -1;
}