package intermediateRepresentation;

import symbols.Type;

public class IfElse extends Stmt {

	Expr	expr;
	Stmt	stmt1, stmt2;

	public IfElse(Expr x, Stmt s1, Stmt s2) throws ErrorSemantics{
		expr = x;
		stmt1 = s1;
		stmt2 = s2;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in if");
	}

	@Override
	public void gen(int beginLabel, int afterLabel) {
		int ifBodyLabel = Node.newLabel();
		int elseBodyLabel = Node.newLabel();
		
		expr.jumping(0, elseBodyLabel);
		
		Node.emitLabel(ifBodyLabel);
		stmt1.gen(ifBodyLabel, afterLabel);
		emit("goto label " + afterLabel);
		
		Node.emitLabel(elseBodyLabel);
		stmt2.gen(elseBodyLabel, afterLabel);
		Node.emitLabel(afterLabel);
	}
}