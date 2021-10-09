package lexer;

public class Word extends Token{
	public final String lexeme;
	
	public Word(String lexeme, int tag){
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
		READCHAR= new Word("readchar", Tag.READCHAR),
		READFLOAT= new Word("readfloat", Tag.READFLOAT),
		READINT= new Word("readint", Tag.READINT),
		WRITECHAR= new Word("writechar", Tag.WRITECHAR),
		WRITEFLOAT= new Word("writefloat", Tag.WRITEFLOAT),
		WRITEINT= new Word("writeint", Tag.WRITEINT),
		
		//Compound operators
		ANDAND= new Word("&&", Tag.ANDAND),	OR	=new Word("||", Tag.OR),
		EQ= new Word("==", Tag.EQ),			NE= new Word("!=", Tag.NE),
		LE= new Word("<=", Tag.LE),			GE= new Word(">=", Tag.GE),
		
		//Used in building syntax-tree and not in the lexer
		MINUS= new Word("minus", Tag.MINUS),
		TEMP= new Word("t", Tag.TEMP);
		
	@Override
	public String toString(){
		if( this.tag == Tag.ID )
			return "id";
		else if( this.tag==Tag.TRUE || this.tag==Tag.FALSE )
			return "bool_literal";	//considered here, because I interpreted them as a Word(keyword)
		else
			return this.lexeme;
	}
	
	@Override
	public String getLexeme() {
		return this.lexeme;
	}
}