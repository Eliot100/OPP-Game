package dataStructure;


public class microDGraph {
	private microEdge[] Edges;
	private microNode[] Nodes;
	//		public microDGraph(microEdge[] Edges, microNode[] Nodes) {
	//			this.Edges = Edges;
	//			this.Nodes = Nodes;
	//		}
	public microNode[] getNodes() {
		return Nodes;
	}
	public void setNodes(microNode[] nodes) {
		Nodes = nodes;
	}
	public microEdge[] getEdges() {
		return Edges;
	}
	public void setEdges(microEdge[] edges) {
		Edges = edges;
	}
}
