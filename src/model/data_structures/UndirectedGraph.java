package model.data_structures;

public class UndirectedGraph<Key extends Comparable<Key>, Vertex> implements IUndirectedGraph<Key, Vertex>{

	private EdgeWeightedGraph graph;
	private LinearProbingHash<Key, Integer> keyToInteger;
	private LinearProbingHash<Integer, Key> integerToKey;

	private int V;
	private LinearProbingHash<Key, Vertex> infoVertex;
	
	public UndirectedGraph( int v ){
		graph = new EdgeWeightedGraph(v);
		keyToInteger = new LinearProbingHash<Key, Integer>();
		integerToKey = new LinearProbingHash<Integer, Key>();
		infoVertex =  new LinearProbingHash<Key, Vertex>();
		V = 0;
	}
	
	public int V() {
		
		return graph.V();
	}

	
	public int E() {
		
		return graph.E();
	}

	
	public void addEdge(Key idVertexIni, Key idVertexEnd, double cost) {
		if(!keyToInteger.contains(idVertexIni)){
			keyToInteger.put(idVertexIni, V);
			V++;
		}
		
		if(!keyToInteger.contains(idVertexEnd)){
			keyToInteger.put(idVertexEnd, V);
			V++;
		}
		
		Edge e = new Edge(keyToInteger.get(idVertexIni), keyToInteger.get(idVertexEnd), cost);
		graph.addEdge(e);
	}

	
	public Vertex getInfoVertex(Key idVertex) {
		
		return infoVertex.get(idVertex);
	}

	
	public void setInfoVertex(Key idVertex, Vertex infoV) {
		
		infoVertex.put(idVertex, infoV);
	}

	
	public double getCostArc(Key idVertexIni, Key idVertexEnd) {
		
//		keyToInteger.get(idVertexIni);
//		
//		return graph.;
	}

	
	public void setCostArc(Key idVertexIni, Key idVertexEnd, double cost) {
		
		
	}

	
	public void addVertex(Key idVertex, Vertex infoV) {
		if(V >= graph.V()) {
			//resize, muy ineficiente
			EdgeWeightedGraph g2 = new EdgeWeightedGraph( (int)(V*1.5) );
			for(int n = 0; n < graph.V(); n++) {
				for(Edge e: graph.adj(n)) {
					g2.addEdge(e);
				}
			}
			graph = g2;
		}
		
		infoVertex.put(idVertex, infoV);
		
		if( !keyToInteger.contains(idVertex) ){
			keyToInteger.put(idVertex, V);
			V++;
		}
	}

	
	public Iterable<Key> adj(Key idVertex) {
		
		Iterable<Edge> adjacentes = graph.adj(keyToInteger.get(idVertex));
		Bag<Key> res = new Bag<>();

		for(Edge e: adjacentes) {
			Key id1 = integerToKey.get(e.either());
			if(id1.equals(idVertex)) {
				Key id2 = integerToKey.get(e.other(e.either()));
				res.add(id2);
			}
			else {
				res.add(id1);
			}
			
		}
		
		return res;
	}

	
	public void uncheck() {
		
		
	}

	
	public void dfs(Key idVertex) {
		
		
	}

	private CC calculatedCC;
	
	public int cc() {
		
		calculatedCC = new CC(graph);
		return calculatedCC.count();
	}

	
	public Iterable<Key> getCC(Key idVertex) {
		
		Bag<Key> res = new Bag<>();
		
		int id = calculatedCC.id(keyToInteger.get(idVertex));
		for(int n = 0; n < graph.V(); n++) {
			if(calculatedCC.id(n) == id) {
				res.add(integerToKey.get(n));
			}
		}
		return res;
	}

}
