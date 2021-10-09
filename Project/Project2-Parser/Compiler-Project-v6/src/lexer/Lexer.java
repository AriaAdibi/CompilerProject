package lexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Stack;

import symbols.Type;

public class Lexer{
	public static final char EOF= '\u001a';
	public static final char[] HEX_DIGITS=
		{
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f',
			'A', 'B', 'C', 'D', 'E', 'F'
		};
	
	private static boolean isHexDigit(char theChar){
		for( char ch: Lexer.HEX_DIGITS )
			if( ch == theChar )
				return true;
		return false;
	}
	
	private static boolean isDecDigit(char theChar){
		return Character.isDigit(theChar);
	}
	
	private static final char[] LITERALS_OF_INCLUDE=
		{
			'i', 'n', 'c', 'l', 'u', 'd', 'e'
		};
	
	private int lineNum= 1;
	private String prevLines= "";
	
	public String getTokenLocation(){
		return "line "+ this.lineNum+ this.prevLines;
	}
	private boolean EOFisReported= false;

	private String curDirAddress= null;	
	private InputStream topInStream= null;
	private Stack<LexerInputStreamStackElem> inStreamStack= null;
	
	private char peek= ' ';
	private Hashtable<String, Word> words= new Hashtable<String, Word>();

	private void reserve(Word w){
		this.words.put(w.lexeme, w);
	}
	
	public Lexer(){
		this.peek= ' ';
		this.words= new Hashtable<String, Word>();
		
		reserve( Word.BREAK );		reserve( Word.CONTINUE );
		reserve( Word.IF );			reserve( Word.ELSE );
		reserve( Word.TRUE );		reserve( Word.FALSE );
		reserve( Word.FOR );		reserve( Word.WHILE );
		reserve( Word.RETURN );		reserve( Word.VOID );
		
		reserve( Word.READCHAR );	reserve( Word.WRITECHAR );
		reserve( Word.READINT );	reserve( Word.WRITEINT );
		reserve( Word.READFLOAT );	reserve( Word.WRITEFLOAT );
		
		reserve( Type.Float );		reserve( Type.Int );
		reserve( Type.Char );		reserve( Type.Bool );
		
		this.lineNum= 1;
		this.prevLines= "";
		this.EOFisReported= false;
		
		this.curDirAddress= null;
		this.inStreamStack= new Stack<LexerInputStreamStackElem>();
		this.topInStream= System.in;
	}
	
	public Lexer( String fileAddress ) throws FileNotFoundException{
		this.peek= ' ';
		this.words= new Hashtable<String, Word>();
		
		reserve( Word.BREAK );		reserve( Word.CONTINUE );
		reserve( Word.IF );			reserve( Word.ELSE );
		reserve( Word.TRUE );		reserve( Word.FALSE );
		reserve( Word.FOR );		reserve( Word.WHILE );
		reserve( Word.RETURN );		reserve( Word.VOID );
		
		reserve( Word.READCHAR );	reserve( Word.WRITECHAR );
		reserve( Word.READINT );	reserve( Word.WRITEINT );
		reserve( Word.READFLOAT );	reserve( Word.WRITEFLOAT );
		
		reserve( Type.Float );		reserve( Type.Int );
		reserve( Type.Char );		reserve( Type.Bool );
		
		this.lineNum= 1;
		this.prevLines= "";
		this.EOFisReported= false;
		
		this.curDirAddress= fileAddress.substring( 0, fileAddress.lastIndexOf('\\') );//TODO
		this.inStreamStack= new Stack<LexerInputStreamStackElem>();
		this.topInStream= new FileInputStream(fileAddress);
	}
	
	/*
	 * Error recovery policy in Lexer is simply chosen to be ignoring
	 * the not matched characters and continue from next character.
	*/
	private void errorHandler(String theError) throws ErrorLexer{
		throw new ErrorLexer(this.getTokenLocation() + theError);
	}
	
	public Token nextToken() throws IOException, ErrorLexer{
		//Skipping White Spaces and comments
		skipWhiteSpaces();
		while(peek == '/'){
			readch();
			if( peek == '/' ){
				ignoreLineComment();
				//peek= \n
				peek= ' ';//For continuing at char after peeks
			}
			else if( peek == '*' ){
				ignoreMultiLineComment();
				//peek= /
				peek= ' ';//For continuing at char after peeks
			}
			else
				return new Token(Tag.DIVISION);
			skipWhiteSpaces();
		}
		
		//#include
		if(peek == '#')
			return handleInclude();
		
		//Potentially Compound Operators - /(handled above)
		switch(peek){
			case '&':
				if( readch('&') )
					return Word.ANDAND;
				else
					errorHandler("--Invalid token= &");
			case '|':
				if( readch('|') )
					return Word.OROR;
				else
					errorHandler("--Invalid token= |");
			case '=':
				if( readch('=') )
					return Word.EQ;
				else
					return new Token(Tag.ASSIGNMENT);
			case '!':
				if( readch('=') )
					return Word.NE;
				else
					return new Token(Tag.NOT);
			case '<':
				if( readch('=') )
					return Word.LE;
				else
					return new Token(Tag.LESS_THAN);
			case '>':
				if( readch('=') )
					return Word.GE;
				else
					return new Token(Tag.GREATER_THAN);
			//no default, other scenarios are handled bellow
		}
		
		//Number Literals
		if( Lexer.isDecDigit(peek) || peek=='.' )
			return readNumberLiteral();
		
		//Char Literals
		if( peek == '\'' )
			return readCharLiteral();
		
		//Identifiers & Keywords (Word)
		if( Character.isLetter(peek) )
			return readWord();
		
		//EOF
		if( peek == Lexer.EOF )
			if( this.inStreamStack.isEmpty() == false ){
				LexerInputStreamStackElem topElem= this.inStreamStack.pop();
				this.lineNum= topElem.getLineNum();
				this.curDirAddress= topElem.getCurDirAddress();
				this.topInStream= topElem.getInStream();
				
				this.prevLines= this.prevLines.substring(2);
				peek= ' ';//For reading next char (continuing)
				return nextToken();			
			}
			else
				if(this.EOFisReported==false){
					this.EOFisReported= true;
					return Token.EOF;
				}
				else
					throw new RuntimeException("line "+ this.lineNum+ "--EOF is reached previously");
		
		//Others which in this situation means 
		//Operators without the potentiality of being compound
		if( !Token.isValidUnPCompoundSCharTok(peek) ){
			peek= ' ';//For continuing at char after peeks
			errorHandler("--Invalid token= "+ peek);
		}
		Token tok= new Token(Tag.getTagGivenCode( (int)peek ));
		peek= ' ';//For continuing at char after peeks
		return tok;
	}
	
	private void readch() throws IOException{
		int r= this.topInStream.read();
		if( r == -1 )
			peek= Lexer.EOF;
		else 
			peek= (char)r;
	}
	
	private boolean readch(char expected) throws IOException{
		readch();
		if(peek!=expected)
			return false;
		peek= ' ';//For continuing at char after peeks
		return true;
	}

	private void skipWhiteSpaces() throws IOException{
		while( Character.isWhitespace(peek) ){
			if( peek == '\n' )
				this.lineNum++;
			readch();
		}
	}
	
	private void ignoreLineComment() throws IOException{
		//already has read "//" -- peek= /
		while( peek != '\n' ){
			readch();
			if( peek == Lexer.EOF )
				return;
		}
		//peek= '\n'
		this.lineNum++;
	}

	private void ignoreMultiLineComment() throws IOException, ErrorLexer{
		//already has read "/*" -- peek= *
		readch();
		while(true){
			while( peek != '*' ){
				if( peek == '\n' )
					this.lineNum++;
				readch();
				if( peek == Lexer.EOF )//next time nextToken is invoked EOF will be reported.
					errorHandler("--MultiLine comment is never closed");
			}
			readch();
			if( peek == '\n' )
				this.lineNum++;
				
			if( peek == '/' )
				break;
			else if( peek == Lexer.EOF )//next time nextToken is invoked EOF will be reported.
				errorHandler("--MultiLine comment is never closed");
			else
				continue;
		}
	}
	
	private Token handleInclude() throws IOException, ErrorLexer{
		//Match "include"
		readch();
		for( int i=0; i<Lexer.LITERALS_OF_INCLUDE.length; i++, readch() )
			if( peek != Lexer.LITERALS_OF_INCLUDE[i] ){
				StringBuffer strB= new StringBuffer();
				for( int j=0; j<i; j++ )
					strB.append(Lexer.LITERALS_OF_INCLUDE[j]);
				errorHandler("--Expecting \"include\" after #= #"+ strB.toString());
			}

		//Skip spaces except \n
		while( Character.isWhitespace(peek) && peek != '\n')
			readch();
		
		//Get file address
		StringBuffer fileAddress= new StringBuffer();
		if(peek == '\"'){
			readch();
			while(peek != '\"'){
				if( peek == Lexer.EOF )//next time nextToken is invoked EOF will be reported.
					errorHandler("--Include file address is never closed");
				fileAddress.append(peek);
				readch();
			}
			peek= ' ';//For continuing at char after peek
			
			//handles local file convention
			if( fileAddress.indexOf("\\") == -1 )
				fileAddress.insert(0, this.curDirAddress+ "\\");
		}
		else
			errorHandler("--Expecting \" after #include= #include\"?");
		
		//instantiate the FileInputStream
		InputStream includedFileInStream= null;
		try{
			includedFileInStream= new FileInputStream( fileAddress.toString() );
		}catch (FileNotFoundException e){
			errorHandler("--The file specified in #include is not found.");
		}
		
		//Update InputStream (Push)
		this.inStreamStack.push(
			new LexerInputStreamStackElem(this.topInStream, this.lineNum, this.curDirAddress) );
		this.prevLines= ":"+ this.lineNum+ this.prevLines;
		this.lineNum= 1;
		this.curDirAddress= fileAddress.substring(0, fileAddress.lastIndexOf("\\"));
		this.topInStream= includedFileInStream;
		peek= ' ';//Necessary initialization
		
		return nextToken();
	}
	
	private void appendDecNumber(StringBuffer val) throws IOException{
		while( Lexer.isDecDigit(peek) ){
			val.append(peek);
			readch();
		}
	}

	private void appendHexNumber(StringBuffer val) throws IOException{
		while( Lexer.isHexDigit(peek) ){
			val.append(peek);
			readch();
		}
	}
	
	private Token readNumberLiteral() throws IOException, ErrorLexer{
		//peek= decDigit or .
		StringBuffer intVal= new StringBuffer();
		boolean isHex= false;
		if( peek == '0' ){	//handling 0x
			intVal.append('0');
			readch();
			if( peek=='x' || peek=='X' ){
				isHex= true;
				intVal.append('x');
				readch();
				appendHexNumber(intVal);
			}
			else
				appendDecNumber(intVal);
		}
		else if( peek != '.' )
			appendDecNumber(intVal);
	
	
		if( peek!='.')
			return new IntNum( intVal.toString() );
	
	
		StringBuffer realVal= intVal;
		realVal.append('.');
		readch();
		if(isHex)
			errorHandler("--Hexadecimal numbers must be integers");
		//appendHexNumber(realVal);
		else
			appendDecNumber(realVal);
			
		if( realVal.length()==1 )//=1 for .
			errorHandler("--Invalid token= .");
		return new RealNum( realVal.toString() );
	}
	
	private Token readCharLiteral() throws IOException, ErrorLexer{
		//peek= '
		readch();
		if( peek == '\'' ){
			peek= ' ';//For continuing at char after peek
			return new CharLiteral("");
		}			
		else if( peek == '\\' ){
			readch();
			if(peek=='\'' || peek=='\\' || peek=='t' || peek=='n'){
				char savedPeek= peek;
				if( readch('\'') )
					return new CharLiteral("\\"+ savedPeek);
				else
					errorHandler("--Missing \' in chat literal= \'\\" + savedPeek+ "'?");
			}
			else
				errorHandler("--In char literal after \\ one of \', \\, t, n is expected= \'\\?");
		}
		else if( CharLiteral.isPrintableChar(peek) ){
			char savedPeek= peek;
			if( readch('\'') )
				return new CharLiteral( String.valueOf(savedPeek) );
			else
				errorHandler("--Missing \' in chat literal= \'" + savedPeek+ "'?");
		}
		else
			errorHandler("--Invalid Token= \' ::next character violates char literal rules");
			
		return null;
	}
	
	private Token readWord() throws IOException{
		//peek= 'a letter'
		StringBuffer theWord= new StringBuffer();

		do{
			theWord.append(peek);
			readch();
		}while( Character.isLetterOrDigit(peek) );
		String strTheWord= theWord.toString();
		Word w= words.get(strTheWord);
		if( w != null )
			return w;
		else{
			w= new Word(strTheWord, Tag.ID);
			words.put(strTheWord, w);
			return w;
		}
	}
}