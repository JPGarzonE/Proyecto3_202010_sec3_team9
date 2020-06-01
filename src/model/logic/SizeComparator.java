package model.logic;

import java.util.Comparator;

public class SizeComparator<T extends Intersection> implements Comparator<T> 

{
	
	public int compare(T o1, T o2) {
		
		if( o1.getFeatures().size()>o2.getFeatures().size() )
			return 1;
		else if( o2.getFeatures().size() < o1.getFeatures().size() )
			return -1;
		else {
			return 0;
		}
	}
}


