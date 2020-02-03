package algorithms;

import java.util.ArrayList;
import java.util.Iterator;

import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.DGraph;
import dataStructure.FruitsType;
import dataStructure.node_data;
/**
 * This class is the algorithms you can do on the arena_data of the game. 
 * @author Eli Ruvinov
 */
public class Arena_Algo {
	private static final double eps = 0.00001;
	/**
	 * @param arena - arena_data
	 * @param f - fruit_data
	 * @return the edge_data which f is on
	 */
	public static edge_data getFruitEdge(DGraph g, fruit_data f) {
		ArrayList<edge_data> TheEdge = new ArrayList<edge_data>();
		edge_data fruitEdge = null;
		try { 
			for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
				node_data node = iterator.next();
				for (Iterator<edge_data> iterator2 = g.getE(node.getKey()).iterator(); iterator2.hasNext();) {
					edge_data edge = iterator2.next();
					if ( eps >= Math.abs( node.getLocation().distance2D(g.getNode(edge.getDest()).getLocation()) - 
							(node.getLocation().distance2D(f.getPos()) + f.getPos().distance2D(g.getNode(edge.getDest()).getLocation())))){

						if (f.getType() == FruitsType.banana && edge.getSrc() > edge.getDest()) {
							TheEdge.add(edge);
						} else if (f.getType() == FruitsType.apple && edge.getSrc() < edge.getDest()) {
							TheEdge.add(edge);
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		
		int r = (int)(1+(Math.random()*(TheEdge.size()-1)));
		Iterator<edge_data> itr = TheEdge.iterator();
		for(int i=0; i<r ;i++) 
			fruitEdge = itr.next();
//		while(itr.hasNext())
		return fruitEdge;
	}
	
}