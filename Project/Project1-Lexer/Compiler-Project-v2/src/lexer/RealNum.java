package lexer;

public class RealNum extends Token{
	public final String realLiteral;
	
	public RealNum(String realLiteral){
		super(Tag.REAL);
		this.realLiteral= realLiteral;
	}
	
	@Override
	public String toString(){
		return "float_literal";
	}
	
	@Override
	public String getLexeme(){
		return this.realLiteral;
	}
}