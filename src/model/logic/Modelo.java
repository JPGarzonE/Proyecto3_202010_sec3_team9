package model.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import model.data_structures.Bag;
import model.data_structures.BreadthFirstPaths;
import model.data_structures.DijkstraUndirectedSP;
import model.data_structures.EagerPrimMST;
import model.data_structures.Edge;
import model.data_structures.GenericEdge;
import model.data_structures.IndexMaxPQ;
import model.data_structures.LinearProbingHash;
import model.data_structures.MaxPQ;
import model.data_structures.UndirectedGraph;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	
	/**
	 * UndirectedGraph
	 */
	private UndirectedGraph<Geometry, Intersection> graph;
	
	/**
	 * Feature with biggest id
	 */
	private Feature featureWithBiggestId;
	
	/**
	 * Police station with biggest id
	 */
	private PoliceStation policeStationWithBiggestId;
	
	/**
	 * Vertex biggest id
	 */
	private Integer vertexWithBiggestId;
	
	/**
	 * The total size of features in the graph
	 */
	private int featuresSize;
	
	/**
	 * The total size of features in the graph
	 */
	private int policeStationSize;
	
	/**
	 * Hash that stores the id's respective index
	 */
	private LinearProbingHash<Integer, Integer> idToIndex;
	
	/**
	 * Hash that stores the index respective id
	 */
	private LinearProbingHash<Integer, Integer> indexToId;
	
	/**
	 * Stores all the intersections ordered by severity
	 */
	private IndexMaxPQ<Intersection> topSeverityIntersections;
	
	/**
	 * Stores all the intersections ordered by it's size
	 */ 
	private IndexMaxPQ<Intersection> topFeaturesIntersections;
	
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
		graph = new UndirectedGraph<Geometry, Intersection>(capacity);
		idToIndex = new LinearProbingHash<Integer, Integer>(capacity);
		indexToId = new LinearProbingHash<Integer, Integer>(capacity);
	}
	
	/**
	 * Crea un modelo a partir de la ruta de un archivo
	 * @param path ruta donde esta guardado el grafo
	 */
	public Modelo( String path ){
		System.out.println("Constructor Modelo --> path: " + path);
		loadGsonGraph(path);
		System.out.println("End constructor...");
	}
	
	public int vertexSize(){
		return graph.V();
	}
	
	public int edgesSize(){
		return graph.E();
	}
	
	public int featuresSize(){
		return featuresSize;
	}
	
	public int policeStationSize(){
		return policeStationSize;
	}
	
	public int getVertexBiggestId(){
		return vertexWithBiggestId;
	}
	
	public PoliceStation getPoliceStationWithBiggestId(){
		return policeStationWithBiggestId;
	}
	
	public Feature getFeatureWithBiggestId(){
		return featureWithBiggestId;
	}
	
	public Geometry getGeometryById( int id ){
		int index = idToIndex.get(id);
		return graph.getKeyByIdx(index);
	}
	
	public int getNearestVertexId( Double latitud, Double longitud ){
		Geometry key = new Geometry(-1, "Point", latitud, longitud);
		int nearestIdx = graph.getNearestVertexIdx(key);
		return indexToId.get(nearestIdx);
	}
	
	public CheapestPath<Geometry> cheapestPathByDistance( Double initLat, Double initLong, Double endLat, Double endLong ){
	
		int initIndex = graph.getNearestVertexIdx(new Geometry( -1, "Point", initLat, initLong ));
		int endIndex = graph.getNearestVertexIdx(new Geometry( -1, "Point", endLat, endLong ));
		
		DijkstraUndirectedSP cheapestPath = new DijkstraUndirectedSP(graph.graph(), initIndex, 1);
		
		CheapestPath<Geometry> cp = new CheapestPath<Geometry>();
		
		for( Edge e : cheapestPath.pathTo(endIndex) ){
			Geometry step = graph.getKeyByIdx( e.either() );
			cp.addPathStep(step, e.weight1());
		}
		
		return cp;
	}
	
	public CheapestPath<Geometry> cheapestPathByFeatures( Double initLat, Double initLong, Double endLat, Double endLong ){
		
		int initIndex = graph.getNearestVertexIdx(new Geometry( -1, "Point", initLat, initLong ));
		int endIndex = graph.getNearestVertexIdx(new Geometry( -1, "Point", endLat, endLong ));
		
		DijkstraUndirectedSP cheapestPath = new DijkstraUndirectedSP(graph.graph(), initIndex, 2);
		
		CheapestPath<Geometry> cp = new CheapestPath<Geometry>();
		
		for( Edge e : cheapestPath.pathTo(endIndex) ){
			Geometry step = graph.getKeyByIdx( e.either() );
			cp.addPathStep(step, e.weight1());
		}
		
		return cp;
	}
	
	public CheapestPath<GenericEdge<Geometry>> cheapestNetworkBySeverity( int m ){
		
		MaxPQ<Integer> topSevereInter = getTopSeverityIntersections().maxPQ(m);
		
		EagerPrimMST mst = new EagerPrimMST( graph.graph(), topSevereInter.delMax() );
		
		Iterator<Edge> edgeIterator = mst.networkPath( topSevereInter ).getPath();
		CheapestPath<GenericEdge<Geometry>> cp = new CheapestPath<>();
		
		while( edgeIterator.hasNext()){
			Edge e = edgeIterator.next();
			Geometry eitherGeom = graph.getKeyByIdx( e.either() );
			Geometry otherGeom = graph.getKeyByIdx( e.other( e.either() ) );
			
			GenericEdge<Geometry> edgeGeom = new GenericEdge<Geometry>(eitherGeom, otherGeom, e.weight1(), e.weight2());
			cp.addPathStep(edgeGeom, edgeGeom.weight1());
		}
		
		return cp;
	}
	
	public CheapestPath<GenericEdge<Geometry>> cheapestNetworkByFeaturesSize( int m ){
		
		MaxPQ<Integer> topFeatureInter = getTopFeaturesIntersections().maxPQ(m);
		
		EagerPrimMST mst = new EagerPrimMST( graph.graph(), topFeatureInter.delMax() );
		
		Iterator<Edge> edgeIterator = mst.networkPath( topFeatureInter ).getPath();
		CheapestPath<GenericEdge<Geometry>> cp = new CheapestPath<>();
		
		while( edgeIterator.hasNext()){
			Edge e = edgeIterator.next();
			Geometry eitherGeom = graph.getKeyByIdx( e.either() );
			Geometry otherGeom = graph.getKeyByIdx( e.other( e.either() ) );
			
			GenericEdge<Geometry> edgeGeom = new GenericEdge<Geometry>(eitherGeom, otherGeom, e.weight1(), e.weight2());
			cp.addPathStep(edgeGeom, edgeGeom.weight1());
		}
		
		return cp;
	}
	
	public CheapestPath<GenericEdge<Geometry>>[] cheapestPathsToAttendFeatures( int m ){
		
		Integer[] topSeverityInter = getTopSeverityIntersections().max(m);
		
		BreadthFirstPaths bfs = new BreadthFirstPaths(graph, topSeverityInter, true);
		
		CheapestPath<GenericEdge<Geometry>>[] cp = (CheapestPath<GenericEdge<Geometry>>[]) new CheapestPath[topSeverityInter.length];
		
		int i = 0;
		for( Integer inter : topSeverityInter ){
			Iterable<Integer> interPath = bfs.pathToPoliceStation(inter);
			CheapestPath<GenericEdge<Geometry>> interCP = new CheapestPath<>();
			Integer previous = null;
			
			if( interPath != null ){
				for( Integer pathStep : interPath ){
					if( previous != null ){
						Geometry geoInit = graph.getKeyByIdx(previous);
						Geometry geoEnd = graph.getKeyByIdx(pathStep);
						Edge edge = graph.getEdge(geoInit, geoEnd);
						Double w1 = edge == null ? 0 : edge.weight1();
						Double w2 = edge == null ? 0 : edge.weight2();
						
						interCP.addPathStep(new GenericEdge<Geometry>(geoInit, geoEnd, w1, w2), w1);
					}
					
					previous = pathStep;
				}
			}
		
			cp[i] = interCP;
			i++;
		}
		
		return cp;
	}
	
	public IndexMaxPQ<Intersection> getTopSeverityIntersections(){
		
		if( topSeverityIntersections != null && topSeverityIntersections.size() == vertexSize() )
			return topSeverityIntersections;
		
		topSeverityIntersections = new IndexMaxPQ<>(vertexSize(), new SevereComparator<Intersection>());
		
		for( int e = 0; e < vertexSize(); e++ ){
			Intersection inter = graph.getInfoVertexByIdx( e );
			
			try{
				topSeverityIntersections.insert( e, inter );
			}
			catch( IllegalArgumentException exception ){}
		}
		
		return topSeverityIntersections;
	}
	
	public IndexMaxPQ<Intersection> getTopFeaturesIntersections(){
		
		if( topFeaturesIntersections != null && topFeaturesIntersections.size() == vertexSize() )
			return topFeaturesIntersections;
		
		topFeaturesIntersections = new IndexMaxPQ<>(vertexSize(), new SizeComparator<Intersection>());
		
		for( int e = 0; e < vertexSize(); e++ ){
			Intersection inter = graph.getInfoVertexByIdx( e );
			
			try{
				topFeaturesIntersections.insert( e, inter );
			}
			catch( IllegalArgumentException exception ){}
		}
		
		return topFeaturesIntersections;
	}
	
	public boolean loadStreets(String verticesPath, String intersectionsPath){
		
		try{
			
		  String str;
		  FileReader f = new FileReader(verticesPath);
		  BufferedReader b = new BufferedReader(f);
		  System.out.println("vertices:");
		  while((str = b.readLine())!=null) {
		      String[] line = str.split(",");
		      int id = Integer.valueOf( line[0] );
		      double longitud = Double.parseDouble(line[1]);
		      double latitud = Double.parseDouble(line[2]);
		      
		      loadIntersection(new Geometry(id, "point", latitud, longitud), new Intersection());
		  }
		  b.close();
		  
		  f = new FileReader(intersectionsPath);
		  b = new BufferedReader(f);
		  System.out.println("intersection");
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
	
	public boolean loadGsonFeatures(String path) {

		try {
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

				loadFeatureElement(feature);

				if( featureWithBiggestId == null )
					featureWithBiggestId = feature;
				else if( featureWithBiggestId.getObjectId() < feature.getObjectId() )
					featureWithBiggestId = feature;
				
				featuresSize++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR! File not found\n\n");
			return false;
		}
		
		return true;

	}
	
	public boolean loadGsonPoliceStations(String path){
		
		try {
			JsonReader reader = new JsonReader(new FileReader(path));
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();

			for (JsonElement element : jsonFeaturesArray) {

				String elemType = element.getAsJsonObject().get("type").getAsString();

				JsonElement elemProperties = element.getAsJsonObject().get("properties");

				int elemId = elemProperties.getAsJsonObject().get("OBJECTID").getAsInt();
				String elemDescription = elemProperties.getAsJsonObject().get("EPODESCRIP").getAsString();
				String elemAddress = elemProperties.getAsJsonObject().get("EPODIR_SITIO").getAsString();
				String elemService = elemProperties.getAsJsonObject().get("EPOSERVICIO").getAsString();
				String elemSchedule = elemProperties.getAsJsonObject().get("EPOHORARIO").getAsString();
				String elemTelephone = elemProperties.getAsJsonObject().get("EPOTELEFON").getAsString();
				String elemLocalIdentifier = elemProperties.getAsJsonObject().get("EPOIULOCAL").getAsString();
				
				JsonElement elemGeometry = element.getAsJsonObject().get("geometry");

				String elemGeomType = elemGeometry.getAsJsonObject().get("type").getAsString();
				JsonArray elemGeomCoordinates = elemGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
				ArrayList<Double> elemCoordinates = new ArrayList<Double>();

				for (JsonElement elemCoord : elemGeomCoordinates) {
					Double actualCoord = elemCoord.getAsDouble();
					elemCoordinates.add(actualCoord);
				}
				
				PoliceStation policeStation = new PoliceStation(elemType, elemId, elemDescription, elemAddress, elemService,
						elemSchedule, elemTelephone, elemLocalIdentifier, elemGeomType, elemCoordinates);

				loadPoliceStationElement(policeStation);

				if( policeStationWithBiggestId == null )
					policeStationWithBiggestId = policeStation;
				else if( policeStationWithBiggestId.getObjectId() < policeStationWithBiggestId.getObjectId() )
					policeStationWithBiggestId = policeStation;
				
				policeStationSize++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR! File not found\n\n");
			return false;
		}
		
		return true;
		
	}
	
	private void loadIntersection(Geometry key, Intersection inter){
		if( vertexWithBiggestId == null )
			vertexWithBiggestId = key.getId();
		else if( vertexWithBiggestId < key.getId() )
			vertexWithBiggestId = key.getId();
		
		if( !idToIndex.contains(key.getId()) ){
			graph.addVertex(key, inter);
			idToIndex.put(key.getId(), graph.V()-1);
			indexToId.put(graph.V()-1, key.getId());
		}
	}
	
	private void loadEdge(Geometry keyInit, Geometry keyEnd, int idInit, int idEnd, Double haversianCost, Double totalFeaturesCost){
		
		boolean alredyContainsEnd = true;
		boolean alredyContainsInit = true;

		if( !idToIndex.contains(idEnd) ){
			alredyContainsEnd = false;

			idToIndex.put(idEnd, graph.V());
			indexToId.put(graph.V(), idEnd);
			if( !idToIndex.contains(idInit) ){
				idToIndex.put(idInit, graph.V()-1);
				indexToId.put(graph.V()-1, idInit);
			}
		}
		else if( !idToIndex.contains(idInit) ){
			alredyContainsInit = false;
			int idx = idToIndex.get(idEnd);
			keyEnd = graph.getKeyByIdx(idx);
			
			idToIndex.put(idInit, graph.V()-1);
			indexToId.put(graph.V()-1, idInit);
		}
		else if( !(alredyContainsEnd && alredyContainsInit) ){
			int idx = idToIndex.get(idEnd);
			keyEnd = graph.getKeyByIdx(idx);
			idx = idToIndex.get(idInit);
			keyInit = graph.getKeyByIdx(idx);
		}
		
		graph.addEdge(keyInit, keyEnd, haversianCost, totalFeaturesCost);
	}
	
	private void loadEdge(String[] adj){
		Integer id = Integer.valueOf( adj[0] );
		Integer idx = idToIndex.get( id );
		Geometry vertexKey = graph.getKeyByIdx(idx);
		
		for( int i = 1; i < adj.length; i++ ){
			Integer actualId = Integer.valueOf( adj[i] );
			Integer actualIdx = idToIndex.get( actualId );
			Geometry actualVertexKey = graph.getKeyByIdx( actualIdx );
			double haversineDist = Haversine.distance(vertexKey.getLatitud(), vertexKey.getLongitud(),
					actualVertexKey.getLatitud(), actualVertexKey.getLongitud());
			
			graph.addEdge(vertexKey, actualVertexKey, haversineDist, 0);			
		}
	}
	
	public boolean graphToJson(String path)
	{
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();
				
		for( int i = 0; i < graph.V(); i++ ){
			Geometry keyCoordinates = graph.getKeyByIdx(i);
			int keyId = indexToId.get(i);
			
			JSONObject vertex = new JSONObject();
			vertex.put("type","Location");
			vertex.put("id", keyId);
			
			JSONObject geometry = new JSONObject();
			geometry.put("type", "Point");

			JSONArray coordinates = new JSONArray();
			coordinates.add(keyCoordinates.getLatitud());
			coordinates.add(keyCoordinates.getLongitud());
			
			geometry.put("coordinates", coordinates);
			vertex.put("geometry", geometry);
						
			Bag<Edge> vertexEdges = graph.getVertexEdgesByIdx(i);
			
			if( vertexEdges.isEmpty() )
				continue;
			
			JSONArray edges = new JSONArray();
			
			for( Edge e : vertexEdges ){
				JSONObject edge = new JSONObject();
				int endIdx = e.other(i);
				int keyEndId = indexToId.get(endIdx);
				
				if( keyId > keyEndId )
					continue;
				
				Geometry vertexEndCoords = graph.getKeyByIdx( endIdx );
				
				edge.put("type", "Line");
				edge.put("distance", e.weight1());
				
				JSONObject vertexEnd = new JSONObject();
				vertexEnd.put("id", keyEndId);
				
				JSONObject vertexEndGeometry = new JSONObject();
				vertexEndGeometry.put("type", "Point");

				JSONArray vertexEndCoordinates = new JSONArray();
				vertexEndCoordinates.add(vertexEndCoords.getLatitud());
				vertexEndCoordinates.add(vertexEndCoords.getLongitud());
				
				vertexEndGeometry.put("coordinates", vertexEndCoordinates);
				vertexEnd.put("geometry", vertexEndGeometry);
				
				edge.put("vertexEnd", vertexEnd );
				edges.add(edge);		
			}
			
			vertex.put("adjEdges", edges);
						
			list.add(vertex);
		}
		
		obj.put("id", 1);
		obj.put("type", "BogotaRoadMesh");
		obj.put("verticesNumber", graph.V());
		obj.put("edgesNumber", graph.E());
		obj.put("vertices", list);
		
		try {

			FileWriter file = new FileWriter(path);
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			//manejar error
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
	private boolean loadGsonGraph(String path) {

		try {
			System.out.println(path);
			JsonReader reader = new JsonReader(new FileReader(path));
			JsonObject graph = JsonParser.parseReader(reader).getAsJsonObject();

			int verticesNumber = graph.get("verticesNumber").getAsInt();
			JsonElement verticesElement = graph.get("vertices");
			JsonArray jsonVerticesArray = verticesElement.getAsJsonArray();
			
			this.graph = new UndirectedGraph<Geometry, Intersection>(verticesNumber);
			idToIndex = new LinearProbingHash<Integer, Integer>(verticesNumber);
			indexToId = new LinearProbingHash<Integer, Integer>(verticesNumber);

			for (JsonElement element : jsonVerticesArray) {
				int elemVertexId = element.getAsJsonObject().get("id").getAsInt();
				
				JsonElement elemGeometry = element.getAsJsonObject().get("geometry");
				String elemGeomType = elemGeometry.getAsJsonObject().get("type").getAsString();
				JsonArray elemGeomCoordinates = elemGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
				
				ArrayList<Double> elemCoordinates = new ArrayList<Double>();
				
				for ( JsonElement elemCoord : elemGeomCoordinates ) {
					Double actualCoord = elemCoord.getAsDouble();
					elemCoordinates.add(actualCoord);
				}
				
				Geometry vertexKey = new Geometry(elemVertexId, elemGeomType, elemCoordinates);
				
				loadIntersection(vertexKey, new Intersection());

				JsonArray vertexEdges = element.getAsJsonObject().get("adjEdges").getAsJsonArray();
				
				for( JsonElement vertexEdge : vertexEdges ){
					Double vertexEdgeDistance = vertexEdge.getAsJsonObject().get("distance").getAsDouble();
					JsonElement eitherVertex = vertexEdge.getAsJsonObject().get("vertexEnd");
					
					int eitherVertexId = eitherVertex.getAsJsonObject().get("id").getAsInt();
					
					JsonElement eitherVertexGeometry = eitherVertex.getAsJsonObject().get("geometry");
					String eitherVertexGeomType = eitherVertexGeometry.getAsJsonObject().get("type").getAsString();
					JsonArray eitherVertexGeomCoordinates = eitherVertexGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
					
					ArrayList<Double> eitherElemCoordinates = new ArrayList<Double>();

					for ( JsonElement elemCoord : eitherVertexGeomCoordinates ) {
						Double actualCoord = elemCoord.getAsDouble();
						eitherElemCoordinates.add(actualCoord);
					}
					
					Geometry eitherVertexKey = new Geometry(eitherVertexId, eitherVertexGeomType, eitherElemCoordinates);
					
					loadIntersection(eitherVertexKey, new Intersection());
					loadEdge(vertexKey, eitherVertexKey, elemVertexId, eitherVertexId, vertexEdgeDistance, 0.0);
				}
				
			}

		} catch (FileNotFoundException e) {
			System.out.println("\n-------------------------------------------\n");
			System.out.println("ERROR! File not found");
			System.out.println("\n-------------------------------------------\n\n");
			return false;
		}
		catch (IllegalArgumentException e){
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;

	}
	
	private void graphToJsonWithElements(){
		
	}
	
	private void loadGsonGraphWithElements(){
		
	}
	
	private void loadFeatureElement(Feature feature){
		int nearestIdx = graph.getNearestVertexIdx(feature.getGeometry());
		Intersection intersection = graph.getInfoVertexByIdx(nearestIdx);
		intersection.addFeature(feature);
		graph.increaseWeight2ByVertexIdx(nearestIdx, 1);
	}

	private void loadPoliceStationElement(PoliceStation policeStation){
		int nearestIdx = graph.getNearestVertexIdx(policeStation.getGeometry());
		Intersection intersection = graph.getInfoVertexByIdx(nearestIdx);
		intersection.addPoliceStation(policeStation);
	}

}