package dataStructure;


/**
 * This class represent a directed path 
 * between 2 Nodes in the directed graph.
 * @author Eli Ruvinov
 */
public class Edge implements edge_data {
	private int source;
	private int destination;
	private double weight;
	private String info;
	private int tag;
	
	/**
	 * This is a constructor for an Edge.
	 */
	public Edge(int src, int dest, double w ) {//Edge weight must be positive
		if (w <= 0) {
			System.out.println("Edge weight must be positive.");
			return;
		}
		source = src;
		destination = dest;
		weight = w;
		info = "";
		tag = 0;
	}
	/**
	 * 
	 * @return
	 */
	public String toString() {
		return ("edge("+source+", "+destination+", "+weight+")"); 
	}
	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean equals(Object e) {
		if(!(e instanceof Edge))
			return false;
		if(((Edge) e).destination != destination)
			return false;
		if(!((Edge) e).info.equals(info))
			return false;
		if(((Edge) e).source != source)
			return false;
		if(((Edge) e).tag != tag)
			return false;
		if(((Edge) e).weight != weight)
			return false;
		return true;
	}
	
	@Override
	public int getSrc() {
		return source;
	}

	@Override
	public int getDest() {
		return destination;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}

}
