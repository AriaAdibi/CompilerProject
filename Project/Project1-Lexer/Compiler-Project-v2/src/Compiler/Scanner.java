package Compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexer.ErrorLexer;
import lexer.Lexer;
import lexer.Token;

public class Scanner{
	public String CV= null;
	
	private final Lexer lexer;
	
	public Scanner(){
		this.lexer= new Lexer();
	}
	
	public Scanner(String fileAddress) throws FileNotFoundException{
		this.lexer= new Lexer(fileAddress);
	}
	
	public String NextToken() throws IOException, ErrorLexer{
		Token tok= this.lexer.nextToken();
		this.CV= tok.getLexeme();
		return tok.toString();
	}
	
	public static void main(String[] args){
		/**/
		if( args.length != 1 ){
			System.out.println("Wrong usage: input must be address of the source program");
			return;
		}
		
		String fileAddress= args[0];
		Scanner sc= null;
		try{
			sc= new Scanner(fileAddress);
		}catch (FileNotFoundException e){
			System.out.println("File not found.");
			e.printStackTrace();
			return;
		}
		/**/
//		Scanner sc= new Scanner();
		
		String nextTok= null;
		do{
			try{
				nextTok= sc.NextToken();
				System.out.print( nextTok );
				System.out.println("\t--CV= "+ sc.CV);
			}catch (IOException e){
				System.err.println("IOEXception");
				e.printStackTrace();
			}catch (ErrorLexer e){
				System.out.println(e.getMessage());
			}
		}while(!nextTok.equals("EOF"));
	}
}