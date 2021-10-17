package parserSemanticsAST;

import java.util.ArrayList;

public class Variable extends LLStackElem{

	public final int code;
	private final String name;
	
	public ArrayList<Object> inhAttr= new ArrayList<Object>();//TODO
	
	public Variable(int code, String name){
		this.code= code;
		this.name= name;
	}
	
	public static final int N_VARIABLES= 42;
	public static final Variable 
		//Initiator
		START= new Variable(0, "<start>"),
		PROGRAM= new Variable(1, "<program>"),	R_PROGRAM= new Variable(2, "<rProgram>"),
		
		//Global declarations
		FORMAL_PARAMETERS= new Variable(3, "<formalParameters>" ),	
		R_FORMAL_PARAMETERS= new Variable(4, "<rFormalParameters>"),
		R_VAR_LIST= new Variable(5, "<rVarList>"),
		DIM_DECLARATION= new Variable(6, "<dimDeclaration>"),
		
		//Block and it's content
		BLOCK= new Variable(7, "<block>"),	BLOCK_CONTENTS= new Variable(8, "<blockContents>"),
		
		//Statement	
		STATEMENT= new Variable(9, "<statement>"),	
		ASSIGNMENT_OR_METHODCALL= new Variable(10, "<assignmentOrMethodCall>"),
		OPT_ELSE= new Variable(11, "<optElse>"),
		DIM_LOCATION= new Variable(12, "<dimLocation>"),
		LOCATION= new Variable(13, "<location>"),
		ASSIGNMENT= new Variable(14, "<assignment>"),
		RET_EXPR= new Variable(15, "<retExpr>"),
		
		//Expression
		EXPRESSION= new Variable(16, "<expression>"),
		EXPR_0= new Variable(17, "<expr0>"),	R_EXPR_0= new Variable(18, "<rExpr0>"),	
		EXPR_1= new Variable(19, "<expr1>"),	R_EXPR_1= new Variable(20, "<rExpr1>"),
		EXPR_2= new Variable(21, "<expr2>"),	R_EXPR_2= new Variable(22, "<rExpr2>"),
		EXPR_3= new Variable(23, "<expr3>"),	R_EXPR_3= new Variable(24, "<rExpr3>"),
		EXPR_4= new Variable(25, "<expr4>"),	R_EXPR_4= new Variable(26, "<rExpr4>"),
		EXPR_5= new Variable(27, "<expr5>"),	R_EXPR_5= new Variable(28, "<rExpr5>"),
		EXPR_6= new Variable(29, "<expr6>"),
		EXPR_7= new Variable(30, "<expr7>"),
		EXPR_8= new Variable(31, "<expr8>"),
		
		COND_OP_0= new Variable(32, "<condOp0>"),	COND_OP_1= new Variable(33, "<condOp1>"),
		EQ_OP= new Variable(34, "<eqOp>"),
		REL_OP= new Variable(35, "<relOp>"),
		ARITH_OP_0= new Variable(36, "<arithOp0>"),	ARITH_OP_1= new Variable(37, "<arithOp1>"),
		
		ATOMIC_EXPR= new Variable(38, "<atomicExpr>"),
		LOCATION_OR_METHODCALL= new Variable(39, "<locationOrMethodCall>"),
		PARAMETERS= new Variable(40, "<parameters>"),
		R_PARAMETERS= new Variable(41, "<rParameters>");
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString(){
		if( this.name == null )
			throw new RuntimeException("Invalid Variable-- neme= "+ this.name);
		else
			return this.name;		
	}
}