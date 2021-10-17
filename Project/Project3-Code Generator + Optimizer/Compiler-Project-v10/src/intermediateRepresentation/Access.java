package intermediateRepresentation;

import lexer.Tag;
import lexer.Word;
import symbols.Environment;
import symbols.Type;

public class Access extends Op {

	public Location	array;
	public Expr		index;

	public Access(Environment env, Location array, Expr index, Type type) {
		super(env, new Word("[]", Tag.INDEX), type);
		this.array = array;
		this.index = index;
	}

	@Override
	public Expr gen() {
		return new Access(env, array, index.reduce(), type);
	}

	@Override
	public void jumping(int t, int f) {
		emitJumps(reduce().toString(), t, f);
	}

	@Override
	public String toString() {
		return array.toString() + " [ " + index.reduce() + " ]";
	}
}