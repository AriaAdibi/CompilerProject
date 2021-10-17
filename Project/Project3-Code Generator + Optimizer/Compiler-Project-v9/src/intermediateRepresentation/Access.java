package intermediateRepresentation;

import lexer.Tag;
import lexer.Word;
import symbols.Type;

public class Access extends Op {

	public Location	array;
	public Expr		index;

	public Access(Location array, Expr index, Type type) {
		super(new Word("[]", Tag.INDEX), type);
		this.array = array;
		this.index = index;
	}

	@Override
	public Expr gen() {
		return new Access(array, index.reduce(), type);
	}

	@Override
	public void jumping(int trueLabel, int falseLabel) {
		emitJumps(reduce().toString(), trueLabel, falseLabel);
	}

	@Override
	public String toString() {
		return array.toString() + " [ " + index.reduce() + " ]";
	}
}