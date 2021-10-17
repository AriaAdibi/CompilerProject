package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;
import symbols.Type;

public class Arith extends Op {

	public Expr expr1, expr2;

	public Arith(Environment env, Token tok, Expr expr1, Expr expr2) throws ErrorSemantics {
		super(env, tok, null);
		this.expr1 = expr1;
		this.expr2 = expr2;

		if (!expr1.type.equals(expr2.type))
			Node.errorHandler("--operands must have the same type.");
		else if (!expr1.type.equals(Type.INT) && !expr1.type.equals(Type.FLOAT))
			Node.errorHandler(
					"--operands must be one of the types int or float.");
		
		this.type = expr1.type;
	}

	@Override
	public Expr gen() {
		try {
			return new Arith(env, op, expr1.reduce(), expr2.reduce());
		}
		catch (Exception exc) {
			System.err.println("Error in Arith.gen()");
			System.exit(-1);
			return null;
		}
	}

	@Override
	public String toString() {
		return expr1.toString() + " " + op.toString() + " " + expr2.toString();
	}
}
