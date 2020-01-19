package gameClient;

import java.util.Iterator;

import algorithms.Arena_Algo;
//import algorithms.Graph_Algo;
import dataStructure.Arena;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.node_data;
import dataStructure.robot_data;

public class AutoGame {
	private game_support Support;
	private Arena arena;
//	private Graph_Algo ga;

	public AutoGame(game_support support, Arena arena) {
		this.Support = support;
		this.arena = arena;
//		ga = new Graph_Algo(this.arena.getGraph());
	}

	public void setRobots() {
		int robotsNum = Support.robotsSize();
		int fruitsNum = Support.fruitsSize();
		fruit_data[] fruits = Support.getFruits();

		if (robotsNum <= fruitsNum) {
			for (int i = 0; i < robotsNum; i++) {
				Support.placeRobot(Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
			}
		}
		else {
			for (int i = 0; i < fruitsNum; i++) {
				Support.placeRobot(Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
			}
			for (int i = 0; i < robotsNum-fruitsNum; i++) {
				Support.placeRobot(randomPlace(arena.getGraph()));
			}
		}

	}

	private int randomPlace(DGraph graph) {
		int ans = 0;
		int r = (int)(1+(Math.random()*(graph.nodeSize()-1)));
		Iterator<node_data> itr = graph.getV().iterator();
		for(int i=0; i<r ;i++) 
			ans = itr.next().getKey();
		return ans;
	}

	public void moveRobots() {
		Thread t = new Thread(new randomWalk());
		t.start();
	}
	
	private void randomWalk(robot_data robot) {
		int dest = -1;
		int r = (int)(Math.random()*(arena.getGraph().getE(robot.getSrc()).size()));
		Iterator<edge_data> itr = arena.getGraph().getE(robot.getSrc()).iterator();
		for(int i=0; i<r ;i++) 
			dest = itr.next().getDest();
		Support.RobotNextNode(robot.getId(), dest);
	}

	private class randomWalk implements Runnable {

		@Override
		public void run() {
			while(Support.isRunning()) {
				robot_data[] robots = Support.getRobots();
				for (robot_data robot : robots) {
					if(robot.getDest() == -1)
						randomWalk(robot);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
