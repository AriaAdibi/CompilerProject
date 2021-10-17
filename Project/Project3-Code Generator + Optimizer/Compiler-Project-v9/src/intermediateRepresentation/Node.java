package intermediateRepresentation;

import lexer.Lexer;

public class Node{
	private static int label= 0;
	
	private static StringBuilder ir= new StringBuilder();
	
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
	
	public static final Program prog = new Program();
	
	public static String getIR() {
		return ir.toString();
	}
}