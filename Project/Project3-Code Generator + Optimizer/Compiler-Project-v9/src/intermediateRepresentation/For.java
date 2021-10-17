package intermediateRepresentation;

import symbols.Type;

public class For extends Stmt{
	//TODO

	private Stmt	initStmt;
	private Expr	expr;
	private Stmt	iterateStmt;
	private Stmt	bodyStmt;

	public For() {
		initStmt = null;
		expr = null;
		iterateStmt = null;
		bodyStmt = null;
	}

	public void init(Stmt initStmt, Expr expr, Stmt iterateStmt, Stmt bodyStmt) throws ErrorSemantics {
		this.initStmt = initStmt;
		this.expr = expr;
		this.iterateStmt = iterateStmt;
		this.bodyStmt = bodyStmt;
		
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in for");
	}

	@Override
	public void gen(int beginLabel, int afterLabel) {
		int exprCheckingLabel = Node.newLabel();
		int forBodyLabel = Node.newLabel();
		int iterateStmtlabel = Node.newLabel();
		
		super.afterLabel = afterLabel;
		super.continiueLabel = iterateStmtlabel;
		
		initStmt.gen(beginLabel, exprCheckingLabel);
		
		Node.emitLabel(exprCheckingLabel);
		expr.jumping(0, afterLabel);
		
		Node.emitLabel(forBodyLabel);//TODO maybe don't show these label
		bodyStmt.gen(forBodyLabel, iterateStmtlabel);
		
		Node.emitLabel(iterateStmtlabel);
		iterateStmt.gen(iterateStmtlabel, exprCheckingLabel);
		Node.emit("goto label " + exprCheckingLabel);
		Node.emitLabel(afterLabel);
	}
}