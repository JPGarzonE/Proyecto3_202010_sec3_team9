package model.logic;

import java.util.Comparator;

public class SevereComparator<T extends Feature> implements Comparator<T>{

	public int compare(T o1, T o2) {
		
		int servicePriorityO1 = o1.getServiceTypePriority();
		int servicePriorityO2 = o2.getServiceTypePriority();
		
		if( servicePriorityO1 > servicePriorityO2 )
			return 1;
		else if( servicePriorityO1 < servicePriorityO2 )
			return -1;
		else
			return o1.getInfraction().compareTo( o2.getInfraction() );
	}

}
