package intermediateRepresentation;

import symbols.Environment;
import symbols.Type;

public class IfElse extends Stmt {

	Expr	expr;
	Stmt	stmt1, stmt2;

	public IfElse(Environment env, Expr x, Stmt s1, Stmt s2) throws ErrorSemantics{
		super(env);
		
		expr = x;
		stmt1 = s1;
		stmt2 = s2;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in if");
	}

	@Override
	public void gen(int begin, int after) {
		emit("ENV" + env + ": ");
		
		int label1 = Node.newLabel();	// label1 for stmt1
		int label2 = Node.newLabel();	// label2 for stmt2
		expr.jumping(0, label2);		// fall through to stmt1 on true
		Node.emitLabel(label1);
		stmt1.gen(label1, after);
		emit("goto label " + after);
		Node.emitLabel(label2);
		stmt2.gen(label2, after);
		Node.emitLabel(after);
	}
}