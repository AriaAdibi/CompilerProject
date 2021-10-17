package intermediateRepresentation;

import lexer.Lexer;
import symbols.Environment;

public class Node{
	private static int label= 0;
	
	protected static StringBuilder ir= new StringBuilder();
	
	public static int newLabel(){
		return ++label;
	}
	
	protected static void emitLabel(int i){
		ir.append("label "+ i+ ":");
	}
	
	protected static void emit(String str){
		ir.append("\t\t"+ str+ "\n");
	}
	
	public static void errorHandler(String theError) throws ErrorSemantics{
		throw new ErrorSemantics(Lexer.getTokenLocation() + theError);
	}
	
	public static final Program prog = new Program(Environment.GLOBAL_ENV);
	
	//TODO
	public static String getIR() {
		return ir.toString();
	}
	//////////////////////
	protected Environment env;
	public Node(Environment env){
		this.env = env;
	}
	
}