package model.logic;

public class Intersection {

	private double longitud;
	
	private double latitud;

	public Intersection(double longitud, double latitud){
		this.longitud = longitud;
		this.latitud = latitud;
	}
	
	public double getLongitud() {
		return longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
}
