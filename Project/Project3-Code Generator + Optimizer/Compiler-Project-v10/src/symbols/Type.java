package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word{
	public final int width;
	
	public Type(String lexeme, Tag tag, int width){
		super(lexeme, tag);
		this.width= width;
	}
	
	public static final Type
		INT= new Type("int", Tag.BASIC_TYPE, 4),
		FLOAT= new Type("float", Tag.BASIC_TYPE, 8),
		CHAR= new Type("char", Tag.BASIC_TYPE, 1),
		BOOLEAN= new Type("boolean", Tag.BASIC_TYPE, 1);
	
	public static boolean isNumeric(Type p){
		if(p==Type.CHAR || p==Type.INT || p==Type.FLOAT)
			return true;
		return false;
	}
	
	public static Type maxType(Type p0, Type p1){
		if( !isNumeric(p0) || !isNumeric(p1) )
			return null;
		else if(p0==Type.FLOAT || p1==Type.FLOAT)
			return Type.FLOAT;
		else if(p0==Type.INT || p1==Type.INT)
			return Type.INT;
		else
			return Type.CHAR;
	}
	
}