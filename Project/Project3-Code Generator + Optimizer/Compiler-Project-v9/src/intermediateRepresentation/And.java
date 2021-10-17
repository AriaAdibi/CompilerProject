package intermediateRepresentation;

import lexer.Token;

public class And extends LogicalOp {

	public And(Token tok, Expr x1, Expr x2) throws ErrorSemantics {
		super(tok, x1, x2);
	}

	@Override
	public void jumping(int trueLable, int falseLable) {
		int falseResLabel = falseLable != 0 ? falseLable : Node.newLabel();
		
		expr1.jumping(0, falseResLabel);
		expr2.jumping(trueLable, falseLable);
		
		if (falseLable == 0)
			Node.emitLabel(falseResLabel);
	}
}