package intermediateRepresentation;

import symbols.Environment;
import symbols.ProcInfo;

public class Proc extends Node {
	
	private ProcInfo procInfo;
	private Stmt stmt;
	
	public Proc(Environment env, ProcInfo procInfo, Stmt stmt) {
		super(env);
		
		this.procInfo = procInfo;
		this.stmt = stmt;
	}
	
	public void gen() {
		emit("PROC _" + procInfo.getProcId() + ":");
		this.stmt.gen(Node.newLabel(), Node.newLabel());
		emit("END PROC");
	}

}