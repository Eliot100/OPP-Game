package algorithms;

import java.util.Iterator;

import dataStructure.Fruit;
import dataStructure.arena_data;
import dataStructure.edge_data;
import dataStructure.fruits;
import dataStructure.node_data;
import gameClient.game_support;

public class Arena_Algo {
	
	private static final double eps = 0.00001;

	public static void setGameFruits(arena_data arena, game_support gameSupport) {
		arena.setFruits( gameSupport.getFruits());
	}
	
	public static void setGameRobots(arena_data arena, game_support gameSupport) {
		arena.setRobots(gameSupport.getRobots());
	}
	
	public static edge_data getFruitEdge(arena_data arena, Fruit f) {
		for (Iterator<node_data> iterator = arena.getGraph().getV().iterator(); iterator.hasNext();) {
			node_data node = iterator.next();
			for (Iterator<edge_data> iterator2 = arena.getGraph().getE(node.getKey()).iterator(); iterator2.hasNext();) {
				edge_data edge = iterator2.next();
				if ( eps > Math.abs( node.getLocation().distance3D(arena.getGraph().getNode(edge.getDest()).getLocation()) - 
					(node.getLocation().distance3D(f.getPos()) + f.getPos().distance3D(arena.getGraph().getNode(edge.getDest()).getLocation())))){
					
					if (f.getType() == fruits.banana && edge.getSrc() > edge.getDest()) {
						return edge;
					} else if (f.getType() == fruits.apple && edge.getSrc() < edge.getDest()) {
						return edge;
					}
				}
			}
		}
		return null;
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