package model.logic;

import java.util.Comparator;


	public class NearComparator<T extends Feature> implements Comparator<T>
{
		
		private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
		private static double LATITUDE = 4.647586;
		private static final double LONGITUDE = 74.078122;
		
		public int compare(T o1, T o2) 
		{
			
			double nearestPriorityO1 = distance(o1);
			double nearestPriorityO2 = distance(o2);
			
			
			if( nearestPriorityO1 > nearestPriorityO2 )
				return 1;
			else if( nearestPriorityO1 < nearestPriorityO2 )
				return -1;
			else
				return 0;
		}

		/**
		 * Jason Winn
		 * http://jasonwinn.org
		 * Created July 10, 2013
		 *
		 * Description: Small class that provides approximate distance between
		 * two points using the Haversine formula.
		 *
		 * Call in a static context:
		 * Haversine.distance(47.6788206, -122.3271205,
		 *                    47.6788206, -122.5271205)
		 * --> 14.973190481586224 [km]
		 *
		 */
		
		public double distance(T o1)
	    {
	    	double latitudeO1 = o1.getLatitud();
	    	double longitudeO1 = o1.getLongitud();
	    	
	    	
	        double dLat  = Math.toRadians((LATITUDE - latitudeO1));
	        double dLong = Math.toRadians((LONGITUDE - longitudeO1));

	        latitudeO1 = Math.toRadians(latitudeO1);
	        LATITUDE = Math.toRadians(LATITUDE);

	        double a = haversin(dLat) + Math.cos(latitudeO1) * Math.cos(LATITUDE) * haversin(dLong);
	        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	        return EARTH_RADIUS * c; // <-- d
	    }

	    public static double haversin(double val) {
	        return Math.pow(Math.sin(val / 2), 2);
	    }
}
