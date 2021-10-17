package intermediateRepresentation;

import lexer.Token;
import symbols.Environment;
import symbols.Type;

public abstract class Expr extends Node{
	
	public Token op;
	public Type type;
	
	public Expr(Environment env, Token op, Type type){
		super(env);
		
		this.op= op;
		this.type= type;
	}
	
	/*	returns the term on the right
	*		for example for E= P + Q --> returns x + y where x and y 
	*		are addresses of P and Q, respectively.
	*/
	abstract public Expr gen();
	
	/*	reduce to single address(constant, identifier, temporary name)
	*/
	abstract public Expr reduce();
	
	public void jumping(int t, int f){
		emitJumps(toString(), t, f);
	}
	
	//0 is our convention for no jump
	public void emitJumps(String theTest, int t, int f){
		if(t != 0 && f != 0) {
			emit("if "+ theTest+ " goto label "+ t);
			emit("goto label "+ f);
		}
		else if(t != 0)
			emit("if "+ theTest+ " goto label "+ t);
		else if(f != 0)
			emit("iffalse "+ theTest+ " goto label "+ f);
		else ; // nothing since both t and f fall through
	}
	
	@Override
	public String toString(){
		return op.toString();
	}
}	