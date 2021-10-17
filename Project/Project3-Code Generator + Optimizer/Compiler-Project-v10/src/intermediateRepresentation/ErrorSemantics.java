package intermediateRepresentation;

public class ErrorSemantics extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ErrorSemantics(){
		super();
	}
	
	public ErrorSemantics(String errMsg){
		super(errMsg);
	}
	
	@Override
	public String getMessage(){
		return "Error in Semantic Analyzer: " + super.getMessage();
	}
}