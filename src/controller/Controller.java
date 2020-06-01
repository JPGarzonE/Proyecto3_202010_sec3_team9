package controller;

import java.util.Iterator;
import java.util.Scanner;

import com.teamdev.jxmaps.LatLng;

import model.data_structures.DynamicArray;
import model.data_structures.GenericEdge;
import model.logic.CheapestPath;
import model.logic.Feature;
import model.logic.Geometry;
import model.logic.Modelo;
import model.logic.PoliceStation;
import view.CustomMap;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	static final String DATA_PATH_FEATURES = "./data/comparendos_dei_2018_Bogot�_D.C.geojson";
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
		
		view.printMessage("\n La aplicaci�n va a cargar la malla v�al desde este archivo: " + DATA_PATH_BOGOTA_MESH_JSON);
		view.printMessage("\n �Esta de acuerdo? (Y/n)");
		String answer = lector.next();

		if( answer.equalsIgnoreCase("Y") ){
//			loadGraphFromTXT();
//			loadGraphFromJSON();
			loadGraphFromJSONWithElements();
		}else{
			view.printMessage("\n Cambie el archivo y vuelva a entrar. Gracias!");
			fin = true;
		}
		
		long startTime = 0;
		long endTime = 0;
		
		DynamicArray<LatLng[]> paths = null;

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
					
				case 3:
					view.printMessage("Begin location");
					view.printMessage("\tEnter latitud:");
					Double initLat = lector.nextDouble();
					view.printMessage("\tEnter longitud:");
					Double initLong = lector.nextDouble();
					
					view.printMessage("\nEnd location");
					view.printMessage("\tEnter latitud:");
					Double endLat = lector.nextDouble();
					view.printMessage("\tEnter longitud:");
					Double endLong = lector.nextDouble();
					
					startTime = System.currentTimeMillis();
					CheapestPath<Geometry> cp = modelo.cheapestPathByDistance(initLat, initLong, endLat, endLong);
					
					paths = new DynamicArray<LatLng[]>();
					
					Iterator<Geometry> pathIterator = cp.getPath();
					LatLng previous = null;
					int i = 0;
					while( pathIterator.hasNext() ){
						Geometry step = pathIterator.next();
						view.printVertex(step.getId(), step.getLatitud(), step.getLongitud());
						
						if( previous != null ){
							System.out.println("Lat: " + step.getLatitud() + " || Long: " + step.getLongitud());
							LatLng[] path = {previous, new LatLng(step.getLatitud(), step.getLongitud()) };
							paths.add( path );
							System.out.println("i: " + i + " || " + path[0].getLat() + ", " + path[0].getLng()
									+ " || " + path[1].getLat() + ", " + path[1].getLng());
						}
						
						previous = new LatLng(step.getLatitud(), step.getLongitud());
					}

					view.printMessage("Numero de vertices: " + cp.size());
					view.printMessage("El costo m�nimo: " + cp.getMinimumCost());
					view.printMessage("La distancia estimada: " + cp.getDistance());
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					view.printMessage("Desplegando mapa...");
							
					new CustomMap(paths);
					
					break;
					
				case 4:
					view.printMessage("M puntos más grave: ");
					int m = lector.nextInt();
					
					startTime = System.currentTimeMillis();
					
					paths = new DynamicArray<LatLng[]>();
					CheapestPath<GenericEdge<Geometry>> cpEdge = modelo.cheapestNetworkBySeverity(m);
					
					Iterator<GenericEdge<Geometry>> pathEdgeIterator = cpEdge.getPath();

					while( pathEdgeIterator.hasNext() ){
						GenericEdge<Geometry> step = pathEdgeIterator.next();
						view.printGenericGeometryEdge(step);
						
						LatLng[] path = {new LatLng(step.either().getLatitud(), step.either().getLongitud()), 
								new LatLng(step.other( step.either() ).getLatitud(), step.other( step.either() ).getLongitud()) };
						paths.add( path );
					}
					
					view.printMessage("El costo monetario total: $" + (cpEdge.getDistance()*10000) + " USD");
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					view.printMessage("Desplegando mapa...");
							
					new CustomMap(paths);
					
					break;
				
				case 5:
					view.printMessage("Begin location");
					view.printMessage("\tEnter latitud:");
					Double initLat2 = lector.nextDouble();
					view.printMessage("\tEnter longitud:");
					Double initLong2 = lector.nextDouble();
					
					view.printMessage("\nEnd location");
					view.printMessage("\tEnter latitud:");
					Double endLat2 = lector.nextDouble();
					view.printMessage("\tEnter longitud:");
					Double endLong2 = lector.nextDouble();
					
					startTime = System.currentTimeMillis();
					CheapestPath<Geometry> cp2 = modelo.cheapestPathByFeatures(initLat2, initLong2, endLat2, endLong2);
					
					paths = new DynamicArray<LatLng[]>();
					
					Iterator<Geometry> pathIterator2 = cp2.getPath();
					LatLng previous2 = null;
					int i2 = 0;
					while( pathIterator2.hasNext() ){
						Geometry step = pathIterator2.next();
						view.printVertex(step.getId(), step.getLatitud(), step.getLongitud());
						
						if( previous2 != null ){
							System.out.println("Lat: " + step.getLatitud() + " || Long: " + step.getLongitud());
							LatLng[] path = {previous2, new LatLng(step.getLatitud(), step.getLongitud()) };
							paths.add( path );
							System.out.println("i: " + i2 + " || " + path[0].getLat() + ", " + path[0].getLng()
									+ " || " + path[1].getLat() + ", " + path[1].getLng());
						}
						
						previous2 = new LatLng(step.getLatitud(), step.getLongitud());
					}

					view.printMessage("Numero de vertices: " + cp2.size());
					view.printMessage("El costo m�nimo: " + cp2.getMinimumCost());
					view.printMessage("La distancia estimada: " + cp2.getDistance());
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					view.printMessage("Desplegando mapa...");
							
					new CustomMap(paths);
					
					break;
					
				case 6:
					view.printMessage("M puntos: ");
					int m2 = lector.nextInt();
					
					startTime = System.currentTimeMillis();
					
					paths = new DynamicArray<LatLng[]>();
					CheapestPath<GenericEdge<Geometry>> cpEdge2 = modelo.cheapestNetworkByFeaturesSize(m2);
					
					Iterator<GenericEdge<Geometry>> pathEdgeIterator2 = cpEdge2.getPath();

					while( pathEdgeIterator2.hasNext() ){
						GenericEdge<Geometry> step = pathEdgeIterator2.next();
						view.printGenericGeometryEdge(step);
						
						LatLng[] path = {new LatLng(step.either().getLatitud(), step.either().getLongitud()), 
								new LatLng(step.other( step.either() ).getLatitud(), step.other( step.either() ).getLongitud()) };
						paths.add( path );
					}
					
					view.printMessage("El costo monetario total: $" + (cpEdge2.getDistance()*10000) + " USD");
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					view.printMessage("Desplegando mapa...");
							
					new CustomMap(paths);
					
					break;
					
				case 7:
					view.printMessage("M puntos más graves: ");
					int n = lector.nextInt();
					
					startTime = System.currentTimeMillis();
					
					paths = new DynamicArray<LatLng[]>();
					CheapestPath<GenericEdge<Geometry>>[] cheapestAttendPaths = modelo.cheapestPathsToAttendFeatures(n);
					
					int j = 1;
					for( CheapestPath<GenericEdge<Geometry>> cheapPath : cheapestAttendPaths ){
						view.printMessage("\nM: " + j);
						String pathstr = "";
						
						Iterator<GenericEdge<Geometry>> attendPathIterator = cheapPath.getPath();

						while( attendPathIterator.hasNext() ){
							GenericEdge<Geometry> step = attendPathIterator.next();
							
							pathstr += "(" + step.either().getId() + " --> " + step.other( step.either() ).getId() + "), ";
							
							LatLng[] path = {new LatLng(step.either().getLatitud(), step.either().getLongitud()), 
									new LatLng(step.other( step.either() ).getLatitud(), step.other( step.either() ).getLongitud()) };
							paths.add( path );
						}
						
						view.printMessage("\tPath: " + pathstr);
						view.printMessage("\tCosto total: " + cheapPath.getDistance());
						
						j++;
					}
					
					endTime = System.currentTimeMillis() - startTime;
					view.printExecutionTime(endTime);
					
					view.printMessage("Desplegando mapa...");
							
					new CustomMap(paths);
					
					break;
					
				case 8:
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
	
	private void loadGraphFromJSONWithElements(){
		loadGraphFromJSON();
		loadFeatures();
		loadPoliceStations();
	}
	
	private void loadPoliceStations(){
		System.out.println("CARGANDO ESTACIONES DE POLICIA...");
		long startTime = System.currentTimeMillis();
		
		loadPoliceStaionsInGraph();
		
		long endTime = System.currentTimeMillis() - startTime;
		view.printExecutionTime(endTime);
	}
	
	private void loadFeatures(){
		System.out.println("CARGANDO COMPARENDOS...");
		long startTime = System.currentTimeMillis();
		
		loadFeaturesInGraph();
		
		long endTime = System.currentTimeMillis() - startTime;
		view.printExecutionTime(endTime);
	}
	
	private void loadGraphFromTXT(){
		view.printMessage("--------- \nCargando malla v�al de Bogot�...");
	    modelo = new Modelo();
	    modelo.loadStreets(DATA_PATH_VERTICES, DATA_PATH_EDGES);
	    int vertexSize = modelo.vertexSize();
	    int edgesSize = modelo.edgesSize();
	    int biggestVertexId = modelo.getVertexBiggestId();
	    Geometry geom = modelo.getGeometryById(biggestVertexId);
	    
	    view.printGeneralRoadMeshInfo(vertexSize, edgesSize, biggestVertexId, geom.getLatitud(), geom.getLongitud());
	}
	
	private void loadGraphFromJSON(){
		view.printMessage("--------- \nCARGANDO MALLA VIAL DE BOGOTA...");
		long startTime = System.currentTimeMillis();
		
	    modelo = new Modelo(DATA_PATH_BOGOTA_MESH_JSON);
	    int vertexSize = modelo.vertexSize();
	    int edgesSize = modelo.edgesSize();
	    int biggestVertexId = modelo.getVertexBiggestId();
	    Geometry geom = modelo.getGeometryById(biggestVertexId);
	    
	    view.printGeneralRoadMeshInfo(vertexSize, edgesSize, biggestVertexId, geom.getLatitud(), geom.getLongitud());
		
	    long endTime = System.currentTimeMillis() - startTime;
		view.printExecutionTime(endTime);
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
