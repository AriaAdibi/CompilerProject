package intermediateRepresentation;

import lexer.Token;
import symbols.Type;

public class UnaryMinus extends Op {

	public Expr expr;

	public UnaryMinus(Token tok, Expr expr) throws ErrorSemantics {
		super(tok, null);
		this.expr = expr;

		if (!expr.type.equals(Type.INT) && !expr.type.equals(Type.FLOAT))
			Node.errorHandler("--operand must be one of the types int or float.");
		
		this.type= expr.type;
	}

	public Expr gen() {
		try {
			return new UnaryMinus(op, expr.reduce());
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