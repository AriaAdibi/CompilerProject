package intermediateRepresentation;

import symbols.Type;

public class Assign extends Stmt{
	public Location location;
	public Expr expr;
	
	public Assign(Location location, Expr expr) throws ErrorSemantics{
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
		emit( location.toString() + " = " + expr.gen().toString() );
	}
}