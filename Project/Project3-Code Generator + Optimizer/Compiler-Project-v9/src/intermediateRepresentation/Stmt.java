package intermediateRepresentation;

public class Stmt extends Node{
	public Stmt(){
	}
	
	public void gen(int beginLabel, int afterLabel){}	// called with labels begin and after
	
	public final static Stmt NULL = new Stmt();
	
	protected int continiueLabel= 0; // saves label begin
	protected int afterLabel= 0; // saves label after
	public static Stmt enclosing= Stmt.NULL;	// used for break stmts
}