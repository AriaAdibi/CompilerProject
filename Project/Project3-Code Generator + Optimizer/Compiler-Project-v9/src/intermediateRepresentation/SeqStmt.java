package intermediateRepresentation;

public class SeqStmt extends Stmt {

	Stmt	stmt1;
	Stmt	stmt2;

	public SeqStmt(Stmt s1, Stmt s2) {
		stmt1 = s1;
		stmt2 = s2;
	}

	public void gen(int beginLabel, int afterLabel){
		if (stmt1 == Stmt.NULL)
			stmt2.gen(beginLabel, afterLabel);
		else if (stmt2 == Stmt.NULL)
			stmt1.gen(beginLabel, afterLabel);
		else {
			int afterStmt1Label = Node.newLabel();
			stmt1.gen(beginLabel, afterStmt1Label);
			Node.emitLabel(afterStmt1Label);
			stmt2.gen(afterStmt1Label, afterLabel);
		}
	}
}