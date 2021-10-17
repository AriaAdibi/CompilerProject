package symbols;

import java.util.ArrayList;

import lexer.Word;
import utilitarian.Pair;

public class ProcInfo extends SymTableInfo{
	
	public static final ProcInfo NULL = new ProcInfo(null, null);
	
	public static ProcInfo enclosingProcInfo = ProcInfo.NULL;
	
	
	private Type retType= null; //null is for void
	
	private final Word procId;
	
	private ArrayList< Pair<Type, Word> > formalPars= new ArrayList< Pair<Type, Word> >();

	public ProcInfo(Type retType, Word procId){
		this.retType= retType;
		this.procId= procId;
	}

	public Type getRetType(){
		return this.retType;
	}
	
	public void setRetType(Type retType){
		// XXX check
		this.retType= retType;
	}
	
	public Word getProcId(){
		return this.procId;
	}
	
	public ArrayList< Pair<Type, Word> > getFormalPars() {
		return this.formalPars;
	}
	
	public boolean isDuplicatedParName(Word aWord){
		for(Pair<Type, Word> ptw: this.formalPars){
			Word theId= ptw.second;
			if( theId.equals(aWord) )	//works because lexer returns same reference 
				return true;			//for same lexeme as id
		}
		return false;
	}
		
	//left to right 
	public void addFormalPar(Type type, Word id){
		this.formalPars.add( new Pair<Type, Word>(type, id) );
	}
	
	@Override
	public String toString() {
		//TODO
		return procId + "(" + formalPars + "): " + retType;  
	}
}