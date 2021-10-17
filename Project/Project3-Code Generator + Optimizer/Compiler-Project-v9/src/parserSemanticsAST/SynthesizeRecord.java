package parserSemanticsAST;

import java.util.ArrayList;

public class SynthesizeRecord extends LLStackElem{
	private final String synthRecName;
	
	public ArrayList<Object> synthAttr= new ArrayList<Object>();//TODO
	
	public SynthesizeRecord(String synthRecName){
		this.synthRecName= synthRecName;
	}
	
	public String getSynthesizeRecName(){
		return this.synthRecName;
	}
}