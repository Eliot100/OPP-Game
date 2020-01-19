package algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
/**
 * This class represents the set of graph-theory algorithms.
 * @author Eli Ruvinov
 */
public class Graph_Algo implements graph_algorithms{
	public DGraph graph;
	
	public Graph_Algo() {}
	public Graph_Algo(graph graph) {
		if(graph instanceof DGraph) {
			this.init(graph);
		} else {
			throw new RuntimeException("The graph must be an instanceof DGraph!");
		}
	}

	public static void main(String[] arr) {
		Graph_Algo ga = new Graph_Algo();
		Graph_Algo ga2 = new Graph_Algo();
		Graph_Algo ga3 = new Graph_Algo();
		DGraph d = new DGraph();
		int action = 11;
		if(action == 1) {
			for (int i = 0; i < 1000; i++) {
				if(i%2 == 1)
					d.addNode(new Node(d.newId(), new Point3D(0, i)));
				else
					d.addNode(new Node(d.newId(), new Point3D(i, 0)));
			}
			for (int i = 1; i < 1000; i++) {
				d.connect(i, i+1, 1);
			}
		} else {
			for (int i = 1; i < 100; i++) {
				d.addNode(new Node( i, new Point3D(i, i)));
			}
			for (int i = 1; i < 100; i++) {
				d.connect(i, i+1,(double) i/5);
			}
		}
		ga.init(d);
		ga.save("2");
		ga2.init("2");
		if(ga2.isConnected()) {
			System.out.println("rong");
		}
		else 
			System.out.println("isConnected = false");
		ga2.graph.connect(99, 1, 45);
		if(ga2.isConnected()) {
			System.out.println("isConnected = true");
		}
		else 
			System.out.println("rong");
		double x = ga2.shortestPathDist(1, 6);
		System.out.println("shortestPathDist(1, 6) = "+x);
		ga3.graph = (DGraph) ga2.copy();
		if(ga3.isConnected()) {
			System.out.println("isConnected = true");
		}
		else 
			System.out.println("rong");
		double x2 = ga3.shortestPathDist(1, 10);
		System.out.println("shortestPathDist(1, 10) = "+x2);
	}
	
	@Override
	public void init(graph g) {
		if (g instanceof DGraph) {
			graph = (DGraph) g;
		} else {
			throw new RuntimeException("The graph must be DGrap.");
		}
	}

	@Override
	public void init(String file_name) {
		try {
			File DGraph_JASON = new File(file_name+".txt");
			BufferedReader br = new BufferedReader(new FileReader(DGraph_JASON));
			String st0 = "";
			String st;
			while((st = br.readLine()) != null) {
				st0 += st;
			}
			br.close();
			Gson gson = new Gson();
			DG_params params = gson.fromJson(st0, DG_params.class);
			graph = new DGraph(params.lastId, params.MC, params.nodeHash, params.edgeHash, params.edgeHashSize);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private class DG_params {
		private int lastId;
		private int MC;
		private HashMap<Integer, Node> nodeHash;
		private HashMap<Integer, HashMap<Integer, Edge>> edgeHash;
		private int edgeHashSize;
	}

	@Override
	public void save(String file_name) {
		try {
			File DGraph_JASON = new File(file_name+".txt");
			PrintWriter pw = new PrintWriter(new FileWriter(DGraph_JASON));
			pw.println(new Gson().toJson(graph));
			pw.close();
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean isConnected() {
		if( graph.nodeSize() < 2)
			return true;
		Iterator<node_data> itr = graph.getV().iterator();
		node_data n = itr.next();
		this.achivedAll(n);
		while (itr.hasNext()) {
			node_data temp = itr.next();
			if (temp.getInfo().equals("Through")) {
				continue;
			}
			if (!temp.getInfo().equals("Achieved")) {
				return false;
			}
			List<node_data> shortestPath  = this.shortestPath(temp.getKey(), n.getKey()) ;
			if (shortestPath == null) {
//				System.out.println("shortestPath("+temp.getKey()+", "+n.getKey()+" ) == null");
				return false;
			}
			else {
				for (Iterator<node_data> iterator = shortestPath.iterator(); iterator.hasNext();) {
					node_data node_data = (node_data) iterator.next();
					node_data.setInfo("Through");
				}
			}
		}
		return true;
	}
	/**
	 * This function is sets all the nodes, that can by reached from n, infos to be "Achieved".
	 * @param n - is the start node_data. 
	 */
	private void achivedAll(node_data n) {
		for (edge_data e : graph.getE(n.getKey())) {
			if(!graph.getNode(e.getDest()).getInfo().equals("Achieved")) {
				graph.getNode(e.getDest()).setInfo("Achieved");
//				System.out.println(e+"\nNode dest info : "+graph.getNode(e.getDest()).getInfo());
				this.achivedAll(graph.getNode(e.getDest()));
			}
		}
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		LinkedList<node_data> path = (LinkedList<node_data>) this.shortestPath(src, dest);
		if (path == null) {
			throw new RuntimeException("It isn't posibol to Reache dest node from src node");
		}
		Iterator<node_data> iterator = path.iterator();
		node_data lastNode_data; 
		if(iterator.hasNext()) {
			lastNode_data = (node_data) iterator.next();
		}
		else
			return 0;
		double sum = 0;
		while ( iterator.hasNext() ) {
			node_data node_data = (node_data) iterator.next();
			sum += graph.getEdge(lastNode_data.getKey(), node_data.getKey()).getWeight();
			lastNode_data =node_data;
		}
		return sum;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		node_data source = graph.getNode(src);
		node_data destanation = graph.getNode(dest);
		if(source.equals(destanation))
			return new LinkedList<node_data>();
		if(source == null || destanation == null) {
			return null;
		}
		HashMap<Integer, LinkedList<node_data>> pathes = new HashMap<Integer, LinkedList<node_data>>();
		ArrayList<Integer> onEdge = new ArrayList<Integer>();
		LinkedList<edge_data> walkenEdges = new LinkedList<edge_data>();
		onEdge.add(source.getKey());
		pathes.put(source.getKey(), new LinkedList<node_data>());
		pathes.get(source.getKey()).add(source);
		
		for (Iterator<node_data> iterator = graph.getV().iterator(); iterator.hasNext();) {
			node_data node = iterator.next();
			node.setWeight(0);
			node.setTag(graph.getE(node.getKey()).size());
		}
		
		while (!onEdge.isEmpty()) {
			edge_data minWeight = null;
			boolean first = true;
			for (Integer key : onEdge) {
				for (edge_data edge : graph.getE(key)) {
					if (walkenEdges.contains(edge) || onEdge.contains(edge.getDest()) ) {
						continue;
					} else if (first) {
						minWeight = edge;
						first = false;
//						System.out.println(minWeight);
					} else if (edge.getWeight() + graph.getNode(edge.getSrc()).getWeight() < minWeight.getWeight() + graph.getNode(minWeight.getSrc()).getWeight() ) {
							minWeight = edge;
//							System.out.println(minWeight);
					}
				}
			}
//			System.out.println();
			if (minWeight == null) {
				return null;
			}
			walkenEdges.add(minWeight);
			graph.getNode(minWeight.getDest()).setWeight(minWeight.getWeight() + graph.getNode(minWeight.getSrc()).getWeight());
			onEdge.add(minWeight.getDest());
			pathes.put(minWeight.getDest(), copyAndAdd(pathes.get(minWeight.getSrc()), graph.getNode(minWeight.getDest()) ));
			if(minWeight.getDest() == dest) {
				return pathes.get(minWeight.getDest());
			}
			graph.getNode(minWeight.getSrc()).setTag(graph.getNode(minWeight.getSrc()).getTag()-1);		}
		return pathes.get(destanation.getKey());
		
	}
	/** TODO
	 * @param path 
	 * @param n
	 * @return
	 */
	private static LinkedList<node_data> copyAndAdd(LinkedList<node_data> path, node_data n){
		LinkedList<node_data> newPath = new LinkedList<node_data>();
		for (Iterator<node_data> iter = path.iterator(); iter.hasNext();) {
			newPath.add(iter.next()); 
		}
		newPath.add(n); 
		return newPath;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		HashMap<Integer, node_data> targetNodes = new HashMap<Integer, node_data>();
		for (Integer key : targets) {
			if (graph.getNode(key) == null ) {
				return null;
			} else {
				node_data node = graph.getNode(key);
				node.setInfo("");
				node.setTag(0);
				targetNodes.put(key, node);
			}
		}
		
		if(targetNodes.size() < 2) {
			LinkedList<node_data> TSP = new LinkedList<node_data>();
			for (Integer key : targets) {
				TSP.add( graph.getNode(key));
			}
			return TSP;
		}
		
		LinkedList<node_data> TSP = getTSPbyHead(targetNodes); 
		return TSP;
	}
	/**
	 * TODO
	 * @param targetNodes
	 * @return
	 */
	private LinkedList<node_data> getTSPbyHead(HashMap<Integer, node_data> targetNodes) {
		Iterator<Integer> itr = targetNodes.keySet().iterator();
		int headKey = itr.next();
		HashMap<Integer, node_data> reched = new HashMap<Integer, node_data>();
		LinkedList<node_data> TSP = new LinkedList<node_data>();
		node_data headNode = graph.getNode(headKey);
		headNode.setWeight(0); 
		reched.put(headKey, headNode);
		TSP.add(headNode);
		int miningNodesNum = 1;
		HashMap<Integer, LinkedList<node_data>> pathes = new HashMap<Integer, LinkedList<node_data>>();
		LinkedList<node_data> path = new LinkedList<node_data>();
		path.add(headNode);
		pathes.put(headKey, path);
		ArrayList<node_data> onEdge = new ArrayList<node_data>();
		onEdge.add(headNode);
		LinkedList<edge_data> walkenEdges = new LinkedList<edge_data>();
		while (!onEdge.isEmpty()) {
			edge_data minWeight = null;
			boolean first = true;
			for (node_data node_data : onEdge) {
				for (edge_data edge : graph.getE(node_data.getKey())) {
					if (walkenEdges.contains(edge)) {
						continue;
					} else if (first) {
						minWeight = edge;
						first = false;
					} else if (edge.getWeight() + graph.getNode(edge.getSrc()).getWeight() < minWeight.getWeight() + graph.getNode(minWeight.getSrc()).getWeight() ) {
							minWeight = edge;
					}
				}
			}
			if (minWeight == null ) {
				return null;
			}
			walkenEdges.add(minWeight);
			node_data tempDest = graph.getNode(minWeight.getDest());
			tempDest.setWeight(minWeight.getWeight() + graph.getNode(minWeight.getSrc()).getWeight());
			if(targetNodes.containsKey(tempDest.getKey()) && (!reched.containsKey(tempDest.getKey()))) {
					reched.put(minWeight.getDest(), tempDest);
					LinkedList<node_data> newPath = copyAndAdd(pathes.get(minWeight.getSrc()), tempDest);
					pathes.clear();
					pathes.put(tempDest.getKey(), newPath);
					TSP = newPath;
					miningNodesNum++;
					onEdge.clear();
					onEdge.add(tempDest);
					walkenEdges.clear();
					if (targetNodes.size() == miningNodesNum) {
						return TSP;
					}
			} else {
				pathes.put(minWeight.getDest(), copyAndAdd(pathes.get(minWeight.getSrc()), tempDest));
			}
		}
		return null;
	}

	@Override
	public graph copy() {
		return new DGraph(graph);
	}

}
