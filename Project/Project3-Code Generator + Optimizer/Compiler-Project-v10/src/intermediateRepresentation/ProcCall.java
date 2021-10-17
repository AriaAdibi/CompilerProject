package intermediateRepresentation;

import java.util.ArrayList;

import lexer.Word;
import symbols.Environment;
import symbols.ProcInfo;
import symbols.Type;
import utilitarian.Pair;

public class ProcCall extends Op {
	
	private ProcInfo procInfo;
	private ArrayList<Expr> actualParams;
	
	public ProcCall(Environment env, ProcInfo procInfo, ArrayList<Expr> actualParams) throws ErrorSemantics {
		super(env, procInfo.getProcId(), procInfo.getRetType());
		this.procInfo = procInfo;
		this.actualParams = actualParams;
		
		Pair< Type, Pair<Type, Word> > chk = check(procInfo);
		if (chk != null)
			Node.errorHandler("--Expected " + chk.second.second + " to be of type "
								+ chk.second.first + ":: " + chk.first + " is given instead");
	}
	
	private Pair< Type, Pair<Type, Word> > check(ProcInfo procInfo) {
		for(int i = 0; i< actualParams.size(); i++) {
			Type actualParamType = actualParams.get(i).type;
			Type formalParamType = procInfo.getFormalPars().get(i).first;
			
			if( !actualParamType.equals(formalParamType))
				return new Pair< Type, Pair<Type,Word> >(actualParamType,
								procInfo.getFormalPars().get(i));
		}
		
		return null;
	}

	@Override
	public Expr gen() {
		ArrayList<Expr> reducedParams = new ArrayList<Expr>();
		for(Expr actualParam : actualParams)
			reducedParams.add(actualParam.reduce());
		
		for(Expr reducedParam : reducedParams)
			Node.emit("param " + reducedParam.toString());
		
		try {
			return new ProcCall(env, procInfo, reducedParams);
		} catch(Exception exc) {
			System.err.println("Error during ProcCall.gen()");
			System.exit(-1);
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "call " + op + ", " + actualParams.size();
	}

}
