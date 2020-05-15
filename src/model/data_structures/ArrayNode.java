package model.data_structures;

public class ArrayNode<Key, Value> {

	private Key key;
	
	private Value value;

	public ArrayNode( Key key, Value val ){
		this.key = key;
		this.value = val;
	}
	
	public Key getKey() {
		return key;
	}

	public Value getValue() {
		return value;
	}
}
