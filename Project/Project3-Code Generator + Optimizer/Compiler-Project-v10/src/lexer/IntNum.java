package lexer;

public class IntNum extends Token{
	public final String intNumLexeme;
	
	private int value= 0;
	
	public IntNum(String numLiteral){
		super(Tag.INT_NUM);
		this.intNumLexeme= numLiteral;
		this.evaluateTheValue();
	}
	
	public IntNum(int value) {
		super(Tag.INT_NUM);
		this.intNumLexeme = String.valueOf(value);
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	private void evaluateTheValue(){
		try {
			//TODO -2^31
			this.value = Integer.decode(intNumLexeme);
		} catch(Exception exc) {
			System.err.println("Error in casting IntNum value to int");
			System.exit(-1);
		}
	}
	
	@Override
	public String toString() {
		return this.intNumLexeme;
	}
	
	@Override
	public String getLexeme(){
		return this.intNumLexeme;
	}
}