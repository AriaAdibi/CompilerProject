package intermediateRepresentation;

import lexer.Token;

public class Not extends LogicalOp {

	public Not(Token tok, Expr x2) throws ErrorSemantics {
		//TODO
		super(tok, x2, x2);
	}

	@Override
	public void jumping(int tueLabel, int falseLabel) {
		expr2.jumping(falseLabel, tueLabel);
	}

	@Override
	public String toString() {
		return op.toString() + " " + expr2.toString();
	}
}