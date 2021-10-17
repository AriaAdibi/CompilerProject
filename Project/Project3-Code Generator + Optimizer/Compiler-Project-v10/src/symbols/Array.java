package symbols;

import lexer.Tag;

public class Array extends Type{
	public final Type of;	//Array *of* type
	public final int nElem;
	
	public Array(Type of, int nElem){
		super("[]", Tag.INDEX, nElem* of.width);
		this.of= of;
		this.nElem= nElem;
	}
	
	@Override
	public boolean equals(Object obj) {
		return of.equals(obj);
	}
	
	@Override
	public String toString(){
		return of.toString()+ "["+ this.nElem+ "] "; 
	}
}