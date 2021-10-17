package intermediateRepresentation;

import symbols.Type;

public class If extends Stmt {

	Expr	expr;
	Stmt	stmt;

	public If(Expr x, Stmt s) throws ErrorSemantics {
		expr = x;
		stmt = s;
		if (expr.type != Type.BOOLEAN)
			Node.errorHandler("--boolean required in if");
	}

	@Override
	public void gen(int beginLabel, int afterLabel){
		int ifBodylabel = Node.newLabel();
		
		expr.jumping(0, afterLabel);
		
		Node.emitLabel(ifBodylabel);
		stmt.gen(ifBodylabel, afterLabel);
		Node.emitLabel(afterLabel);
	}
}