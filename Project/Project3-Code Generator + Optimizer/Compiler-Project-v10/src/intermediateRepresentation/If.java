package intermediateRepresentation;

import symbols.Environment;
import symbols.Type;

public class If extends Stmt {

	Expr	expr;
	Stmt	stmt;

	public If(Environment env, Expr x, Stmt s) throws ErrorSemantics {
		super(env);
		
		expr = x;
		stmt = s;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in if");
	}

	@Override
	public void gen(int begin, int after) {
		emit("ENV" + env + ": ");
		int label = Node.newLabel();
		expr.jumping(0, after);
		Node.emitLabel(label);
		stmt.gen(label, after);
		Node.emitLabel(after);
	}
}