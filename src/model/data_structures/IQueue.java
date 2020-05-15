package model.data_structures;

import java.util.Iterator;

public interface IQueue<Item> {

	public boolean isEmpty();
	
	public int size();
	
	public Item peek();
	
	public void enqueue( Item item );
	
	public Item dequeue();
	
	public Iterator<Item> iterator();
}