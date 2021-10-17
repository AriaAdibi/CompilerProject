package intermediateRepresentation;

import symbols.Environment;

public class Continue extends Stmt {

	Stmt enclosingStmt;

	public Continue(Environment env) throws ErrorSemantics {
		super(env);
		
		if (Stmt.enclosing == Stmt.NULL)
			Node.errorHandler("--continue statement is not enclosed by any loops");
		enclosingStmt = Stmt.enclosing;
	}

	@Override
	public void gen(int begin, int after) {
		emit("ENV" + env + ": " + "goto label " + enclosingStmt.continiueLabel);
	}
}