package intermediateRepresentation;


public class ProcCallStmt extends Stmt {
	private ProcCall procCall;
	
	public ProcCallStmt(ProcCall procCall) {
		this.procCall = procCall;
	}
	
	@Override
	public void gen(int beginLabel, int afterLabel) {
		if( this.procCall.getRetType() != null )
			this.procCall.gen().reduce();
		else
			emit( this.procCall.gen().toString() );
	}
}
