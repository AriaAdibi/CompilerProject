package intermediateRepresentation;

import java.util.ArrayList;

import symbols.Environment;

public class Program extends Node{
	
	private ArrayList<Proc> procedures = new ArrayList<Proc>();
	
	public Program(Environment env) {
		super(env);
	}
	
	public void addProc(Proc proc) {
		this.procedures.add(proc);
	}
	
	public void gen() {
		for(Proc proc : this.procedures) {
			proc.gen();
			Node.ir.append("\n");
		}
	}
	
}