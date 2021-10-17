package intermediateRepresentation;

import lexer.Token;
import lexer.Word;
import symbols.Type;

public class Constant extends Expr{

	public Constant(Token op, Type type) {
		super(op, type);
	}
	
	@Override
	public Expr gen(){
		return this;
	}

	@Override
	public Expr reduce(){
		return this;
	}
	
	@Override
	public void jumping(int trueLabel, int falseLabel){
		if(this == Constant.TRUE && trueLabel != 0)
			emit("goto label " + trueLabel);
		else if (this == Constant.FALSE && falseLabel != 0)
			emit("goto label " + falseLabel);
	}
	
	public static final Constant
		TRUE = new Constant(Word.TRUE, Type.BOOLEAN),
		FALSE = new Constant(Word.FALSE, Type.BOOLEAN);
}