package intermediateRepresentation;

import java.util.ArrayList;

public class Program extends Node{
	
	private ArrayList<Proc> procedures = new ArrayList<Proc>();
	
	public void addProc(Proc proc) {
		this.procedures.add(proc);
	}
	
	public void gen() {
		for(Proc proc : this.procedures) {
			proc.gen();
			Node.emit("");
		}
	}
	
}