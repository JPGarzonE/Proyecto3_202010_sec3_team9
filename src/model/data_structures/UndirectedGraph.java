package model.data_structures;

import java.util.NoSuchElementException;

public class UndirectedGraph<Key extends NearComparable<Key>, Vertex> implements IUndirectedGraph<Key, Vertex>{

	private EdgeWeightedGraph graph;
	
	private IRedBlackBST<Key, Integer> keyToIndex;
	private LinearProbingHash<Integer, Key> indexToKey;

	private int V;
	private LinearProbingHash<Key, Vertex> infoVertex;
	
	public UndirectedGraph( int v ){
		graph = new EdgeWeightedGraph(v);
		keyToIndex = new RedBlackBST<Key, Integer>();
		indexToKey = new LinearProbingHash<Integer, Key>(v);
		infoVertex =  new LinearProbingHash<Key, Vertex>(v);
		
		V = 0;
	}
	
	public int V() {
		
		return V;
	}

	
	public int E() {
		
		return graph.E();
	}

	
	public void addEdge(Key vertexIniKey, Key vertexEndKey, double haversianCost, double totalFeaturesCost) {
		if(!keyToIndex.contains(vertexIniKey)){
			keyToIndex.put(vertexIniKey, V);
			indexToKey.put(V, vertexIniKey);
			V++;
		}
		
		if(!keyToIndex.contains(vertexEndKey)){
			keyToIndex.put(vertexEndKey, V);
			indexToKey.put(V, vertexEndKey);
			V++;
		}
		
		Edge e = new Edge(keyToIndex.get(vertexIniKey), keyToIndex.get(vertexEndKey), haversianCost, totalFeaturesCost);
		graph.addEdge(e);
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

		if( !keyToIndex.contains(idVertex) ){
			keyToIndex.put(idVertex, V);
			indexToKey.put(V, idVertex);
			V++;
		}
	}

	public Key getKeyByIdx( int idx ){
		return indexToKey.get(idx);
	}
	
	public Vertex getInfoVertex(Key idVertex) {
		
		return infoVertex.get(idVertex);
	}
	
	public EdgeWeightedGraph graph(){
		return graph;
	}

	public Vertex getInfoVertexByIdx(int idVertex){
		Key vertexKey = indexToKey.get(idVertex);
		return infoVertex.get(vertexKey);
	}
	
	public void setInfoVertex(Key idVertex, Vertex infoV) {
		
		infoVertex.put(idVertex, infoV);
	}

	
	public double getHaversianCostArc(Key idVertexIni, Key idVertexEnd) throws NoSuchElementException{
		return getEdge(idVertexIni, idVertexEnd).weight1();
	}
	
	public double getTotalFeaturesCostArc(Key idVertexIni, Key idVertexEnd) throws NoSuchElementException{
		return getEdge(idVertexIni, idVertexEnd).weight2();
	}
	
	public void setHaversianCostArc(Key idVertexIni, Key idVertexEnd, double cost)  throws NoSuchElementException{
		getEdge(idVertexIni, idVertexEnd).setWeight1(cost);
	}
	
	public void setTotalFeaturesCostArc(Key idVertexIni, Key idVertexEnd, double cost) throws NoSuchElementException{
		getEdge(idVertexIni, idVertexEnd).setWeight2(cost);;
	}
	
	public Edge getEdge(Key idVertexIni, Key idVertexEnd) throws NoSuchElementException{
		Integer keyIni = keyToIndex.get(idVertexIni);
		Integer keyEnd = keyToIndex.get(idVertexEnd);
		
		if( keyIni == null )
			throw new NoSuchElementException("idVertexIni vertex is not in the graph");
		else if( keyEnd == null )
			throw new NoSuchElementException("idVertexEnd vertex is not in the graph");
		
		Bag<Edge> edges = getVertexEdgesByIdx(keyIni);
		
		for( Edge e : edges ){
			if( e.either() == keyEnd || e.other(keyIni) == keyEnd)
				return e;
		}
		
		throw new NoSuchElementException("No exist an arc between idVertexIni and idVertexEnd");
	}
	
	public Bag<Edge> getVertexEdgesByIdx( int idVertex ){
		return graph.getVertexEdges(idVertex);
	}

	public int getNearestVertexIdx( Key idVertex ){
		int idx = keyToIndex.getNearestTo(idVertex);
		return idx;
	}
	
	public void increaseWeight2ByVertexIdx(int vertexIdx, double increaseVal){
		Bag<Edge> edges = getVertexEdgesByIdx(vertexIdx);
		
		for( Edge e : edges )
			e.setWeight2( e.weight2() + increaseVal );
	}
	
	public Iterable<Key> adj(Key idVertex) {
		
		Iterable<Edge> adjacentes = graph.adj(keyToIndex.get(idVertex));
		Bag<Key> res = new Bag<Key>();

		for(Edge e: adjacentes) {
			Key id1 = indexToKey.get(e.either());
			if(id1.equals(idVertex)) {
				Key id2 = indexToKey.get(e.other(e.either()));
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
	
	private void bfs(Key idVertex){
		
	}

	private CC calculatedCC;
	
	public int cc() {
		
		calculatedCC = new CC(graph);
		return calculatedCC.count();
	}

	
	public Iterable<Key> getCC(Key idVertex) {
		
		Bag<Key> res = new Bag<Key>();
		
		int id = calculatedCC.id(keyToIndex.get(idVertex));
		for(int n = 0; n < graph.V(); n++) {
			if(calculatedCC.id(n) == id) {
				res.add(indexToKey.get(n));
			}
		}
		return res;
	}

}
