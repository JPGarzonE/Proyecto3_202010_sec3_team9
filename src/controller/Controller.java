package controller;

import java.util.Scanner;

import Exception.DataStructureException;
import model.logic.Feature;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	static final String DATA_PATH = "./data/comparendos_dei_2018_Bogotá_D.C_small.geojson";
	static final String DATA_PATH_EDGES = "./data/bogota_arcos.txt";
	static final String DATA_PATH_VERTICES = "./data/bogota_vertices.txt";
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}
		
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		
		view.printMessage("\n La aplicación va a cargar los datos inmediatamente de esta ruta: " + DATA_PATH);
		view.printMessage("\n ¿Esta de acuerdo? (Y/n)");
		String answer = lector.next();

		if( answer.equalsIgnoreCase("Y") ){
			loadData();
		}else{
			view.printMessage("\n Cambie el archivo y vuelva a entrar. Gracias!");
			fin = true;
		}

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
				case 1:
					break;
				case 2:
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
	
	private void loadData(){
		view.printMessage("--------- \nCargando datos de comparendos...");
	    modelo = new Modelo();
	    modelo.loadStreets(DATA_PATH_VERTICES, DATA_PATH_EDGES);
	    int vertexNum = modelo.vertexSize();
	    int edgesNum = modelo.edgesSize();
	    Feature biggestIdFeature = modelo.getFeatureWithBiggestId();
	    view.printGeneralFeaturesInfo(vertexNum, edgesNum);
	}
	
}
