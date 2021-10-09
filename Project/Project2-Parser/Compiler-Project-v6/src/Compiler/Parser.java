package Compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexer.ErrorLexer;
import parser.ErrorParser;

public class Parser{
	private final parser.Parser parser;
	
	public Parser(){
		this.parser= new parser.Parser();
	}

	public Parser( String fileAddress ) throws FileNotFoundException{
		this.parser= new parser.Parser(fileAddress);
	}
	
	public void Parse() throws IOException, ErrorLexer, ErrorParser{
		this.parser.parse();
	}
	
	public static void main(String[] args){
		/**/
		if( args.length != 1 ){
			System.out.println("Wrong usage: input must be address of the source program");
			return;
		}
		
		String fileAddress= args[0];
		Parser parser= null;
		try{
			parser= new Parser(fileAddress);
		}catch (FileNotFoundException e){
			System.out.println("File not found.");
			e.printStackTrace();
			System.exit(1);
		}
		/**/
		
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
			}
		}while(encounteredAProblem == true);
	}
}