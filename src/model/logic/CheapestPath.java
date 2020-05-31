package model.logic;

import java.util.Iterator;

import model.data_structures.IQueue;
import model.data_structures.Queue;

public class CheapestPath<T> {
	
	private Double minimumCost;
	
	private Double distance;
	
	private IQueue<T> path;
	
	private Integer firstElement;
	
	public CheapestPath(){
		this.path = new Queue<T>();
		this.distance = 0.0;
	}
	
	public CheapestPath( Queue<T> path ){
		this.path = path;
		this.distance = 0.0;
	}
	
	public int size(){
		return path.size();
	}
	
	public Integer firstElement(){
		return firstElement;
	}
	
	public void setFirstElement( Integer el ){
		this.firstElement = el;
	}
	
	public Double getMinimumCost(){
		return minimumCost;
	}
	
	public void setMinimumCost( Double cost ){
		this.minimumCost = cost;
	}
	
	public Double getDistance(){
		return distance;
	}
	
	public Iterator<T> getPath(){
		return path.iterator();
	}
	
	public void addPathStep( T step, Double weight ){
		path.enqueue( step );
		distance += weight;
		
		if( minimumCost == null || minimumCost > weight ) minimumCost = weight;
	}
	
}