package model.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import Exception.DataStructureException;
import model.data_structures.ArrayNode;
import model.data_structures.ILinearProbing;
import model.data_structures.IMaxPQ;
import model.data_structures.IQueue;
import model.data_structures.IRedBlackBST;
import model.data_structures.LinearProbingHash;
import model.data_structures.MaxPQ;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
import model.data_structures.UndirectedGraph;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	
	/**
	 * UndirectedGraph
	 */
	private UndirectedGraph<String, Intersection> graph;
	
	/**
	 * First feature
	 */
	private Feature firstFeature;
	
	/**
	 * Last feature
	 */
	private Feature lastFeature;
	
	/**
	 * Feature with biggest id
	 */
	private Feature featureWithBiggestId;
	
	/**
	 * Size
	 */
	private int size;
	
	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		this(20);
	}
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo(int capacity )
	{ 
		graph = new UndirectedGraph<>(20);
	}
	
	public int vertexSize(){
		return graph.V();
	}
	
	public int edgesSize(){
		return graph.E();
	}
	
	public Feature getFirstFeature(){
		return firstFeature;
	}
	
	public Feature getLastFeature(){
		return lastFeature;
	}
	
	public Feature getFeatureWithBiggestId(){
		return featureWithBiggestId;
	}
	
	public boolean loadStreets(String verticesPath, String intersectionsPath){
		
		try{
			
		  String str;
		  FileReader f = new FileReader(verticesPath);
		  BufferedReader b = new BufferedReader(f);
		  while((str = b.readLine())!=null) {
		      String[] line = str.split(",");
		      String id = line[0];
		      double longitud = Double.parseDouble(line[1]);
		      double latitud = Double.parseDouble(line[2]);
		      
		      loadIntersection(id, new Intersection(longitud, latitud));
		  }
		  b.close();
		  
		  f = new FileReader(intersectionsPath);
		  b = new BufferedReader(f);
		  while((str = b.readLine())!=null) {
			  if( !str.startsWith("#") ){
			      String[] vertices = str.split(" ");
			      loadEdge(vertices);
			  }
		  }
		  b.close();
		  
		}catch (FileNotFoundException e) {
			System.out.println("ERROR! Streets file not found\n\n");
			return false;
		}
		catch (IOException e) {
			System.out.println("ERROR! Streets file IOException\n\n");
			return false;
		}
		
		return true;
	}

	public boolean loadDataList(String path){
		if( loadGson(path) )
			return true;
		else	
			return false;
	}

	private boolean loadGson(String path) {

		try {
			System.out.println(path);
			JsonReader reader = new JsonReader(new FileReader(path));
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();

			for (JsonElement element : jsonFeaturesArray) {

				String elemType = element.getAsJsonObject().get("type").getAsString();

				JsonElement elemProperties = element.getAsJsonObject().get("properties");

				int elemId = elemProperties.getAsJsonObject().get("OBJECTID").getAsInt();
				String elemDate = elemProperties.getAsJsonObject().get("FECHA_HORA").getAsString();
				String elemDetectionMethod = elemProperties.getAsJsonObject().get("MEDIO_DETECCION").getAsString();
				String elemVehicleClass = elemProperties.getAsJsonObject().get("CLASE_VEHICULO").getAsString();
				String elemServiceType = elemProperties.getAsJsonObject().get("TIPO_SERVICIO").getAsString();
				String elemInfraction = elemProperties.getAsJsonObject().get("INFRACCION").getAsString();
				String elemInfractionReason = elemProperties.getAsJsonObject().get("DES_INFRACCION").getAsString();
				String elemLocality = elemProperties.getAsJsonObject().get("LOCALIDAD").getAsString();
				String elemTown = elemProperties.getAsJsonObject().get("MUNICIPIO").getAsString();
				
				JsonElement elemGeometry = element.getAsJsonObject().get("geometry");

				String elemGeomType = elemGeometry.getAsJsonObject().get("type").getAsString();
				JsonArray elemGeomCoordinates = elemGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
				ArrayList<Double> elemCoordinates = new ArrayList<Double>();

				for (JsonElement elemCoord : elemGeomCoordinates) {
					Double actualCoord = elemCoord.getAsDouble();
					elemCoordinates.add(actualCoord);
				}

				elemVehicleClass = elemVehicleClass.startsWith("AUTOM") ? "AUTOMOVIL" : elemVehicleClass;
				
				Feature feature = new Feature(elemType, elemId, elemDate, elemDetectionMethod, elemVehicleClass,
						elemServiceType, elemInfraction, elemInfractionReason, elemLocality, elemTown, elemGeomType,
						elemCoordinates);

//				loadElement(feature);

				if( featureWithBiggestId == null )
					featureWithBiggestId = feature;
				else if( featureWithBiggestId.getObjectId() < feature.getObjectId() )
					featureWithBiggestId = feature;
				
				if( firstFeature == null )
					firstFeature = feature;
				
				lastFeature = feature;
			}

//			size = priorityQueue.size();

		} catch (FileNotFoundException e) {
			System.out.println("ERROR! File not found\n\n");
			return false;
		}
		
		return true;

	}
	
	private void loadIntersection(String id, Intersection inter){
		graph.addVertex(id, inter);
	}
	
	private void loadEdge(String[] adj){
		String id = adj[0];
		Intersection edge = graph.getInfoVertex(id);
		
		for( int i = 1; i < adj.length; i++ ){
			Intersection actInter = graph.getInfoVertex(adj[i]);
			double haversineDist = Haversine.distance(edge.getLatitud(), edge.getLongitud(),
					actInter.getLatitud(), actInter.getLongitud());
			
			graph.addEdge(id, adj[i], haversineDist);			
		}
	}
	
//	private void loadElement(Feature feature){
//		
//		graph.addVertex(idVertex, feature);
//		
//	}


}