package parserSemanticsAST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import intermediateRepresentation.Access;
import intermediateRepresentation.And;
import intermediateRepresentation.Arith;
import intermediateRepresentation.ArrayAssign;
import intermediateRepresentation.Assign;
import intermediateRepresentation.Break;
import intermediateRepresentation.Constant;
import intermediateRepresentation.Continue;
import intermediateRepresentation.ErrorSemantics;
import intermediateRepresentation.Expr;
import intermediateRepresentation.For;
import intermediateRepresentation.If;
import intermediateRepresentation.IfElse;
import intermediateRepresentation.Location;
import intermediateRepresentation.Node;
import intermediateRepresentation.Not;
import intermediateRepresentation.Or;
import intermediateRepresentation.Proc;
import intermediateRepresentation.ProcCall;
import intermediateRepresentation.ProcCallStmt;
import intermediateRepresentation.Reader;
import intermediateRepresentation.RelOp;
import intermediateRepresentation.Return;
import intermediateRepresentation.SeqStmt;
import intermediateRepresentation.Stmt;
import intermediateRepresentation.UnaryMinus;
import intermediateRepresentation.While;
import intermediateRepresentation.Writer;
import lexer.CharLiteral;
import lexer.ErrorLexer;
import lexer.IntNum;
import lexer.Lexer;
import lexer.RealNum;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Array;
import symbols.Environment;
import symbols.ProcInfo;
import symbols.SymTableInfo;
import symbols.Type;
import symbols.VarInfo;
import utilitarian.Pair;

public class Parser{
	private Lexer lexer= null;
	private Token look= null;

	private Environment topEnv= Environment.GLOBAL_ENV;	// current or top symbol table
	public int usedStorage= 0;							// storage used for declarations
	
	private static final int N_GRAMMAR_RULES= 88;
	@SuppressWarnings("unchecked")
	private static final Pair<Variable, List<LLStackElem>> rules[]= new Pair[N_GRAMMAR_RULES];
	
	//Maybe later I changed the DS of this. Possibly to set.
	@SuppressWarnings("unchecked")
	private static final List<Tag> first[]= new List[ Variable.N_VARIABLES ];
	@SuppressWarnings("unchecked")
	private static final List<Tag> follow[]= new List[ Variable.N_VARIABLES ];
	
	//(variable, token) --> rule number
	private static final Map< Pair<Integer, Integer>, Integer > LL1Table= 
						new HashMap< Pair<Integer, Integer>, Integer >();
	private final Stack<LLStackElem> LL1Stack= 
						new Stack<LLStackElem>();
	
	private boolean recoverd= true;
	
	private void getNextToken() throws IOException, ErrorLexer{
		this.look= this.lexer.nextToken();
	}
	
	public Parser( Lexer lexer ){
		this.lexer= lexer;
		
		//Initialize the LL(1)Stack
		this.LL1Stack.push(Variable.START);
		
		this.recoverd= true;
	}

	//Initialize the grammar
	//Maybe later we read the grammar from a file 
	static{
		/////////0
		LLStackElem rBody0[]= {
			new ActionRecord("prod0 start->"),
			Variable.PROGRAM,
			new SynthesizeRecord("prod0Program_"),
			new ActionRecord("prod0_EOF"),
			Tag.EOF 
		};
		Parser.rules[0]= new Pair<Variable, List<LLStackElem>>(
				Variable.START, new ArrayList<LLStackElem>( Arrays.asList(rBody0)) );
		/////////1
		LLStackElem rBody1[]= {
			new ActionRecord("prod1 program->"),
			Tag.VOID,
			new ActionRecord("prod1_Id"), 
			Tag.ID,
			Tag.OPEN_PARENTHESIS,
			Variable.FORMAL_PARAMETERS,
			new SynthesizeRecord("prod1FormalParameters_"),
			Tag.CLOSE_PARENTHESIS,
			Variable.BLOCK,
			new SynthesizeRecord("prod1Block_"),
			Variable.PROGRAM,
			new SynthesizeRecord("prod1Program_"),
			
		};
		Parser.rules[1]= new Pair<Variable, List<LLStackElem>>(
				Variable.PROGRAM, new ArrayList<LLStackElem>( Arrays.asList(rBody1)) );
		/////////2
		LLStackElem rBody2[]= {
			new ActionRecord("prod2 program->"),
			new ActionRecord("prod2_basicType"),
			Tag.BASIC_TYPE,
			new ActionRecord("prod2_id"),
			Tag.ID,
			Variable.R_PROGRAM
		};
		Parser.rules[2]= new Pair<Variable, List<LLStackElem>>(
				Variable.PROGRAM, new ArrayList<LLStackElem>( Arrays.asList(rBody2)) );
		/////////3
		LLStackElem rBody3[]= {
			new ActionRecord("prod3 program->"),
		};
		Parser.rules[3]= new Pair<Variable, List<LLStackElem>>(
				Variable.PROGRAM, new ArrayList<LLStackElem>( Arrays.asList(rBody3)) );
		/////////4
		LLStackElem rBody4[]= {
			new ActionRecord("prod4 rProgram->"),
			Variable.DIM_DECLARATION,
			new SynthesizeRecord("prod4DimDeclaration_"),
			Variable.R_VAR_LIST,
			new SynthesizeRecord("prod4RVarList_"),
			Tag.SEMICOLON,
			Variable.PROGRAM
		};
		Parser.rules[4]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_PROGRAM, new ArrayList<LLStackElem>( Arrays.asList(rBody4)) );
		/////////5
		LLStackElem rBody5[]= {
			new ActionRecord("prod5 rProgram->"),
			Tag.OPEN_PARENTHESIS,
			Variable.FORMAL_PARAMETERS,
			new SynthesizeRecord("prod5FormalParameters_"),
			Tag.CLOSE_PARENTHESIS,
			Variable.BLOCK,
			new SynthesizeRecord("prod5Block_"),
			Variable.PROGRAM,
			new SynthesizeRecord("prod5Program_"),
		};
		Parser.rules[5]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_PROGRAM, new ArrayList<LLStackElem>( Arrays.asList(rBody5)) );
		/////////6
		LLStackElem rBody6[]= {
			new ActionRecord("prod6 formalParameters->"),
			new ActionRecord("prod6_BasicType"),
			Tag.BASIC_TYPE,
			new ActionRecord("prod6_Id"),
			Tag.ID,
			Variable.R_FORMAL_PARAMETERS,  
			new SynthesizeRecord("prod6RFormalParameters_")
		};
		Parser.rules[6]= new Pair<Variable, List<LLStackElem>>(
				Variable.FORMAL_PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody6)) );
		/////////7
		LLStackElem rBody7[]= {
			new ActionRecord("prod7 formalParameters->"),
			new SynthesizeRecord("prod7Eps_")
		};
		Parser.rules[7]= new Pair<Variable, List<LLStackElem>>(
				Variable.FORMAL_PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody7)) );
		/////////8
		LLStackElem rBody8[]= {
			new ActionRecord("prod8 rFormalParameters->"),
			Tag.COMMA,
			new ActionRecord("prod8_BasicType"),
			Tag.BASIC_TYPE,
			new ActionRecord("prod8_Id"),
			Tag.ID,
			Variable.R_FORMAL_PARAMETERS,
			new SynthesizeRecord("prod8RFormalParameters_")   
		};
		Parser.rules[8]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_FORMAL_PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody8)) );
		/////////9
		LLStackElem rBody9[]= {
			new ActionRecord("prod9 rFormalParameters->"),
			new SynthesizeRecord("prod9Eps_")
		};
		Parser.rules[9]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_FORMAL_PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody9)) );
		/////////10
		LLStackElem rBody10[]= {
			new ActionRecord("prod10 rVarList->"),
			Tag.COMMA,
			new ActionRecord("prod10_id"),
			Tag.ID,
			Variable.DIM_DECLARATION,
			new SynthesizeRecord("prod10DimDeclaration_"),
			Variable.R_VAR_LIST,
			new SynthesizeRecord("prod10RVarList_"),
		};
		Parser.rules[10]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_VAR_LIST, new ArrayList<LLStackElem>( Arrays.asList(rBody10)) );
		/////////11
		LLStackElem rBody11[]= {
			new ActionRecord("prod11 rVarList->"),
		};
		Parser.rules[11]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_VAR_LIST, new ArrayList<LLStackElem>( Arrays.asList(rBody11)) );
		/////////12
		LLStackElem rBody12[]= {
			new ActionRecord("prod12 dimDeclaration->"),
			Tag.OPEN_BRACKET,
			new ActionRecord("prod12_intNum"),
			Tag.INT_NUM, Tag.CLOSE_BRACKET,
			Variable.DIM_DECLARATION,
			new SynthesizeRecord("prod12DimDeclaration_")
		};
		Parser.rules[12]= new Pair<Variable, List<LLStackElem>>(
				Variable.DIM_DECLARATION, new ArrayList<LLStackElem>( Arrays.asList(rBody12)) );
		/////////13
		LLStackElem rBody13[]= {
			new ActionRecord("prod13 dimDeclaration->"),
			new SynthesizeRecord("prod13Eps_")
		};
		Parser.rules[13]= new Pair<Variable, List<LLStackElem>>(
				Variable.DIM_DECLARATION, new ArrayList<LLStackElem>( Arrays.asList(rBody13)) );
		////////14
		LLStackElem rBody14[]= {
			new ActionRecord("prod14 block->"),
			Tag.OPEN_BRACE,
			new ActionRecord("prod14_blockContents"),
			Variable.BLOCK_CONTENTS,
			new SynthesizeRecord("prod14BlockContents_"),
			new ActionRecord("prod14_closeBrace"),
			Tag.CLOSE_BRACE
		};
		Parser.rules[14]= new Pair<Variable, List<LLStackElem>>(
				Variable.BLOCK, new ArrayList<LLStackElem>( Arrays.asList(rBody14)) );
		////////15
		LLStackElem rBody15[]= {
			new ActionRecord("prod15 blockContents->"),
			new ActionRecord("prod15_basicType"),
			Tag.BASIC_TYPE,
			new ActionRecord("prod15_id"),
			Tag.ID,
			Variable.DIM_DECLARATION,
			new SynthesizeRecord("prod15DimDeclaratioin_"),
			Variable.R_VAR_LIST, Tag.SEMICOLON, Variable.BLOCK_CONTENTS
		};
		Parser.rules[15]= new Pair<Variable, List<LLStackElem>>(
				Variable.BLOCK_CONTENTS, new ArrayList<LLStackElem>( Arrays.asList(rBody15)) );
		////////16
		LLStackElem rBody16[]= {
			new ActionRecord("prod16 blockContents->"),
			Variable.STATEMENT,
			new SynthesizeRecord("prod16Statement_"),
			Variable.BLOCK_CONTENTS,
			new SynthesizeRecord("prod16BlockContents_"),
		};
		Parser.rules[16]= new Pair<Variable, List<LLStackElem>>(
				Variable.BLOCK_CONTENTS, new ArrayList<LLStackElem>( Arrays.asList(rBody16)) );
		////////17
		LLStackElem rBody17[]= {
			new ActionRecord("prod17 blockContents->"),
			new SynthesizeRecord("prod17Eps_"),
		};
		Parser.rules[17]= new Pair<Variable, List<LLStackElem>>(
				Variable.BLOCK_CONTENTS, new ArrayList<LLStackElem>( Arrays.asList(rBody17)) );
		////////18
		LLStackElem rBody18[]= {
			new ActionRecord("prod18 statement->"),
			new ActionRecord("prod18_id"),
			Tag.ID,
			Variable.ASSIGNMENT_OR_METHODCALL
		};
		Parser.rules[18]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody18)) );
		////////19
		LLStackElem rBody19[]= {
			new ActionRecord("prod19 statement->"),
			Tag.IF,
			Tag.OPEN_PARENTHESIS,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod19Expression_"),
			Tag.CLOSE_PARENTHESIS,
			Variable.BLOCK,
			new SynthesizeRecord("prod19Block_"),
			Variable.OPT_ELSE,
			new SynthesizeRecord("prod19OptElse_"),
			new SynthesizeRecord("prod19OptElse__"),
		};
		Parser.rules[19]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody19)) );
		////////20
		LLStackElem rBody20[]= {
			new ActionRecord("prod20 statement->"),
			Tag.WHILE,
			new ActionRecord("prod20_openParenthesis"),
			Tag.OPEN_PARENTHESIS,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod20Expression_"),
			Tag.CLOSE_PARENTHESIS,
			Variable.BLOCK,
			new SynthesizeRecord("prod20Block_"),
			new SynthesizeRecord("prod20Block__"),
		};
		Parser.rules[20]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody20)) );
		////////21
		LLStackElem rBody21[]= {
			new ActionRecord("prod21 statement->"),
			Tag.FOR,
			new ActionRecord("prod21_openParenthesis"),
			Tag.OPEN_PARENTHESIS,
			Variable.ASSIGNMENT,
			new SynthesizeRecord("prod21InitAssignment_"),
			Tag.SEMICOLON,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod21Expression_"),
			Tag.SEMICOLON,
			Variable.ASSIGNMENT,
			new SynthesizeRecord("prod21IterateAssignment_"),
			Tag.CLOSE_PARENTHESIS,
			Variable.BLOCK,
			new SynthesizeRecord("prod21Block_"),
			new SynthesizeRecord("prod21Block__"),
		};
		Parser.rules[21]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody21)) );
		////////22
		LLStackElem rBody22[]= {
			new ActionRecord("prod22 statement->"),
			Tag.RETURN,
			Variable.RET_EXPR,
			new SynthesizeRecord("prod22RetExpr_"),
			Tag.SEMICOLON
		};
		Parser.rules[22]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody22)) );
		////////23
		LLStackElem rBody23[]= {
			new ActionRecord("prod23 statement->"),
			Tag.BREAK,
			Tag.SEMICOLON,
			new SynthesizeRecord("prod23Semicolon_")
		};
		Parser.rules[23]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody23)) );
		////////24
		LLStackElem rBody24[]= {
			new ActionRecord("prod24 statement->"),
			Tag.CONTINUE,
			Tag.SEMICOLON,
			new SynthesizeRecord("prod24Semicolon_")
		};
		Parser.rules[24]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody24)) );
		////////25
		LLStackElem rBody25[]= {
			new ActionRecord("prod25 statement->"),
			Variable.BLOCK
		};
		Parser.rules[25]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody25)) );
		////////26
		LLStackElem rBody26[]= {
			new ActionRecord("prod26 statement->"),
			new ActionRecord("prod26_reader"),
			Tag.READER,
			Variable.LOCATION,
			new SynthesizeRecord("prod26Location_"),
			Tag.SEMICOLON
		};
		Parser.rules[26]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody26)) );
		////////27
		LLStackElem rBody27[]= {
			new ActionRecord("prod27 statement->"),
			new ActionRecord("prod27_writer"),
			Tag.WRITER,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod27Expression_"),
			Tag.SEMICOLON
		};
		Parser.rules[27]= new Pair<Variable, List<LLStackElem>>(
				Variable.STATEMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody27)) );
		////////28
		LLStackElem rBody28[]= {
			new ActionRecord("prod28 assignmentOrMethodCall->"),
			Variable.DIM_LOCATION,
			new SynthesizeRecord("prod28DimLocation_"),
			Tag.ASSIGNMENT,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod28Expression_"),
			Tag.SEMICOLON,
			new SynthesizeRecord("prod28Semicolon_"),
		};
		Parser.rules[28]= new Pair<Variable, List<LLStackElem>>(
				Variable.ASSIGNMENT_OR_METHODCALL, new ArrayList<LLStackElem>( Arrays.asList(rBody28)) );
		////////29
		LLStackElem rBody29[]= {
			new ActionRecord("prod29 assignmentOrMethodCall->"),
			Tag.OPEN_PARENTHESIS,
			Variable.PARAMETERS,
			new SynthesizeRecord("prod29Parameters_"),
			Tag.CLOSE_PARENTHESIS,
			Tag.SEMICOLON,
			new SynthesizeRecord("prod29Semicolon_"),
		};
		Parser.rules[29]= new Pair<Variable, List<LLStackElem>>(
				Variable.ASSIGNMENT_OR_METHODCALL, new ArrayList<LLStackElem>( Arrays.asList(rBody29)) );
		////////30
		LLStackElem rBody30[]= {
			new ActionRecord("prod30 optElse->"),
			Tag.ELSE, Variable.BLOCK
		};
		Parser.rules[30]= new Pair<Variable, List<LLStackElem>>(
				Variable.OPT_ELSE, new ArrayList<LLStackElem>( Arrays.asList(rBody30)) );
		////////31
		LLStackElem rBody31[]= {
			new ActionRecord("prod31 optElse->"),
		};
		Parser.rules[31]= new Pair<Variable, List<LLStackElem>>(
				Variable.OPT_ELSE, new ArrayList<LLStackElem>( Arrays.asList(rBody31)) );
		////////32
		LLStackElem rBody32[]= {
			new ActionRecord("prod32 dimLocation->"),
			Tag.OPEN_BRACKET,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod32Expression_"),
			Tag.CLOSE_BRACKET,
			Variable.DIM_LOCATION
		};
		Parser.rules[32]= new Pair<Variable, List<LLStackElem>>(
			Variable.DIM_LOCATION, new ArrayList<LLStackElem>( Arrays.asList(rBody32)) );
		////////33
		LLStackElem rBody33[]= {
			new ActionRecord("prod33 dimLocation->"),
			new SynthesizeRecord("prod33Eps_")
		};
		Parser.rules[33]= new Pair<Variable, List<LLStackElem>>(
			Variable.DIM_LOCATION, new ArrayList<LLStackElem>( Arrays.asList(rBody33)) );
		////////34
		LLStackElem rBody34[]= {
			new ActionRecord("prod34 location->"),
			new ActionRecord("prod34_id"),
			Tag.ID,
			Variable.DIM_LOCATION
		};
		Parser.rules[34]= new Pair<Variable, List<LLStackElem>>(
			Variable.LOCATION, new ArrayList<LLStackElem>( Arrays.asList(rBody34)) );
		////////35
		LLStackElem rBody35[]= {
			new ActionRecord("prod35 assignment->"),
			Variable.LOCATION,
			new SynthesizeRecord("prod35Location_"),
			Tag.ASSIGNMENT,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod35Expression_"),
			new SynthesizeRecord("prod35Expression__")
		};
		Parser.rules[35]= new Pair<Variable, List<LLStackElem>>(
			Variable.ASSIGNMENT, new ArrayList<LLStackElem>( Arrays.asList(rBody35)) );
		////////36
		LLStackElem rBody36[]= {
			new ActionRecord("prod36 retExpr->"),
			Variable.EXPRESSION
		};
		Parser.rules[36]= new Pair<Variable, List<LLStackElem>>(
				Variable.RET_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody36)) );
		////////37
		LLStackElem rBody37[]= {
			new ActionRecord("prod37 retExpr->"),
		};
		Parser.rules[37]= new Pair<Variable, List<LLStackElem>>(
				Variable.RET_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody37)) );
		////////38
		LLStackElem rBody38[]= {
			new ActionRecord("prod38 expression->"),
			Variable.EXPR_0
		};
		Parser.rules[38]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPRESSION, new ArrayList<LLStackElem>( Arrays.asList(rBody38)) );
		////////39
		LLStackElem rBody39[]= {
			new ActionRecord("prod39 expr0->"),
			Variable.EXPR_1,
			new SynthesizeRecord("prod39Expr1_"),
			Variable.R_EXPR_0,
			new SynthesizeRecord("prod39RExpr0_"),
			new SynthesizeRecord("prod39RExpr0__"),
		};
		Parser.rules[39]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_0, new ArrayList<LLStackElem>( Arrays.asList(rBody39)) );
		////////40
		LLStackElem rBody40[]= {
			new ActionRecord("prod40 rExpr0->"),
			Variable.COND_OP_0,
			Variable.EXPR_1,
			new SynthesizeRecord("prod40Expr1_"),
			Variable.R_EXPR_0,
			new SynthesizeRecord("prod40RExpr0_"),
			new SynthesizeRecord("prod40RExpr0__"),
		};
		Parser.rules[40]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_0, new ArrayList<LLStackElem>( Arrays.asList(rBody40)) );
		////////41
		LLStackElem rBody41[]= {
			new ActionRecord("prod41 rExpr0->"),
		};
		Parser.rules[41]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_0, new ArrayList<LLStackElem>( Arrays.asList(rBody41)) );
		////////42
		LLStackElem rBody42[]= {
			new ActionRecord("prod42 expr1->"),
			Variable.EXPR_2,
			new SynthesizeRecord("prod42Expr2_"),
			Variable.R_EXPR_1,
			new SynthesizeRecord("prod42RExpr1_"),
			new SynthesizeRecord("prod42RExpr1__"),
		};
		Parser.rules[42]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_1, new ArrayList<LLStackElem>( Arrays.asList(rBody42)) );
		////////43
		LLStackElem rBody43[]= {
			new ActionRecord("prod43 rExpr1->"),
			Variable.COND_OP_1,
			Variable.EXPR_2,
			new SynthesizeRecord("prod43Expr2_"),
			Variable.R_EXPR_1,
			new SynthesizeRecord("prod43RExpr1_"),
			new SynthesizeRecord("prod43RExpr1__"),
		};
		Parser.rules[43]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_1, new ArrayList<LLStackElem>( Arrays.asList(rBody43)) );
		////////44
		LLStackElem rBody44[]= {
			new ActionRecord("prod44 rExpr1->"),
		};
		Parser.rules[44]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_1, new ArrayList<LLStackElem>( Arrays.asList(rBody44)) );
		////////45
		LLStackElem rBody45[]= {
			new ActionRecord("prod45 expr2->"),
			Variable.EXPR_3,
			new SynthesizeRecord("prod45Expr3_"),
			Variable.R_EXPR_2,
			new SynthesizeRecord("prod45RExpr2_"),
			new SynthesizeRecord("prod45RExpr2__"),
		};
		Parser.rules[45]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_2, new ArrayList<LLStackElem>( Arrays.asList(rBody45)) );
		////////46
		LLStackElem rBody46[]= {
			new ActionRecord("prod46 rExpr2->"),
			new ActionRecord("prod46_eqOp"),
			Variable.EQ_OP,
			Variable.EXPR_3,
			new SynthesizeRecord("prod46Expr3_"),
			Variable.R_EXPR_2,
			new SynthesizeRecord("prod46RExpr2_"),
			new SynthesizeRecord("prod46RExpr2__"),
		};
		Parser.rules[46]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_2, new ArrayList<LLStackElem>( Arrays.asList(rBody46)) );
		////////47
		LLStackElem rBody47[]= {
			new ActionRecord("prod47 rExpr2->"),
		};
		Parser.rules[47]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_2, new ArrayList<LLStackElem>( Arrays.asList(rBody47)) );
		////////48
		LLStackElem rBody48[]= {
			new ActionRecord("prod48 expr3->"),
			Variable.EXPR_4,
			new SynthesizeRecord("prod48Expr4_"),
			Variable.R_EXPR_3,
			new SynthesizeRecord("prod48RExpr3_"),
			new SynthesizeRecord("prod48RExpr3__"),
		};
		Parser.rules[48]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_3, new ArrayList<LLStackElem>( Arrays.asList(rBody48)) );
		////////49
		LLStackElem rBody49[]= {
			new ActionRecord("prod49 rExpr3->"),
			new ActionRecord("prod49_relOp"),
			Variable.REL_OP,
			Variable.EXPR_4,
			new SynthesizeRecord("prod49Expr4_"),
			Variable.R_EXPR_3,
			new SynthesizeRecord("prod49RExpr3_"),
			new SynthesizeRecord("prod49RExpr3__"),
		};
		Parser.rules[49]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_3, new ArrayList<LLStackElem>( Arrays.asList(rBody49)) );
		////////50
		LLStackElem rBody50[]= {
			new ActionRecord("prod50 rExpr3->"),
		};
		Parser.rules[50]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_3, new ArrayList<LLStackElem>( Arrays.asList(rBody50)) );
		////////51
		LLStackElem rBody51[]= {
			new ActionRecord("prod51 expr4->"),
			Variable.EXPR_5,
			new SynthesizeRecord("prod51Expr5_"),
			Variable.R_EXPR_4,
			new SynthesizeRecord("prod51RExpr4_"),
			new SynthesizeRecord("prod51RExpr4__"),
		};
		Parser.rules[51]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_4, new ArrayList<LLStackElem>( Arrays.asList(rBody51)) );
		////////52
		LLStackElem rBody52[]= {
			new ActionRecord("prod52 rExpr4->"),
			new ActionRecord("prod52_arithOp0"),
			Variable.ARITH_OP_0,
			Variable.EXPR_5,
			new SynthesizeRecord("prod52Expr5_"),
			Variable.R_EXPR_4,
			new SynthesizeRecord("prod52RExpr4_"),
			new SynthesizeRecord("prod52RExpr4__"),
		};
		Parser.rules[52]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_4, new ArrayList<LLStackElem>( Arrays.asList(rBody52)) );
		////////53
		LLStackElem rBody53[]= {
			new ActionRecord("prod53 rExpr4->"),
		};
		Parser.rules[53]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_4, new ArrayList<LLStackElem>( Arrays.asList(rBody53)) );
		////////54
		LLStackElem rBody54[]= {
			new ActionRecord("prod54 expr5->"),
			Variable.EXPR_6,
			new SynthesizeRecord("prod54Expr6_"),
			Variable.R_EXPR_5,
			new SynthesizeRecord("prod54RExpr5_"),
			new SynthesizeRecord("prod54RExpr5__"),
		};
		Parser.rules[54]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_5, new ArrayList<LLStackElem>( Arrays.asList(rBody54)) );
		////////55
		LLStackElem rBody55[]= {
			new ActionRecord("prod55 rExpr5->"),
			new ActionRecord("prod55_arithOp1"),
			Variable.ARITH_OP_1,
			Variable.EXPR_6,
			new SynthesizeRecord("prod55Expr6_"),
			Variable.R_EXPR_5,
			new SynthesizeRecord("prod55RExpr5_"),
			new SynthesizeRecord("prod55RExpr5__"),
		};
		Parser.rules[55]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_5, new ArrayList<LLStackElem>( Arrays.asList(rBody55)) );
		////////56
		LLStackElem rBody56[]= {
			new ActionRecord("prod56 rExpr5->"),
		};
		Parser.rules[56]= new Pair<Variable, List<LLStackElem>>(
				Variable.R_EXPR_5, new ArrayList<LLStackElem>( Arrays.asList(rBody56)) );
		////////57
		LLStackElem rBody57[]= {
			new ActionRecord("prod57 expr6->"),
			Tag.NOT,
			Variable.EXPR_6,
			new SynthesizeRecord("prod57Expr6_"),
		};
		Parser.rules[57]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_6, new ArrayList<LLStackElem>( Arrays.asList(rBody57)) );
		////////58
		LLStackElem rBody58[]= {
			new ActionRecord("prod58 expr6->"),
			Variable.EXPR_7
		};
		Parser.rules[58]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_6, new ArrayList<LLStackElem>( Arrays.asList(rBody58)) );
		////////59
		LLStackElem rBody59[]= {
			new ActionRecord("prod59 expr7->"),
			Tag.MINUS,
			Variable.EXPR_7,
			new SynthesizeRecord("prod59Expr7_"),
		};
		Parser.rules[59]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_7, new ArrayList<LLStackElem>( Arrays.asList(rBody59)) );
		////////60
		LLStackElem rBody60[]= {
			new ActionRecord("prod60 expr7->"),
			Variable.EXPR_8
		};
		Parser.rules[60]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_7, new ArrayList<LLStackElem>( Arrays.asList(rBody60)) );
		////////61
		LLStackElem rBody61[]= {
			new ActionRecord("prod61 expr8->"),
			Variable.ATOMIC_EXPR
		};
		Parser.rules[61]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_8, new ArrayList<LLStackElem>( Arrays.asList(rBody61)) );
		////////62
		LLStackElem rBody62[]= {
			new ActionRecord("prod62 expr8->"),
			Tag.OPEN_PARENTHESIS,
			Variable.EXPR_0,
			new SynthesizeRecord("prod62Expr0_"),
			Tag.CLOSE_PARENTHESIS
		};
		Parser.rules[62]= new Pair<Variable, List<LLStackElem>>(
				Variable.EXPR_8, new ArrayList<LLStackElem>( Arrays.asList(rBody62)) );
		////////63
		LLStackElem rBody63[]= {
			new ActionRecord("prod63 condOp0->"),
			Tag.OROR
		};
		Parser.rules[63]= new Pair<Variable, List<LLStackElem>>(
				Variable.COND_OP_0, new ArrayList<LLStackElem>( Arrays.asList(rBody63)) );
		////////64
		LLStackElem rBody64[]= {
			new ActionRecord("prod64 condOp1->"),
			Tag.ANDAND
		};
		Parser.rules[64]= new Pair<Variable, List<LLStackElem>>(
				Variable.COND_OP_1, new ArrayList<LLStackElem>( Arrays.asList(rBody64)) );
		////////65
		LLStackElem rBody65[]= {
			new ActionRecord("prod65 eqOp->"),
			Tag.EQ
		};
		Parser.rules[65]= new Pair<Variable, List<LLStackElem>>(
				Variable.EQ_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody65)) );
		////////66
		LLStackElem rBody66[]= {
			new ActionRecord("prod66 eqOp->"),
			Tag.NEQ
		};
		Parser.rules[66]= new Pair<Variable, List<LLStackElem>>(
				Variable.EQ_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody66)) );
		////////67
		LLStackElem rBody67[]= {
			new ActionRecord("prod67 relOp->"),
			Tag.LESS_THAN
		};
		Parser.rules[67]= new Pair<Variable, List<LLStackElem>>(
				Variable.REL_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody67)) );
		////////68
		LLStackElem rBody68[]= {
			new ActionRecord("prod68 relOp->"),
			Tag.LE
		};
		Parser.rules[68]= new Pair<Variable, List<LLStackElem>>(
				Variable.REL_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody68)) );
		////////69
		LLStackElem rBody69[]= {
			new ActionRecord("prod69 relOp->"),
			Tag.GE
		};
		Parser.rules[69]= new Pair<Variable, List<LLStackElem>>(
				Variable.REL_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody69)) );
		////////70
		LLStackElem rBody70[]= {
			new ActionRecord("prod70 relOp->"),
			Tag.GREATER_THAN
		};
		Parser.rules[70]= new Pair<Variable, List<LLStackElem>>(
				Variable.REL_OP, new ArrayList<LLStackElem>( Arrays.asList(rBody70)) );
		////////71
		LLStackElem rBody71[]= {
			new ActionRecord("prod71 arithOp0->"),
			Tag.PLUS
		};
		Parser.rules[71]= new Pair<Variable, List<LLStackElem>>(
				Variable.ARITH_OP_0, new ArrayList<LLStackElem>( Arrays.asList(rBody71)) );
		////////72
		LLStackElem rBody72[]= {
			new ActionRecord("prod72 arithOp0->"),
			Tag.MINUS
		};
		Parser.rules[72]= new Pair<Variable, List<LLStackElem>>(
				Variable.ARITH_OP_0, new ArrayList<LLStackElem>( Arrays.asList(rBody72)) );
		////////73
		LLStackElem rBody73[]= {
			new ActionRecord("prod73 arithOp1->"),
			Tag.MULTIPLICATION
		};
		Parser.rules[73]= new Pair<Variable, List<LLStackElem>>(
				Variable.ARITH_OP_1, new ArrayList<LLStackElem>( Arrays.asList(rBody73)) );
		////////74
		LLStackElem rBody74[]= {
			new ActionRecord("prod74 arithOp1->"),
			Tag.DIVISION
		};
		Parser.rules[74]= new Pair<Variable, List<LLStackElem>>(
				Variable.ARITH_OP_1, new ArrayList<LLStackElem>( Arrays.asList(rBody74)) );
		////////75
		LLStackElem rBody75[]= {
			new ActionRecord("prod75 arithOp1->"),
			Tag.MODULE
		};
		Parser.rules[75]= new Pair<Variable, List<LLStackElem>>(
				Variable.ARITH_OP_1, new ArrayList<LLStackElem>( Arrays.asList(rBody75)) );
		////////76
		LLStackElem rBody76[]= {
			new ActionRecord("prod76 atomicExpr->"),
			new ActionRecord("prod76_id"),
			Tag.ID,
			Variable.LOCATION_OR_METHODCALL
		};
		Parser.rules[76]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody76)) );
		////////77
		LLStackElem rBody77[]= {
			new ActionRecord("prod77 atomicExpr->"),
			new ActionRecord("prod77_intNum"),
			Tag.INT_NUM,
			new SynthesizeRecord("prod77IntNum_")
		};
		Parser.rules[77]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody77)) );
		////////78
		LLStackElem rBody78[]= {
			new ActionRecord("prod78 atomicExpr->"),
			new ActionRecord("prod78_realNum"),
			Tag.REAL_NUM,
			new SynthesizeRecord("prod78RealNum_")
		};
		Parser.rules[78]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody78)) );
		////////79
		LLStackElem rBody79[]= {
			new ActionRecord("prod79 atomicExpr->"),
			new ActionRecord("prod79_charLiteral"),
			Tag.CHAR_LITERAL,
			new SynthesizeRecord("prod79CharLiteral_")
		};
		Parser.rules[79]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody79)) );
		////////80
		LLStackElem rBody80[]= {
			new ActionRecord("prod80 atomicExpr->"),
			Tag.TRUE,
			new SynthesizeRecord("prod80True_")
		};
		Parser.rules[80]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody80)) );
		////////81
		LLStackElem rBody81[]= {
			new ActionRecord("prod81 atomicExpr->"),
			Tag.FALSE,
			new SynthesizeRecord("prod81False_")
		};
		Parser.rules[81]= new Pair<Variable, List<LLStackElem>>(
				Variable.ATOMIC_EXPR, new ArrayList<LLStackElem>( Arrays.asList(rBody81)) );
		////////82
		LLStackElem rBody82[]= {
			new ActionRecord("prod82 locationOrMethodCall->"),
			Variable.DIM_LOCATION
		};
		Parser.rules[82]= new Pair<Variable, List<LLStackElem>>(
				Variable.LOCATION_OR_METHODCALL, new ArrayList<LLStackElem>( Arrays.asList(rBody82)) );
		////////83
		LLStackElem rBody83[]= {
			new ActionRecord("prod83 locationOrMethodCall->"),
			Tag.OPEN_PARENTHESIS,
			Variable.PARAMETERS,
			new SynthesizeRecord("prod83Parameters_"),
			Tag.CLOSE_PARENTHESIS,
			new SynthesizeRecord("prod83CloseParenthesis_"),
		};
		Parser.rules[83]= new Pair<Variable, List<LLStackElem>>(
				Variable.LOCATION_OR_METHODCALL, new ArrayList<LLStackElem>( Arrays.asList(rBody83)) );
		////////84
		LLStackElem rBody84[]= {
			new ActionRecord("prod84 parameters->"),
			Variable.EXPRESSION,
			new SynthesizeRecord("prod84Expression_"),
			Variable.R_PARAMETERS
		};
		Parser.rules[84]= new Pair<Variable, List<LLStackElem>>(
				Variable.PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody84)) );
		////////85
		LLStackElem rBody85[]= {
			new ActionRecord("prod85 parameters->"),
		};
		Parser.rules[85]= new Pair<Variable, List<LLStackElem>>(
				Variable.PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody85)) );
		////////86
		LLStackElem rBody86[]= {
			new ActionRecord("prod86 rParameters->"),
			Tag.COMMA,
			Variable.EXPRESSION,
			new SynthesizeRecord("prod86Expression_"),
			Variable.R_PARAMETERS
		};
		Parser.rules[86]= new Pair<Variable, List<LLStackElem>>(
				Variable.PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody86)) );
		////////87
		LLStackElem rBody87[]= {
			new ActionRecord("prod87 rParameters->"),
		};
		Parser.rules[87]= new Pair<Variable, List<LLStackElem>>(
				Variable.PARAMETERS, new ArrayList<LLStackElem>( Arrays.asList(rBody87)) );
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
		String errLocation= Lexer.getTokenLocation();
		
		List<Tag> synchTags= null;
		LLStackElem topSymbol= this.LL1Stack.peek();
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
	public void parse() throws IOException, ErrorLexer, ErrorParser, ErrorSemantics{
		if( this.recoverd)
			this.getNextToken();
		else	//	errorHandler have chosen the right Token
			this.recoverd= true;
			
		Tag inputTag= this.look.tag;
		LLStackElem topSymbol= this.LL1Stack.peek();
		
		while(!this.LL1Stack.empty()){
			
			if( topSymbol instanceof Tag ){
				Tag stackTag= (Tag)topSymbol;
				
				if( stackTag.equals(inputTag) ){
					this.LL1Stack.pop();
					
					if( inputTag == Tag.EOF )
						break;
						
					this.getNextToken();
					inputTag= this.look.tag;
				}
				else
					errorHandler("--Unexpected Token= "+ this.look.getLexeme()+ "--"+ stackTag+ "is expected", true);
			}
			else if( topSymbol instanceof Variable ){
				Variable var= (Variable)topSymbol;
				
				List<LLStackElem> ruleBody= getDerivationFromLL1Table(var, inputTag);
				if( ruleBody == null )
					errorHandler("--Unexpected Token= "+ this.look.getLexeme()+ "\n \t\t--"+
						"previous Token is missing or the Token must be one of the following \n \t\t"+ 
						getExpectedTags(var), false );
				else{
					this.LL1Stack.pop();
					
					List<LLStackElem> rB= new ArrayList<LLStackElem>(ruleBody);
					Collections.reverse(rB);
					for( LLStackElem symbol: rB ) {
						
						if( symbol instanceof ActionRecord)
							symbol = new ActionRecord( ((ActionRecord) symbol).getActionRecName() );
						else if( symbol instanceof SynthesizeRecord)
							symbol = new SynthesizeRecord( ((SynthesizeRecord) symbol).getSynthesizeRecName() );
						else if( symbol instanceof Variable)
							symbol = new Variable( ((Variable) symbol).code, ((Variable) symbol).getName() );
						
						this.LL1Stack.push(symbol);
					}
					
					//move inh attributes to our conventional action record
					ActionRecord acRec= (ActionRecord)this.LL1Stack.pop();
					acRec.inhAttr= var.inhAttr;
					this.LL1Stack.push(acRec);
				}
			}
			else if( topSymbol instanceof ActionRecord ){
				this.LL1Stack.pop();
				doTheAction( ((ActionRecord)topSymbol).getActionRecName(), (ActionRecord)topSymbol );
			}
			else if( topSymbol instanceof SynthesizeRecord ){
				this.LL1Stack.pop();
				synthesize( ((SynthesizeRecord)topSymbol).getSynthesizeRecName(), (SynthesizeRecord)topSymbol );
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

	private List<LLStackElem> getDerivationFromLL1Table(Variable var, Tag tag){
		Pair<Integer, Integer> p= new Pair<Integer, Integer>(var.code, tag.code );
		Integer ruleNumber= Parser.LL1Table.get(p);
		if( ruleNumber == null )
			return null;
		else{
			Pair<Variable, List<LLStackElem>> theRule= Parser.rules[ruleNumber];
			return theRule.second;
		}
	}
	
	
	private boolean haveSeenMain= false;
	private int usedMemory = 0;
	
	private Access offset(Location loc, ArrayList<Expr> dims) throws ErrorSemantics {
		Expr dim; Expr typeWidth; Expr t1, t2; Expr index; // inherit id
		
		Type type = loc.type;
		dim = dims.get(0);
		type = ((Array)type).of;
		
		typeWidth = new Constant(this.topEnv, new IntNum(type.width), Type.INT);
		t1 = new Arith(this.topEnv, new Token(Tag.MULTIPLICATION), dim, typeWidth);
		index = t1;
		for (int i = 1; i < dims.size(); i++) {
			dim = dims.get(i);
			type = ((Array)type).of;
			typeWidth = new Constant(this.topEnv, new IntNum(type.width), Type.INT);
			t1 = new Arith(this.topEnv, new Token(Tag.MULTIPLICATION), dim, typeWidth);
			t2 = new Arith(this.topEnv, new Token(Tag.PLUS), index, t1);
			index = t2;
		}
		 return new Access(this.topEnv, loc, index, type);
	}
	
	private ArrayList<LLStackElem> tempPoped= new ArrayList<LLStackElem>();
	
	private LLStackElem tempoararyPop(int ntimes) {
		for(int i= 0; i<ntimes; i++)
			tempPoped.add( this.LL1Stack.pop() );
		Collections.reverse(tempPoped);
		return tempPoped.get(0);
	}
	
	private void pushBackTempPoped() {
		for(LLStackElem gSym: tempPoped)
			this.LL1Stack.push(gSym);
		tempPoped.clear();
	}
	
	private void addAllAttrToVariable(int nth, Object[] objs){
		Variable var= (Variable)tempoararyPop(nth);
		for( Object obj: objs )
			var.inhAttr.add(obj);
		tempPoped.set(0, var);
		pushBackTempPoped();
	}
	
	private void addAllAttrToActionRec(int nth, Object[] objs){
		ActionRecord acRec= (ActionRecord)tempoararyPop(nth);
		for( Object obj: objs )
			acRec.inhAttr.add(obj);
		tempPoped.set(0, acRec);
		pushBackTempPoped();
	}
	
	private void addAllAttrToSynthesizeRec(int nth, Object[] objs){
		SynthesizeRecord synthRec= (SynthesizeRecord)tempoararyPop(nth);
		for( Object obj: objs )
			synthRec.synthAttr.add(obj);
		tempPoped.set(0, synthRec);
		pushBackTempPoped();
	}
	
	private void doTheAction(String actionRecName, ActionRecord theActionRec) throws ErrorSemantics{
		
		switch(actionRecName){
			case "prod0_EOF":
				prod0_EOF();
				break;
			
			case "prod1_Id":
				prod1_Id();
				break;
				
			case "prod2_basicType":
				prod2_basicType();
				break;
				
			case "prod2_id":
				prod2_id();
				break;
				
			case "prod4 rProgram->":
				prod4RProgramInh(theActionRec);
				break;
				
			case "prod5 rProgram->":
				prod5RProgramInh(theActionRec);
				break;
				
			case "prod6_BasicType":
				prod6_BasicType();
				break;
				
			case "prod6_Id":
				prod6_Id();
				break;
			
			case "prod7 formalParameters->":
				prod7FormalParametersInh(theActionRec);
				break;
				
			///no need for new methods already have in prod6
			case "prod8_BasicType":
				prod6_BasicType();
				break;
				
			case "prod8_Id":
				prod6_Id();
				break;
			///
			
			case "prod9 rFormalParameters->":
				prod9RFormalParametersInh(theActionRec);
				break;
			
			case "prod10 rVarList->":
				prod10RVarListInh(theActionRec);
				break;
				
			case "prod10_id":
				prod10_id();
				break;
				
			case "prod12 dimDeclaration->":
				prod12DimDeclarationinh(theActionRec);
				break;
			
			case "prod12_intNum":
				prod12_intNum();
				break;
			
			case "prod13 dimDeclaration->":
				prod13DimDeclarationInh(theActionRec);
				break;
				
			case "prod14 block->":
				prod14BlockInh(theActionRec);
				break;
				
			case "prod14_blockContents":
				prod14_blockContents(theActionRec);
				break;
			
			case "prod14_closeBrace":
				prod14_closeBrace(theActionRec);
				break;
				
			case "prod15_basicType":
				prod15_basicType();
				break;
				
			case "prod15_id":
				prod15_id();
				break;
			////
				
			case "prod18_id":
				prod18_id();
				break;
				
			case "prod20_openParenthesis":
				prod20_openParenthesis();
				break;
				
			case "prod21_openParenthesis":
				prod21_openParenthesis();
				break;
				
			case "prod26_reader":
				prod26_reader();
				break;
				
			case "prod27_writer":
				prod27_writer();
				break;	
				
			case "prod28 assignmentOrMethodCall->":
				prod28AssignmentOrMethodCallInh(theActionRec);
				break;

			case "prod29 assignmentOrMethodCall->":
				prod29AssignmentOrMethodCallInh(theActionRec);
				break;
				
			case "prod32 dimLocation->":
				prod32DimLocationInh(theActionRec);
				break;
				
			case "prod33 dimLocation->":
				prod33DimLocationInh(theActionRec);
				break;
			
			case "prod34_id":
				prod34_id();
				break;
				
			case "prod46_eqOp":
				prod46_eqOp();
				break;
				
			case "prod49_relOp":
				prod46_eqOp();
				break;
				
			case "prod52_arithOp0":
				prod46_eqOp();
				break;
				
			case "prod55_arithOp1":
				prod46_eqOp();
				break;
				
			case "prod76_id":
				prod76_id();
				break;
				
			case "prod77_intNum":
				prod77_intNum();
				break;
				
			case "prod78_realNum":
				prod77_intNum();
				break;
				
			case "prod79_charLiteral":
				prod77_intNum();
				break;
				
			case "prod82 locationOrMethodCall->":
				prod82LocationOrMethodCallInh(theActionRec);
				break;
				
			case "prod83 locationOrMethodCall->":
				prod83LocationOrMethodCallInh(theActionRec);
				break;
				
			default:
				//do nothing
				break;
		}
	}
	
	private void prod0_EOF() throws ErrorSemantics{
		if(!haveSeenMain)
			Node.errorHandler("--The program must contain void main()");
	}
	
	private void prod1_Id(){
		Object[] objs= {null, this.look};//we know the type is void hence the null
		addAllAttrToSynthesizeRec(4, objs);
	}
	
	private void prod2_basicType() {
		Object[] objs = { this.look };
		addAllAttrToVariable(4, objs);
	}
	
	private void prod2_id() {
		Object[] objs = { this.look };
		addAllAttrToVariable(2, objs);
	}
	
	private void prod4RProgramInh(ActionRecord theActionRec) {
		//ActionRecord must contain: type, id
		Object type = theActionRec.inhAttr.get(0);
		Object[] objs = { type };
		addAllAttrToVariable(1, objs);
		
		addAllAttrToVariable(3, objs);
		
		Object id = theActionRec.inhAttr.get(1);
		objs = new Object[] { id };
		addAllAttrToSynthesizeRec(2, objs);
	}
	
	private void prod5RProgramInh(ActionRecord theActionRec) {
		addAllAttrToSynthesizeRec(3, theActionRec.inhAttr.toArray());
	}
	
	private void prod6_BasicType(){
		Object[] objs= {this.look};
		addAllAttrToSynthesizeRec(5, objs);
	}
	
	private void prod6_Id(){
		Object[] objs= {this.look};
		addAllAttrToSynthesizeRec(3, objs);
	}
	
	private void prod7FormalParametersInh(ActionRecord theActionRec){
		addAllAttrToSynthesizeRec(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod9RFormalParametersInh(ActionRecord theActionRec){
		addAllAttrToSynthesizeRec(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod10RVarListInh(ActionRecord theActionRec) {
		addAllAttrToVariable(4, theActionRec.inhAttr.toArray());
		
		addAllAttrToVariable(6, theActionRec.inhAttr.toArray());
	}
	
	private void prod10_id() {
		Object[] objs = { this.look };
		addAllAttrToSynthesizeRec(3, objs);
	}
	
	private void prod12DimDeclarationinh(ActionRecord theActionRec){
		addAllAttrToVariable(5, theActionRec.inhAttr.toArray());
	}
	
	private void prod12_intNum(){
		Object[] objs= {this.look};
		addAllAttrToSynthesizeRec(4, objs);
	}
	
	private void prod13DimDeclarationInh(ActionRecord theActionRec){
		addAllAttrToSynthesizeRec(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod14BlockInh(ActionRecord theActionRec){
		addAllAttrToActionRec(2, theActionRec.inhAttr.toArray());
	}
	
	private void prod14_blockContents(ActionRecord theActionRec) {
		//for does not define any new variable hence
		//we have already covered it
		//for procedure call: must add argument to environment too
		//ActionRecord must contain: (procInfo)?
		Object[] objs = { this.topEnv };
		addAllAttrToActionRec(3, objs);
		
		this.topEnv = new Environment(this.topEnv);
		
		int len= theActionRec.inhAttr.size();
		if( len == 0 || len == 1 ){
			if( len == 1){
				ProcInfo procInfo= (ProcInfo) theActionRec.inhAttr.get(0);
				for( Pair<Type, Word> ptw: procInfo.getFormalPars() ){
					VarInfo varInfo= new VarInfo(ptw.second, ptw.first, this.usedMemory, false);
					this.usedMemory+= ptw.first.width;
					this.topEnv.put(ptw.second, varInfo);
				}
			}
		}
		else{
			System.err.println("bug in passing attributes to prod14_blockContents");
			System.exit(-1);
		}
			
	}
	
	private void prod14_closeBrace(ActionRecord theActionRec) {
		//ActionRecord must contain: prevEnv
		Environment prevEnv = (Environment) theActionRec.inhAttr.get(0);
		
		this.topEnv = prevEnv;
	}
	
	private void prod15_basicType(){
		Object[] objs= {this.look};
		addAllAttrToVariable(4, objs);
	}

	private void prod15_id(){
		Object[] objs= {this.look};
		addAllAttrToSynthesizeRec(3, objs);
	}

	private void prod18_id() {
		Object[] objs = { this.look };
		addAllAttrToVariable(2, objs);
	}
	
	private void prod20_openParenthesis() {
		While whileNode = new While(this.topEnv);

		Object[] objs = { Stmt.enclosing, whileNode };
		addAllAttrToSynthesizeRec(7, objs);
		
		Stmt.enclosing = whileNode;
	}
	
	private void prod21_openParenthesis() {
		For forNode = new For(this.topEnv);
		
		Object[] objs = { Stmt.enclosing, forNode };
		addAllAttrToSynthesizeRec(13, objs);
		
		Stmt.enclosing = forNode;
	}
	
	private void prod26_reader(){
		Object[] objs= { this.look };
		addAllAttrToSynthesizeRec(3, objs);
	}
	
	private void prod27_writer(){
		Object[] objs= { this.look };
		addAllAttrToSynthesizeRec(3, objs);
	}
	
	private void prod28AssignmentOrMethodCallInh(ActionRecord theActionRec) {
		addAllAttrToVariable(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod29AssignmentOrMethodCallInh(ActionRecord theActionRec){
		addAllAttrToSynthesizeRec(6, theActionRec.inhAttr.toArray());
	}
	
	private void prod32DimLocationInh(ActionRecord theActionRec) {
		addAllAttrToVariable(5, theActionRec.inhAttr.toArray());
	}
	
	private void prod33DimLocationInh(ActionRecord theActionRec) {
		addAllAttrToSynthesizeRec(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod34_id(){
		Object[] objs= { this.look };
		addAllAttrToVariable(2, objs);
	}
	
	private void prod46_eqOp() {
		Object[] objs = { this.look };
		addAllAttrToSynthesizeRec(6, objs);
	}
	
	private void prod76_id() {
		Object[] objs = { this.look };
		addAllAttrToVariable(2, objs);
	}
	
	private void prod77_intNum() {
		Object[] objs = { this.look };
		addAllAttrToSynthesizeRec(2, objs);
	}
	
	private void prod82LocationOrMethodCallInh(ActionRecord theActionRec) {
		addAllAttrToVariable(1, theActionRec.inhAttr.toArray());
	}
	
	private void prod83LocationOrMethodCallInh(ActionRecord theActionRec) {
		addAllAttrToSynthesizeRec(5, theActionRec.inhAttr.toArray());
	}
	
	private void synthesize(String synthRecName, SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		switch (synthRecName){
			case "prod0Program_":
				prod0Program_(theSynthsizeRec);
				break;
			
			case "prod1FormalParameters_":
				prod1FormalParameters_(theSynthsizeRec);
				break;
				
			case "prod1Block_":
				prod1Block_(theSynthsizeRec);
				break;
			
			case "prod1Program_":
				prod1Program_(theSynthsizeRec);
				break;
				
			case "prod4DimDeclaration_":
				prod4DimDeclaration_(theSynthsizeRec);
				break;
				
			case "prod4RVarList_":
				prod4RVarList_(theSynthsizeRec);
				break;
				
			case "prod5FormalParameters_":
				prod1FormalParameters_(theSynthsizeRec); // no need for new method
				break;
				
			case "prod5Block_":
				prod1Block_(theSynthsizeRec); // no need for new method
				break;
				
			case "prod5Program_":
				prod1Program_(theSynthsizeRec); // no need for new method
				break;
				
			case "prod6RFormalParameters_":
				prod6RFormalParameters_(theSynthsizeRec);
				break;
			
			case "prod7Eps_":
				prod7Eps_(theSynthsizeRec);
				break;
			
			//identical to 6
			case "prod8RFormalParameters_":
				prod6RFormalParameters_(theSynthsizeRec);
				break;
				
			case "prod9Eps_":
				prod9Eps_(theSynthsizeRec);
				break;
				
			case "prod10DimDeclaration_":
				prod10DimDeclaration_(theSynthsizeRec);
				break;
				
			case "prod10RVarList_":
				prod10RVarList_(theSynthsizeRec);
				break;
				
			case "prod12DimDeclaration_":
				prod12DimDeclaration_(theSynthsizeRec);
				break;
			
			case "prod13Eps_":
				prod13Eps_(theSynthsizeRec);
				break;

			case "prod14BlockContents_":
				prod14BlockContents_(theSynthsizeRec);
				break;
			
			case "prod15DimDeclaratioin_":
				prod15DimDeclaratioin_(theSynthsizeRec);
				break;
				
			case "prod16Statement_":
				prod16Statement_(theSynthsizeRec);
				break;
			
			case "prod16BlockContents_":
				prod16BlockContents_(theSynthsizeRec);
				break;
				
			case "prod17Eps_":
				prod17Eps_();
				break;
				
			case "prod19Expression_":
				prod19Expression_(theSynthsizeRec);
				break;
				
			case "prod19Block_":
				prod19Block_(theSynthsizeRec);
				break;
				
			case "prod19OptElse_":
				prod19OptElse_(theSynthsizeRec);
				break;
				
			case "prod19OptElse__":
				prod19OptElse__(theSynthsizeRec);
				break;
				
			case "prod20Expression_":
				prod20Expression_(theSynthsizeRec);
				break;
				
			case "prod20Block_":
				prod20Block_(theSynthsizeRec);
				break;
				
			case "prod20Block__":
				prod20Block__(theSynthsizeRec);
				break;
				
			case "prod21InitAssignment_":
				prod21InitAssignment_(theSynthsizeRec);
				break;
				
			case "prod21Expression_":
				prod21Expression_(theSynthsizeRec);
				break;
				
			case "prod21IterateAssignment_":
				prod21IterateAssignment_(theSynthsizeRec);
				break;
				
			case "prod21Block_":
				prod21Block_(theSynthsizeRec);
				break;
				
			case "prod21Block__":
				prod21Block__(theSynthsizeRec);
				break;
				
			case "prod22RetExpr_":
				prod22RetExpr_(theSynthsizeRec);
				break;
				
			case "prod23Semicolon_":
				prod23Semicolon_();
				break;
				
			case "prod24Semicolon_":
				prod24Semicolon_();
				break;
				
			case "prod26Location_":
				prod26Location_(theSynthsizeRec);
				break;
				
			case "prod27Expression_":
				prod27Expression_(theSynthsizeRec);
				break;
				
			case "prod28DimLocation_":
				prod28DimLocation_(theSynthsizeRec);
				break;
				
			case "prod28Expression_":
				prod28Expression_(theSynthsizeRec);
				break;
				
			case "prod28Semicolon_":
				prod28Semicolon_(theSynthsizeRec);
				break;
				
			case "prod29Parameters_":
				prod29Parameters_(theSynthsizeRec);
				break;
				
			case "prod29Semicolon_":
				prod29Semicolon_(theSynthsizeRec);
				break;
				
			case "prod32Expression_":
				prod32Expression_(theSynthsizeRec);
				break;
				
			case "prod33Eps_":
				prod33Eps_(theSynthsizeRec);
				break;
				
			case "prod35Location_":
				prod35Location_(theSynthsizeRec);
				break;
				
			case "prod35Expression_":
				prod35Expression_(theSynthsizeRec);
				break;

			case "prod35Expression__":
				prod28Semicolon_(theSynthsizeRec);//no need for new method
				break;
				
			case "prod39Expr1_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod39RExpr0_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod39RExpr0__":
				prod39RExpr0__(theSynthsizeRec);
				break;
				
			case "prod40Expr1_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod40RExpr0_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod40RExpr0__":
				prod40RExpr0__(theSynthsizeRec);
				break;
				
			case "prod42Expr2_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod42RExpr1_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod42RExpr1__":
				prod42RExpr1__(theSynthsizeRec);
				break;
				
			case "prod43Expr2_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod43RExpr1_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod43RExpr1__":
				prod43RExpr1__(theSynthsizeRec);
				break;
				
			case "prod45Expr3_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod45RExpr2_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod45RExpr2__":
				prod45RExpr2__(theSynthsizeRec);
				break;
				
			case "prod46Expr3_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod46RExpr2_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod46RExpr2__":
				prod46RExpr2__(theSynthsizeRec);
				break;
				
			case "prod48Expr4_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod48RExpr3_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod48RExpr3__":
				prod48RExpr3__(theSynthsizeRec);
				break;
				
			case "prod49Expr4_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod49RExpr3_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod49RExpr3__":
				prod49RExpr3__(theSynthsizeRec);
				break;
				
			case "prod51Expr5_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod51RExpr4_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod51RExpr4__":
				prod51RExpr4__(theSynthsizeRec);
				break;
				
			case "prod52Expr5_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod52RExpr4_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod52RExpr4__":
				prod52RExpr4__(theSynthsizeRec);
				break;
				
			case "prod54Expr6_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod54RExpr5_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod54RExpr5__":
				prod54RExpr5__(theSynthsizeRec);
				break;
				
			case "prod55Expr6_":
				prod39Expr1_(theSynthsizeRec);
				break;
				
			case "prod55RExpr5_":
				prod39RExpr0_(theSynthsizeRec);
				break;
				
			case "prod55RExpr5__":
				prod55RExpr5__(theSynthsizeRec);
				break;
				
			case "prod57Expr6_":
				prod57Expr6_(theSynthsizeRec);
				break;
				
			case "prod59Expr7_":
				prod59Expr7_(theSynthsizeRec);
				break;
				
			case "prod62Expr0_":
				prod62Expr0_(theSynthsizeRec);
				break;
				
			case "prod77IntNum_":
				prod77IntNum_(theSynthsizeRec);
				break;
				
			case "prod78RealNum_":
				prod78RealNum_(theSynthsizeRec);
				break;
				
			case "prod79CharLiteral_":
				prod79CharLiteral_(theSynthsizeRec);
				break;
				
			case "prod80True_":
				prod80True_(theSynthsizeRec);
				break;
				
			case "prod81False_":
				prod81False_(theSynthsizeRec);
				break;
				
			case "prod83Parameters_":
				prod83Parameters_(theSynthsizeRec);
				break;
				
			case "prod83CloseParenthesis_":
				prod83CloseParenthesis_(theSynthsizeRec);
				break;
				
			case "prod84Expression_":
				prod84Expression_(theSynthsizeRec);
				break;
				
			case "prod86Expression_":
				prod84Expression_(theSynthsizeRec);
				break;
				
			default:
				//do nothing
				break;
		}
	}
	
	private void prod0Program_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- list of procedures
		for(Object obj: theSynthsizeRec.synthAttr)
			Node.prog.addProc((Proc) obj);
	}

	private void prod1FormalParameters_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- type=null, id, arguments(type, id)
		Type retType= (Type)theSynthsizeRec.synthAttr.get(0);
		Word procId= (Word)theSynthsizeRec.synthAttr.get(1);
		
		ProcInfo procInfo= new ProcInfo(retType, procId);
		int len= theSynthsizeRec.synthAttr.size();
		if(len%2 != 0){
			System.err.println("Something is wrong in prod1FormalParameters_:: numAttr is odd");
			System.exit(-1);
		}
		
		for(int i=2; i<len; i+=2){
			Type argType= (Type)theSynthsizeRec.synthAttr.get(i);
			Word argId= (Word)theSynthsizeRec.synthAttr.get(i+1);
			
			if( procInfo.isDuplicatedParName(argId) )
				Node.errorHandler("--duplicated parameter name "+ argId+ "in procedure "+ procId);
			procInfo.addFormalPar(argType, argId);
		}
		
		//don't have polymorphism
		if( Environment.isDuplicatedIdInGlobalEnv(procId) )
			Node.errorHandler("--duplicated procedure name:: " + procId + ", don't have polymorphism");
		
		if(procId.getLexeme().equals("main") && procInfo.getFormalPars().isEmpty() && retType==null)
			if(!haveSeenMain)
				haveSeenMain= true;
			else
				Node.errorHandler("--void main() declared twice");
		
		if( this.topEnv.equals(Environment.GLOBAL_ENV) == false )
			Node.errorHandler("--Can't have procedure definition unless it is in Global environment");
		
		this.topEnv.put(procId, procInfo);
		
		Object[] objs = { procInfo };
		addAllAttrToVariable(2, objs);
		addAllAttrToSynthesizeRec(3, objs);
		
		objs = new Object[] { ProcInfo.enclosingProcInfo };
		addAllAttrToSynthesizeRec(3, objs);
		
		ProcInfo.enclosingProcInfo = procInfo;
	}
	
	private void prod1Block_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- procInfo, enclosingProcInfo, stmt
		ProcInfo procInfo = (ProcInfo) theSynthsizeRec.synthAttr.get(0);
		Stmt stmt = (Stmt) theSynthsizeRec.synthAttr.get(2);
		
		Proc proc = new Proc(this.topEnv, procInfo, stmt);

		Object[] objs = { proc };
		addAllAttrToSynthesizeRec(2, objs);
		
		ProcInfo.enclosingProcInfo = (ProcInfo) theSynthsizeRec.synthAttr.get(1);
	}

	private void prod1Program_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod4DimDeclaration_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod4RVarList_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- list of (id, type)

		if(theSynthsizeRec.synthAttr.size() % 2 != 0) {
			System.err.println("Odd number of elements passed to prod4RVarList_");
			System.exit(-1);
		}
		
		if(this.topEnv != Environment.GLOBAL_ENV) {
			System.err.println("Environment is not global when declaring global variables");
			System.exit(-1);
		}
		
		for(int i = 0; i < theSynthsizeRec.synthAttr.size(); i+=2) {
			Word aWord = (Word) theSynthsizeRec.synthAttr.get(i);
			Type type = (Type) theSynthsizeRec.synthAttr.get(i+1);
			
			if(this.topEnv.doesExist(aWord, false))
				Node.errorHandler("--global variable " + aWord + " is already defined.");
			
			VarInfo varInfo = new VarInfo(aWord, type, usedMemory, true);
			usedMemory += type.width;
			
			this.topEnv.put(aWord, varInfo);
		}
	}
	
	private void prod6RFormalParameters_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod7Eps_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod9Eps_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod10DimDeclaration_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- id, type
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod10RVarList_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- list of (id, type)s
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod12DimDeclaration_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- intNum, type
		Object obj= theSynthsizeRec.synthAttr.get(0);
		if( !(obj instanceof IntNum) )
			Node.errorHandler("--dimension quantifiers (i.e. [THIS]) must be integer type");
		IntNum i= (IntNum) obj;
		int iValue= i.getValue();
		if( iValue <= 0 )
			Node.errorHandler("--dimension quantifiers must be greater than 0");
		
		Type type= (Type)theSynthsizeRec.synthAttr.get(1);
		
		Type exType= new Array(type, iValue);
		Object[] objs= {exType};
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod13Eps_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod14BlockContents_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(3, theSynthsizeRec.synthAttr.toArray());
	}

	private void prod15DimDeclaratioin_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- id, type
		Word aWord = (Word) theSynthsizeRec.synthAttr.get(0);
		Type type = (Type) theSynthsizeRec.synthAttr.get(1);
		
		if( this.topEnv.doesExist(aWord, false) )
			Node.errorHandler("--Duplicated local variable:: " + aWord);
		
		VarInfo varInfo = new VarInfo(aWord, type, usedMemory, false);
		usedMemory += type.width;
		
		this.topEnv.put(aWord, varInfo);
	}

	private void prod16Statement_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod16BlockContents_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- Stmt, SeqStmt
		Stmt stmt = (Stmt) theSynthsizeRec.synthAttr.get(0);
		Stmt seq = (Stmt) theSynthsizeRec.synthAttr.get(1);
		
		SeqStmt resultSeq = new SeqStmt(this.topEnv, stmt, seq);
		
		Object[] objs = {resultSeq};
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod17Eps_() {
		Object[] objs = { Stmt.NULL };
		addAllAttrToSynthesizeRec(1, objs);
	}

	private void prod19Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(6, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod19Block_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(3, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod19OptElse_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod19OptElse__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, stmt, (stmt)?
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(0);
		Stmt ifStmt = (Stmt) theSynthsizeRec.synthAttr.get(1);
		
		Stmt ifOrIfElse = null;
		
		if(theSynthsizeRec.synthAttr.size() == 3) {
			Stmt elseStmt = (Stmt) theSynthsizeRec.synthAttr.get(2);
			ifOrIfElse = new IfElse(this.topEnv, expr, ifStmt, elseStmt);
		}
		else if(theSynthsizeRec.synthAttr.size() == 2){
			ifOrIfElse = new If(this.topEnv, expr, ifStmt);
		}
		else {
			System.err.println("A bug in passing arguments to prod19OptElse__()");
			System.exit(-1);
		}
		
		Object[] objs = { ifOrIfElse };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod20Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(4, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod20Block_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod20Block__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- enclosingStmt, whileNode, expr, stmt
		
		While whileNode = (While) theSynthsizeRec.synthAttr.get(1);
		
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(2);
		Stmt stmt = (Stmt) theSynthsizeRec.synthAttr.get(3);
		whileNode.init(expr, stmt);
		
		Stmt.enclosing = (Stmt) theSynthsizeRec.synthAttr.get(0);
		
		Object[] objs = { whileNode };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod21InitAssignment_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(10, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod21Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(7, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod21IterateAssignment_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(4, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod21Block_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod21Block__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- enclosingStmt, forNode, stmt, expr, stmt, stmt
		For forNode = (For) theSynthsizeRec.synthAttr.get(1);
		
		Stmt initStmt = (Stmt) theSynthsizeRec.synthAttr.get(2);
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(3);
		Stmt iterateStmt = (Stmt) theSynthsizeRec.synthAttr.get(4);
		Stmt bodyStmt = (Stmt) theSynthsizeRec.synthAttr.get(5);
		forNode.init(initStmt, expr, iterateStmt, bodyStmt);
		
		Stmt.enclosing = (Stmt) theSynthsizeRec.synthAttr.get(0);
		
		Object[] objs = { forNode };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod22RetExpr_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{ 
		//synthesize record contains -- expr
		Expr retExpr= (Expr)theSynthsizeRec.synthAttr.get(0);
		
		Return ret= new Return(this.topEnv, retExpr);
		
		Object[] objs= { ret };
		addAllAttrToSynthesizeRec(2, objs);
	}
	
	private void prod23Semicolon_() throws ErrorSemantics{
		Break breakStmt= new Break(this.topEnv);
		
		Object[] objs= { breakStmt };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod24Semicolon_() throws ErrorSemantics{
		Continue continueStmt= new Continue(this.topEnv);
		
		Object[] objs= { continueStmt };
		addAllAttrToSynthesizeRec(1, objs);	
	}
	
	private void prod26Location_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- reader (word), location or access
		Word readerWord= (Word)theSynthsizeRec.synthAttr.get(0);
		Expr locationOrAccess= (Expr)theSynthsizeRec.synthAttr.get(1);
		
		Reader reader = new Reader(this.topEnv, readerWord, locationOrAccess);
		
		Object[] objs = { reader };
		addAllAttrToSynthesizeRec(2, objs);
	}
	
	private void prod27Expression_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- writer (word), expr
		Word writerWord= (Word)theSynthsizeRec.synthAttr.get(0);
		Expr expr= (Expr)theSynthsizeRec.synthAttr.get(1);
		
		Writer writer= new Writer(this.topEnv, writerWord, expr);
		
		Object[] objs= { writer };
		addAllAttrToSynthesizeRec(2, objs);
	}
	
	private void prod28DimLocation_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(5, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod28Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod28Semicolon_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- location or access, expr
		Expr locationOrAccess = (Expr) theSynthsizeRec.synthAttr.get(0);		
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(1);

		Object assign;
		
		if(locationOrAccess instanceof Location) {
			// Assign checks that lValue is the same type as rValue
			assign = new Assign(this.topEnv, (Location) locationOrAccess, expr);
		}
		else {
			assign = new ArrayAssign(this.topEnv, (Access) locationOrAccess, expr);
		}
		
		Object[] objs = { assign };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod29Parameters_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(3, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod29Semicolon_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- id, list of exprs
		Word aWord = (Word) theSynthsizeRec.synthAttr.get(0);
				
		if(!Environment.GLOBAL_ENV.doesExist(aWord, false))
			Node.errorHandler("--Procedure " + aWord + " is not defined");
				
		SymTableInfo symInfo = Environment.GLOBAL_ENV.get(aWord);
				
		if(symInfo instanceof VarInfo)
			Node.errorHandler("--Procedure " + aWord + " is not defined");
				
		ProcInfo procInfo = (ProcInfo) symInfo;
		ArrayList<Expr> actualParams = new ArrayList<Expr>();
		for(int i = 1; i < theSynthsizeRec.synthAttr.size(); i++)
			actualParams.add((Expr) theSynthsizeRec.synthAttr.get(i));
				
		ProcCall procCall = new ProcCall(this.topEnv, procInfo, actualParams);
		ProcCallStmt procCallStmt = new ProcCallStmt(this.topEnv, procCall);
		
		Object[] objs = { procCallStmt };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod32Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToVariable(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod33Eps_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics{
		//synthesize record contains -- id (Word), ([expr])*
		Word aWord = (Word) theSynthsizeRec.synthAttr.get(0);
		SymTableInfo symInfo = this.topEnv.get(aWord);
		
		if(symInfo == null)
			Node.errorHandler("--" + aWord + " is not declared.");
		
		if(symInfo instanceof ProcInfo)
			Node.errorHandler("--Can't assign values to a procedure.");
		
		VarInfo varInfo = (VarInfo) symInfo;
		Location loc = new Location(this.topEnv, varInfo.id, varInfo.type, varInfo.offset);
		Object locationOrAccess;
		if(theSynthsizeRec.synthAttr.size() == 1)
			locationOrAccess = loc;
		else {
			ArrayList<Expr> dims = new ArrayList<Expr>();
			for( int i = 1; i < theSynthsizeRec.synthAttr.size(); i++)
				dims.add((Expr) theSynthsizeRec.synthAttr.get(i));
			
			locationOrAccess = offset(loc, dims);
		}
		
		Object[] objs = { locationOrAccess };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod35Location_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(4, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod35Expression_(SynthesizeRecord theSynthsizeRec){
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod39Expr1_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(3, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod39RExpr0_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod39RExpr0__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is ||
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			Or or = new Or(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { or };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod40RExpr0__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is ||
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
		if(theSynthsizeRec.synthAttr.size() == 1) {
			Object[] objs = { Word.OROR, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			Or or = new Or(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { Word.OROR, or };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod42RExpr1__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics  {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is &&
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			And and = new And(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { and };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod43RExpr1__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is &&
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
		if(theSynthsizeRec.synthAttr.size() == 1) {
			Object[] objs = { Word.ANDAND, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			And and = new And(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { Word.ANDAND, and };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod45RExpr2__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is == or !=
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			RelOp rel = new RelOp(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { rel };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod46RExpr2__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- op, expr, (op, expr)?
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(1);
		if(theSynthsizeRec.synthAttr.size() == 2) {
			Token op = (Token) theSynthsizeRec.synthAttr.get(0);
			Object[] objs = { op, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op1 = (Token) theSynthsizeRec.synthAttr.get(2);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(3);
			
			RelOp rel = new RelOp(this.topEnv, op1, expr1, expr2);
			
			Token op2 = (Token) theSynthsizeRec.synthAttr.get(0);
			
			Object[] objs = { op2, rel };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod48RExpr3__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is < or <= or > or >=
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			RelOp rel = new RelOp(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { rel };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod49RExpr3__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- op, expr, (op, expr)?
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(1);
		if(theSynthsizeRec.synthAttr.size() == 2) {
			Token op = (Token) theSynthsizeRec.synthAttr.get(0);
			Object[] objs = { op, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op1 = (Token) theSynthsizeRec.synthAttr.get(2);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(3);
			
			RelOp rel = new RelOp(this.topEnv, op1, expr1, expr2);
			
			Token op2 = (Token) theSynthsizeRec.synthAttr.get(0);
			
			Object[] objs = { op2, rel };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod51RExpr4__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is + or -
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			Arith arith = new Arith(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { arith };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod52RExpr4__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- op, expr, (op, expr)?
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(1);
		if(theSynthsizeRec.synthAttr.size() == 2) {
			Token op = (Token) theSynthsizeRec.synthAttr.get(0);
			Object[] objs = { op, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op1 = (Token) theSynthsizeRec.synthAttr.get(2);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(3);
			
			Arith arith = new Arith(this.topEnv, op1, expr1, expr2);
			
			Token op2 = (Token) theSynthsizeRec.synthAttr.get(0);
			
			Object[] objs = { op2, arith };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod54RExpr5__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr, (op, expr)?
		//we now the operator is * or / or %
		if(theSynthsizeRec.synthAttr.size() == 1)
			addAllAttrToSynthesizeRec(1, theSynthsizeRec.synthAttr.toArray());
		else {
			Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(0);
			Token op = (Token) theSynthsizeRec.synthAttr.get(1);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(2);
			
			Arith arith = new Arith(this.topEnv, op, expr1, expr2);
			
			Object[] objs = { arith };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod55RExpr5__(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- op, expr, (op, expr)?
		Expr expr1 = (Expr) theSynthsizeRec.synthAttr.get(1);
		if(theSynthsizeRec.synthAttr.size() == 2) {
			Token op = (Token) theSynthsizeRec.synthAttr.get(0);
			Object[] objs = { op, expr1};
			addAllAttrToSynthesizeRec(1, objs);
		}
		else {
			Token op1 = (Token) theSynthsizeRec.synthAttr.get(2);
			Expr expr2 = (Expr) theSynthsizeRec.synthAttr.get(3);
			
			Arith arith = new Arith(this.topEnv, op1, expr1, expr2);
			
			Token op2 = (Token) theSynthsizeRec.synthAttr.get(0);
			
			Object[] objs = { op2, arith };
			addAllAttrToSynthesizeRec(1, objs);
		}
	}
	
	private void prod57Expr6_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(0);
		
		Not not = new Not(this.topEnv, new Token(Tag.NOT), expr);
		
		Object[] objs = { not };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod59Expr7_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- expr
		Expr expr = (Expr) theSynthsizeRec.synthAttr.get(0);
		
		UnaryMinus unaryMinus = new UnaryMinus(this.topEnv, Word.UNARY_MINUS, expr);
		
		Object[] objs = { unaryMinus };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod62Expr0_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod77IntNum_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- int (IntNum)
		IntNum intNum = (IntNum) theSynthsizeRec.synthAttr.get(0);
		
		Constant integer = new Constant(this.topEnv, intNum, Type.INT);
		Object[] objs = { integer };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod78RealNum_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- real (RealNum)
		RealNum realNum = (RealNum) theSynthsizeRec.synthAttr.get(0);
		realNum.evaluateTheValue();
		
		Constant real = new Constant(this.topEnv, realNum, Type.FLOAT);
		Object[] objs = { real };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod79CharLiteral_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- char (CharLiteral)
		CharLiteral charLiteral = (CharLiteral) theSynthsizeRec.synthAttr.get(0);
		
		Constant character = new Constant(this.topEnv, charLiteral, Type.CHAR);
		Object[] objs = { character };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod80True_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- true
		Object[] objs = { Constant.TRUE };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod81False_(SynthesizeRecord theSynthsizeRec) {
		//synthesize record contains -- false
		Object[] objs = { Constant.FALSE };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod83Parameters_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
	
	private void prod83CloseParenthesis_(SynthesizeRecord theSynthsizeRec) throws ErrorSemantics {
		//synthesize record contains -- id, list of exprs
		Word aWord = (Word) theSynthsizeRec.synthAttr.get(0);
		
		if(!Environment.GLOBAL_ENV.doesExist(aWord, false))
			Node.errorHandler("--Procedure " + aWord + " is not defined");
		
		SymTableInfo symInfo = Environment.GLOBAL_ENV.get(aWord);
		
		if(symInfo instanceof VarInfo)
			Node.errorHandler("--Procedure " + aWord + " is not defined");
		
		ProcInfo procInfo = (ProcInfo) symInfo;
		ArrayList<Expr> actualParams = new ArrayList<Expr>();
		for(int i = 1; i < theSynthsizeRec.synthAttr.size(); i++)
			actualParams.add((Expr) theSynthsizeRec.synthAttr.get(i));
		
		ProcCall procCall = new ProcCall(this.topEnv, procInfo, actualParams);
		
		Object[] objs = { procCall };
		addAllAttrToSynthesizeRec(1, objs);
	}
	
	private void prod84Expression_(SynthesizeRecord theSynthsizeRec) {
		addAllAttrToSynthesizeRec(2, theSynthsizeRec.synthAttr.toArray());
	}
}