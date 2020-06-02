package view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import model.data_structures.ArrayNode;
import model.data_structures.Edge;
import model.data_structures.GenericEdge;
import model.logic.Feature;
import model.logic.Geometry;
import model.logic.PoliceStation;;

public class View 
{
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Write graph in JSON");
			System.out.println("2. Find the nearest vertex id by a latitud and a longitud given");
			System.out.println("3. (Req A1) - Cheapest path between two locations by distance");
			System.out.println("4. (Req A2) - Cheapest communications net for video cameras in the M most severe intersections");
			System.out.println("5. (Req B1) - Cheapest path between two locations by features");
			System.out.println("6. (Req B2) - Cheapest communications net for video cameras in the M with most features");
			System.out.println("7. (Req C1) - Shortes path to attend the M most severe features");
			System.out.println("8. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}
		
		public void printFeature(Feature feature){
			
			if(feature == null){
				System.out.println("No hay info de este comparendo o no existe.");
			}
			else{
				System.out.println("\nCOMPARENDO:");
				System.out.println("\tOBJECTID: " + feature.getObjectId());
				System.out.println("\tFECHA_HORA: " + feature.getDate());
				System.out.println("\tINFRACCION: " + feature.getInfraction());
				System.out.println("\tCLASE_VEHI: " + feature.getVehicleClass());
				System.out.println("\tTIPO_SERVI: " + feature.getServiceType());
				System.out.println("\tLOCALIDAD: " + feature.getLocality());
				System.out.println("\n");
			}
			
		}
		
		public void printGeneralFeaturesInfo( int featuresSize, Feature biggestFeature ){
			
			System.out.println("\nCOMPARENDOS - DATOS GENERALES:");
			System.out.println("-----------------------------------");
			
			System.out.println("NUMERO TOTAL DE COMPARENDOS: " + featuresSize);
			printFeature(biggestFeature);
						
		}
		
		public void printGeneralRoadMeshInfo( int vertexSize, int edgesSize, int biggestVertexId, Double biggestVertexLatitud,
				Double biggestVertexLongitud){
			
			System.out.println("\nMALLA VIAL - DATOS GENERALES:");
			System.out.println("-----------------------------------");
			
			System.out.println("NUMERO TOTAL DE VERTICES: " + vertexSize);
			System.out.println("NUMERO TOTAL DE ARCOS: " + edgesSize);
			printVertex(biggestVertexId, biggestVertexLatitud, biggestVertexLongitud);
			
		}
		
		public void printVertex(int id, Double latitud, Double longitud){
			
			System.out.println("\nVERTICE:");
			System.out.println("\tID: " + id);
			System.out.println("\tLATITUD: " + latitud);
			System.out.println("\tLONGITUD: " + longitud);
			System.out.println("\n");
			
		}
		
		public void printGenericGeometryEdge( GenericEdge<Geometry> edge ){
			
			System.out.println("ARCO:");
			System.out.println("\t" + edge.either().getId() + " --> ( " + edge.either().getId() + "-" + edge.other( edge.either() ).getId() + " )");
		}
		
		public void printGeneralPoliceStationsInfo( int totalSize, PoliceStation biggestPoliceStation ){
			
			System.out.println("\nESTACIONES DE POLICIA - DATOS GENERALES:");
			System.out.println("-----------------------------------");
			
			System.out.println("NUMERO TOTAL DE ESTACIONES: " + totalSize);
			printPoliceStation(biggestPoliceStation);
			
		}
		
		public void printPoliceStation( PoliceStation policeStation) {
			if(policeStation == null){
				System.out.println("No hay info de esta estacion de policia o no existe.");
			}
			else{
				System.out.println("\nESTACION DE POLICIA:");
				System.out.println("\tOBJECTID: " + policeStation.getObjectId());
				System.out.println("\tEPODESCRIP: " + policeStation.getDescription());
				System.out.println("\tEPODIR_SITIO: " + policeStation.getAddress());
				System.out.println("\tEPOLATITUD: " + policeStation.getLatitud());
				System.out.println("\tEPOLONGITUD: " + policeStation.getLongitud());
				System.out.println("\tEPOSERVICIO: " + policeStation.getService());
				System.out.println("\tEPOHORARIO: " + policeStation.getSchedule());
				System.out.println("\tEPOTELEFON: " + policeStation.getTelephone());
				System.out.println("\tEPOIULOCAL: " + policeStation.getLocalIdentifier());
				System.out.println("\n");
			}
		}
		
		public void printExecutionTime( long miliseconds ){
			double seconds = (double)miliseconds / (double)1000;
			System.out.println("\nEl tiempo de ejecuci�n de la operaci�n fue de:");
			System.out.println("\t" + miliseconds + " milisegundos ---> " + seconds + " segundos\n\n");
		}
		
		public void printMinimax( double[] minmax ){
			
			int altoRect = 10;
			int anchoRect = 20;
			
			DecimalFormat format = new DecimalFormat("#.##");
			
			String maxlat = format.format(minmax[2]);
			String maxlong = format.format(minmax[3]);
			String minlat = format.format(minmax[0]);
			String minlong = format.format(minmax[1]);
			
			System.out.println("MaxLat (" + maxlat + ")");
			System.out.println("MaxLong (" + maxlong + ")");

			System.out.println("minLat (" + minlat + ")");
			System.out.println("minLong (" + minlong + ")\n");
			
			for(int i = 0; i < altoRect ; i++){
	            
				String linea = "";
				
				for(int j = 0; j < anchoRect ; j++){
					
					if( i == 0 && j == 0 )
						linea += (maxlat);
					else if( i == 0 && j == anchoRect - 1)
						linea += (maxlong);
					else if( i == altoRect - 1 && j == 0 )
						linea += (minlat);
					else if( i == altoRect - 1 && j == anchoRect - 1 )
						linea += (minlong);
					else if( i == 0 || i == altoRect - 1 || j == 0 || j == anchoRect - 1){
						if(  j == anchoRect - 1  ){
							linea += "        * ";
						}else{
							linea += "* ";
						}
					}else
						linea += "  ";
			    }
				
				System.out.println(linea);
			 }
			
			System.out.println("\n\n");
			
		}
		
		public void printFeatures( ArrayList<Feature> features ){
			printMessage("\n Resultado-----------------------------");
			for(int i = 0; i < features.size(); i++)
				printFeature(features.get(i));
		}
		
		public void printFeatures( Feature[] features ){
			printMessage("\n Resultado-----------------------------");
			for(int i = 0; i < features.length; i++)
				printFeature(features[i]);
		}
		
		public void printFeatures( Iterator<Feature> features ){
			printMessage("\n Resultado-----------------------------");
			while( features.hasNext() )
				printFeature( features.next() );
		}
		
		public void printFeaturesQuantityInDateRange( ArrayList<ArrayNode<String, Integer>> featuresQuantities, int totalFeaturesQuantity ){
			int averageQuantityPerRow = totalFeaturesQuantity/featuresQuantities.size();

			int maxStarQuantityPerRow = 25;
			int starUnit = averageQuantityPerRow / maxStarQuantityPerRow;
			
			printMessage("		Rango de fechas 		|		Comparendos durante el a�o");
			printMessage("--------------------------------------------------------------------------------------------------------------");
			featuresQuantities.forEach( (node)->{
				
				try{
					int starQuantity = (int) Math.ceil(node.getValue() / starUnit);
					String starsString = "";
					for( int i = 0; i < starQuantity*(3/2); i++ )
						starsString += "*";
					
					if(!starsString.equals("")) printMessage( node.getKey() + "	| " + starsString );
				}
				catch(ArithmeticException e){
				
				}

				
			});
			
			printMessage("Cada * representa " + starUnit + " Comparendos");
		}
		
		public void printFeaturesComparatedByDate( String date1, String date2, Map<String, Integer> dates1, 
				Map<String, Integer> dates2){
			
			printMessage("Infracci�n	|	" + date1 + "	|	" + date2);
			
			dates1.forEach( (key, val) -> {
				boolean dates2ContainKey = dates2.containsKey(key);
				int dates2Num = dates2ContainKey ? dates2.get(key) : 0;
				printMessage(key + "		|		" + val + "	|	" + dates2Num);
				if( dates2ContainKey ) dates2.remove(key);
			});
			
			dates2.forEach( (key, val) -> {
				printMessage(key + "		|		" + 0 + "	|	" + val);
			});
		}
		
		public void printFeaturesComparatedByServiceType( String particularType, String publicType, Map<String, Integer> particularT, 
				Map<String, Integer> publicT){
			
			printMessage("Infracci�n	|	" + particularType + "	|	" + publicType);
			
			particularT.forEach( (key, val) -> {
				boolean publicTContainKey = publicT.containsKey(key);
				int publicTNum = publicTContainKey ? publicT.get(key) : 0;
				printMessage(key + "		|		" + val + "	|	" + publicTNum);
				if( publicTContainKey ) publicT.remove(key);
			});
			
			publicT.forEach( (key, val) -> {
				printMessage(key + "		|		" + 0 + "	|	" + val);
			});
		}
		
		
		public void printFeaturesBetweenTwoDates( String initialDate, String finalDate, 
				Map<String, Integer> features, String message ){
			
			printMessage(message + " del " + initialDate + " al " + finalDate);
			
			printMessage("Infracci�n	|	#Comparendos");
			
			features.forEach( (key, val) -> {
				printMessage(key + "		|	" + val);
			});
			
			printMessage("\n\n");
		}
		
		
		public void printHistogramL (String locality, Map<String, Integer>features)
		{
			printMessage ("Localidad		|			#Comparendos");
			
			features.forEach( (key, val) -> {
				printMessage(key + "			|			" + val);
			});
			
			printMessage("\n\n");
		}
		
		public void printHistogramLocality (Map<String, Integer>features)
		{
			features.forEach((key, val) -> {
				String resp = "";
				for (int i = 0; i<val ; i++)
				{
					resp += "*";
				}
				System.out.println(key+"	|	"+resp);
			});
		}
		
		private ArrayList<Integer> number (int n)
		{
			ArrayList<Integer> numeros = new ArrayList<Integer>();
			for (int i = 1; i<=n; i++)
			{
				numeros.add(n*i);
			}
			return numeros;
		}
}
