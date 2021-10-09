package lexer;

public class IntNum extends Token{
	public final String intNumLexeme;
	
	public IntNum(String numLiteral){
		super(Tag.INT_NUM);
		this.intNumLexeme= numLiteral;
	}
	
	@Override
	public String getLexeme(){
		return this.intNumLexeme;
	}
}