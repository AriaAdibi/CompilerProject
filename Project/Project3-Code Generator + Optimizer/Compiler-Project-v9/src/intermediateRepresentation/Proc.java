package intermediateRepresentation;

import symbols.ProcInfo;

public class Proc extends Node {
	
	private ProcInfo procInfo;
	private Stmt stmt;
	
	public Proc(ProcInfo procInfo, Stmt stmt) {
		this.procInfo = procInfo;
		this.stmt = stmt;
	}
	
	public void gen() {
		int beginProcLabel= Node.newLabel();
		int endProcLabel= Node.newLabel();
		
		emit("PROC _" + procInfo.getProcId() + ":");
		Node.emitLabel(beginProcLabel);
		
		this.stmt.gen(beginProcLabel, endProcLabel);
		
		Node.emitLabel(endProcLabel);
		Node.emit("");
		emit("END PROC");
	}

}