package lexer;

public class Token{
	public final Tag tag;
	
	//Valid, unpotentially compound, single char tokens = validUpCompoundSCharTok
	private static final char[] validUnPCompoundSCharTok=
		{
		'(', ')', '{', '}', '[', ']', ';',
		',', '+', '-', '*', '%'
		};
	
	public static boolean isValidUnPCompoundSCharTok(char theCh){
		for(char ch: Token.validUnPCompoundSCharTok)
			if( ch == theCh )
				return true;
		return false;
	}//TODO
	
	public Token(Tag tag){
		this.tag= tag;
	}
	
	public static final Token
		EOF= new Token(Tag.EOF);
	
	@Override
	public String toString(){
		return this.tag.toString();
	}
	
	public String getLexeme(){
		return this.toString();
	}
	
}