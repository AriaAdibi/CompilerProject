package lexer;

public class Word extends Token{
	public final String lexeme;
	
	public Word(String lexeme, Tag tag){
		super(tag);
		this.lexeme= lexeme;
	}
	
	public static final Word
		//Keywords
		/*Types including Array are declared in subclasses*/
		BREAK= new Word("break", Tag.BREAK),	CONTINUE= new Word("continue", Tag.CONTINUE),
		IF= new Word("if", Tag.IF),				ELSE= new Word("else", Tag.ELSE),
		TRUE= new Word("true", Tag.TRUE),		FALSE= new Word("false", Tag.FALSE),
		FOR= new Word("for", Tag.FOR),			WHILE= new Word("while", Tag.WHILE),
		RETURN= new Word("return", Tag.RETURN),	VOID= new Word("void", Tag.VOID),
		
		READCHAR= new Word("readchar", Tag.READER),
		READFLOAT= new Word("readfloat", Tag.READER),
		READINT= new Word("readint", Tag.READER),
		WRITECHAR= new Word("writechar", Tag.WRITER),
		WRITEFLOAT= new Word("writefloat", Tag.WRITER),
		WRITEINT= new Word("writeint", Tag.WRITER),
		
		//Compound operators
		ANDAND= new Word("&&", Tag.ANDAND),	OROR= new Word("||", Tag.OROR),
		EQ= new Word("==", Tag.EQ),			NE= new Word("!=", Tag.NEQ),
		LE= new Word("<=", Tag.LE),			GE= new Word(">=", Tag.GE),
		
		//Used in building syntax-tree and not in the lexer
		UNARY_MINUS= new Word("unaryMinus", Tag.MINUS),
		TEMP= new Word("t", Tag.TEMP);
	
	@Override
	public String getLexeme(){
		return this.lexeme;
	}
}