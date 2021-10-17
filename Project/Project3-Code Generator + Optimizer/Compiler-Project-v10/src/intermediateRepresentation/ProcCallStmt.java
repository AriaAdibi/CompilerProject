package intermediateRepresentation;

import symbols.Environment;

public class ProcCallStmt extends Stmt {
	private ProcCall procCall;
	
	public ProcCallStmt(Environment env, ProcCall procCall) {
		super(env);
		
		this.procCall = procCall;
	}
	
	@Override
	public void gen(int begin, int after) {
		emit("ENV" + env + ": ");
		
		Node.emitLabel(begin);
		this.procCall.gen();
		Node.emitLabel(after);
	}
}
