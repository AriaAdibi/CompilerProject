package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;

public class Not extends LogicalOp {

	public Not(Environment env, Token tok, Expr x2) throws ErrorSemantics {
		//TODO
		super(env, tok, x2, x2);
	}

	@Override
	public void jumping(int t, int f) {
		expr2.jumping(f, t);
	}

	@Override
	public String toString() {
		return op.toString() + " " + expr2.toString();
	}
}