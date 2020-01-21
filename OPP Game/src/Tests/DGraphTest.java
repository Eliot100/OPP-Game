package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

class DGraphTest {

	@Test
	void testDGraphInLessThen10Sec() {
		System.out.println("***testDGraphInLessThen10Sec***");
		long first = ZonedDateTime.now().toInstant().toEpochMilli();
		DGraph g = new DGraph();
		for (int i = 1; i < 1000000 ; i++) {
			g.addNode(new Node(g.newId(), new Point3D(i, i)));
			if (i >= 10) {
				for (int j = 0; j < 10; j++) {
					g.connect(i, j, i+j);
				}
			}
		}
		for (int i = 1; i < 11; i++) {
			g.connect(i, i+10, 10*Math.sqrt(2));
		}
		long last = ZonedDateTime.now().toInstant().toEpochMilli();
		System.out.println("Time in secends : "+((double)(last - first)/1000));
		if ((last - first)/1000 > 10 ) {
			fail("Should have happened in les then 10 secends.");
		}
		//run in 2.48 seconds  
	}

	@Test
	void testEqualsObject() {
		System.out.println("***testEqualsObject***");
		DGraph d = new DGraph();
		DGraph g = new DGraph(d);
		assertEquals(d , g);
	}
	
	@Test
	void testDGraphGraph() {
		System.out.println("***testDGraphGraph***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10 ; i++) {
			g.addNode(new Node(i, new Point3D(i, i)));
		}
		for (int i = 0; i < 10; i++) {
			g.connect(i, (i+1)%10, 1);
		}
		DGraph d = new DGraph(g);
		System.out.println("g.edgeSize = "+g.edgeSize());
		System.out.println("d.edgeSize = "+d.edgeSize());
		assertEquals(d.edgeSize() , g.edgeSize());
		System.out.println("g.nodeSize = "+g.nodeSize());
		System.out.println("d.nodeSize = "+d.nodeSize());
		assertEquals(d.edgeSize() , g.edgeSize());
		for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
			node_data node = iterator.next();
			assertEquals(node, d.getNode(node.getKey()));
			for (edge_data edge : g.getE(node.getKey())) {
				assertEquals(edge, d.getEdge(edge.getSrc(), edge.getDest()));
			}
		}
	}

	@Test
	void testGetNodeAndAddNode() {
//		System.out.println("***testGetNodeAndAddNode***");
		DGraph g = new DGraph();
		for (int i = 0; i < 100; i+=5) {
			Node n = new Node(i, new Point3D(i, i+5));
			g.addNode(n);
			assertEquals(g.getNode(i), n);
			assertNotEquals(g.getNode(i), new Node(i, new Point3D(i, i)));
		}
		assertEquals(g.getV().size(), 20);
	}

	@Test
	void testGetEdge() {
//		System.out.println("***testGetEdge***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10 ; i++) {
			g.addNode(new Node(i, new Point3D(i, i)));
		}
		for (int i = 0; i < 10; i++) {
			g.connect(i, (i+1)%10, 1);
			assertEquals(g.getEdge(i, (i+1)%10), new Edge(i, (i+1)%10, 1));
			assertNotEquals(g.getEdge(i, (i+2)%10), new Edge(i, (i+1)%10, 1));
		}
	}

	@Test
	void testConnect() {
//		System.out.println("***testConnect***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10 ; i++) {
			g.addNode(new Node(i, new Point3D(i, i)));
		}
		for (int i = 0; i < 10; i++) {
			g.connect(i, (i+1)%10, 1);
		}
		assertEquals(g.getEdge(0, 1) ,new Edge(0, 1, 1));
		assertNotEquals(g.getEdge(0, 2) , new Edge(0, 1, 1));
	}

	@Test
	void testGetV() {
//		System.out.println("***testGetV***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10; i++) {
			g.addNode(new Node(i, new Point3D(i, i)));
		}
		assertEquals(g.getV().size(), 10);
		for (node_data n : g.getV()) {
//			System.out.println(n.getKey()+"  "+n.getLocation());
			assertEquals(n, new Node(n.getKey(), new Point3D(n.getKey(), n.getKey())));
		}
	}
	
	@Test
	void testRemoveNodeAndEdgeAndTestGetE() {
		System.out.println("***testRemoveNodeAndEdgeAndTestGetE***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10; i++) {
			g.addNode(new Node(g.newId(), new Point3D(i, i)));
		}
		int size1 = g.nodeSize();
		System.out.println("myDGraph number of nodes = "+g.nodeSize()+" (before remove)");
		g.removeNode(1);
		int size2 = g.nodeSize();
		System.out.println("myDGraph number of nodes = "+g.nodeSize()+" (after remove)");
		assertNotEquals(size1, size2);
		assertEquals(size1, 10);
		if (g.getNode(1) != null)
			fail("The node with index 1 dosn't been removed.");
		for (int i = 2; i < 10; i++) {
			g.connect(i, i+1, 1.5);
		}
		int size3 = g.edgeSize();
		System.out.println("myDGraph number of edges = "+g.edgeSize()+" (before remove)");
		edge_data i = g.removeEdge(2, 3);
		edge_data j = g.getEdge(2, 3);
		assertNotEquals(i, j);
		assertEquals(j, null);
		int size4 = g.edgeSize();
		System.out.println("myDGraph number of edges = "+g.edgeSize()+" (after remove)");
		assertNotEquals(size3, size4);
		assertEquals(size4, 7);
		System.out.println("g.getE(4).size = "+g.getE(4).size());
		assertEquals(g.getE(4).size(), 1);
		System.out.println("g.getE(5).size = "+g.getE(5).size());
		assertNotEquals(g.getE(5).size(), 2);
		
	}

	@Test
	void testNodeSizeAndEdgeSize() {
		System.out.println("***testNodeSizeAndEdgeSize***");
		DGraph g = new DGraph();
		for (int i = 0; i < 10 ; i++) {
			g.addNode(new Node(i, new Point3D(i, i)));
		}
		for (int i = 0; i < 10; i++) {
			g.connect(i, (i+1)%10, 1);
		}
		
		System.out.println("g.edgeSize = "+g.edgeSize());
		DGraph d = new DGraph(g);
		System.out.println("d.edgeSize = "+d.edgeSize());
		assertEquals(d.edgeSize() , g.edgeSize());
		d.removeEdge(1, 2);
		System.out.println("g.edgeSize = "+g.edgeSize());
		System.out.println("d.edgeSize = "+d.edgeSize());
		assertNotEquals(d.edgeSize() , g.edgeSize());
	}

	@Test
	void testGetMC() {
//		System.out.println("***testGetMC***");
		DGraph g = new DGraph();
		int MC0 = g.getMC();
		g.addNode(new Node(0, new Point3D(0, 0)));
		int MC1 = g.getMC();
		g.addNode(new Node(1, new Point3D(1, 1)));
		int MC2 = g.getMC();
		int MC3 = g.getMC();
		assertNotEquals(MC0,MC1);
		assertEquals(MC2,MC3);
	}

}
