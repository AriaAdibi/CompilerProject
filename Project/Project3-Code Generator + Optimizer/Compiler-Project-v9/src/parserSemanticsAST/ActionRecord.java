package parserSemanticsAST;

import java.util.ArrayList;

public class ActionRecord extends LLStackElem{
	private final String actionRecName;
	
	public ArrayList<Object> inhAttr= new ArrayList<Object>();//TODO
	
	public ActionRecord(String actionRecName){
		this.actionRecName= actionRecName;
	}
	
	public String getActionRecName(){
		return this.actionRecName;
	}
}