package model.data_structures;

import java.util.Comparator;
import java.util.Iterator;

public interface IMaxPQ<Val> extends Iterable<Val>{

	public void insert(Val v);
	
	public Val max();
	
	public Iterator<Val> max(int n);
	
	public Val delMax();
	
	public boolean isEmpty();
	
	public void changeComparator(Comparator<Val> comp);
	
	public int size();
	
	public Iterator<Val> iterator();
}
