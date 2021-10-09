package lexer;

public class RealNum extends Token{
	public final String realNumLexeme;
	
	public RealNum(String realLiteral){
		super(Tag.REAL_NUM);
		this.realNumLexeme= realLiteral;
	}
	
	@Override
	public String getLexeme(){
		return this.realNumLexeme;
	}
}