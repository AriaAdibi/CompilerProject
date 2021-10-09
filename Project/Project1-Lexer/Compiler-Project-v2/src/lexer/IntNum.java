package lexer;

public class IntNum extends Token{
	public final String numLiteral;
	
	public IntNum(String numLiteral){
		super(Tag.NUM);
		this.numLiteral= numLiteral;
	}
	
	@Override
	public String toString(){
		return "int_literal";
	}
	
	@Override
	public String getLexeme() {
		return this.numLiteral;
	}
}