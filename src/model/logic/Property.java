package model.logic;

public class Property {
	
	public int objectId;
	
	public String date;
	
	public String detectionMethod;
	
	public String vehicleClass;
	
	public String serviceType;
	
	public int serviceTypePriority;
	
	public String infraction;
	
	public String reason;
	
	public String locality;
	
	public String town;
	
	public Property( int objectId, String date, String detectionMethod, String vehicleClass,
			String serviceType, String infraction, String reason, String locality, String town ){
		
		this.objectId = objectId;
		this.date = date;
		this.detectionMethod = detectionMethod;
		this.vehicleClass = vehicleClass;
		this.serviceType = serviceType;
		this.infraction = infraction;
		this.reason = reason;
		this.locality = locality;
		this.town = town;
		
		if( this.serviceType.equalsIgnoreCase("publico") )
			this.serviceTypePriority = 10;
		else if (this.serviceType.equalsIgnoreCase("oficial"))
			this.serviceTypePriority = 5;
		else if (this.serviceType.equalsIgnoreCase("particular"))
			this.serviceTypePriority = 1;
	}
}