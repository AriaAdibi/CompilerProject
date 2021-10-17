package intermediateRepresentation;

import symbols.Environment;
import symbols.Type;

public class While extends Stmt{

	private Expr	expr;
	private Stmt	bodyStmt;

	public While(Environment env) {
		super(env);
		
		expr = null;
		bodyStmt = null;
	}

	public void init(Expr expr, Stmt stmt) throws ErrorSemantics {
		this.expr = expr;
		this.bodyStmt = stmt;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in while");
	}

	public void gen(int begin, int after) {
		emit("ENV" + env + ": ");
		
		super.continiueLabel = begin;
		super.afterLabel = after;
		
		Node.emitLabel(begin);
		expr.jumping(0, after);
		int label = Node.newLabel();
		Node.emitLabel(label);
		bodyStmt.gen(label, begin);
		Node.emit("goto label " + begin);
		Node.emitLabel(after);
	}
}