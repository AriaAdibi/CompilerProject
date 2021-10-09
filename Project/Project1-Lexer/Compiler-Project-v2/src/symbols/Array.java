package symbols;

import lexer.Tag;

//Not used in the lexer
public class Array extends Type{
	public final Type of;	//Array *of* type
	public final int nElem;
	
	public Array(Type of, int nElem){
		super("[]", Tag.INDEX, nElem* of.width);
		this.of= of;
		this.nElem= nElem;
	}
	
	@Override
	public String toString(){
		return "["+ this.nElem+ "] "+ of.toString(); 
	}
}