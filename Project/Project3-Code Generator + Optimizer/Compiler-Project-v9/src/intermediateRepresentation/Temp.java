package intermediateRepresentation;

import lexer.Word;
import symbols.Type;

public class Temp extends Expr {

	private static int	count	= 0;
	private int			number	= 0;

	public Temp(Type type) {
		super(Word.TEMP, type);
		number = ++count;
	}

	@Override
	public String toString() {
		return "t" + number;
	}

	@Override
	public Expr gen() {
		return this;
	}
	
	@Override
	public Expr reduce() {
		return this;
	}
}