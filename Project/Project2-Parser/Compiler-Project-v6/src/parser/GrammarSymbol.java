package parser;

import lexer.Tag;

public class GrammarSymbol{
	@Override
	public String toString(){
		if( this instanceof Variable)
			return ((Variable)this).toString();
		else if(this instanceof Tag)
			return ((Tag)this).toString();
		else
			throw new RuntimeException("GrammarSymbol in neither Variable or Tag");
	}
}