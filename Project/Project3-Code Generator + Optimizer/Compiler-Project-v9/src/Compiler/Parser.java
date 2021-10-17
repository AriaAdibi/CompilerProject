package Compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

import intermediateRepresentation.ErrorSemantics;
import lexer.ErrorLexer;
import lexer.Lexer;
import parserSemanticsAST.ErrorParser;

public class Parser{
	private final parserSemanticsAST.Parser parser;
	
	public Parser(Lexer lexer){
		this.parser= new parserSemanticsAST.Parser(lexer);
	}

	public void Parse() throws IOException, ErrorLexer, ErrorParser, ErrorSemantics{
		this.parser.parse();
	}
	
	public static void main(String[] args){
		/**/
		if( args.length != 1 ){
			System.out.println("Wrong usage: input must be address of the source program");
			return;
		}
		
		String fileAddress= args[0];
		Lexer lexer= null;
		Parser parser= null;
		try{
			lexer= new Lexer(fileAddress);
		}catch (FileNotFoundException e){
			System.out.println("File not found.");
			e.printStackTrace();
			System.exit(1);
		}
		/**/
		
		parser= new Parser(lexer);
		boolean encounteredAProblem= false;
		do{
			encounteredAProblem= false;
			try{
				parser.Parse();
			}catch (IOException e){
				System.out.println("IOEXception");
				e.printStackTrace();
				System.exit(-1);
			}catch (ErrorLexer e){
				encounteredAProblem= true;
				System.out.println(e.getMessage());
			}catch (ErrorParser e){
				encounteredAProblem= true;
				System.out.println(e.getMessage());
				if( e.isRFailed ){
					System.out.println(e.getFailedRStatement());
					System.exit(-1);
				}
			} catch (ErrorSemantics e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(encounteredAProblem == true);
	}
}