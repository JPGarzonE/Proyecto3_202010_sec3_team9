package model.logic;

import model.data_structures.Bag;

public class Intersection {
	
	private Bag<Feature> features;
	
	private Bag<PoliceStation> policeStations;

	public Intersection(){		
		this.features = new Bag<Feature>();
		this.policeStations = new Bag<PoliceStation>();
	}
	
	public void addFeature( Feature el ){
		features.add( el );
	}
	
	public void addPoliceStation( PoliceStation polS ){
		policeStations.add( polS );
	}
	
	public Bag<PoliceStation> getPoliceStations(){
		return policeStations;
	}
	
	public Bag<Feature> getFeatures(){
		return features;
	}
}
