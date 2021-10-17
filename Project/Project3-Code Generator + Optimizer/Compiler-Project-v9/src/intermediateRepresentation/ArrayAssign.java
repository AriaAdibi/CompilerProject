package intermediateRepresentation;

import symbols.Array;
import symbols.Type;

public class ArrayAssign extends Stmt {

	public Location	array;
	public Expr	index;
	public Expr	expr;

	public ArrayAssign(Access access, Expr expr) throws ErrorSemantics {
		array = access.array;
		index = access.index;
		this.expr = expr;
		if (check(access.type, expr.type) == null) {
			/**/
			System.out.println(access.type);
			System.out.println(expr.type);
			/**/
			Node.errorHandler("--lType and rType are not the same type in assignment.");
		}
	}

	public Type check(Type type1, Type type2) {
		if (type1 instanceof Array || type2 instanceof Array)
			return null;
		else if (type1.equals(type2))
			return type2;
		else
			return null;
	}

	@Override
	public void gen(int b, int a) {
		String s1 = index.reduce().toString();
		String s2 = expr.reduce().toString();
		emit(array.toString() + " [ " + s1 + " ] = " + s2);
	}
}