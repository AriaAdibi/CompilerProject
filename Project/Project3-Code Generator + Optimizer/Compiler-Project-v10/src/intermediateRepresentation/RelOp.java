package intermediateRepresentation;

import lexer.Token;
import symbols.Array;
import symbols.Environment;
import symbols.Type;

public class RelOp extends LogicalOp {

	public RelOp(Environment env, Token tok, Expr expr1, Expr expr2) throws ErrorSemantics {
		super(env, tok, expr1, expr2);
	}

	@Override
	public Type check(Type p1, Type p2) {
		if (p1 instanceof Array || p2 instanceof Array)
			return null;
		else if (p1 == p2 && (p2.equals(Type.INT) || p2.equals(Type.FLOAT) || p2.equals(Type.CHAR)))
			return Type.BOOLEAN;
		else
			return null;
	}

	@Override
	public void jumping(int t, int f) {
		Expr a = expr1.reduce();
		Expr b = expr2.reduce();

		String test = a.toString() + " " + op.toString() + " " + b.toString();
		emitJumps(test, t, f);
	}
}