package intermediateRepresentation;

import symbols.Environment;
import symbols.ProcInfo;
import symbols.Type;

public class Return extends Stmt{
	ProcInfo procInfo;
	Expr retExpr;
	
	public Return(Environment env, Expr retExpr ) throws ErrorSemantics{
		super(env);
		
		if( ProcInfo.enclosingProcInfo == ProcInfo.NULL )
			Node.errorHandler("--Return Expression does not belong to any procedure");
		this.procInfo= ProcInfo.enclosingProcInfo;
		this.retExpr= retExpr;
		
		Type encPIRetType= this.procInfo.getRetType();
		Type retExpeType= this.retExpr.type;
		if( !encPIRetType.equals(retExpeType) )
			Node.errorHandler("--Return expression Type must be of "+ encPIRetType+
					":: return expression type = "+ retExpeType);
	}
	
	@Override
	public void gen(int begin, int after){
		Node.emit("ENV" + env + ": " +  "return "+ this.retExpr.reduce() );
	}	
}