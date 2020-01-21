package algorithms;

import java.util.ArrayList;
import java.util.Iterator;

import dataStructure.arena_data;
import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.FruitsType;
import dataStructure.node_data;
import gameClient.game_server;

public class Arena_Algo {
	
	private static final double eps = 0.00001;

	public static void setGameFruits(arena_data arena, game_server gameSupport) {
		arena.setFruits( gameSupport.getFruits());
	}
	
	public static void setGameRobots(arena_data arena, game_server gameSupport) {
		arena.setRobots(gameSupport.getRobots());
	}
	
	public static edge_data getFruitEdge(arena_data arena, fruit_data f) {
		ArrayList<edge_data> TheEdge = new ArrayList<edge_data>();
		edge_data fruitEdge = null;
		try { 
			for (Iterator<node_data> iterator = arena.getGraph().getV().iterator(); iterator.hasNext();) {
				node_data node = iterator.next();
				for (Iterator<edge_data> iterator2 = arena.getGraph().getE(node.getKey()).iterator(); iterator2.hasNext();) {
					edge_data edge = iterator2.next();
					if ( eps >= Math.abs( node.getLocation().distance2D(arena.getGraph().getNode(edge.getDest()).getLocation()) - 
							(node.getLocation().distance2D(f.getPos()) + f.getPos().distance2D(arena.getGraph().getNode(edge.getDest()).getLocation())))){

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
		return fruitEdge;
	}
	
}
///**
// * This function is change the Arena fruits to be new fruit_data 
// * array that contains all the fruits from the game.
// * @param gameSupport - the game_support which the game plays throw.
// */
//@Override
//public void setFruits(game_support gameSupport) {
//	fruit_data[] fruits = new fruit_data[gameSupport.fruitsSize()]; 
//	Iterator<String> f_iter = gameSupport.getGameService().getFruits().iterator();
//	for(int i = 0; f_iter.hasNext(); i++) {
//		JSONObject jsonrobot;
//		try {
//			jsonrobot = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
//			fruits[i] = new Fruit(jsonrobot);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	this.fruits = fruits;
//}
///**
// * This function is change the Arena robots to be new robot_data 
// * array that contains all the robots from the game.
// * @param gameSupport - the game_support which the game plays throw.
// */
//@Override
//public void setRobots(game_support gameSupport) {
//	robot_data[] robots = new robot_data[gameSupport.robotsSize()];
//	Iterator<String> r_iter = gameSupport.getGameService().getRobots().iterator();
//	for(int i = 0; r_iter.hasNext(); i++) {
//		JSONObject jsonrobot;
//		try {
//			jsonrobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
//			this.robots[i] = new Robot(jsonrobot);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	this.robots = robots;
//}



///**
// * This function is change the arena_data robots to be new robot_data array.
// * @param gameSupport - the game_support which the game plays throw.
// */
//public void setRobots(game_support gameSupport) ;
///**
// * This function is change the arena_data fruits to be new fruit_data array.
// * @param gameSupport - the game_support which the game plays throw.
// */
//public void setFruits(game_support gameSupport) ;