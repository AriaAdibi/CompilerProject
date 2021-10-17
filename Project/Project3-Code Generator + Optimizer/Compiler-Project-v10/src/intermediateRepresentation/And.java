package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;

public class And extends LogicalOp {

	public And(Environment env, Token tok, Expr x1, Expr x2) throws ErrorSemantics {
		super(env, tok, x1, x2);
	}

	@Override
	public void jumping(int t, int f) {
		int label = f != 0 ? f : Node.newLabel();
		
		expr1.jumping(0, label);
		expr2.jumping(t, f);
		
		if (f == 0)
			Node.emitLabel(label);
	}
}