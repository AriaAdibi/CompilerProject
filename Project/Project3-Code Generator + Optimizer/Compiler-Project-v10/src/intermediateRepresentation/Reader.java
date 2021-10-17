package intermediateRepresentation;

import lexer.Word;
import symbols.Environment;
import symbols.Type;

public class Reader extends Stmt {

	Expr	locationOrAccess;
	Word	readerWord;

	public Reader(Environment env, Word readerWord, Expr locationOrAccess) throws ErrorSemantics {
		super(env);
		
		Type readerType = null;
		switch (readerWord.getLexeme()) {
			case "readchar":
				readerType = Type.CHAR;
				break;
			case "readint":
				readerType = Type.INT;
				break;
			case "readfloat":
				readerType = Type.FLOAT;
				break;
			default:
				System.err.println("reader name is wrong!");
				System.exit(-1);
		}

		if (!readerType.equals(locationOrAccess.type))
			Node.errorHandler("--Type of location for "
					+ readerWord.getLexeme() + "must be " + readerType);

		this.locationOrAccess = locationOrAccess;
		this.readerWord = readerWord;
	}

	@Override
	public void gen(int begin, int after) {
		//TODO
		Node.emit("ENV" + env + ": " + this.readerWord.getLexeme() + " " + this.locationOrAccess.toString());
	}
}