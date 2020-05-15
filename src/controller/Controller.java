package controller;

import java.util.Scanner;

import Exception.DataStructureException;
import model.logic.Feature;
import model.logic.Geometry;
import model.logic.Modelo;
import model.logic.PoliceStation;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	static final String DATA_PATH_FEATURES = "./data/comparendos_dei_2018_Bogotá_D.C.geojson";
	static final String DATA_PATH_POLICE_STATIONS = "./data/estacionpolicia.geojson";
	static final String DATA_PATH_EDGES = "./data/bogota_arcos.txt";
	static final String DATA_PATH_VERTICES = "./data/bogota_vertices.txt";
	static final String DATA_PATH_BOGOTA_MESH_JSON = "./data/BogotaRoadMeshGraph.json";
	static final String OUTPUT_BOGOTA_MESH_JSON = "./data/BogotaRoadMeshGraph.json";
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
	}
		
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		
		view.printMessage("\n La aplicación va a cargar la malla víal desde este archivo: " + DATA_PATH_BOGOTA_MESH_JSON);
		view.printMessage("\n ¿Esta de acuerdo? (Y/n)");
		String answer = lector.next();

		if( answer.equalsIgnoreCase("Y") ){
			long startTime = System.currentTimeMillis();
//			loadGraphFromTXT();
			loadGraphFromJSON();
			long endTime = System.currentTimeMillis() - startTime;
			view.printExecutionTime(endTime);
		}else{
			view.printMessage("\n Cambie el archivo y vuelva a entrar. Gracias!");
			fin = true;
		}
		
		long startTime = 0;
		long endTime = 0;

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
				case 1:
					startTime = System.currentTimeMillis();

					if( modelo.graphToJson(OUTPUT_BOGOTA_MESH_JSON) )
						view.printMessage("JSON writed succesfully in path: " + OUTPUT_BOGOTA_MESH_JSON);
					else
						view.printMessage("JSON writed error.");
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					break;
				case 2:
					startTime = System.currentTimeMillis();
					
					loadFeaturesInGraph();
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					break;
				case 3:
					startTime = System.currentTimeMillis();
					
					loadPoliceStaionsInGraph();
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					break;
				case 4:
					view.printMessage("Enter Latitud:");
					Double latitud = lector.nextDouble();
					view.printMessage("Enter Longitud:");
					Double longitud = lector.nextDouble();
					
					startTime = System.currentTimeMillis();					
					
					int vertexId = modelo.getNearestVertexId(latitud, longitud);
					
					view.printMessage("Nearest vertex id: " + vertexId);
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					break;
				case 5:
					view.printMessage("--------- \n Hasta pronto !! \n---------"); 
					lector.close();
					fin = true;
					break;

				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}

		}
		
	}	
	
	private void loadGraphFromTXT(){
		view.printMessage("--------- \nCargando malla víal de Bogotá...");
	    modelo = new Modelo();
	    modelo.loadStreets(DATA_PATH_VERTICES, DATA_PATH_EDGES);
	    int vertexSize = modelo.vertexSize();
	    int edgesSize = modelo.edgesSize();
	    int biggestVertexId = modelo.getVertexBiggestId();
	    Geometry geom = modelo.getGeometryById(biggestVertexId);
	    
	    view.printGeneralRoadMeshInfo(vertexSize, edgesSize, biggestVertexId, geom.getLatitud(), geom.getLongitud());
	}
	
	private void loadGraphFromJSON(){
		view.printMessage("--------- \nCargando malla víal de Bogotá...");
	    modelo = new Modelo(DATA_PATH_BOGOTA_MESH_JSON);
	    int vertexSize = modelo.vertexSize();
	    int edgesSize = modelo.edgesSize();
	    int biggestVertexId = modelo.getVertexBiggestId();
	    Geometry geom = modelo.getGeometryById(biggestVertexId);
	    
	    view.printGeneralRoadMeshInfo(vertexSize, edgesSize, biggestVertexId, geom.getLatitud(), geom.getLongitud());
	}
	
	private void loadFeaturesInGraph(){
		if( modelo == null && modelo.vertexSize() > 0)
			throw new IllegalArgumentException("Model graph has not been initialized");
		
		view.printMessage("Los comparendos se estan cargando de este archivo: " + DATA_PATH_FEATURES);
		
		if( modelo.loadGsonFeatures(DATA_PATH_FEATURES) ){
			int featuresSize = modelo.featuresSize();
			Feature biggestFeature = modelo.getFeatureWithBiggestId();
			view.printGeneralFeaturesInfo(featuresSize, biggestFeature);
		}
		else{
			view.printMessage("Los comparendos no se pudieron imprimir. Lo siento =(");
		}
	}
	
	private void loadPoliceStaionsInGraph(){
		if( modelo == null && modelo.vertexSize() > 0)
			throw new IllegalArgumentException("Model graph has not been initialized");
		
		view.printMessage("Las estaciones de policia se estan cargando de este archivo: " + DATA_PATH_POLICE_STATIONS);
		
		if( modelo.loadGsonPoliceStations(DATA_PATH_POLICE_STATIONS) ){
			int policeStationSize = modelo.policeStationSize();
			PoliceStation biggestPoliceStation = modelo.getPoliceStationWithBiggestId();
			
			view.printGeneralPoliceStationsInfo(policeStationSize, biggestPoliceStation);
		}else{
			view.printMessage("Las estaciones de policia no se pudieron imprimir. Lo siento =(");
		}
	}
	
}
