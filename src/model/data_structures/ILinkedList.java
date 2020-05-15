package model.data_structures;

public interface ILinkedList<T> {

	public int size();
	
	public DataNode<T> getFirstNode();
		
	public void addNode( T nodeInfo );
	
}
