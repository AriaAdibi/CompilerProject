package intermediateRepresentation;

public class Continue extends Stmt {

	Stmt enclosingStmt;

	public Continue() throws ErrorSemantics {
		if (Stmt.enclosing == Stmt.NULL)
			Node.errorHandler("--continue statement is not enclosed by any loops");
		enclosingStmt = Stmt.enclosing;
	}

	@Override
	public void gen(int begin, int after) {
		emit("goto label " + enclosingStmt.continiueLabel);
	}
}