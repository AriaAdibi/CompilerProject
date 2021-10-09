package lexer;

public class CharLiteral extends Token{
	public final String charLiteral;
	
	public CharLiteral(String charLiteral){
		super(Tag.CHAR);
		this.charLiteral= charLiteral;
	}
	
	public static boolean isPrintableChar(char ch){
		int r= ch;
		if( r<32 || r>126 )
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return "char_literal";
	}
	
	@Override
	public String getLexeme() {
		return this.charLiteral;
	}
}