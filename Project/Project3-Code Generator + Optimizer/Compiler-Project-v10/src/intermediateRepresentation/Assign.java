package intermediateRepresentation;

import symbols.Environment;
import symbols.Type;

public class Assign extends Stmt{
	public Location location;
	public Expr expr;
	
	public Assign(Environment env, Location location, Expr expr) throws ErrorSemantics{
		super(env);
		
		this.location = location;
		this.expr = expr;
		if( !Assign.areSameTypes(location.type, expr.type) )
			Node.errorHandler("--lType and rType are not the same type in assignment.");
	}
	
	public static boolean areSameTypes(Type lType, Type rType){
		if( lType.equals(rType) )
			return true;
		else
			return false;
	}
	
	public void gen(int begin, int after){
		emit("ENV" + env + ": " + location.toString() + "= " + expr.gen().toString() );
	}
}