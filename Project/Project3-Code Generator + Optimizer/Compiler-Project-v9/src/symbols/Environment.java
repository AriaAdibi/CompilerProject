package symbols;

import java.util.Hashtable;

import lexer.Word;

public class Environment{
	//TODO movaghat
	public Hashtable<Word, SymTableInfo> table;
	
	private Environment prev;
	
	public Environment(Environment prev){
		this.table= new Hashtable<Word, SymTableInfo>();
		this.prev= prev;
	}

	public void put(Word id, SymTableInfo info){
		this.table.put(id, info);
	}
	
	public SymTableInfo get(Word aWord){
		for( Environment env= this; env != null; env= env.prev ){
			SymTableInfo found= env.table.get(aWord);
			if(found != null)
				return found;
		}
		return null;
	}
	
	public boolean doesExist(Word aWord, boolean checkPrevEnvs){
		if(checkPrevEnvs) {
			if( this.get(aWord) == null )
				return false;
			return true;
		}
		else {
			return this.table.containsKey(aWord);
		}
	}
	
	public static final Environment GLOBAL_ENV= 
			new Environment(null);
	public static boolean isDuplicatedIdInGlobalEnv(Word aWord){
		if( Environment.GLOBAL_ENV.get(aWord)==null )
			return false;
		return true;
	}
}