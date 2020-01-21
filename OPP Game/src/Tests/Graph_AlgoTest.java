package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import utils.Point3D;

class Graph_AlgoTest {

	@Test
	void testInitGraph() {
		System.out.println("***testInitGraph***");
		Graph_Algo ga = new Graph_Algo();
		DGraph d = DGraph.makeRandomGraph(5, 100);
		ga.init(d);
		assertEquals(ga.graph, d);
	}

	@Test
	void testInitString_testSave() {
		Graph_Algo ga = new Graph_Algo();
		DGraph d = DGraph.makeRandomGraph(5, 100);
		ga.init(d);
		String file_name = "newDGraph"; 
		ga.save(file_name);
		Graph_Algo ga2 = new Graph_Algo();
		ga2.init(file_name);
		assertEquals(ga.graph, ga2.graph);
	}

	@Test
	void testIsConnected() {
		Graph_Algo ga = new Graph_Algo();
		DGraph d = DGraph.makeRandomGraph(5, 100);
		ga.init(d);
		assertEquals(ga.isConnected(), true);
		d.addNode(new Node(d.newId(), new Point3D(18, 24)));
		assertNotEquals(ga.isConnected(), true);
	}

	@Test
	void testShortestPathDist() {
		Graph_Algo ga = new Graph_Algo();
		ga.init("butyGraph");
		assertEquals(ga.shortestPathDist(1, 5), ga.shortestPathDist(5, 1));
		ga.graph.removeEdge(1, 5);
		assertNotEquals(ga.shortestPathDist(1, 5), ga.shortestPathDist(5, 1));
	}

	@Test
	void testShortestPath() {
		Graph_Algo ga = new Graph_Algo();
		ga.init("butyGraph");
		List<Node> path = new LinkedList<Node>();
		path.add((Node) ga.graph.getNode(1));
		path.add((Node) ga.graph.getNode(5));
		assertEquals(ga.shortestPath(1, 5), path);
		ga.graph.removeEdge(1, 5);
		assertNotEquals(ga.shortestPath(1, 5), ga.shortestPath(5, 1));
	}

	@Test
	void testTSP() {
		Graph_Algo ga = new Graph_Algo();
		ga.init("butyGraph");
		List<Integer> targets = new LinkedList<Integer>();
		for (int i = 1; i < 5; i++) {
			targets.add(i);
		}
		List<Node> TSP = new LinkedList<Node>();
		TSP.add((Node) ga.graph.getNode(1));
		TSP.add((Node) ga.graph.getNode(4));
		TSP.add((Node) ga.graph.getNode(2));
		TSP.add((Node) ga.graph.getNode(3));
		assertEquals(ga.TSP(targets ), TSP);
	}

	@Test
	void testCopy() {
		Graph_Algo ga = new Graph_Algo();
		DGraph d = DGraph.makeRandomGraph(5, 100);
		ga.init(d);
		DGraph g = (DGraph) ga.copy();
		assertEquals(d, g);
	}

}
