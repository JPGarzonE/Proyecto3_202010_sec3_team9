package model.data_structures;

public class TreeNode<Key extends Comparable<Key>, Value> {

	private static final boolean RED   = true;
	
    private static final boolean BLACK = false;
	
	private Key key;
	
    private Value val;
    
    /**
     * Children nodes
     */
    private TreeNode<Key, Value> left, right;
    
    /**
     * Color of the parent node (Black or Red)
     */
    private boolean color;
    
    /**
     * Counter of the elements number in the subtree
     */
    private int size;

    public TreeNode(Key key, Value val, boolean color, int size) {
        this.key = key;
        this.val = val;
        this.color = color;
        this.size = size;
    }

	public Key getKey() {
		return key;
	}

	public Value getVal() {
		return val;
	}

	public TreeNode<Key, Value> getLeft() {
		return left;
	}

	public TreeNode<Key, Value> getRight() {
		return right;
	}

	public boolean isRed() {
		return color == RED;
	}

	public int getSize() {
		return size;
	}

	public void setLeft(TreeNode<Key, Value> left) {
		this.left = left;
	}

	public void setRight(TreeNode<Key, Value> right) {
		this.right = right;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public void increaseSize(int size) {
		this.size += size;
	}
	
	public void setSize( int size ){
		this.size = size;
	}

	public void setVal(Value val) {
		this.val = val;
	}
	
}