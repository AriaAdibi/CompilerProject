package lexer;

public class ErrorLexer extends Exception{
	private static final long serialVersionUID = 1L;

	public ErrorLexer(){
		super();
	}
	
	public ErrorLexer(String errMsg){
		super(errMsg);
	}
	
	@Override
	public String getMessage(){
		return "Error in Lexer: " + super.getMessage();
	}
}