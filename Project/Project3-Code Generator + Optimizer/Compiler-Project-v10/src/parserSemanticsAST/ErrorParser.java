package parserSemanticsAST;

public class ErrorParser extends Exception{
	private static final long serialVersionUID = 1L;
	
	public final boolean isRFailed;
	private final String failedRStatement;
	
	public ErrorParser(boolean isRFailed, String failedRStatement){
		super();
		this.isRFailed= isRFailed;
		this.failedRStatement= failedRStatement;
	}
	
	public ErrorParser(String errMsg, boolean isRFailed, String failedRStatement){
		super(errMsg);
		this.isRFailed= isRFailed;
		this.failedRStatement= failedRStatement;
	}

	public String getFailedRStatement(){
		return this.failedRStatement;
	}
	
	@Override
	public String getMessage(){
		return "Error in Parser: " + super.getMessage();
	}
}