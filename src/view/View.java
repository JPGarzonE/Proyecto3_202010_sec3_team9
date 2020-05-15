package view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import model.data_structures.ArrayNode;
import model.logic.Feature;

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
			System.out.println("1. (Req1A) - Obtener los M comparendos con mayor gravedad");
			System.out.println("2. Exit");
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
				System.out.println("\n\tOBJECTID: " + feature.getObjectId());
				System.out.println("\n\tFECHA_HORA: " + feature.getDate());
				System.out.println("\n\tINFRACCION: " + feature.getInfraction());
				System.out.println("\n\tCLASE_VEHI: " + feature.getVehicleClass());
				System.out.println("\n\tTIPO_SERVI: " + feature.getServiceType());
				System.out.println("\n\tLOCALIDAD: " + feature.getLocality());
				System.out.println("\n");
			}
			
		}
		
		public void printGeneralFeaturesInfo(int edgesSize, int vertexSize){
			
			System.out.println("\nDATOS GENERALES:");
			System.out.println("\n-----------------------------------");
			
			System.out.println("\nNUMERO TOTAL DE VERTICES: " + vertexSize);
			System.out.println("\nNUMERO TOTAL DE ARCOS: " + edgesSize);
						
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
