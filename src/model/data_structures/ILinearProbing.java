package model.data_structures;

import java.util.Iterator;

public interface ILinearProbing<Key extends Comparable<Key>, Value> {

	public void put(Key key ,Value value);

	public void resize(int capacity);
	
	public Iterator<Value> get(Key key);
		
	public IQueue<Value>[] getValues();
	
	public int size();
	
	public int hash(Key key);
	
	public boolean isEmpty();
	
	public boolean contains(Key key);
	
	public Iterator<Key> keys();
	
	public Iterator<Value> values();
	
	public Iterator<Value> deleteAndReturn();
	
}
