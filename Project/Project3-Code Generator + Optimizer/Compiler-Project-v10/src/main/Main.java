package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import codeGenerator.CodeGenerator;
import intermediateRepresentation.ErrorSemantics;
import intermediateRepresentation.Node;
import lexer.ErrorLexer;
import lexer.Lexer;
import parserSemanticsAST.ErrorParser;
import parserSemanticsAST.Parser;

public class Main {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JFileChooser jFileChooser = new JFileChooser();
		int ans = jFileChooser.showOpenDialog(null);
		if (ans == JFileChooser.APPROVE_OPTION) {
			args = new String[] {
					jFileChooser.getSelectedFile().getAbsolutePath() };
		}
		else {
			System.err.println("Please choose a file to compile.");
			System.exit(1);
		}

		JFileChooser jFileChooser2 = new JFileChooser();
		ans = jFileChooser2.showSaveDialog(null);
		if (ans == JFileChooser.APPROVE_OPTION) {
			String save = jFileChooser2.getSelectedFile().getAbsolutePath();
			if (args.length != 1) {
				System.out.println(
						"Wrong usage: input must be address of the source program");
				return;
			}

			String fileAddress = args[0];
			Lexer lexer = null;
			Parser parser = null;
			try {
				lexer = new Lexer(fileAddress);
			}
			catch (FileNotFoundException e) {
				System.out.println("File not found.");
				e.printStackTrace();
				System.exit(1);
			}

			parser = new Parser(lexer);
			boolean encounteredAProblem = false;
			do {
				encounteredAProblem = false;
				try {
					parser.parse();
				}
				catch (IOException e) {
					System.out.println("IOEXception");
					e.printStackTrace();
					System.exit(-1);
				}
				catch (ErrorLexer e) {
					encounteredAProblem = false; // TODO
					System.out.println(e.getMessage());
				}
				catch (ErrorParser e) {
					encounteredAProblem = false; // TODO
					System.out.println(e.getMessage());
					if (e.isRFailed) {
						System.out.println(e.getFailedRStatement());
						System.exit(-1);
					}
				}
				catch (ErrorSemantics e) {
					System.out.println(e.getMessage());
				}
			} while (encounteredAProblem == true);

			Node.prog.gen();

			CodeGenerator gen = new CodeGenerator(
					save, Node.getIR(),
					lexer);
			gen.gen();

			VM.VM.main(new String[] { save });
		}
		else {
			System.err.println("Please choose a path to save compiled file.");
			System.exit(1);
		}
	}
}