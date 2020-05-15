package model.data_structures;

import java.util.Iterator;

public interface IRedBlackBST<Key extends Comparable<Key>, Value> {

	public void emptyTree();
	
	public int size();
	
	public boolean isEmpty();
	
	public Value get(Key key);
	
	public int getHeight(Key key);
	
	public boolean contains(Key key);
	
	public void put(Key key, Value value);
	
	public int height();
	
	public Key min();
	
	public Key max();
		
	public Iterator<Key> keys();
	
	public Iterator<Value> valuesInRange(Key init, Key end);
	
	public Iterator<Key> keysInRange(Key init, Key end);
	
	public int quantityOfValuesInRange(Key init, Key end);
	
}