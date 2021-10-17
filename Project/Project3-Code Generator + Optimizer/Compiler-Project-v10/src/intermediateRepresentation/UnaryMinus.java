package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;
import symbols.Type;

public class UnaryMinus extends Op {

	public Expr expr;

	public UnaryMinus(Environment env, Token tok, Expr expr) throws ErrorSemantics {
		super(env, tok, null);
		this.expr = expr;

		if (!expr.equals(Type.INT) && !expr.type.equals(Type.FLOAT))
			Node.errorHandler("--operand must be one of the types int or float.");
	}

	public Expr gen() {
		try {
			return new UnaryMinus(env, op, expr.reduce());
		}
		catch (Exception exc) {
			System.err.println("Error in UnaryMinus.gen()");
			System.exit(-1);
			return null;
		}
	}

	public String toString() {
		return op.toString() + " " + expr.toString();
	}
}