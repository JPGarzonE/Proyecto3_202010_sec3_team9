package model.logic;

import java.util.ArrayList;

import model.data_structures.NearComparable;

public class Geometry implements NearComparable<Geometry>{

	public String type;
	
	public ArrayList<Double> coordinates;
	
	public Geometry(String type, ArrayList<Double> coordinates){
		
		this.type = type;
		this.coordinates = coordinates;
		
	}
	
	public Geometry(String type, Double latitud, Double longitud){
		
		this.type = type;
		this.coordinates = new ArrayList<Double>(3);
		this.coordinates.add(longitud);
		this.coordinates.add(latitud);
		
	}
	
	public double getLatitud(){
		return coordinates.get(0);
	}
	
	public double getLongitud(){
		return coordinates.get(1);
	}

	public int compareTo(Geometry either) {
		
		Double mayorLatitud = 90.0;
		Double mayorLongitud = 180.0;
		
		Double ownHaversine = Haversine.distance(getLatitud(), getLongitud(), mayorLatitud, mayorLongitud);
		Double eitherHaversine = Haversine.distance(either.getLatitud(), either.getLongitud(), mayorLatitud, mayorLongitud);
		
		if( ownHaversine > eitherHaversine ){
			return 1;
		}
		else if( ownHaversine < eitherHaversine ){
			return -1;
		}
		else if( ownHaversine == eitherHaversine ){
			if( getLongitud() > either.getLongitud() ){
				return 1;
			}
			else if( getLongitud() < either.getLongitud() ){
				return -1;
			}
		}
		
		return 0;
	}

	public int compareNearest(Geometry other1, Geometry other2) {
		
		Double haversine1 = Haversine.distance(getLatitud(), getLongitud(), other1.getLatitud(), other1.getLongitud() );
		
		Double haversine2 = Haversine.distance(getLatitud(), getLongitud(), other2.getLatitud(), other2.getLongitud() );
		
		if( haversine1 > haversine2 )
			return 1;
		else if( haversine1 < haversine2 )
			return -1;
			
		return 0;
	}
	
	public String toString(){
		return "Longitud: " + getLongitud() + " | Latitud: " + getLatitud();
	}
	
}
