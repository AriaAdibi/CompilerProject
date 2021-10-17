package lexer;

public class CharLiteral extends Token{
	public final String charLiteralLexeme;
	
	public CharLiteral(String charLiteral){
		super(Tag.CHAR_LITERAL);
		this.charLiteralLexeme= charLiteral;
	}
	
	public static boolean isPrintableChar(char ch){
		int r= ch;
		if( r<32 || r>126 )
			return false;
		return true;
	}
	
	@Override
	public String getLexeme(){
		return this.charLiteralLexeme;
	}
	
	@Override
	public String toString() {
		return "'" + this.charLiteralLexeme + "'";
	}
}