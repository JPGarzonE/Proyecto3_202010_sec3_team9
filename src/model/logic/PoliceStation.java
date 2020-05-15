package model.logic;

import java.util.ArrayList;

public class PoliceStation {

	private String type;
	
	private int objectId;
	
	private String description;
	
	private String address;
	
	private String service;
	
	private String schedule;
	
	private String telephone;
	
	private String localIdentifier;
	
	private Geometry featureGeometry;
	
	public PoliceStation(String type,  int objectId, String description, String address, String service, String schedule,
			String telephone, String localIdentifier, String geomType, ArrayList<Double> coordinates){
		
		this.objectId = objectId;
		this.description = description;
		this.address = address;
		this.service = service;
		this.schedule = schedule;
		this.telephone = telephone;
		this.localIdentifier = localIdentifier;
		
		featureGeometry = new Geometry(geomType, coordinates);	
	}

	public String getType() {
		return type;
	}

	public int getObjectId() {
		return objectId;
	}

	public String getDescription() {
		return description;
	}

	public String getAddress() {
		return address;
	}

	public String getService() {
		return service;
	}

	public String getSchedule() {
		return schedule;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getLocalIdentifier() {
		return localIdentifier;
	}

	public Geometry getGeometry() {
		return featureGeometry;
	}
	
	public Double getLatitud(){
		return featureGeometry.getLatitud();
	}
	
	public Double getLongitud(){
		return featureGeometry.getLongitud();
	}
}
