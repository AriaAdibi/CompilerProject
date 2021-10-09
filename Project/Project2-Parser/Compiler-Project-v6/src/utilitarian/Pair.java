package utilitarian;

public class Pair <T1, T2>{
	public T1 first= null;
	public T2 second= null;
	
	public Pair(T1 first, T2 second){
		this.first= first;
		this.second= second;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Pair<?, ?>){
			Pair<?, ?> p= (Pair<?, ?>)o;
			return ( this.first.equals(p.first) && this.second.equals(p.second) );
		}
		else
			throw new RuntimeException("Comparing Pair<T1, T2> with some other type");
	}
	
	@Override
	public int hashCode(){
		return 37 * this.first.hashCode() + this.second.hashCode();
	}
}