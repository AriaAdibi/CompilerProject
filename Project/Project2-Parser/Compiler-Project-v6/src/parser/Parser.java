package parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import lexer.ErrorLexer;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import utilitarian.Pair;

public class Parser{
	private Lexer lexer= null;
	private Token look= null;

	private static final int N_GRAMMAR_RULES= 88;
	@SuppressWarnings("unchecked")
	private static final Pair<Variable, List<GrammarSymbol>> rules[]= new Pair[N_GRAMMAR_RULES];
	
	//Maybe later I changed the DS of this. Possibly to set.
	@SuppressWarnings("unchecked")
	private static final List<Tag> first[]= new List[ Variable.N_VARIABLES ];
	@SuppressWarnings("unchecked")
	private static final List<Tag> follow[]= new List[ Variable.N_VARIABLES ];
	
	//(variable, token) --> rule number
	private static final Map< Pair<Integer, Integer>, Integer > LL1Table= 
						new HashMap< Pair<Integer, Integer>, Integer >();
	private final Stack<GrammarSymbol> LL1Stack= 
						new Stack<GrammarSymbol>();
	
	private boolean recoverd= true;
	
	private void getNextToken() throws IOException, ErrorLexer{
		this.look= this.lexer.nextToken();
	}
	
	public Parser(){
		this.lexer= new Lexer();
		
		//Initialize the LL(1)Stack
		this.LL1Stack.push(Variable.START);
		
		this.recoverd= true;
	}

	public Parser( String fileAddress ) throws FileNotFoundException{
		this.lexer= new Lexer(fileAddress);

		//Initialize the LL(1)Stack
		this.LL1Stack.push(Variable.START);

		this.recoverd= true;
	}

	//Initialize the grammar
	//Maybe later we read the grammar from a file 
	static{
		/////////0
		GrammarSymbol rBody0[]= { Variable.PROGRAM, Tag.EOF 
		};
		Parser.rules[0]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.START, new ArrayList<GrammarSymbol>( Arrays.asList(rBody0)) );
		/////////1
		GrammarSymbol rBody1[]= { Tag.VOID, Tag.ID, Tag.OPEN_PARENTHESIS, Variable.FORMAL_PARAMETERS,
				Tag.CLOSE_PARENTHESIS, Variable.BLOCK, Variable.PROGRAM
		};
		Parser.rules[1]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PROGRAM, new ArrayList<GrammarSymbol>( Arrays.asList(rBody1)) );
		/////////2
		GrammarSymbol rBody2[]= { Tag.BASIC_TYPE, Tag.ID, Variable.R_PROGRAM
		};
		Parser.rules[2]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PROGRAM, new ArrayList<GrammarSymbol>( Arrays.asList(rBody2)) );
		/////////3
		GrammarSymbol rBody3[]= {
		};
		Parser.rules[3]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PROGRAM, new ArrayList<GrammarSymbol>( Arrays.asList(rBody3)) );
		/////////4
		GrammarSymbol rBody4[]= { Variable.DIM_DECLARATION, Variable.R_VAR_LIST, Tag.SEMICOLON, 
				Variable.PROGRAM
		};
		Parser.rules[4]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_PROGRAM, new ArrayList<GrammarSymbol>( Arrays.asList(rBody4)) );
		/////////5
		GrammarSymbol rBody5[]= { Tag.OPEN_PARENTHESIS, Variable.FORMAL_PARAMETERS, 
				Tag.CLOSE_PARENTHESIS, Variable.BLOCK, Variable.PROGRAM
		};
		Parser.rules[5]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_PROGRAM, new ArrayList<GrammarSymbol>( Arrays.asList(rBody5)) );
		/////////6
		GrammarSymbol rBody6[]= { Tag.BASIC_TYPE, Tag.ID, Variable.R_FORMAL_PARAMETERS  
		};
		Parser.rules[6]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.FORMAL_PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody6)) );
		/////////7
		GrammarSymbol rBody7[]= {
		};
		Parser.rules[7]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.FORMAL_PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody7)) );
		/////////8
		GrammarSymbol rBody8[]= { Tag.COMMA, Tag.BASIC_TYPE, Tag.ID, Variable.R_FORMAL_PARAMETERS   
		};
		Parser.rules[8]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_FORMAL_PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody8)) );
		/////////9
		GrammarSymbol rBody9[]= { 
		};
		Parser.rules[9]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_FORMAL_PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody9)) );
		/////////10
		GrammarSymbol rBody10[]= { Tag.COMMA, Tag.ID, Variable.DIM_DECLARATION, Variable.R_VAR_LIST   
		};
		Parser.rules[10]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_VAR_LIST, new ArrayList<GrammarSymbol>( Arrays.asList(rBody10)) );
		/////////11
		GrammarSymbol rBody11[]= {
		};
		Parser.rules[11]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_VAR_LIST, new ArrayList<GrammarSymbol>( Arrays.asList(rBody11)) );
		/////////12
		GrammarSymbol rBody12[]= { Tag.OPEN_BRACKET, Tag.INT_NUM, Tag.CLOSE_BRACKET,
				Variable.DIM_DECLARATION
		};
		Parser.rules[12]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.DIM_DECLARATION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody12)) );
		/////////13
		GrammarSymbol rBody13[]= {
		};
		Parser.rules[13]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.DIM_DECLARATION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody13)) );
		////////14
		GrammarSymbol rBody14[]= { Tag.OPEN_BRACE,	Variable.BLOCK_CONTENTS, Tag.CLOSE_BRACE
		};
		Parser.rules[14]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.BLOCK, new ArrayList<GrammarSymbol>( Arrays.asList(rBody14)) );
		////////15
		GrammarSymbol rBody15[]= { Tag.BASIC_TYPE, Tag.ID, Variable.DIM_DECLARATION, 
				Variable.R_VAR_LIST, Tag.SEMICOLON, Variable.BLOCK_CONTENTS
		};
		Parser.rules[15]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.BLOCK_CONTENTS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody15)) );
		////////16
		GrammarSymbol rBody16[]= { Variable.STATEMENT, Variable.BLOCK_CONTENTS 
		};
		Parser.rules[16]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.BLOCK_CONTENTS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody16)) );
		////////17
		GrammarSymbol rBody17[]= { 
		};
		Parser.rules[17]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.BLOCK_CONTENTS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody17)) );
		////////18
		GrammarSymbol rBody18[]= { Tag.ID, Variable.ASSIGNMENT_OR_METHODCALL
		};
		Parser.rules[18]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody18)) );
		////////19
		GrammarSymbol rBody19[]= { Tag.IF, Tag.OPEN_PARENTHESIS, Variable.EXPRESSION, 
				Tag.CLOSE_PARENTHESIS, Variable.BLOCK, Variable.OPT_ELSE
		};
		Parser.rules[19]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody19)) );
		////////20
		GrammarSymbol rBody20[]= { Tag.WHILE, Tag.OPEN_PARENTHESIS, Variable.EXPRESSION,
				Tag.CLOSE_PARENTHESIS, Variable.BLOCK
		};
		Parser.rules[20]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody20)) );
		////////21
		GrammarSymbol rBody21[]= { Tag.FOR, Tag.OPEN_PARENTHESIS, Variable.ASSIGNMENT,
				Tag.SEMICOLON, Variable.EXPRESSION, Tag.SEMICOLON, Variable.ASSIGNMENT,
				Tag.CLOSE_PARENTHESIS, Variable.BLOCK
		};
		Parser.rules[21]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody21)) );
		////////22
		GrammarSymbol rBody22[]= { Tag.RETURN, Variable.RET_EXPR, Tag.SEMICOLON
		};
		Parser.rules[22]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody22)) );
		////////23
		GrammarSymbol rBody23[]= { Tag.BREAK, Tag.SEMICOLON
		};
		Parser.rules[23]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody23)) );
		////////24
		GrammarSymbol rBody24[]= { Tag.CONTINUE, Tag.SEMICOLON
		};
		Parser.rules[24]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody24)) );
		////////25
		GrammarSymbol rBody25[]= { Variable.BLOCK
		};
		Parser.rules[25]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody25)) );
		////////26
		GrammarSymbol rBody26[]= { Tag.READER, Variable.LOCATION, Tag.SEMICOLON
		};
		Parser.rules[26]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody26)) );
		////////27
		GrammarSymbol rBody27[]= { Tag.WRITER, Variable.EXPRESSION, Tag.SEMICOLON
		};
		Parser.rules[27]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.STATEMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody27)) );
		////////28
		GrammarSymbol rBody28[]= { Variable.DIM_LOCATION, Tag.ASSIGNMENT, Variable.EXPRESSION,
				Tag.SEMICOLON
		};
		Parser.rules[28]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ASSIGNMENT_OR_METHODCALL, new ArrayList<GrammarSymbol>( Arrays.asList(rBody28)) );
		////////29
		GrammarSymbol rBody29[]= { Tag.OPEN_PARENTHESIS, Variable.PARAMETERS, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON
		};
		Parser.rules[29]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ASSIGNMENT_OR_METHODCALL, new ArrayList<GrammarSymbol>( Arrays.asList(rBody29)) );
		////////30
		GrammarSymbol rBody30[]= { Tag.ELSE, Variable.BLOCK
		};
		Parser.rules[30]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.OPT_ELSE, new ArrayList<GrammarSymbol>( Arrays.asList(rBody30)) );
		////////31
		GrammarSymbol rBody31[]= {
		};
		Parser.rules[31]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.OPT_ELSE, new ArrayList<GrammarSymbol>( Arrays.asList(rBody31)) );
		////////32
		GrammarSymbol rBody32[]= { Tag.OPEN_BRACKET, Variable.EXPRESSION, Tag.CLOSE_BRACKET,
				Variable.DIM_LOCATION
		};
		Parser.rules[32]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.DIM_LOCATION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody32)) );
		////////33
		GrammarSymbol rBody33[]= {
		};
		Parser.rules[33]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.DIM_LOCATION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody33)) );
		////////34
		GrammarSymbol rBody34[]= { Tag.ID, Variable.DIM_LOCATION
		};
		Parser.rules[34]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.LOCATION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody34)) );
		////////35
		GrammarSymbol rBody35[]= { Variable.LOCATION, Tag.ASSIGNMENT, Variable.EXPRESSION
		};
		Parser.rules[35]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ASSIGNMENT, new ArrayList<GrammarSymbol>( Arrays.asList(rBody35)) );
		////////36
		GrammarSymbol rBody36[]= { Variable.EXPRESSION
		};
		Parser.rules[36]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.RET_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody36)) );
		////////37
		GrammarSymbol rBody37[]= {
		};
		Parser.rules[37]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.RET_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody37)) );
		////////38
		GrammarSymbol rBody38[]= { Variable.EXPR_0
		};
		Parser.rules[38]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPRESSION, new ArrayList<GrammarSymbol>( Arrays.asList(rBody38)) );
		////////39
		GrammarSymbol rBody39[]= { Variable.EXPR_1, Variable.R_EXPR_0
		};
		Parser.rules[39]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody39)) );
		////////40
		GrammarSymbol rBody40[]= { Variable.COND_OP_0, Variable.EXPR_1, Variable.R_EXPR_0
		};
		Parser.rules[40]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody40)) );
		////////41
		GrammarSymbol rBody41[]= {
		};
		Parser.rules[41]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody41)) );
		////////42
		GrammarSymbol rBody42[]= { Variable.EXPR_2, Variable.R_EXPR_1
		};
		Parser.rules[42]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody42)) );
		////////43
		GrammarSymbol rBody43[]= { Variable.COND_OP_1, Variable.EXPR_2, Variable.R_EXPR_1
		};
		Parser.rules[43]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody43)) );
		////////44
		GrammarSymbol rBody44[]= {
		};
		Parser.rules[44]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody44)) );
		////////45
		GrammarSymbol rBody45[]= { Variable.EXPR_3, Variable.R_EXPR_2
		};
		Parser.rules[45]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_2, new ArrayList<GrammarSymbol>( Arrays.asList(rBody45)) );
		////////46
		GrammarSymbol rBody46[]= { Variable.EQ_OP, Variable.EXPR_3, Variable.R_EXPR_2
		};
		Parser.rules[46]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_2, new ArrayList<GrammarSymbol>( Arrays.asList(rBody46)) );
		////////47
		GrammarSymbol rBody47[]= {
		};
		Parser.rules[47]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_2, new ArrayList<GrammarSymbol>( Arrays.asList(rBody47)) );
		////////48
		GrammarSymbol rBody48[]= { Variable.EXPR_4, Variable.R_EXPR_3
		};
		Parser.rules[48]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_3, new ArrayList<GrammarSymbol>( Arrays.asList(rBody48)) );
		////////49
		GrammarSymbol rBody49[]= { Variable.REL_OP, Variable.EXPR_4, Variable.R_EXPR_3
		};
		Parser.rules[49]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_3, new ArrayList<GrammarSymbol>( Arrays.asList(rBody49)) );
		////////50
		GrammarSymbol rBody50[]= {
		};
		Parser.rules[50]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_3, new ArrayList<GrammarSymbol>( Arrays.asList(rBody50)) );
		////////51
		GrammarSymbol rBody51[]= { Variable.EXPR_5, Variable.R_EXPR_4
		};
		Parser.rules[51]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_4, new ArrayList<GrammarSymbol>( Arrays.asList(rBody51)) );
		////////52
		GrammarSymbol rBody52[]= { Variable.ARITH_OP_0, Variable.EXPR_5, Variable.R_EXPR_4
		};
		Parser.rules[52]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_4, new ArrayList<GrammarSymbol>( Arrays.asList(rBody52)) );
		////////53
		GrammarSymbol rBody53[]= {
		};
		Parser.rules[53]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_4, new ArrayList<GrammarSymbol>( Arrays.asList(rBody53)) );
		////////54
		GrammarSymbol rBody54[]= { Variable.EXPR_6, Variable.R_EXPR_5
		};
		Parser.rules[54]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_5, new ArrayList<GrammarSymbol>( Arrays.asList(rBody54)) );
		////////55
		GrammarSymbol rBody55[]= { Variable.ARITH_OP_1, Variable.EXPR_6, Variable.R_EXPR_5
		};
		Parser.rules[55]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_5, new ArrayList<GrammarSymbol>( Arrays.asList(rBody55)) );
		////////56
		GrammarSymbol rBody56[]= {
		};
		Parser.rules[56]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.R_EXPR_5, new ArrayList<GrammarSymbol>( Arrays.asList(rBody56)) );
		////////57
		GrammarSymbol rBody57[]= { Tag.NOT, Variable.EXPR_6
		};
		Parser.rules[57]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_6, new ArrayList<GrammarSymbol>( Arrays.asList(rBody57)) );
		////////58
		GrammarSymbol rBody58[]= { Variable.EXPR_7
		};
		Parser.rules[58]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_6, new ArrayList<GrammarSymbol>( Arrays.asList(rBody58)) );
		////////59
		GrammarSymbol rBody59[]= { Tag.MINUS, Variable.EXPR_7
		};
		Parser.rules[59]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_7, new ArrayList<GrammarSymbol>( Arrays.asList(rBody59)) );
		////////60
		GrammarSymbol rBody60[]= { Variable.EXPR_8
		};
		Parser.rules[60]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_7, new ArrayList<GrammarSymbol>( Arrays.asList(rBody60)) );
		////////61
		GrammarSymbol rBody61[]= { Variable.ATOMIC_EXPR
		};
		Parser.rules[61]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_8, new ArrayList<GrammarSymbol>( Arrays.asList(rBody61)) );
		////////62
		GrammarSymbol rBody62[]= { Tag.OPEN_PARENTHESIS, Variable.EXPR_0, Tag.CLOSE_PARENTHESIS
		};
		Parser.rules[62]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EXPR_8, new ArrayList<GrammarSymbol>( Arrays.asList(rBody62)) );
		////////63
		GrammarSymbol rBody63[]= { Tag.OROR
		};
		Parser.rules[63]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.COND_OP_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody63)) );
		////////64
		GrammarSymbol rBody64[]= { Tag.ANDAND
		};
		Parser.rules[64]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.COND_OP_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody64)) );
		////////65
		GrammarSymbol rBody65[]= { Tag.EQ
		};
		Parser.rules[65]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EQ_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody65)) );
		////////66
		GrammarSymbol rBody66[]= { Tag.NEQ
		};
		Parser.rules[66]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.EQ_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody66)) );
		////////67
		GrammarSymbol rBody67[]= { Tag.LESS_THAN
		};
		Parser.rules[67]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.REL_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody67)) );
		////////68
		GrammarSymbol rBody68[]= { Tag.LE
		};
		Parser.rules[68]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.REL_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody68)) );
		////////69
		GrammarSymbol rBody69[]= { Tag.GE
		};
		Parser.rules[69]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.REL_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody69)) );
		////////70
		GrammarSymbol rBody70[]= { Tag.GREATER_THAN
		};
		Parser.rules[70]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.REL_OP, new ArrayList<GrammarSymbol>( Arrays.asList(rBody70)) );
		////////71
		GrammarSymbol rBody71[]= { Tag.PLUS
		};
		Parser.rules[71]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ARITH_OP_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody71)) );
		////////72
		GrammarSymbol rBody72[]= { Tag.MINUS
		};
		Parser.rules[72]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ARITH_OP_0, new ArrayList<GrammarSymbol>( Arrays.asList(rBody72)) );
		////////73
		GrammarSymbol rBody73[]= { Tag.MULTIPLICATION
		};
		Parser.rules[73]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ARITH_OP_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody73)) );
		////////74
		GrammarSymbol rBody74[]= { Tag.DIVISION
		};
		Parser.rules[74]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ARITH_OP_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody74)) );
		////////75
		GrammarSymbol rBody75[]= { Tag.MODULE
		};
		Parser.rules[75]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ARITH_OP_1, new ArrayList<GrammarSymbol>( Arrays.asList(rBody75)) );
		////////76
		GrammarSymbol rBody76[]= { Tag.ID, Variable.LOCATION_OR_METHODCALL
		};
		Parser.rules[76]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody76)) );
		////////77
		GrammarSymbol rBody77[]= { Tag.INT_NUM
		};
		Parser.rules[77]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody77)) );
		////////78
		GrammarSymbol rBody78[]= { Tag.REAL_NUM
		};
		Parser.rules[78]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody78)) );
		////////79
		GrammarSymbol rBody79[]= { Tag.CHAR_LITERAL
		};
		Parser.rules[79]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody79)) );
		////////80
		GrammarSymbol rBody80[]= { Tag.TRUE
		};
		Parser.rules[80]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody80)) );
		////////81
		GrammarSymbol rBody81[]= { Tag.FALSE
		};
		Parser.rules[81]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.ATOMIC_EXPR, new ArrayList<GrammarSymbol>( Arrays.asList(rBody81)) );
		////////82
		GrammarSymbol rBody82[]= { Variable.DIM_LOCATION
		};
		Parser.rules[82]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.LOCATION_OR_METHODCALL, new ArrayList<GrammarSymbol>( Arrays.asList(rBody82)) );
		////////83
		GrammarSymbol rBody83[]= { Tag.OPEN_PARENTHESIS, Variable.PARAMETERS, Tag.CLOSE_PARENTHESIS
		};
		Parser.rules[83]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.LOCATION_OR_METHODCALL, new ArrayList<GrammarSymbol>( Arrays.asList(rBody83)) );
		////////84
		GrammarSymbol rBody84[]= { Variable.EXPRESSION, Variable.R_PARAMETERS
		};
		Parser.rules[84]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody84)) );
		////////85
		GrammarSymbol rBody85[]= {
		};
		Parser.rules[85]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody85)) );
		////////86
		GrammarSymbol rBody86[]= { Tag.COMMA, Variable.EXPRESSION, Variable.R_PARAMETERS
		};
		Parser.rules[86]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody86)) );
		////////87
		GrammarSymbol rBody87[]= {
		};
		Parser.rules[87]= new Pair<Variable, List<GrammarSymbol>>(
				Variable.PARAMETERS, new ArrayList<GrammarSymbol>( Arrays.asList(rBody87)) );
	}

	//Initialize first
	//Maybe calculate the first for our grammar
	//***	Here I add Tag.EPSILON if needed. Be careful about it.
	//		The Reason is for recognizing nullable situation.  
	//***	It is necessary to put Tag.EPSILON at the for convenience
	//		of other methods( currently only getExpectedTags() ).
	static{
		//0 start
		Tag fStart[]= { Tag.VOID, Tag.BASIC_TYPE, Tag.EOF
		};
		Parser.first[0]= new ArrayList<Tag>( Arrays.asList(fStart) );

		//1 program
		Tag fProgram[]= { Tag.VOID, Tag.BASIC_TYPE, Tag.EPSILON
		};
		Parser.first[1]= new ArrayList<Tag>( Arrays.asList(fProgram) );

		//2 rProgram
		Tag fRProgram[]= { Tag.OPEN_BRACKET, Tag.COMMA, Tag.SEMICOLON, Tag.OPEN_PARENTHESIS
		};
		Parser.first[2]= new ArrayList<Tag>( Arrays.asList(fRProgram) );

		//3 formalParameters
		Tag fFormalParameters[]= { Tag.BASIC_TYPE, Tag.EPSILON
		};
		Parser.first[3]= new ArrayList<Tag>( Arrays.asList(fFormalParameters) );

		//4 rFormalParameters
		Tag fRFormalParameters[]= { Tag.COMMA, Tag.EPSILON
		};
		Parser.first[4]= new ArrayList<Tag>( Arrays.asList(fRFormalParameters) );

		//5 rVarList
		Tag fRVarList[]= { Tag.COMMA, Tag.EPSILON
		};
		Parser.first[5]= new ArrayList<Tag>( Arrays.asList(fRVarList) );

		//6 dimDeclaration
		Tag fDimDeclaration[]= { Tag.OPEN_BRACKET, Tag.EPSILON
		};
		Parser.first[6]= new ArrayList<Tag>( Arrays.asList(fDimDeclaration) );

		//7 block
		Tag fBlock[]= { Tag.OPEN_BRACE
		};
		Parser.first[7]= new ArrayList<Tag>( Arrays.asList(fBlock) );

		//8 blockContents
		Tag fBlockContents[]= { Tag.BASIC_TYPE, Tag.ID, Tag.IF, Tag.WHILE, 
				Tag.FOR, Tag.RETURN, Tag.BREAK, Tag.CONTINUE, Tag.OPEN_BRACE, 
				Tag.READER, Tag.WRITER, Tag.EPSILON
		};
		Parser.first[8]= new ArrayList<Tag>( Arrays.asList(fBlockContents) );

		//9 statement
		Tag fStatement[]= { Tag.ID, Tag.IF, Tag.WHILE, Tag.FOR, Tag.RETURN,
				Tag.BREAK, Tag.CONTINUE, Tag.OPEN_BRACE, Tag.READER, Tag.WRITER
		};
		Parser.first[9]= new ArrayList<Tag>( Arrays.asList(fStatement) );

		//10 assignmentOrMethodCall
		Tag fAssignmentOrMethodCall[]= { Tag.OPEN_BRACKET, Tag.ASSIGNMENT, 
				Tag.OPEN_PARENTHESIS
		};
		Parser.first[10]= new ArrayList<Tag>( Arrays.asList(fAssignmentOrMethodCall) );

		//11 optElse
		Tag fOptElse[]= { Tag.ELSE, Tag.EPSILON
		};
		Parser.first[11]= new ArrayList<Tag>( Arrays.asList(fOptElse) );

		//12 dimLocation
		Tag fDimLocation[]= { Tag.OPEN_BRACKET, Tag.EPSILON
		};
		Parser.first[12]= new ArrayList<Tag>( Arrays.asList(fDimLocation) );

		//13 location
		Tag fLocation[]= { Tag.ID
		};
		Parser.first[13]= new ArrayList<Tag>( Arrays.asList(fLocation) );

		//14 assignment
		Tag fAssignment[]= { Tag.ID
		};
		Parser.first[14]= new ArrayList<Tag>( Arrays.asList(fAssignment) );

		//15 retExpr
		Tag fRetExpr[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
				Tag.EPSILON
		};
		Parser.first[15]= new ArrayList<Tag>( Arrays.asList(fRetExpr) );

		//16 expression
		Tag fExpression[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[16]= new ArrayList<Tag>( Arrays.asList(fExpression) );

		//17 expr0
		Tag fExpr0[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE
		};
		Parser.first[17]= new ArrayList<Tag>( Arrays.asList(fExpr0) );

		//18 rExpr0
		Tag fRExpr0[]= { Tag.OROR
		};
		Parser.first[18]= new ArrayList<Tag>( Arrays.asList(fRExpr0) );

		//19 expr1
		Tag fExpr1[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[19]= new ArrayList<Tag>( Arrays.asList(fExpr1) );

		//20 rExpr1
		Tag fRExpr1[]= { Tag.ANDAND
		};
		Parser.first[20]= new ArrayList<Tag>( Arrays.asList(fRExpr1) );

		//21 expr2
		Tag fExpr2[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[21]= new ArrayList<Tag>( Arrays.asList(fExpr2) );

		//22 rExpr2
		Tag fRExpr2[]= { Tag.EQ, Tag.NEQ
		};
		Parser.first[22]= new ArrayList<Tag>( Arrays.asList(fRExpr2) );

		//23 expr3
		Tag fExpr3[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[23]= new ArrayList<Tag>( Arrays.asList(fExpr3) );

		//24 rExpr3
		Tag fRExpr3[]= { Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN
		};
		Parser.first[24]= new ArrayList<Tag>( Arrays.asList(fRExpr3) );

		//25 expr4
		Tag fExpr4[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[25]= new ArrayList<Tag>( Arrays.asList(fExpr4) );

		//26 rExpr4
		Tag fRExpr4[]= { Tag.PLUS, Tag.MINUS
		};
		Parser.first[26]= new ArrayList<Tag>( Arrays.asList(fRExpr4) );

		//27 expr5
		Tag fExpr5[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[27]= new ArrayList<Tag>( Arrays.asList(fExpr5) );

		//28 rExpr5
		Tag fRExpr5[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE
		};
		Parser.first[28]= new ArrayList<Tag>( Arrays.asList(fRExpr5) );

		//29 expr6
		Tag fExpr6[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[29]= new ArrayList<Tag>( Arrays.asList(fExpr6) );

		//30 expr7
		Tag fExpr7[]= { Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[30]= new ArrayList<Tag>( Arrays.asList(fExpr7) );

		//31 expr8
		Tag fExpr8[]= { Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[31]= new ArrayList<Tag>( Arrays.asList(fExpr8) );

		//32 condOp0
		Tag fCondOp0[]= { Tag.OROR
		};
		Parser.first[32]= new ArrayList<Tag>( Arrays.asList(fCondOp0) );

		//33 condOp1
		Tag fCondOp1[]= { Tag.ANDAND
		};
		Parser.first[33]= new ArrayList<Tag>( Arrays.asList(fCondOp1) );

		//34 eqOp
		Tag fEqOp[]= { Tag.EQ, Tag.NEQ
		};
		Parser.first[34]= new ArrayList<Tag>( Arrays.asList(fEqOp) );

		//35 relOp
		Tag fRelOp[]= { Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN
		};
		Parser.first[35]= new ArrayList<Tag>( Arrays.asList(fRelOp) );

		//36 arithOp0
		Tag fArithOp0[]= { Tag.PLUS, Tag.MINUS
		};
		Parser.first[36]= new ArrayList<Tag>( Arrays.asList(fArithOp0) );

		//37 arithOp1
		Tag fArithOp1[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE
		};
		Parser.first[37]= new ArrayList<Tag>( Arrays.asList(fArithOp1) );

		//38 atomicExpr
		Tag fAtomicExpr[]= { Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.first[38]= new ArrayList<Tag>( Arrays.asList(fAtomicExpr) );

		//39 locationOrMethodCall
		Tag fLocationOrMethodCall[]= { Tag.OPEN_PARENTHESIS, Tag.OPEN_BRACKET,
				Tag.EPSILON
		};
		Parser.first[39]= new ArrayList<Tag>( Arrays.asList(fLocationOrMethodCall) );

		//40 parameters
		Tag fParameters[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
				Tag.EPSILON
		};
		Parser.first[40]= new ArrayList<Tag>( Arrays.asList(fParameters) );

		//41 rParameters
		Tag fRParameters[]= { Tag.COMMA, Tag.EPSILON
		};
		Parser.first[41]= new ArrayList<Tag>( Arrays.asList(fRParameters) );
	}

	//Initialize follow
	//Maybe calculate the follow for our grammar
	static{
		//0 start
		Tag foStart[]= {
		};
		Parser.follow[0]= new ArrayList<Tag>( Arrays.asList(foStart) );

		//1 program
		Tag foProgram[]= { Tag.EOF
		};
		Parser.follow[1]= new ArrayList<Tag>( Arrays.asList(foProgram) );

		//2 rProgram
		Tag foRProgram[]= { Tag.EOF
		};
		Parser.follow[2]= new ArrayList<Tag>( Arrays.asList(foRProgram) );

		//3 formalParameters
		Tag foFormalParameters[]= { Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[3]= new ArrayList<Tag>( Arrays.asList(foFormalParameters) );

		//4 rFormalParameters
		Tag foRFormalParameters[]= { Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[4]= new ArrayList<Tag>( Arrays.asList(foRFormalParameters) );

		//5 rVarList
		Tag foRVarList[]= { Tag.SEMICOLON
		};
		Parser.follow[5]= new ArrayList<Tag>( Arrays.asList(foRVarList) );

		//6 dimDeclaration
		Tag foDimDeclaration[]= { Tag.COMMA, Tag.SEMICOLON
		};
		Parser.follow[6]= new ArrayList<Tag>( Arrays.asList(foDimDeclaration) );

		//7 block
		Tag foBlock[]= { Tag.VOID, Tag.BASIC_TYPE, Tag.EOF, Tag.ELSE,
				Tag.ID, Tag.IF, Tag.WHILE, Tag.FOR, Tag.RETURN, Tag.BREAK,
				Tag.CONTINUE, Tag.OPEN_BRACE, Tag.READER, Tag.WRITER,
				Tag.CLOSE_BRACE
		};
		Parser.follow[7]= new ArrayList<Tag>( Arrays.asList(foBlock) );

		//8 blockContents
		Tag foBlockContents[]= { Tag.CLOSE_BRACE
		};
		Parser.follow[8]= new ArrayList<Tag>( Arrays.asList(foBlockContents) );

		//9 statement
		Tag foStatement[]= { Tag.BASIC_TYPE, Tag.ID, Tag.IF, Tag.WHILE, 
				Tag.FOR, Tag.RETURN, Tag.BREAK, Tag.CONTINUE, Tag.OPEN_BRACE, 
				Tag.READER, Tag.WRITER, Tag.CLOSE_BRACE
		};
		Parser.follow[9]= new ArrayList<Tag>( Arrays.asList(foStatement) );

		//10 assignmentOrMethodCall
		Tag foAssignmentOrMethodCall[]= { Tag.BASIC_TYPE, Tag.ID, Tag.IF, 
				Tag.WHILE, Tag.FOR, Tag.RETURN, Tag.BREAK, Tag.CONTINUE, 
				Tag.OPEN_BRACE, Tag.READER, Tag.WRITER, Tag.CLOSE_BRACE
		};
		Parser.follow[10]= new ArrayList<Tag>( Arrays.asList(foAssignmentOrMethodCall) );

		//11 optElse
		Tag foOptElse[]= { Tag.BASIC_TYPE, Tag.ID, Tag.IF, 
				Tag.WHILE, Tag.FOR, Tag.RETURN, Tag.BREAK, Tag.CONTINUE, 
				Tag.OPEN_BRACE, Tag.READER, Tag.WRITER, Tag.CLOSE_BRACE
		};
		Parser.follow[11]= new ArrayList<Tag>( Arrays.asList(foOptElse) );

		//12 dimLocation
		Tag foDimLocation[]= { Tag.ASSIGNMENT, Tag.SEMICOLON,
				Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE, Tag.PLUS, Tag.MINUS,
				Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN, Tag.EQ, Tag.NEQ,
				Tag.OROR, Tag.ANDAND, Tag.COMMA, Tag.CLOSE_BRACKET, Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[12]= new ArrayList<Tag>( Arrays.asList(foDimLocation) );

		//13 location
		Tag foLocation[]= { Tag.ASSIGNMENT, Tag.SEMICOLON,
				Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE, Tag.PLUS, Tag.MINUS,
				Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN, Tag.EQ, Tag.NEQ,
				Tag.OROR, Tag.ANDAND
		};
		Parser.follow[13]= new ArrayList<Tag>( Arrays.asList(foLocation) );

		//14 assignment
		Tag foAssignment[]= { Tag.SEMICOLON, Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[14]= new ArrayList<Tag>( Arrays.asList(foAssignment) );

		//15 retExpr
		Tag foRetExpr[]= { Tag.SEMICOLON
		};
		Parser.follow[15]= new ArrayList<Tag>( Arrays.asList(foRetExpr) );

		//16 expression
		Tag foExpression[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET
		};
		Parser.follow[16]= new ArrayList<Tag>( Arrays.asList(foExpression) );

		//17 expr0
		Tag foExpr0[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET
		};
		Parser.follow[17]= new ArrayList<Tag>( Arrays.asList(foExpr0) );

		//18 rExpr0
		Tag foRExpr0[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET
		};
		Parser.follow[18]= new ArrayList<Tag>( Arrays.asList(foRExpr0) );

		//19 expr1
		Tag foExpr1[]= { Tag.OROR, Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET
		};
		Parser.follow[19]= new ArrayList<Tag>( Arrays.asList(foExpr1) );

		//20 rExpr1
		Tag foRExpr1[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR
		};
		Parser.follow[20]= new ArrayList<Tag>( Arrays.asList(foRExpr1) );

		//21 expr2
		Tag foExpr2[]= { Tag.ANDAND, Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR
		};
		Parser.follow[21]= new ArrayList<Tag>( Arrays.asList(foExpr2) );

		//22 rExpr2
		Tag foRExpr2[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND
		};
		Parser.follow[22]= new ArrayList<Tag>( Arrays.asList(foRExpr2) );

		//23 expr3
		Tag foExpr3[]= { Tag.EQ, Tag.NEQ, Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND
		};
		Parser.follow[23]= new ArrayList<Tag>( Arrays.asList(foExpr3) );

		//24 rExpr3
		Tag foRExpr3[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND,
				Tag.EQ, Tag.NEQ
		};
		Parser.follow[24]= new ArrayList<Tag>( Arrays.asList(foRExpr3) );

		//25 expr4
		Tag foExpr4[]= { Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ
		};
		Parser.follow[25]= new ArrayList<Tag>( Arrays.asList(foExpr4) );

		//26 rExpr4
		Tag foRExpr4[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND,
				Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN
		};
		Parser.follow[26]= new ArrayList<Tag>( Arrays.asList(foRExpr4) );

		//27 expr5
		Tag foExpr5[]= { Tag.PLUS, Tag.MINUS, Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND,
				Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN
		};
		Parser.follow[27]= new ArrayList<Tag>( Arrays.asList(foExpr5) );

		//28 rExpr5
		Tag foRExpr5[]= { Tag.COMMA, Tag.CLOSE_PARENTHESIS,
				Tag.SEMICOLON, Tag.CLOSE_BRACKET, Tag.OROR, Tag.ANDAND,
				Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE, Tag.GREATER_THAN,
				Tag.PLUS, Tag.MINUS
		};
		Parser.follow[28]= new ArrayList<Tag>( Arrays.asList(foRExpr5) );

		//29 expr6
		Tag foExpr6[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE,
				Tag.GREATER_THAN, Tag.PLUS, Tag.MINUS
		};
		Parser.follow[29]= new ArrayList<Tag>( Arrays.asList(foExpr6) );

		//30 expr7
		Tag foExpr7[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE,
				Tag.GREATER_THAN, Tag.PLUS, Tag.MINUS
		};
		Parser.follow[30]= new ArrayList<Tag>( Arrays.asList(foExpr7) );

		//31 expr8
		Tag foExpr8[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE,
				Tag.GREATER_THAN, Tag.PLUS, Tag.MINUS
		};
		Parser.follow[31]= new ArrayList<Tag>( Arrays.asList(foExpr8) );

		//32 condOp0
		Tag foCondOp0[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[32]= new ArrayList<Tag>( Arrays.asList(foCondOp0) );

		//33 condOp1
		Tag foCondOp1[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[33]= new ArrayList<Tag>( Arrays.asList(foCondOp1) );

		//34 eqOp
		Tag foEqOp[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[34]= new ArrayList<Tag>( Arrays.asList(foEqOp) );

		//35 relOp
		Tag foRelOp[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[35]= new ArrayList<Tag>( Arrays.asList(foRelOp) );

		//36 arithOp0
		Tag foArithOp0[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[36]= new ArrayList<Tag>( Arrays.asList(foArithOp0) );

		//37 arithOp1
		Tag foArithOp1[]= { Tag.NOT, Tag.MINUS, Tag.OPEN_PARENTHESIS, Tag.ID,
				Tag.INT_NUM, Tag.REAL_NUM, Tag.CHAR_LITERAL, Tag.TRUE, Tag.FALSE,
		};
		Parser.follow[37]= new ArrayList<Tag>( Arrays.asList(foArithOp1) );

		//38 atomicExpr
		Tag foAtomicExpr[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE,
				Tag.GREATER_THAN, Tag.PLUS, Tag.MINUS
		};
		Parser.follow[38]= new ArrayList<Tag>( Arrays.asList(foAtomicExpr) );

		//39 locationOrMethodCall
		Tag foLocationOrMethodCall[]= { Tag.MULTIPLICATION, Tag.DIVISION, Tag.MODULE,
				Tag.COMMA, Tag.CLOSE_PARENTHESIS, Tag.SEMICOLON, Tag.CLOSE_BRACKET,
				Tag.OROR, Tag.ANDAND, Tag.EQ, Tag.NEQ, Tag.LESS_THAN, Tag.LE, Tag.GE,
				Tag.GREATER_THAN, Tag.PLUS, Tag.MINUS
		};
		Parser.follow[39]= new ArrayList<Tag>( Arrays.asList(foLocationOrMethodCall) );

		//40 parameters
		Tag foParameters[]= { Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[40]= new ArrayList<Tag>( Arrays.asList(foParameters) );

		//41 rParameters
		Tag foRParameters[]= { Tag.CLOSE_PARENTHESIS
		};
		Parser.follow[41]= new ArrayList<Tag>( Arrays.asList(foRParameters) );
	}

	//Initialize the LL(1) Table
	//Maybe calculate the table for our grammar
	static{
		//0 start
		Parser.LL1Table.put(new Pair<Integer, Integer>(0, 256), 0);
		Parser.LL1Table.put(new Pair<Integer, Integer>(0, 268), 0);
		Parser.LL1Table.put(new Pair<Integer, Integer>(0, 26), 0);

		//1 program
		Parser.LL1Table.put(new Pair<Integer, Integer>(1, 256), 2);
		Parser.LL1Table.put(new Pair<Integer, Integer>(1, 268), 1);
		Parser.LL1Table.put(new Pair<Integer, Integer>(1, 26), 3);

		//2 rProgram
		Parser.LL1Table.put(new Pair<Integer, Integer>(2, 40), 5);
		Parser.LL1Table.put(new Pair<Integer, Integer>(2, 91), 4);
		Parser.LL1Table.put(new Pair<Integer, Integer>(2, 59), 4);
		Parser.LL1Table.put(new Pair<Integer, Integer>(2, 44), 4);

		//3 formalParameters
		Parser.LL1Table.put(new Pair<Integer, Integer>(3, 256), 6);
		Parser.LL1Table.put(new Pair<Integer, Integer>(3, 41), 7);

		//4 rFormalParameters
		Parser.LL1Table.put(new Pair<Integer, Integer>(4, 41), 9);
		Parser.LL1Table.put(new Pair<Integer, Integer>(4, 44), 8);

		//5 rVarList
		Parser.LL1Table.put(new Pair<Integer, Integer>(5, 59), 11);
		Parser.LL1Table.put(new Pair<Integer, Integer>(5, 44), 10);

		//6 dimDeclaration
		Parser.LL1Table.put(new Pair<Integer, Integer>(6, 91), 12);
		Parser.LL1Table.put(new Pair<Integer, Integer>(6, 59), 13);
		Parser.LL1Table.put(new Pair<Integer, Integer>(6, 44), 13);

		//7 block
		Parser.LL1Table.put(new Pair<Integer, Integer>(7, 123), 14);

		//8 blockContents
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 256), 15);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 257), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 258), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 259), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 263), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 264), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 265), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 266), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 267), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 278), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 123), 16);
		Parser.LL1Table.put(new Pair<Integer, Integer>(8, 125), 17);

		//9 statement
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 257), 23);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 258), 24);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 259), 19);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 263), 21);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 264), 20);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 265), 26);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 266), 27);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 267), 22);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 278), 18);
		Parser.LL1Table.put(new Pair<Integer, Integer>(9, 123), 25);

		//10 assignmentOrMethodCall
		Parser.LL1Table.put(new Pair<Integer, Integer>(10, 40), 29);
		Parser.LL1Table.put(new Pair<Integer, Integer>(10, 91), 28);
		Parser.LL1Table.put(new Pair<Integer, Integer>(10, 61), 28);

		//11 optElse
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 256), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 257), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 258), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 259), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 260), 30);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 263), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 264), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 265), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 266), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 267), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 278), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 123), 31);
		Parser.LL1Table.put(new Pair<Integer, Integer>(11, 125), 31);

		//12 dimLocation
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 269), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 270), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 271), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 272), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 273), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 274), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 41), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 91), 32);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 93), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 59), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 44), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 43), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 45), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 42), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 37), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 47), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 61), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 60), 33);
		Parser.LL1Table.put(new Pair<Integer, Integer>(12, 62), 33);

		//13 location
		Parser.LL1Table.put(new Pair<Integer, Integer>(13, 278), 34);

		//14 assignment
		Parser.LL1Table.put(new Pair<Integer, Integer>(14, 278), 35);

		//15 retExpr
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 261), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 262), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 275), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 276), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 277), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 278), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 40), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 59), 37);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 45), 36);
		Parser.LL1Table.put(new Pair<Integer, Integer>(15, 33), 36);

		//16 expression
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 261), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 262), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 275), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 276), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 277), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 278), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 40), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 45), 38);
		Parser.LL1Table.put(new Pair<Integer, Integer>(16, 33), 38);

		//17 expr0
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 261), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 262), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 275), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 276), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 277), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 278), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 40), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 45), 39);
		Parser.LL1Table.put(new Pair<Integer, Integer>(17, 33), 39);

		//18 rExpr0
		Parser.LL1Table.put(new Pair<Integer, Integer>(18, 274), 40);
		Parser.LL1Table.put(new Pair<Integer, Integer>(18, 41), 41);
		Parser.LL1Table.put(new Pair<Integer, Integer>(18, 93), 41);
		Parser.LL1Table.put(new Pair<Integer, Integer>(18, 59), 41);
		Parser.LL1Table.put(new Pair<Integer, Integer>(18, 44), 41);

		//19 expr1
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 261), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 262), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 275), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 276), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 277), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 278), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 40), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 45), 42);
		Parser.LL1Table.put(new Pair<Integer, Integer>(19, 33), 42);

		//20 rExpr1
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 273), 43);
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 274), 44);
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 41), 44);
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 93), 44);
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 59), 44);
		Parser.LL1Table.put(new Pair<Integer, Integer>(20, 44), 44);

		//21 expr2
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 261), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 262), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 275), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 276), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 277), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 278), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 40), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 45), 45);
		Parser.LL1Table.put(new Pair<Integer, Integer>(21, 33), 45);

		//22 rExpr2
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 269), 46);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 270), 46);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 273), 47);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 274), 47);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 41), 47);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 93), 47);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 59), 47);
		Parser.LL1Table.put(new Pair<Integer, Integer>(22, 44), 47);

		//23 expr3
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 261), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 262), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 275), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 276), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 277), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 278), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 40), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 45), 48);
		Parser.LL1Table.put(new Pair<Integer, Integer>(23, 33), 48);

		//24 rExpr3
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 269), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 270), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 271), 49);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 272), 49);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 273), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 274), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 41), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 93), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 59), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 44), 50);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 60), 49);
		Parser.LL1Table.put(new Pair<Integer, Integer>(24, 62), 49);

		//25 expr4
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 261), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 262), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 275), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 276), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 277), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 278), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 40), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 45), 51);
		Parser.LL1Table.put(new Pair<Integer, Integer>(25, 33), 51);

		//26 rExpr4
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 269), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 270), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 271), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 272), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 273), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 274), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 41), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 93), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 59), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 44), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 43), 52);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 45), 52);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 60), 53);
		Parser.LL1Table.put(new Pair<Integer, Integer>(26, 62), 53);

		//27 expr5
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 261), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 262), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 275), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 276), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 277), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 278), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 40), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 45), 54);
		Parser.LL1Table.put(new Pair<Integer, Integer>(27, 33), 54);

		//28 rExpr5
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 269), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 270), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 271), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 272), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 273), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 274), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 41), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 93), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 59), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 44), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 43), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 45), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 42), 55);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 37), 55);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 47), 55);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 60), 56);
		Parser.LL1Table.put(new Pair<Integer, Integer>(28, 62), 56);

		//29 expr6
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 261), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 262), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 275), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 276), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 277), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 278), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 40), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 45), 58);
		Parser.LL1Table.put(new Pair<Integer, Integer>(29, 33), 57);

		//30 expr7
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 261), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 262), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 275), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 276), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 277), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 278), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 40), 60);
		Parser.LL1Table.put(new Pair<Integer, Integer>(30, 45), 59);

		//31 expr8
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 261), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 262), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 275), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 276), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 277), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 278), 61);
		Parser.LL1Table.put(new Pair<Integer, Integer>(31, 40), 62);

		//32 condOp0
		Parser.LL1Table.put(new Pair<Integer, Integer>(32, 274), 63);

		//33 condOp1
		Parser.LL1Table.put(new Pair<Integer, Integer>(33, 273), 64);

		//34 eqOp
		Parser.LL1Table.put(new Pair<Integer, Integer>(34, 269), 65);
		Parser.LL1Table.put(new Pair<Integer, Integer>(34, 270), 66);

		//35 relOp
		Parser.LL1Table.put(new Pair<Integer, Integer>(35, 271), 69);
		Parser.LL1Table.put(new Pair<Integer, Integer>(35, 272), 68);
		Parser.LL1Table.put(new Pair<Integer, Integer>(35, 60), 67);
		Parser.LL1Table.put(new Pair<Integer, Integer>(35, 62), 70);

		//36 arithOp0
		Parser.LL1Table.put(new Pair<Integer, Integer>(36, 43), 71);
		Parser.LL1Table.put(new Pair<Integer, Integer>(36, 45), 72);

		//37 arithOp1
		Parser.LL1Table.put(new Pair<Integer, Integer>(37, 42), 73);
		Parser.LL1Table.put(new Pair<Integer, Integer>(37, 37), 75);
		Parser.LL1Table.put(new Pair<Integer, Integer>(37, 47), 74);

		//38 atomicExpr
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 261), 80);
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 262), 81);
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 275), 77);
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 276), 78);
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 277), 79);
		Parser.LL1Table.put(new Pair<Integer, Integer>(38, 278), 76);

		//39 locationOrMethodCall
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 269), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 270), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 271), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 272), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 273), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 274), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 40), 83);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 41), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 91), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 93), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 59), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 44), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 43), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 45), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 42), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 37), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 47), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 60), 82);
		Parser.LL1Table.put(new Pair<Integer, Integer>(39, 62), 82);

		//40 parameters
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 261), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 262), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 275), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 276), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 277), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 278), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 40), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 41), 85);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 45), 84);
		Parser.LL1Table.put(new Pair<Integer, Integer>(40, 33), 84);

		//41 rParameters
		Parser.LL1Table.put(new Pair<Integer, Integer>(41, 41), 87);
		Parser.LL1Table.put(new Pair<Integer, Integer>(41, 44), 86);
	}

	private static List<Tag> getFirst(Variable var){
		return Parser.first[ var.code ];
	}
	
	private static List<Tag> getFollow(Variable var){
		return Parser.follow[ var.code ];
	}
	
	private static List<Tag> getSynchronizingTags(Variable var){
		return Parser.getFirst(var);
	}
	
	//Panic mode recovery is chosen
	//the Synchronizing set is first(A) (for now).
	private void errorHandler(String theError, boolean isTerminal) throws ErrorParser{
		this.recoverd= false;
		String errLocation= this.lexer.getTokenLocation();
		
		List<Tag> synchTags= null;
		GrammarSymbol topSymbol= this.LL1Stack.peek();
		if(isTerminal){
			synchTags= new ArrayList<Tag>();
			synchTags.add( (Tag)topSymbol );
		}
		else{
			synchTags= Parser.getSynchronizingTags( (Variable)topSymbol );
		}

		do{
			try{
				this.getNextToken();
			}catch (IOException e){
				throw new ErrorParser(errLocation + theError, true, "IOEXception");
			}catch (ErrorLexer e){
				//this.look is previous look therefore 
				//we will not exit the do{}while().
				continue;
			}
		}while( synchTags.contains(this.look.tag)==false && this.look.tag!=Tag.EOF );
		
		if( this.look.tag==Tag.EOF && topSymbol != Tag.EOF && !synchTags.contains(Tag.EOF) )
			throw new ErrorParser(errLocation + theError, true, "##Recovery failed --Reached end of the file");
		else
			throw new ErrorParser(errLocation + theError, false, null);
	}
	
	/*
	* Designed to be INDIPENDENT from table structure and 
	* error recovery policy.
	*/
	public void parse() throws IOException, ErrorLexer, ErrorParser{
		if( this.recoverd )
			this.getNextToken();
		else	//	errorHandler have chosen the right Token
			this.recoverd= true;
		Tag inputTag= this.look.tag;
		GrammarSymbol topSymbol= this.LL1Stack.peek();
		
		while(!this.LL1Stack.empty()){

			/**
			if( topSymbol == Variable.LOCATION_OR_METHODCALL){
				System.err.println("Token: "+ inputTag+ "	Top symbol: "+ topSymbol);
				System.err.println("Stack:");
				ArrayList<GrammarSymbol> stackContents= new ArrayList<GrammarSymbol>();
				while( !this.LL1Stack.empty() ){
					GrammarSymbol top= this.LL1Stack.pop();
					stackContents.add(top);
					System.err.print(top+ " ");
				}
				System.err.println("");
				Collections.reverse(stackContents);
				for( GrammarSymbol top: stackContents )
					this.LL1Stack.push(top);
			}
			/**/

			if( topSymbol instanceof Tag ){
				Tag stackTag= (Tag)topSymbol;
				
				if( stackTag.equals(inputTag) ){
					Action( topSymbol, null );
					this.LL1Stack.pop();
					
					if( this.look.tag == Tag.EOF )
						break;
						
					this.getNextToken();
					inputTag= this.look.tag;
				}
				else
					errorHandler("--Unexpected Token= "+ this.look.getLexeme()+ "--"+ stackTag+ "is expected", true);
			}
			else if( topSymbol instanceof Variable ){
				Variable var= (Variable)topSymbol;
				
				List<GrammarSymbol> ruleBody= getDerivationFromLL1Table(var, inputTag);
				/**
				if( var == Variable.R_PROGRAM ){
					System.err.println("*********  "+ ruleBody);
				}
				/**/
				if( ruleBody == null )
					errorHandler("--Unexpected Token= "+ this.look.getLexeme()+ "\n \t\t--"+
						"previous Token is missing or the Token must be one of the following \n \t\t"+ 
						getExpectedTags(var), false );
				else{
					Action( topSymbol, ruleBody );
					this.LL1Stack.pop();
					
					List<GrammarSymbol> rB= new ArrayList<GrammarSymbol>(ruleBody);
					Collections.reverse(rB);
					for( GrammarSymbol symbol: rB )
						this.LL1Stack.push(symbol);
				}
			}
			else
				throw new RuntimeException("A LL1Stack element is neither Variable or Tag");
			topSymbol= this.LL1Stack.peek();
		}
	}
	
	private String getExpectedTags(Variable var){
		StringBuffer expectedTags= new StringBuffer();
		
		List<Tag> list= Parser.getFirst(var);
		
		if( list.size() > 0 && list.get( list.size()-1 ) == Tag.EPSILON ){
			list.remove(list.size()-1);
			list.addAll( Parser.getFollow(var) );
		}
		
		int listLength= list.size();
		for( int i=0; i<listLength-1; i++ )
			expectedTags.append( list.get(i).name+ ", " );
		if( listLength > 0 )
			expectedTags.append( list.get( listLength-1 ).name );
		
		return expectedTags.toString();
	}

	private List<GrammarSymbol> getDerivationFromLL1Table(Variable var, Tag tag){
		Pair<Integer, Integer> p= new Pair<Integer, Integer>(var.code, tag.code );
		Integer ruleNumber= Parser.LL1Table.get(p);
		/**
		if( var == Variable.R_PROGRAM ){
			System.err.println(var+ " "+ var.code);
			System.err.println(tag+ " "+ tag.code+ " ");
			System.err.println("************  "+ ruleNumber);
		}
		/**/
		if( ruleNumber == null )
			return null;
		else{
			Pair<Variable, List<GrammarSymbol>> theRule= Parser.rules[ruleNumber];
			return theRule.second;
		}
	}
	
	private void Action(GrammarSymbol topSymbol, List<GrammarSymbol> ruleBody){
		if( ruleBody == null )
			System.out.println("Matched: "+ (Tag)topSymbol);
		else{
			System.out.print((Variable)topSymbol+ " \t--> \t");
			for(GrammarSymbol symbol: ruleBody)
				System.out.print(" "+ symbol);
			System.out.println("");
		}
	}
}