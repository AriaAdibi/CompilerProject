package intermediateRepresentation;

import lexer.Word;
import symbols.Environment;
import symbols.Type;

public class Writer extends Stmt{
	Expr expr;
	Word writerWord;
	
	public Writer(Environment env, Word writerWord, Expr expr ) throws ErrorSemantics{
		super(env);
		
		Type writerType= null;
		switch( writerWord.getLexeme() ){
			case "writechar":
				writerType= Type.CHAR;
				break;
			case "writeint":
				writerType= Type.INT;
				break;
			case "writefloat":
				writerType= Type.FLOAT;
				break;
			default:
				System.err.println("writer name is wrong!");
				System.exit(-1);
		}
		
		if( !writerType.equals(expr.type) )
			Node.errorHandler("--Type of expression for "+ writerWord.getLexeme()+
							"must be "+ writerType);
		
		this.expr= expr;
		this.writerWord = writerWord;
	}
	
	@Override
	public void gen(int begin, int after){
		Node.emit("ENV" + env + ": " + this.writerWord.getLexeme()+ " "+ this.expr.reduce());
	}
}