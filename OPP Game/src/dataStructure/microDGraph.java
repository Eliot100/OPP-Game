package dataStructure;

public class microDGraph {
	private microEdge[] Edges;
	private microNode[] Nodes;
	
	public microNode[] getNodes() {
		return Nodes;
	}
	
	public microEdge[] getEdges() {
		return Edges;
	}
	
	public class microEdge {
		private int src;
		private double w;
		private int dest;
		public int getSrc() {
			return src;
		}
		public int getDest() {
			return dest;
		}
		public double getW() {
			return w;
		}
	}
	
	public class microNode {
		private int id;
		private String pos;
		
		public String getPos() {
			return pos;
		}
		public int getId() {
			return id;
		}
	}
}
