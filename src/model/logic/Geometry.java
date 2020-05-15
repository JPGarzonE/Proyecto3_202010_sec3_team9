package model.logic;

import java.util.ArrayList;

public class Geometry {

	public String type;
	
	public ArrayList<Double> coordinates;
	
	public Geometry(String type, ArrayList<Double> coordinates){
		
		this.type = type;
		this.coordinates = coordinates;
		
	}
	
	public double getLatitud(){
		return coordinates.get(0);
	}
	
	public double getLongitud(){
		return coordinates.get(1);
	}
	
	public Feature nearestFeature() {
		return null;
	}
	
}
