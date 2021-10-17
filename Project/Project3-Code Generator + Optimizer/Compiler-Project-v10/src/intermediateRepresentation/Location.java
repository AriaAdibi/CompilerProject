package intermediateRepresentation;

import lexer.Word;
import symbols.Environment;
import symbols.Type;

public class Location extends Expr{
	public int offset; // relative address
	
	public Location(Environment env, Word id, Type type, int offset){
		super(env, id, type);
		this.offset = offset;
	}

	@Override
	public Expr gen(){
		return this;
	}

	@Override
	public Expr reduce(){
		return this;
	}
	
}