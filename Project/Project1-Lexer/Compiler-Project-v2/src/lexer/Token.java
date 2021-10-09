package lexer;

public class Token{
	public final int tag;
	
	//Valid, unpotentially compound, single char tokens = validUpCompoundSCharTok
	private static final char[] validUnPCompoundSCharTok=
		{
		'(', ')', '{', '}', '[', ']', ';',
		',', '+', '-', '*', '/', '%'
		};
	
	public static boolean isValidUnPCompoundSCharTok(char theCh){
		for(char ch: Token.validUnPCompoundSCharTok)
			if( ch == theCh )
				return true;
		return false;
	}
	
	public Token(int tag){
		this.tag= tag;
	}
	
	@Override
	public String toString(){
		if(this.tag == Tag.EOF)
				return "EOF";
		if( this.tag < 0 )
				throw new RuntimeException("Invalid Character--Token tag < 0 and != Tag.EOF");		
		return String.valueOf( (char)this.tag );
	}
	
	public String getLexeme(){
		if(this.tag == Tag.EOF)
				return "EOF";
		if( this.tag < 0 )
				throw new RuntimeException("Invalid Character--Token tag < 0 and != Tag.EOF"); 
		return String.valueOf( (char)this.tag );
	}
	
	public static final Token
		EOF= new Token(Tag.EOF);
}