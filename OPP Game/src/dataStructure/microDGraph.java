package dataStructure;

import utils.Point3D;

/**
 * This class is a game DGraph. 
 * @author Eli Ruvinov
 */
public class microDGraph {
	private microEdge[] Edges;
	private microNode[] Nodes;
	/**
	 * @return the Nodes of the microDGraph
	 */
	public microNode[] getNodes() {
		return Nodes;
	}
	/**
	 * @return the Edges of the microDGraph
	 */
	public microEdge[] getEdges() {
		return Edges;
	}
	/**
	 * This class is a game Edge.
	 */
	public class microEdge {
		private int src;
		private double w;
		private int dest;
		/**
		 * @return the microEdge source
		 */
		public int getSrc() {
			return src;
		}
		/**
		 * @return the microEdge destination
		 */
		public int getDest() {
			return dest;
		}
		/**
		 * @return the microEdge weight
		 */
		public double getW() {
			return w;
		}
	}
	/**
	 * This class is a game Node.
	 */
	public class microNode {
		private int id;
		private String pos;
		/**
		 * @return the microNode position (Point3D)
		 */
		public Point3D getPos() {
			String[] xyz = pos.split(",");
			Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
			return p;
		}
		/**
		 * @return the microNode id
		 */
		public int getId() {
			return id;
		}
	}
}
