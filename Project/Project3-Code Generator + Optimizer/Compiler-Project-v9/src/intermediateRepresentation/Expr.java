package intermediateRepresentation;

import lexer.Token;
import symbols.Type;

public abstract class Expr extends Node{
	
	public Token op;
	public Type type;
	
	public Expr(Token op, Type type){
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
	
	public void jumping(int trueLabel, int falseLabel){
		emitJumps(toString(), trueLabel, falseLabel);
	}
	
	//0 is our convention for no jump
	public void emitJumps(String theTest, int trueLable, int falseLabel){
		if(trueLable != 0 && falseLabel != 0) {
			emit("if "+ theTest+ " goto label "+ trueLable);
			emit("goto label "+ falseLabel);
		}
		else if(trueLable != 0)
			emit("if "+ theTest+ " goto label "+ trueLable);
		else if(falseLabel != 0)
			emit("iffalse "+ theTest+ " goto label "+ falseLabel);
		else ; // nothing since both t and f fall through
	}
	
	@Override
	public String toString(){
		return op.toString();
	}
}	