package intermediateRepresentation;

import lexer.Token;
import lexer.Word;
import symbols.Environment;
import symbols.Type;

public class Constant extends Expr{

	public Constant(Environment env, Token op, Type type) {
		super(env, op, type);
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
	public void jumping(int t, int f){
		if(this == Constant.TRUE && t != 0)
			emit("goto label " + t);
		else if (this == Constant.FALSE && f != 0)
			emit("goto label " + f);
	}
	
	public static final Constant
		TRUE = new Constant(Environment.GLOBAL_ENV, Word.TRUE, Type.BOOLEAN),
		FALSE = new Constant(Environment.GLOBAL_ENV, Word.FALSE, Type.BOOLEAN);
}