package model.logic;

import java.util.Comparator;

public class SevereComparator<T extends Intersection> implements Comparator<T>{

	public int compare(T o1, T o2) {
				
		int totalPriorityO1 = 0;
		int totalLexicographicPriorityO1 = 0;
		for( Feature f : o1.getFeatures() ){
			totalPriorityO1 += f.getServiceTypePriority();
			totalLexicographicPriorityO1 += f.getServiceType().hashCode();
		}
		
		int totalPriorityO2 = 0;
		int totalLexicographicPriorityO2 = 0;
		for( Feature f : o2.getFeatures() ){
			totalPriorityO2 += f.getServiceTypePriority();
			totalLexicographicPriorityO1 += f.getServiceType().hashCode();
		}

		if( totalPriorityO1 > totalPriorityO2 )
			return 1;
		else if( totalPriorityO1 < totalPriorityO2 )
			return -1;
		else{
			if( totalLexicographicPriorityO1 > totalLexicographicPriorityO2 )
				return 1;
			else if( totalLexicographicPriorityO1 < totalLexicographicPriorityO2 )
				return -1;
			else
				return 0;
		}
		
	}

}
