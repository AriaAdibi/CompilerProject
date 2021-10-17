package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;

public class Or extends LogicalOp {

	public Or(Environment env, Token tok, Expr x1, Expr x2) throws ErrorSemantics {
		super(env, tok, x1, x2);
	}

	@Override
	public void jumping(int t, int f) {
		int label = t != 0 ? t : Node.newLabel();
		
		expr1.jumping(label, 0);
		expr2.jumping(t, f);
		
		if (t == 0)
			Node.emitLabel(label);
	}
}