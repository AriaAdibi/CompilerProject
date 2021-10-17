package intermediateRepresentation;

import symbols.Type;

public class While extends Stmt{

	private Expr	expr;
	private Stmt	bodyStmt;

	public While() {
		expr = null;
		bodyStmt = null;
	}

	public void init(Expr expr, Stmt stmt) throws ErrorSemantics {
		this.expr = expr;
		this.bodyStmt = stmt;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in while");
	}

	public void gen(int beginLabel, int afterLabel) {
		super.continiueLabel = beginLabel;
		super.afterLabel = afterLabel;
		
		Node.emitLabel(beginLabel);
		expr.jumping(0, afterLabel);
		
		int whileBodyLabel = Node.newLabel();
		Node.emitLabel(whileBodyLabel);
		bodyStmt.gen(whileBodyLabel, beginLabel);
		
		Node.emit("goto label " + beginLabel);
		Node.emitLabel(afterLabel);
	}
}