package view;

import java.awt.BorderLayout;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import com.teamdev.jxmaps.swing.MapView;

import model.data_structures.DynamicArray;
import model.data_structures.LinearProbingHash;

import com.teamdev.jxmaps.*;

public class CustomMap extends MapView {
	
	private static final long serialVersionUID = -8897679201261043477L;

	private static Map map;
	
	private CircleOptions circleOptions;
	
	private PolylineOptions lineOptions;
	
	
	public CustomMap(DynamicArray<LatLng[]> edgeArray){
		
		JFrame frame = new JFrame("Hello world");
		
		setOnMapReadyHandler( status -> {
			System.out.println("status");
			map = getMap();
			MapOptions mapOptions = new MapOptions();
			MapTypeControlOptions controlOptions = new MapTypeControlOptions();
			controlOptions.setPosition(ControlPosition.BOTTOM_LEFT);
			mapOptions.setMapTypeControlOptions(controlOptions);
			map.setOptions(mapOptions);
			map.setCenter(new LatLng(4.660950272189076, -74.06913757324217));
			map.setZoom(12);
		});
		
		try{
			System.out.println("sleep");
			TimeUnit.SECONDS.sleep(5);
		} catch( InterruptedException e ){
			e.printStackTrace();
		}
		
		// Settings
		System.out.println("settings");
		frame.add(this, BorderLayout.CENTER);
		frame.setSize(1500, 1000);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		circleOptions = new CircleOptions();
		circleOptions.setStrokeColor("#FF0000");
		circleOptions.setRadius(30);
		circleOptions.setFillColor("#FF0000");
		circleOptions.setFillOpacity(0.7);
		
		lineOptions = new PolylineOptions();
		lineOptions.setGeodesic(true);
		lineOptions.setStrokeColor("#4400cc");
		lineOptions.setStrokeOpacity(1);
		lineOptions.setStrokeWeight(2.0);
		
		drawAllPaths(edgeArray);
		System.out.println("End draw");
	}
	
	public void generateSimplePath(LatLng[] path){
		Polyline line = new Polyline(map);
		line.setPath(path);
		line.setOptions(lineOptions);

//		System.out.println("Line Begin Point: ");
//		System.out.println(path[0].getLat());
//		System.out.println(path[0].getLng());
//		System.out.println("Line End Point: ");
//		System.out.println(path[1].getLat());
//		System.out.println(path[1].getLng());
	}
	
	public void generateCircle(LatLng center){
		Circle circle = new Circle(map);
		circle.setCenter(center);
		circle.setRadius(20);
		circle.setVisible(true);
		circle.setOptions(circleOptions);

//		System.out.println("Circle End Point: ");
//		System.out.println(center.getLat());
//		System.out.println(center.getLng());
	}
	
	public void drawAllPaths(DynamicArray<LatLng[]> edgeArray){
		LinearProbingHash<String, Integer> vertexMap = new LinearProbingHash<>();
		
		for(int i = 0; i < edgeArray.size(); i++){
//			System.out.println("Draw all paths: " + i);
			
			double lat1 = edgeArray.get(i)[0].getLat();
			double long1 = edgeArray.get(i)[0].getLng();
			
			double lat2 = edgeArray.get(i)[1].getLat();
			double long2 = edgeArray.get(i)[1].getLng();
			
			if( !vertexMap.contains(lat1+"$"+long1) ){
//				System.out.println("pip");
				vertexMap.put(lat1+"$"+long1, 0);
				generateCircle(edgeArray.get(i)[0]);
			}
			
			generateSimplePath(edgeArray.get(i));
			
			if( !vertexMap.contains(lat2+"$"+long2) ){
//				System.out.println("pep");
				vertexMap.put(lat2+"$"+long2, 1);
				generateCircle(edgeArray.get(i)[1]);
			}
			
		}
		
	}
	
}
