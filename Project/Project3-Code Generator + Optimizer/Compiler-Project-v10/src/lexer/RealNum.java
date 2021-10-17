package lexer;

public class RealNum extends Token{
	public final String realNumLexeme;
	
	private float value= 0;
	
	public RealNum(String realLiteral){
		super(Tag.REAL_NUM);
		this.realNumLexeme= realLiteral;
	}
	
	public float getValue(){
		return this.value;
	}
	
	public void evaluateTheValue(){
		try {
			value = Float.parseFloat(realNumLexeme);
		} catch(Exception exc) {
			System.err.println("Error in casting RealNum value to float");
			System.exit(-1);
		}
	}
	
	@Override
	public String getLexeme(){
		return this.realNumLexeme;
	}
	
	@Override
	public String toString() {
		return this.realNumLexeme;
	}
}