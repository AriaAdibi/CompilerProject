package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word{
	public final int width;
	
	public Type(String lexeme, int tag, int width){
		super(lexeme, tag);
		this.width= width;
	}
	
	public static final Type
		Int= new Type("int", Tag.BASIC, 4),
		Float= new Type("float", Tag.BASIC, 8),
		Char= new Type("char", Tag.BASIC, 1),
		Bool= new Type("boolean", Tag.BASIC, 1);
	
	public static boolean isNumeric(Type p){
		if(p==Type.Char || p==Type.Int || p==Type.Float)
			return true;
		return false;
	}
	
	public static Type maxType(Type p0, Type p1){
		if( !isNumeric(p0) || !isNumeric(p1) )
			return null;
		else if(p0==Type.Float || p1==Type.Float)
			return Type.Float;
		else if(p0==Type.Int || p1==Type.Int)
			return Type.Int;
		else
			return Type.Char;
	}
}