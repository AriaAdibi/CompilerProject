package intermediateRepresentation;

import symbols.Environment;

public class Stmt extends Node{
	public Stmt(Environment env){
		super(env);
	}
	
	public void gen(int begin, int after){}	// called with labels begin and after
	
	public final static Stmt NULL = new Stmt(Environment.GLOBAL_ENV);
	
	protected int continiueLabel= 0; // saves label begin
	protected int afterLabel= 0; // saves label after
	public static Stmt enclosing= Stmt.NULL;	// used for break stmts
}