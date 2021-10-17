package intermediateRepresentation;

import symbols.Environment;

public class SeqStmt extends Stmt {

	Stmt	stmt1;
	Stmt	stmt2;

	public SeqStmt(Environment env, Stmt s1, Stmt s2) {
		super(env);
		
		stmt1 = s1;
		stmt2 = s2;
	}

	public void gen(int b, int a) {
		if (stmt1 == Stmt.NULL)
			stmt2.gen(b, a);
		else if (stmt2 == Stmt.NULL)
			stmt1.gen(b, a);
		else {
			int label = Node.newLabel();
			stmt1.gen(b, label);
			Node.emitLabel(label);
			stmt2.gen(label, a);
		}
	}
}