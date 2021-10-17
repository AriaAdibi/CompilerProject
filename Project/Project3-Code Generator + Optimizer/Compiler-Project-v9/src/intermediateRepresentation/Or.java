package intermediateRepresentation;

import lexer.Token;

public class Or extends LogicalOp {

	public Or(Token tok, Expr x1, Expr x2) throws ErrorSemantics {
		super(tok, x1, x2);
	}

	@Override
	public void jumping(int trueLabel, int falseLabel) {
		int trueResLabel = trueLabel != 0 ? trueLabel : Node.newLabel();
		
		expr1.jumping(trueResLabel, 0);
		expr2.jumping(trueLabel, falseLabel);
		
		if (trueLabel == 0)
			Node.emitLabel(trueResLabel);
	}
}