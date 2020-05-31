package model.logic;

import java.util.ArrayList;

public class Feature implements Comparable<Feature> {

	private String type;
	
	private Property featureProperties;
	
	private Geometry featureGeometry;
	
	public Feature(String type,  int objectId, String date, String detectionMethod, String vehicleClass,
			String serviceType, String infraction, String reason, String locality, String town, String geomType, ArrayList<Double> coordinates){
	
		featureProperties = new Property(objectId, date, detectionMethod, vehicleClass, 
				serviceType, infraction, reason, locality, town);
		
		featureGeometry = new Geometry(null, geomType, coordinates);
		
	}

	public String getType() {
		return type;
	}
	

	public String getGeomType() {
		return featureGeometry.type;
	}

	public ArrayList<Double> getCoordinates() {
		return featureGeometry.coordinates;
	}

	public Double getLatitud(){
		return featureGeometry.getLatitud();
	}
	
	public Double getLongitud(){
		return featureGeometry.getLongitud();
	}
	
	public int getObjectId() {
		return featureProperties.objectId;
	}

	public String getDate() {
		return featureProperties.date;
	}

	public String getDetectionMethod() {
		return featureProperties.detectionMethod;
	}
	
	public Geometry getGeometry(){
		return featureGeometry;
	}

	public String getVehicleClass() {
		return featureProperties.vehicleClass;
	}

	public String getServiceType() {
		return featureProperties.serviceType;
	}
	
	public int getServiceTypePriority(){
		return featureProperties.serviceTypePriority;
	}

	public String getInfraction() {
		return featureProperties.infraction;
	}

	public String getReason() {
		return featureProperties.reason;
	}

	public String getLocality() {
		return featureProperties.locality;
	}
	
	public String getTown(){
		return featureProperties.town;
	}

	public int compareTo(Feature compFeature) {
		int comparation = getDate().compareTo(compFeature.getDate());
		
		if( comparation == 0 )
			if( getObjectId() != compFeature.getObjectId() )
				comparation = getObjectId() > compFeature.getObjectId() ? 1 : -1;
		
		return comparation;
	}
	
	public int severityCompareTo( Feature other ){
		
		int servicePriorityO1 = this.getServiceTypePriority();
		int servicePriorityO2 = other.getServiceTypePriority();
		
		if( servicePriorityO1 > servicePriorityO2 )
			return 1;
		else if( servicePriorityO1 < servicePriorityO2 )
			return -1;
		else
			return this.getInfraction().compareTo( other.getInfraction() );
	}
	
	public String toString(){
		return getObjectId()+"";
	}
	
}