package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;
import symbols.Type;

public abstract class Op extends Expr {

	public Op(Environment env, Token tok, Type p) {
		super(env, tok, p);
	}

	public Expr reduce() {
		Expr x = gen();
		Temp t = new Temp(env, type);
		emit("ENV" + env + ": " + t.toString() + " = " + x.toString());
		return t;
	}
}