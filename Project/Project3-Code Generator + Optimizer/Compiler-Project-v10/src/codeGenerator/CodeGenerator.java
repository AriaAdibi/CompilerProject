package codeGenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Lexer;
import lexer.Tag;
import lexer.Word;
import symbols.Array;
import symbols.Environment;
import symbols.SymTableInfo;
import symbols.Type;
import symbols.VarInfo;

public class CodeGenerator {

	private final static Pattern		arith		= Pattern
			.compile("[\\+\\*-/%]");

	private final int					NO_RETURN_EXCEPTION;
	private final int					ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION;

	private final String				BASE_POINTER;
	private final String				BASE_POINTER_CALC;
	private final String				RETURN_INDICATOR;
	private final String				CALC_TMP_INT;
	private final String				CALC_TMP_FLOAT;
	private final String				CALC_TMP_BOOLEAN;
	private final String				CALC_TMP_CHAR;
	private final String				CALC_TMP_LOCATION;
	private final String				CALC_TMP_INDEX;

	private Lexer						lexer;
	private Scanner						reader;
	private CodeWriter					writer;
	private HashMap<String, Integer>	varLocations;
	private HashMap<String, Integer>	codeLocations;
	private HashMap<String, Integer>	globalLocations;
	private String						currentProcedure;
	private String						currentEnv;
	private int							fromBase	= 0;

	public CodeGenerator(String fileName, String code, Lexer lexer)
			throws IOException {
		this.lexer = lexer;
		reader = new Scanner(code);
		writer = new CodeWriter(fileName);

		varLocations = new HashMap<String, Integer>();
		codeLocations = new HashMap<String, Integer>();
		globalLocations = new HashMap<String, Integer>();

		// writer.write("sp:= im_i_4");
		writer.write(String.format("jmp im_i_%d",
				"Method exited without returning a value.".length() + 3));
		NO_RETURN_EXCEPTION = gen_exit(
				"Method exited without returning a value.");

		writer.write(String.format("jmp im_i_%d",
				"Method exited without returning a value.".length() + 3
						+ "Array index out of bounds.".length() + 3));
		ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = gen_exit(
				"Array index out of bounds.");

		int cur = 0;
		Collection<SymTableInfo> globals = Environment.GLOBAL_ENV.table
				.values();
		for (SymTableInfo info : globals) {
			if (info instanceof VarInfo) {
				VarInfo global = (VarInfo) info;
				globalLocations.put(global.id.getLexeme(), cur);
				if (global.type instanceof Array) {
					// TODO check
					Array array = (Array) global.type;

					writer.write(String.format(":= gd_i_%d im_i_%d", cur,
							cur + 2 * Type.INT.width));
					cur += Type.INT.width;

					writer.write(String.format(":= gd_i_%d im_i_%d", cur,
							array.nElem * array.of.width));
					cur += Type.INT.width;

					cur += array.of.width * array.nElem;
				}
				else cur += global.type.width;
			}
		}

		BASE_POINTER = String.format("gd_i_%d", cur);
		cur += Type.INT.width;

		BASE_POINTER_CALC = String.format("gd_i_%d", cur);
		cur += Type.INT.width;

		RETURN_INDICATOR = String.format("gd_b_%d", cur);
		cur += Type.BOOLEAN.width;

		CALC_TMP_INT = String.format("gd_i_%d", cur);
		cur += Type.INT.width;

		CALC_TMP_FLOAT = String.format("gd_f_%d", cur);
		cur += Type.FLOAT.width;

		CALC_TMP_BOOLEAN = String.format("gd_b_%d", cur);
		cur += Type.BOOLEAN.width;

		CALC_TMP_CHAR = String.format("gd_c_%d", cur);
		cur += Type.CHAR.width;

		CALC_TMP_LOCATION = String.format("gd_i_%d", cur);
		cur += Type.INT.width;

		CALC_TMP_INDEX = String.format("gd_i_%d", cur);
		cur += Type.INT.width;
	}

	public void gen() throws IOException {
		String line;
		currentProcedure = "@GLOBAL";
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			convert(line);
		}

		reader.close();

		writer.replaceLabels(codeLocations);
		writer.print();
		writer.close();
	}

	private void convert(String line) {
		ThreeAddressCode code = new ThreeAddressCode(line);
		if (!code.env.equals("")) currentEnv = code.env;
		int tmp;
		if (code.getLabels() != null) {
			for (String label : code.getLabels()) {
				tmp = writer.write("");
				codeLocations.put(label, tmp);
			}
		}
		line = code.getCode();

		if (line.contains("call")) {
			// call
			if (!line.contains("=")) {
				// TODO void
			}
			else {
				// TODO not void
			}
		}
		else if (line.contains("param")) {
			// TODO set parameter for call
		}
		else if (line.contains("return")) {
			// TODO return
		}
		else if (line.contains("write")) {
			if (line.contains("writeint")) {
				writer.write(String.format("wi %s",
						getOperand(line.substring("writeint".length()))));
			}
			if (line.contains("writefloat")) {
				writer.write(String.format("wf %s",
						getOperand(line.substring("writefloat".length()))));
			}
			if (line.contains("writechar")) {
				writer.write(String.format("wc %s",
						getOperand(line.substring("writechar".length()))));
			}
		}
		else if (line.contains("read")) {
			switch (line) {
				case "readint":
					writer.write(String.format("ri %s", getOrCreateVariable(
							line.substring("readint".length()))));
					break;
				case "readfloat":
					writer.write(String.format("rf %s", getOrCreateVariable(
							line.substring("readfloat".length()))));
					break;
				case "readchar":
					writer.write(String.format("rc %s", getOrCreateVariable(
							line.substring("readchar".length()))));
					break;
			}
		}
		else if (line.contains("=") && !line.contains("if")) {
			// assignment
			int assign = line.indexOf('=');
			String LHS = getOrCreateVariable(line.substring(0, assign));
			boolean isInt = LHS.contains("_i_");
			String RHS;

			if (line.contains("-")
					&& line.substring(line.indexOf('=')).startsWith("=-")) {
				RHS = getOperand(line.substring(assign + 2));

				if (isInt) {
					writer.write(
							String.format("- %s im_i_0 %s", CALC_TMP_INT, RHS));
					writer.write(String.format(":= %s %s", LHS, CALC_TMP_INT));
				}
				else {
					writer.write(String.format("- %s im_f_0 %s", CALC_TMP_FLOAT,
							RHS));
					writer.write(
							String.format(":= %s %s", LHS, CALC_TMP_FLOAT));
				}
			}
			else if (line.contains("+") || line.contains("-")
					|| line.contains("*") || line.contains("/")
					|| line.contains("%")) {
				RHS = line.substring(assign + 1);

				// TODO check pattern
				Matcher matcher = arith.matcher(RHS);
				matcher.find();

				char op = matcher.group().charAt(0);
				int position = RHS.indexOf(op);

				String operand1 = getOperand(RHS.substring(0, position));
				String operand2 = getOperand(RHS.substring(position + 1));

				writer.write(String.format("%c %s %s %s", op, LHS, operand1,
						operand2));
			}
			else if (line.contains("!")
					&& line.substring(line.indexOf('=')).startsWith("=!")) {
				RHS = getOperand(line.substring(assign + 2));

				writer.write(String.format("! %s %s", CALC_TMP_BOOLEAN, RHS));
				writer.write(String.format(":= %s %s", LHS, CALC_TMP_BOOLEAN));
			}
			else if (line.contains("<") || line.contains("<=")
					|| line.contains(">") || line.contains(">=")
					|| line.contains("==") || line.contains("!=")
					|| line.contains("||") || line.contains("&&")) {
				RHS = line.substring(assign + 1);
				String[] operators = { "<=", "<", ">=", ">", "==", "!=", "||" };
				String op = "&&";
				for (String test : operators)
					if (RHS.contains(test)) {
						op = test;
						break;
					}

				int position = RHS.indexOf(op);
				int len = op.length();

				String operand1 = getOperand(RHS.substring(0, position));
				String operand2 = getOperand(RHS.substring(position + len));

				writer.write(String.format("%s %s %s %s", op, LHS, operand1,
						operand2));
			}
			else {
				RHS = getOperand(line.substring(assign + 1));
				writer.write(String.format(":= %s %s", LHS, RHS));
			}
		}
		else if (line.contains("goto")) {
			// jump
			String position = line
					.substring(line.indexOf("goto") + "goto".length());

			if (line.contains("if")) {
				// conditional
				if (line.contains("<") || line.contains("<=")
						|| line.contains(">") || line.contains(">=")
						|| line.contains("==") || line.contains("!=")
						|| line.contains("||") || line.contains("&&")) {
					if (line.contains("iffalse")) {
						String condition = line.substring(
								line.indexOf("iffalse") + "iffalse".length(),
								line.indexOf("goto"));
						String[] operators = { "<=", "<", ">=", ">", "==", "!=",
								"||" };
						String op = "&&";
						for (String test : operators)
							if (condition.contains(test)) {
								op = test;
								break;
							}

						int opPosition = condition.indexOf(op);
						int len = op.length();

						String operand1 = getOperand(
								condition.substring(0, opPosition));
						String operand2 = getOperand(
								condition.substring(opPosition + len));

						writer.write(String.format("%s %s %s %s", op,
								CALC_TMP_BOOLEAN, operand1, operand2));
						writer.write(String.format("! %s %s", CALC_TMP_BOOLEAN,
								CALC_TMP_BOOLEAN));
						writer.write(String.format("jt %s im_i_%s",
								CALC_TMP_BOOLEAN, position));
					}
					else {
						String condition = line.substring(
								line.indexOf("if") + "if".length(),
								line.indexOf("goto"));
						String[] operators = { "<=", "<", ">=", ">", "==", "!=",
								"||" };
						String op = "&&";
						for (String test : operators)
							if (condition.contains(test)) {
								op = test;
								break;
							}

						int opPosition = condition.indexOf(op);
						int len = op.length();

						String operand1 = getOperand(
								condition.substring(0, opPosition));
						String operand2 = getOperand(
								condition.substring(opPosition + len));

						writer.write(String.format("%s %s %s %s", op,
								CALC_TMP_BOOLEAN, operand1, operand2));
						writer.write(String.format("jt %s im_i_%s",
								CALC_TMP_BOOLEAN, position));
					}
				}
				else {
					if (line.contains("iffalse")) {
						String condition = getOperand(line.substring(
								"iffalse".length(), line.indexOf("goto")));
						writer.write(String.format("! %s %s", CALC_TMP_BOOLEAN,
								condition));
						writer.write(String.format("jt %s im_i_%s",
								CALC_TMP_BOOLEAN, position));
					}
					else {
						String condition = getOperand(line.substring(
								"if".length(), line.indexOf("goto")));
						writer.write(String.format("jt %s im_i_%s", condition,
								position));
					}
				}
			}
			else {
				// unconditional
				writer.write(String.format("jmp im_i_%s", position));
			}
		}
		else if (line.contains("PROC")) {
			// procedure
			if (line.contains("END")) {
				// end procedure
				currentProcedure = "@GLOBAL";
				writer.write("");
			}
			else {
				// start procedure
				currentProcedure = line.substring(line.indexOf('_') + 1,
						line.length() - 1);
				tmp = writer.write("");
				codeLocations.put(currentProcedure, tmp);
			}
		}
	}

	private String getOperand(String code) {
		// TODO check
		if (code.equals("true")) return "im_b_1";
		if (code.equals("false")) return "im_b_0";

		try {
			int tmp = Integer.decode(code);
			return String.format("im_i_%d", tmp);
		}
		catch (Exception exc1) {
			try {
				float tmp = Float.parseFloat(code);
				return String.format("im_f_%s", tmp);
			}
			catch (Exception exc2) {
				if (code.startsWith("'") && code.endsWith("'")) {
					code = code.substring(1, code.length() - 1);
					code = code.replace("\\n", "\n");
					code = code.replace("\\t", "\t");
					code = code.replace("\\\\", "\\");
					code = code.replace("\\'", "\'");
					return String.format("im_c_%d", (int) code.charAt(0));
				}

				return getVariable(code);
			}
		}
	}

	private String getVariable(String code) {
		if (!code.contains("[")) {
			// location
			Word aWord = lexer.words.get(code);
			if (varLocations.containsKey(code)) {
				// local
				int position = varLocations.get(code);
				Environment env = Environment.all.get(currentEnv);
				VarInfo info = (VarInfo) env.get(new Word(code, Tag.TEMP));
				return String.format("ld_%c_%d",
						MachineCodeTypes.getMachineCodeType(info.type),
						position);
			}
			else {
				// [MUST BE] global
				VarInfo info = (VarInfo) Environment.GLOBAL_ENV.get(aWord);
				return String.format("gd_%c_%d",
						MachineCodeTypes.getMachineCodeType(info.type),
						globalLocations.get(code));
			}
		}
		else {
			// array
			String index = getOperand(
					code.substring(code.indexOf('[') + 1, code.indexOf(']')));
			code = code.substring(0, code.indexOf('['));
			Word aWord = lexer.words.get(code);
			if (varLocations.containsKey(code)) {
				// local
				return null;
			}
			else {
				// [MUST BE] global
				int position = globalLocations.get(code);
				VarInfo info = (VarInfo) Environment.GLOBAL_ENV.get(aWord);

				writer.write(String.format("+ %s %s im_i_%d", CALC_TMP_INDEX,
						index, 8));

				writer.write(String.format("+ %s %s im_i_%d", CALC_TMP_INDEX,
						CALC_TMP_INDEX, position));
				
				writer.write(String.format("+ %s gd_i_%d im_i_%d", CALC_TMP_INT, position + 4, 4));

				writer.write(String.format("> %s %s %s", CALC_TMP_BOOLEAN,
						CALC_TMP_INDEX, CALC_TMP_INT));
				
				outOfBounds();

				return String.format("gi_%c_%s",
						MachineCodeTypes
								.getMachineCodeType(((Array) info.type).of),
						CALC_TMP_INDEX.substring(
								CALC_TMP_INDEX.lastIndexOf('_') + 1));
			}
		}
	}

	private String getOrCreateVariable(String code) {
		String name = code;
		if (code.contains("[")) name = name.substring(0, name.indexOf('['));
		
		if (globalLocations.containsKey(name)) return getVariable(code);

		if (varLocations.containsKey(name)) return getVariable(code);

		Environment env = Environment.all.get(currentEnv);
		VarInfo info = (VarInfo) env.get(new Word(code, Tag.TEMP));
		fromBase -= info.type.width;
		varLocations.put(code, fromBase);
		return getVariable(code);
	}

	private void call() {
		// TODO
	}

	private void outOfBounds() {
		writer.write(String.format("jt %s im_i_%d", CALC_TMP_BOOLEAN,
				ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION));
	}

	private void noReturn() {
		writer.write(String.format("jmp im_i_%d", NO_RETURN_EXCEPTION));
	}

	private int gen_exit(String message) {
		int ret = -1;
		int tmp;
		for (char c : message.toCharArray()) {
			tmp = writer.write(String.format("wc im_c_%d", (int) c));
			if (ret == -1) ret = tmp;
		}
		writer.write("wc im_c_10");
		writer.write(String.format("jmp im_i_%d", Integer.MAX_VALUE));

		return ret;
	}

}
