package dataStructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import gui.GUI_Window;
import utils.Point3D;
/**
 * This class is implementation of directed graph.
 * @author Eli Ruvinov
 */
public class DGraph implements graph {
	private int lastId;
	private int MC;
	private HashMap<Integer, node_data> nodeHash;
	private HashMap<Integer, HashMap<Integer, edge_data>> edgeHash;
	private int edgeHashSize;
	
	public boolean equals(Object g) {
		if(g instanceof DGraph) {
			for (Iterator<node_data> iterator = ((DGraph) g).getV().iterator(); iterator.hasNext();) {
				Node node = (Node) iterator.next();
				if (!node.equals((Node) this.getNode(node.getKey())))
					return false;
				for (Iterator<edge_data> iterator2 = ((DGraph) g).getE(node.getKey()).iterator(); iterator2.hasNext();) {
					Edge edge = (Edge) iterator2.next();
					if (!edge.equals((Edge) this.getEdge(edge.getSrc(), edge.getDest())))
						return false;
				}	
			}
			if (((DGraph) g).nodeSize() != this.nodeSize() || ((DGraph) g).edgeSize() != this.edgeSize())
				return false;
			return true;
		}
		return false;
	}
	/**
	 * @return new key.
	 */
	public int newId() {
		lastId++;
		return lastId;
	}
	/**
	 * this is an example how to make the GUI_Window object from DGraph.
	 */
	public static void main(String[] args) throws InterruptedException {
		DGraph d = makeRandomGraph(6, 200);
		new GUI_Window(d);
	}
	/**
	 * @param nodeNum - the number of node in the new DGraph.
	 * @param edgePresent - the present for an edge to by made. 
							bigger then 100 = always.
							smaller then 0 = never.
	 * @return a new DGraph.
	 */
	public static DGraph makeRandomGraph(int nodeNum, int edgePresent) {
		DGraph g = new DGraph();
		for (int i = 0; i < nodeNum; i++) {
			double x = Math.random()*200;
			double y = Math.random()*100;
			g.addNode(new Node(g.newId(), new Point3D(x, y)));
		}
		for (int i = 1; i <= nodeNum; i++) {
			for (int j = 1; j <= nodeNum; j++) {
				if ( i != j) {
					int r = (int) (Math.random()*100);
					if (r <= edgePresent) {
						Point3D ii = g.getNode(i).getLocation();
						Point3D jj = g.getNode(j).getLocation();
						double dist = Math.abs(ii.distance2D(jj));
						g.connect(i, j, dist);
					}
				}
			}
		}
		return g;
	}
	/**
	 *  This is a constructor for a DGraph from a graph.
	 */
	public DGraph(graph g) {
		int biggestID = 0; 
		MC = g.getMC();
		nodeHash = new HashMap<Integer, node_data>();
		edgeHash = new HashMap<Integer, HashMap<Integer, edge_data>>();
		Iterator<node_data> nodeIter = g.getV().iterator(); 
		while (nodeIter.hasNext()) {
			node_data Node =  nodeIter.next();
			nodeHash.put(Node.getKey(), Node);
			edgeHash.put(Node.getKey(), new HashMap<Integer, edge_data>());
			if(Node.getKey() > biggestID)
				biggestID = Node.getKey();
		}
		lastId = biggestID;
		Iterator<node_data> nodeIter2 = g.getV().iterator(); 
		while (nodeIter2.hasNext()) {
			node_data Node =  nodeIter2.next();
			Iterator<edge_data> edgeIter = g.getE(Node.getKey()).iterator(); 
			while (edgeIter.hasNext()) {
				edge_data edge = edgeIter.next();
				this.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
				
			}
		}
		MC = g.getMC();
		edgeHashSize = g.edgeSize();
	}
	
	/**
	 *  This is a constructor for a new empty DGraph (directed graph).
	 */
	public DGraph() {
		lastId = 0;
		MC = 0;
		edgeHashSize = 0;
		nodeHash = new HashMap<Integer, node_data>();
		edgeHash = new HashMap<Integer, HashMap<Integer, edge_data>>();
	}
	/**
	 * This is an explicit constructor for a new DGraph.
	 */
	public DGraph(int lastId, int MC, HashMap<Integer, Node> nodeHash, HashMap<Integer, HashMap<Integer, Edge>> edgeHash, int edgeHashSize) {
		this.lastId = lastId;
		this.MC = MC;
		this.edgeHashSize = edgeHashSize;
		this.nodeHash = new HashMap<Integer, node_data>();
		this.edgeHash = new HashMap<Integer, HashMap<Integer, edge_data>> ();
		for (node_data node : nodeHash.values()) {
			this.nodeHash.put(node.getKey(), node);
			this.edgeHash.put(node.getKey(), new HashMap<Integer, edge_data>());
			edgeHash.get(node.getKey());
			for (edge_data edge : edgeHash.get(node.getKey()).values()) {
				this.edgeHash.get(edge.getSrc()).put(edge.getDest(), edge);
			}
		}
	}
	
	@Override
	public node_data getNode(int key) {
		return nodeHash.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		return edgeHash.get(src).get(dest);
	}

	@Override
	public synchronized void addNode(node_data n) {
		nodeHash.put(n.getKey(), n);
		edgeHash.put(n.getKey(), new HashMap<Integer, edge_data>());
		if(lastId < n.getKey())
			lastId = n.getKey();
		MC++;
	}

	@Override
	public synchronized void connect(int src, int dest, double w) {
		if (nodeHash.get(src) != null && nodeHash.get(dest) != null ) {
			Edge e = new Edge(src, dest, w);
			edgeHash.get(src).put(dest, e);
		}
		MC++;
		edgeHashSize++;
	}

	@Override
	public Collection<node_data> getV() {
		return nodeHash.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return edgeHash.get(node_id).values();
	}

	@Override
	public synchronized node_data removeNode(int key) {
		MC++;
		edgeHashSize -= edgeHash.get(key).size();
		edgeHash.remove(key);
		Iterator<HashMap<Integer, edge_data>> itr = edgeHash.values().iterator();
		while (itr.hasNext()) {
			HashMap<Integer, edge_data> hashMap = itr.next();
			hashMap.remove(key);
			edge_data edge = hashMap.remove(key);
			if (edge != null)
				edgeHashSize--;
		}
		return nodeHash.remove(key);
	}

	@Override
	public synchronized edge_data removeEdge(int src, int dest) {
		MC++; 
		edge_data edge = edgeHash.get(src).remove(dest);
		if (edge != null)
			edgeHashSize--;
		return edge;
	}

	@Override
	public int nodeSize() {
		return nodeHash.size();
	}

	@Override
	public int edgeSize() {
		return edgeHashSize;
	}

	@Override
	public int getMC() {
		return MC;
	}

}
