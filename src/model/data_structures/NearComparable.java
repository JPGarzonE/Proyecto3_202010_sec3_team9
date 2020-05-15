package model.data_structures;

public interface NearComparable<T> extends Comparable<T>{

	public int compareNearest(T other1, T other2);
	
	public int compareTo(T other);
}
