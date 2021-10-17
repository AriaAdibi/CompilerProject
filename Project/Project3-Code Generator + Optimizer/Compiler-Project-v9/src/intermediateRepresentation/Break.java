package intermediateRepresentation;

public class Break extends Stmt {

	Stmt enclosingStmt;

	public Break() throws ErrorSemantics {
		if (Stmt.enclosing == Stmt.NULL)
			Node.errorHandler("--break statement is not enclosed by any loops");
		enclosingStmt = Stmt.enclosing;
	}

	@Override
	public void gen(int begin, int after) {
		emit("goto label " + enclosingStmt.afterLabel);
	}
}