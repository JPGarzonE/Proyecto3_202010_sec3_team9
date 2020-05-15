package model.data_structures;

import java.util.Iterator;

public class RedBlackBST<Key extends Comparable<Key>, Value> implements IRedBlackBST<Key, Value> {

	private static final boolean RED   = true;
	
    private static final boolean BLACK = false;
    
    private TreeNode<Key, Value> root;
    
    public RedBlackBST() {
    
    }
	
    public void emptyTree(){
    	root = null;
    }
    
	public int size() {
		return size(root);
	}
	
	private int size( TreeNode<Key, Value> node ){
		if (node == null)
			return 0;
		
        return node.getSize();
	}

	
	public boolean isEmpty() {
		
		return root == null;
	}
	
	private boolean isRed( TreeNode<Key, Value> node ){
		if (node == null) 
			return false;
		
        return node.isRed();
	}

	
	public Value get(Key key) {
	
		if( key == null )
			throw new IllegalArgumentException("Key can't be null in get(k)");
			
		TreeNode<Key, Value> actualNode = root;
		
		while( actualNode != null ){
			
			int comp = key.compareTo( actualNode.getKey() );
			
			if( comp < 0 ) actualNode = actualNode.getLeft();
			else if( comp > 0 ) actualNode = actualNode.getRight();
			else return actualNode.getVal();
			
		}
		
		return null;
	}

	
	public int getHeight(Key key) {
		
		if( key == null )
			throw new IllegalArgumentException("Key can't be null in getHeight(key)");
		
		TreeNode<Key, Value> actualNode = root;
		int size = 0;
		
		while( actualNode != null ){
			
			int comp = key.compareTo( actualNode.getKey() );
			
			if( comp < 0 ) actualNode = actualNode.getLeft();
			else if (comp > 0) actualNode = actualNode.getRight();
			else return size;
		
			size++;
		}
		
		return -1;
	}

	
	public boolean contains(Key key) {
		
		return get(key) != null;
	}

	
	public void put(Key key, Value value) {
		if (key == null) 
			throw new IllegalArgumentException("key can't be null in put(key,value)");
        if (value == null)
        	throw new IllegalArgumentException("value can't be null in put(key,value)");
        
        root = put(root, key, value);
        
        root.setColor( BLACK );
		
	}
	
	private TreeNode<Key, Value> put( TreeNode<Key, Value> node, Key key, Value value ){
		
		if( node == null )
			return new TreeNode<Key, Value>(key, value, RED, 1);
		
		int comp = key.compareTo( node.getKey() );
		
		if( comp < 0 ) node.setLeft( put( node.getLeft(), key, value) );
		else if( comp > 0 ) node.setRight( put(node.getRight(), key, value) );
		else node.setVal(value);
		
		if( isRed( node.getRight() ) && !isRed( node.getLeft() )  )
			node = rotateLeft( node );
		if( isRed( node.getLeft() ) && isRed( node.getLeft().getLeft() ) )
			node = rotateRight( node );
		if( isRed( node.getLeft() ) && isRed( node.getRight() ) )
			flipColors( node );
			
		node.setSize( size( node.getLeft() ) + size( node.getRight() ) + 1 );
		
		return node;
	}

	
	public int height() {
		
		return height( root );
	}
	
	private int height( TreeNode<Key, Value> node ){
		
		if( node == null )
			return -1;
		
		return 1+ Math.max( height(node.getLeft()) , height(node.getRight()) );
	}

	public Key min() {
		TreeNode<Key, Value> min = min(root);
		
		return min != null ? min.getKey() : null;
	}
	
	private TreeNode<Key, Value> min( TreeNode<Key, Value> node ) {
		
		TreeNode<Key, Value> actualNode = node;
		
		while( actualNode != null ){
			
			if( actualNode.getLeft() == null )
				return actualNode;
			else
				actualNode = actualNode.getLeft();
			
		}
		
		return null;
	}

	
	public Key max() {
		TreeNode<Key, Value> max = max(root);
		
		return max != null ? max.getKey() : null;
	}
	
	private TreeNode<Key, Value> max( TreeNode<Key, Value> node ){
		
		TreeNode<Key, Value> actualNode = node;
		
		while( actualNode != null ){
			
			if( actualNode.getRight() == null )
				return actualNode;
			else
				actualNode = actualNode.getRight();
			
		}
		
		return null;
		
	}
	
    private TreeNode<Key, Value> rotateRight(TreeNode<Key, Value> node) {
        
    	TreeNode<Key, Value> x = node.getLeft();
    	
        node.setLeft( x.getRight() );
        x.setRight( node );
        x.setColor( x.getRight().isRed() );
        x.getRight().setColor( RED );
        
        x.setSize( node.getSize() );
        node.setSize( size( node.getLeft() ) + size( node.getRight() ) + 1 );
        
        return x;
    }

    private TreeNode<Key, Value> rotateLeft(TreeNode<Key, Value> node) {
        
    	TreeNode<Key, Value> x = node.getRight();
    	
        node.setRight( x.getLeft() );
        x.setLeft( node );
        x.setColor( x.getLeft().isRed() );
        x.getLeft().setColor( RED );
        
        x.setSize( node.getSize() );
        node.setSize( size( node.getLeft() ) + size( node.getRight() ) + 1 );
        
        return x;
    }

    private void flipColors(TreeNode<Key, Value> node) {
    	
        node.setColor( !node.isRed() );
        node.getLeft().setColor( !node.getLeft().isRed() );
        node.getRight().setColor( !node.getRight().isRed() );
    }
	
    public Iterator<Key> keys() {
        if (isEmpty()) 
        	return new Queue<Key>().iterator();
        
        return keysInRange( min(), max() );
    }
    
    public Iterator<Key> keysInRange(Key init, Key end) {
        if (init == null) 
        	throw new IllegalArgumentException("init can't be null in keysInRange(init,end)");
        
        if (end == null) 
        	throw new IllegalArgumentException("end can't be null in keysInRange(init,end)");

        Queue<Key> queue = new Queue<Key>();

        keysInRange(root, queue, init, end);
        
        return queue.iterator();
    } 

    private void keysInRange(TreeNode<Key,Value> node, Queue<Key> queue, Key init, Key end) { 
        if (node == null) 
        	return;
        
        int cmplo = init.compareTo( node.getKey() ); 
        int cmphi = end.compareTo( node.getKey() );
        
        if (cmplo < 0) 
        	keysInRange(node.getLeft(), queue, init, end); 
        
        if (cmplo <= 0 && cmphi >= 0)
        	queue.enqueue( node.getKey() ); 
        
        if (cmphi > 0)
        	keysInRange(node.getRight(), queue, init, end); 
    } 

	
	public Iterator<Value> valuesInRange(Key init, Key end) {
		if (init == null) 
        	throw new IllegalArgumentException("init can't be null in valuesInRange(init,end)");
        
        if (end == null) 
        	throw new IllegalArgumentException("end can't be null in ValuesInRange(init,end)");

        Queue<Value> queue = new Queue<Value>();

        valuesInRange(root, queue, init, end);
        
        return queue.iterator();
	}
	
	private void valuesInRange(TreeNode<Key,Value> node, Queue<Value> queue, Key init, Key end) { 
        if (node == null) 
        	return;
        
        int cmplo = init.compareTo( node.getKey() ); 
        int cmphi = end.compareTo( node.getKey() );
        
        if (cmplo < 0) 
        	valuesInRange(node.getLeft(), queue, init, end); 
        
        if (cmplo <= 0 && cmphi >= 0)
        	queue.enqueue( node.getVal() ); 
        
        if (cmphi > 0)
        	valuesInRange(node.getRight(), queue, init, end); 
    }
	
	public int quantityOfValuesInRange( Key init, Key end ){
		if (init == null) 
        	throw new IllegalArgumentException("init can't be null in valuesInRange(init,end)");
        
        if (end == null) 
        	throw new IllegalArgumentException("end can't be null in ValuesInRange(init,end)");

        int quantityOfValuesInRange = quantityOfValuesInRange(root, init, end);
        
        return quantityOfValuesInRange;
	}
	
	private int quantityOfValuesInRange(TreeNode<Key,Value> node, Key init, Key end){
		if (node == null) 
        	return 0;
        
		int counter = 0;
        int cmplo = init.compareTo( node.getKey() ); 
        int cmphi = end.compareTo( node.getKey() );
        
        if (cmplo < 0) 
        	counter += quantityOfValuesInRange(node.getLeft(), init, end); 
        
        if (cmplo <= 0 && cmphi >= 0)
        	counter += 1; 

        if (cmphi > 0)
        	counter += quantityOfValuesInRange(node.getRight(), init, end); 
        
        return counter;
	}
	
	
   /***************************************************************************
    *  Check integrity of red-black tree data structure.
    *  @author Robert Sedgewick
    *  @author Kevin Wayne
    ***************************************************************************/
    private boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!is23())             System.out.println("Not a 2-3 tree");
        if (!isBalanced())       System.out.println("Not balanced");
        return isBST() && isSizeConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(TreeNode<Key, Value> x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.getKey().compareTo(min) <= 0) return false;
        if (max != null && x.getKey().compareTo(max) >= 0) return false;
        return isBST(x.getLeft(), min, x.getKey()) && isBST(x.getRight(), x.getKey(), max);
    } 

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(TreeNode<Key,Value> x) {
        if (x == null) return true;
        if (x.getSize() != size(x.getLeft()) + size(x.getRight()) + 1) return false;
        return isSizeConsistent(x.getLeft()) && isSizeConsistent(x.getRight());
    } 

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() { return is23(root); }
    private boolean is23(TreeNode<Key,Value> x) {
        if (x == null) return true;
        if (isRed(x.getRight())) return false;
        if (x != root && isRed(x) && isRed(x.getLeft()))
            return false;
        return is23(x.getLeft()) && is23(x.getRight());
    } 

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        TreeNode<Key, Value> x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.getLeft();
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(TreeNode<Key, Value> x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.getLeft(), black) && isBalanced(x.getRight(), black);
    }

}