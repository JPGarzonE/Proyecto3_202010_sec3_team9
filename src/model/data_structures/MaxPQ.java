package model.data_structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

import model.logic.Feature;

/**
 *  The {@code MaxPQ} class represents a priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 *  operations, along with methods for peeking at the maximum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a <em>binary heap</em>.
 *  The <em>insert</em> and <em>delete-the-maximum</em> operations take
 *  &Theta;(log <em>n</em>) amortized time, where <em>n</em> is the number
 *  of elements in the priority queue. This is an amortized bound 
 *  (and not a worst-case bound) because of array resizing operations.
 *  The <em>min</em>, <em>size</em>, and <em>is-empty</em> operations take 
 *  &Theta;(1) time in the worst case.
 *  Construction takes time proportional to the specified capacity or the
 *  number of items used to initialize the data structure.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Key> the generic type of key on this priority queue
 */

public class MaxPQ<Value> implements IMaxPQ<Value>{

	private Value[] priorityQueue;
	
	private int n;
	
	private Comparator<Value> comparator;
	
	public MaxPQ(int capacity){
		priorityQueue = (Value[]) new Object[ capacity + 1 ];
		n = 0;
	}
	
	public MaxPQ(){
		this(1);
	}
	
	public MaxPQ(int capacity, Comparator<Value> comparator){
		this.comparator = comparator;
		priorityQueue = (Value[]) new Object[ capacity + 1 ];
		n = 0;
	}
	
	public MaxPQ(Value[] val){
		n = val.length;
		priorityQueue = (Value[]) new Object[val.length + 1];
		
		for( int i = 0; i < n; i++ )
			priorityQueue[ i+1 ] = val[i];
		
		for( int k = n/2; k >= 1; k--)
			sink(k);
		
	}
	
	public void insert(Value v) {
		
		if (n == priorityQueue.length - 1) resize(2 * priorityQueue.length);
		
		priorityQueue[++n] = v;
        swim(n);
        
	}
	
	private void resize(int capacity) {
        assert capacity > n;
        Value[] temp = (Value[]) new Object[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = priorityQueue[i];
        }
        priorityQueue = temp;
    }
	
	public Value max() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return priorityQueue[1];
	}

	
	public ArrayList<Value> max(int n) {
		
		ArrayList<Value> firstNVals = new ArrayList<Value>(n);

		for( int i = 1; i <= n; i++ )
			firstNVals.add( i-1, priorityQueue[i] );
			
		return firstNVals;
	}

	
	public Value delMax() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Value max = priorityQueue[1];
        exch(1, n--);
        sink(1);
        priorityQueue[n+1] = null;     // to avoid loitering and help with garbage collection
        if ((n > 0) && (n == (priorityQueue.length - 1) / 4)) resize(priorityQueue.length / 2);
        return max;
	}

	
	public boolean isEmpty() {
		return n == 0;
	}

	
	public int size() {
		return n;
	}
	
	/**
	 * Change the comparator of the class with the ojective of changing the priority 
	 * of the data and reorder all the elements in the queue.
	 * @param comp the new comparator to change
	 */
	public void changeComparator(Comparator<Value> comp){
		comparator = comp;

		for( int k = n/2; k >= 1; k--)
			sink(k);
	}
	
	private void swim(int k){
		
		while( k > 1 && less(k/2, k) ){
			exch(k, k/2);
			k = k/2;
		}	
	}
	
	private void sink(int k){
		while(2*k <= n){
			int j = 2*k;
			
			if( j < n && less(j, j+1) )
				j++;
			
			if( !less(k, j) )
				break;
			
            exch(k, j);
            k = j;
		}
	}
	
	private boolean less(int i, int j){
		if(comparator == null){
			return ((Comparable<Value>) priorityQueue[i]).compareTo(priorityQueue[j]) < 0;
		}
		else{
			return comparator.compare(priorityQueue[i], priorityQueue[j]) < 0;
		}
	}
	
	private void exch(int i, int j) {
        Value swap = priorityQueue[i];
        priorityQueue[i] = priorityQueue[j];
        priorityQueue[j] = swap;
    }

}
