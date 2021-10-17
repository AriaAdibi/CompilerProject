package symbols;

import lexer.Word;

public class VarInfo extends SymTableInfo {

	public int	offset;	// relative address
	public Word	id;
	public Type	type;
	public boolean isGlobal;

	public VarInfo(Word id, Type type, int offset, boolean isGlobal) {
		this.id = id;
		this.type = type;
		this.offset = offset;
		this.isGlobal = isGlobal;
	}
	
	@Override
	public String toString() {
		//TODO
		return id + ": " + type + "(" + isGlobal + ")";
	}

}