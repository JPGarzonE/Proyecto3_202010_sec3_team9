package model.data_structures;

public interface IUndirectedGraph<Key, Vertex> {

	public int V();
	
	public int E();
	
	public void addEdge(Key idVertexIni, Key idVertexEnd, double cost);
	
	public Vertex getInfoVertex(Key idVertex);
	
	public void setInfoVertex(Key idVertex, Vertex infoVertex);
	
	public double getCostArc(Key idVertexIni, Key idVertexEnd);
	
	public void setCostArc(Key idVertexIni, Key idVertexEnd, double cost);
	
	public void addVertex(Key idVertex, Vertex infoVertex);
	
	public Iterable<Key> adj(Key idVertex);
	
	public void uncheck();
	
	public void dfs(Key idVertex);
	
	public int cc();
	
	public Iterable<Key> getCC(Key idVertex);
}
