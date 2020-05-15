package model.data_structures;

public class LinkedList<T> implements ILinkedList<T>{

	private DataNode<T> firstNode;
	
	private int size;
	
	public int size(){
		return size;
	}

	public void addNode(T nodeInfo) {
		
		DataNode<T> node = new DataNode<T>(nodeInfo);
		
		if( isEmpty() ){
			firstNode = node;
		}
		else{
			DataNode<T> actual = firstNode;
			
			while( actual.next() != null )
				actual = actual.next();
			
			actual.setNext(node);
		}
		
		size++;
	}

	public DataNode<T> getFirstNode() {
		return firstNode;
	}
	
	public boolean isEmpty(){
		return firstNode == null;
	}
	
}
