package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;
import symbols.Type;

public class LogicalOp extends Expr {

	public Expr expr1, expr2;

	LogicalOp(Environment env, Token tok, Expr expr1, Expr expr2) throws ErrorSemantics {
		super(env, tok, null); // null type to start
		this.expr1 = expr1;
		this.expr2 = expr2;
		type = check(expr1.type, expr2.type);
		if (type == null)
			Node.errorHandler("--operands must be of boolean type");
	}

	public Type check(Type p1, Type p2) {
		if (p1 == Type.BOOLEAN && p2 == Type.BOOLEAN)
			return Type.BOOLEAN;
		else return null;
	}

	@Override
	public Expr gen() {
		int f = Node.newLabel();
		int a = Node.newLabel();
		Temp temp = new Temp(env, type);
		this.jumping(0, f);
		emit(temp.toString() + " = true");
		emit("goto label " + a);
		Node.emitLabel(f);
		emit(temp.toString() + " = false");
		Node.emitLabel(a);
		return temp;
	}
	
	@Override
	public Expr reduce() {
		return this;
	}

	@Override
	public String toString() {
		return expr1.toString() + " " + op.toString() + " " + expr2.toString();
	}
}