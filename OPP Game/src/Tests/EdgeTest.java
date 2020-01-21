package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dataStructure.Edge;


class EdgeTest {

	@Test
	void testEdge() {
		Edge[] arrEdge = {new Edge(0, 2, 0.5), new Edge(-4, 3, 6.5), new Edge(3, 0, 4),
				new Edge(15, 15, 15), new Edge(2, 3, 1.111111), new Edge(-12, 0, 7.7)};
		int[] srcArr = { 0, -4, 3, 15, 2, -12};
		int[] destArr = { 2, 3, 0, 15, 3, 0};
		double[] weightArr = { 0.5, 6.5, 4, 15, 1.111111, 7.7};
		for (int i = 0; i < arrEdge.length; i++) {
			assertEquals(arrEdge[i].getSrc(), srcArr[i]);
			assertEquals(arrEdge[i].getDest(), destArr[i]);
			assertEquals(arrEdge[i].getWeight(), weightArr[i]);
			assertNotEquals(arrEdge[(i+1)%arrEdge.length].getSrc(), arrEdge[i].getSrc());
			assertNotEquals(arrEdge[(i+1)%arrEdge.length].getDest(), arrEdge[i].getDest());
			assertNotEquals(arrEdge[(i+1)%arrEdge.length].getWeight(), arrEdge[i].getWeight());
		}
	}

	@Test
	void testGetSrc() {
		Edge[] arrEdge = {new Edge(2, 2, 0.5), new Edge(5, 3, 6.5), new Edge(12, 0, 4),
				new Edge(4, 15, 15), new Edge(67, 3, 1.111111), new Edge(-22, 0, 7.7)};
		int[] srcArr = { 2, 5, 12, 4, 67, -22};
		int[] notSrcArr = { 0, -4, 3, 15, 2, -12};
		for (int i = 0; i < arrEdge.length; i++) {
			assertEquals(arrEdge[i].getSrc(), srcArr[i]);
			assertNotEquals(arrEdge[i].getSrc(), notSrcArr[i]);
		}
	}

	@Test
	void testGetDest() {
		Edge[] arrEdge = {new Edge(2, 55, 0.5), new Edge(5, -49, 6.5), new Edge(12, 11, 4),
				new Edge(4, -9, 15), new Edge(67, 7, 1.111111), new Edge(-22, -11, 7.7)};
		int[] destArr = { 55, -49, 11, -9, 7, -11};
		int[] notDestArr = { 2, 5, 12, 4, 67, -22};
		for (int i = 0; i < arrEdge.length; i++) {
			assertEquals(arrEdge[i].getDest(), destArr[i]);
			assertNotEquals(arrEdge[i].getDest(), notDestArr[i]);
		}
	}

	@Test
	void testGetWeight() {
		Edge[] arrEdge = {new Edge(2, 55, 12.5), new Edge(5, -49, 30.33333), new Edge(12, 11, 1.4),
				new Edge(4, -9, 7.5), new Edge(67, 7, 200), new Edge(-22, -11, 10)};
		double[] weightArr = { 12.5, 30.33333, 1.4, 7.5, 200, 10};
		double[] notWeightArr = { 0.5, 6.5,  4, 15, 1.111111, 7.7};
		for (int i = 0; i < arrEdge.length; i++) {
			assertEquals(arrEdge[i].getWeight(), weightArr[i]);
			assertNotEquals(arrEdge[i].getWeight(), notWeightArr[i]);
		}
	}

	@Test
	void testGetInfo() {
		Edge e = new Edge(0, 5, 2.5);
		String[] nonsens = {"hhkiy", "htdfy", "khj", "hrsth", "trsj", "yrdjy",
							"tdj", "ytdo", "kytf", "htTWW", "kul", "kkmvolkaf"};
		for (int i = 0; i < 12; i++) {
			String s = "i = "+i;
			e.setInfo(s);
			assertEquals(e.getInfo(), s);
			assertNotEquals(e.getInfo(), nonsens[i]);
		}
	}

	@Test
	void testSetInfo() {
		Edge e = new Edge(1, 15, 7);
		String[] nonsens = {"-hhk1iy", "&htdfy", "kh4j", "hrsth", "tr,sj", "yr=djy",
							"tdj007", "ytdo", "k5ytf", "ht=TWW", "kul", "+kmvolkaf"};
		for (int i = 0; i < 12; i++) {
			String s = "i = "+i;
			e.setInfo(s);
			assertEquals(e.getInfo(), s);
			assertNotEquals(e.getInfo(), nonsens[i]);
		}
	}

	@Test
	void testGetTag() {
		Edge e = new Edge(1, 15, 7);
		int[] notTag = { -2 , 5, 3, 10, 1300, 1, 41, 4345, 4, 89};
		for (int i = 0; i < 10; i++) {
			int t = i;
			e.setTag(t);
			assertEquals(e.getTag(), i);
			assertNotEquals(e.getTag(), notTag[i]);
		}
	}

	@Test
	void testSetTag() {
		Edge e = new Edge(1, 15, 7);
		int[] notTag = { -22 , 54, 35, 11, -3, -1, 0, 91, 2, 3};
		for (int i = 0; i < 10; i++) {
			int t = i;
			e.setTag(t);
			assertEquals(e.getTag(), i);
			assertNotEquals(e.getTag(), notTag[i]-100);
		}
	}

}
