package lexer;

import java.util.HashMap;
import java.util.Map;

import parserSemanticsAST.LLStackElem;

public class Tag extends LLStackElem{
	
	public final int code;
	public final String name;
	
	private static final Map<Integer, Tag> invertedCode= new HashMap<Integer, Tag>();
	
	public Tag(int code, String name){
		this.code= code;
		this.name= name;
		
		//Keywords
		invertedCode.put(256, BASIC_TYPE);
		invertedCode.put(257, BREAK);			invertedCode.put(258, CONTINUE);
		invertedCode.put(259, IF);				invertedCode.put(260, ELSE);
		invertedCode.put(261, TRUE);			invertedCode.put(262, FALSE);
		invertedCode.put(263, FOR);				invertedCode.put(264, WHILE);
		invertedCode.put(265, READER);
		invertedCode.put(266, WRITER);
		invertedCode.put(267, RETURN);
		invertedCode.put(268, VOID);
		
		//Compound operators
		invertedCode.put(269, EQ);		invertedCode.put(270, NEQ);
		invertedCode.put(271, GE);		invertedCode.put(272, LE);
		invertedCode.put(273, ANDAND);	invertedCode.put(274, OROR);
		
		//Literals
		invertedCode.put(275, INT_NUM);	invertedCode.put(276, REAL_NUM);
		invertedCode.put(277, CHAR_LITERAL);
		
		//ID
		invertedCode.put(278, ID);
		
		//Used in building syntax-tree and not in the lexer
		invertedCode.put(279, INDEX);
		invertedCode.put(280, UNARY_MINUS);
		invertedCode.put(281, TEMP);

		//EOF
		invertedCode.put((int)Lexer.EOF, EOF);
		invertedCode.put(-1, EPSILON);
	
		//Single character - Unpotentially compound
		invertedCode.put((int)'(', OPEN_PARENTHESIS);	invertedCode.put((int)')', CLOSE_PARENTHESIS);
		invertedCode.put((int)'{', OPEN_BRACE);			invertedCode.put((int)'}', CLOSE_BRACE);
		invertedCode.put((int)'[', OPEN_BRACKET);		invertedCode.put((int)']', CLOSE_BRACKET);
		invertedCode.put((int)';', SEMICOLON);			invertedCode.put((int)',', COMMA);
		invertedCode.put((int)'+', PLUS);				invertedCode.put((int)'-', MINUS);
		invertedCode.put((int)'*', MULTIPLICATION);		invertedCode.put((int)'%', MODULE);
		
		//Single character - Potentially compound
		invertedCode.put((int)'/', DIVISION);		invertedCode.put((int)'=', ASSIGNMENT);
		invertedCode.put((int)'!', NOT);
		invertedCode.put((int)'<', LESS_THAN);		invertedCode.put((int)'>', GREATER_THAN);
	}
	
	public static Tag getTagGivenCode(int theCode){
		return Tag.invertedCode.get(theCode);
	}
	public static final Tag
		//Keywords
		BASIC_TYPE= new Tag(256, "BASIC_TYPE"),	/*int, float, char, boolean*/ /*Grouped for parser*/	
		BREAK= new Tag(257, "BREAK"),			CONTINUE= new Tag(258, "CONTINUE"),		
		IF= new Tag(259, "IF"),					ELSE= new Tag(260, "ELSE"),
		TRUE= new Tag(261, "TRUE"),				FALSE= new Tag(262, "FALSE"),
		FOR= new Tag(263, "FOR"),				WHILE= new Tag(264, "WHILE"),
		READER= new Tag(265, "READER"),			/*readint, readfloat, readchar*/ /*Grouped for parser*/
		WRITER= new Tag(266, "WRITER"),			/*writeint, writefoat, writechar*/ /*Grouped for parser*/
		RETURN= new Tag(267, "RETURN"),		
		VOID= new Tag(268, "VOID"),
	
		//Compound operators
		EQ= new Tag(269, "EQ"),	NEQ= new Tag(270, "NEQ"),	
		GE= new Tag(271, "GE"),	LE= new Tag(272, "LE"),
		ANDAND= new Tag(273, "ANDAND"),	OROR= new Tag(274, "OROR"),
		
		//Literals
		INT_NUM= new Tag(275, "INT_NUM"),	REAL_NUM= new Tag(276, "REAL_NUM"),
		CHAR_LITERAL= new Tag(277, "CHAR_LITERAL"),		//BOOL	//don't need bool, we have Tag.TRUE & Tag.FALSE instead
														//Bool literals are considered to be a Word(keywords)
		
		//ID
		ID= new Tag(278, "ID"),
		
		//Used in building syntax-tree and used in IR generator
		INDEX= new Tag(279, "INDEX"), UNARY_MINUS= new Tag(280, "UNARY_MINUS"), TEMP= new Tag(281, "TEMP"),
	
		//EOF
		EOF= new Tag((int)Lexer.EOF, "EOF"),
		EPSILON= new Tag(-1, ""),
	
		//Single character - Unpotentially compound 
		OPEN_PARENTHESIS= new Tag( (int)'(', "(" ),	CLOSE_PARENTHESIS= new Tag( (int)')', ")" ),
		OPEN_BRACE= new Tag( (int)'{', "{" ),		CLOSE_BRACE= new Tag( (int)'}', "}" ),
		OPEN_BRACKET= new Tag( (int)'[', "[" ),		CLOSE_BRACKET= new Tag( (int)']', "]" ),
		SEMICOLON= new Tag( (int)';', ";" ),		COMMA= new Tag( (int)',', "," ),
		PLUS= new Tag( (int)'+', "+" ),				MINUS= new Tag( (int)'-', "-" ),
		MULTIPLICATION= new Tag( (int)'*', "*" ),	MODULE= new Tag( (int)'%', "%" ),
		
		//Single character - Potentially compound
		DIVISION= new Tag( (int)'/', "/" ),			ASSIGNMENT= new Tag( (int)'=', "=" ),
		NOT= new Tag( (int)'!', "!" ),				
		LESS_THAN= new Tag( (int)'<', "<" ),		GREATER_THAN= new Tag( (int)'>', ">" );
	
	@Override
	public String toString(){
		if( this.name == null )
			throw new RuntimeException("Invalid Tag-- neme= "+ this.name);
		else
			return this.name;		
	}
}