package model.data_structures;

public class DataNode<T>{
	
	private DataNode<T> next;
	
	private T nodeInfo;
	
	public DataNode(T nodeInfo){
		this.setNodeInfo(nodeInfo);
	}

	public DataNode<T> next() {
		return next;
	}

	public void setNext(DataNode<T> next) {
		this.next = next;
	}

	public T getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(T nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	
}
