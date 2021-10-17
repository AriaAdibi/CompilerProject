package intermediateRepresentation;

import symbols.Environment;
import symbols.Type;

public class For extends Stmt{
	//TODO

	private Stmt	initStmt;
	private Expr	expr;
	private Stmt	iterateStmt;
	private Stmt	bodyStmt;

	public For(Environment env) {
		super(env);
		
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
	public void gen(int begin, int after) {
		int labelFor = Node.newLabel();
		int labelBody = Node.newLabel();
		int labelEndOfBody = Node.newLabel();
		int labelIterate = Node.newLabel();
		super.afterLabel = after;
		super.continiueLabel = labelIterate;
		
		Node.emitLabel(begin);
		
		initStmt.gen(begin, labelFor);
		Node.emitLabel(labelFor);
		expr.jumping(0, after);
		Node.emitLabel(labelBody);
		bodyStmt.gen(labelBody, labelEndOfBody);
		Node.emitLabel(labelEndOfBody);
		
		
		Node.emitLabel(labelIterate);
		iterateStmt.gen(labelIterate, labelFor);
		Node.emit("goto label " + labelFor);
		Node.emitLabel(after);
	}
}