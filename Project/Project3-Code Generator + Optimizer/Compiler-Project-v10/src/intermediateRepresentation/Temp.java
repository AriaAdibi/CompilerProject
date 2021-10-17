package intermediateRepresentation;

import lexer.Tag;
import lexer.Word;
import symbols.Environment;
import symbols.Type;
import symbols.VarInfo;

public class Temp extends Expr {

	private static int	count	= 0;
	private int			number	= 0;

	public Temp(Environment env, Type type) {
		super(env, Word.TEMP, type);
		number = ++count;
		
		Word w = new Word(toString(), Tag.TEMP);
		VarInfo info = new VarInfo(w, type, 0, false);
		env.put(w, info);
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