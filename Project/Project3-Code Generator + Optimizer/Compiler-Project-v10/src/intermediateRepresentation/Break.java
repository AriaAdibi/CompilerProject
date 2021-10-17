package intermediateRepresentation;

import symbols.Environment;

public class Break extends Stmt {

	Stmt enclosingStmt;

	public Break(Environment env) throws ErrorSemantics {
		super(env);
		
		if (Stmt.enclosing == Stmt.NULL)
			Node.errorHandler("--break statement is not enclosed by any loops");
		enclosingStmt = Stmt.enclosing;
	}

	@Override
	public void gen(int begin, int after) {
		emit("ENV" + env + ": " + "goto label " + enclosingStmt.afterLabel);
	}
}