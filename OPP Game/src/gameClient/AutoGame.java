package gameClient;

import java.util.Iterator;
import java.util.LinkedList;

import algorithms.Arena_Algo;
import algorithms.Graph_Algo;
import dataStructure.Arena;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.node_data;
import dataStructure.robot_data;

public class AutoGame {
	private game_support Support;
	private Arena arena;
	private Graph_Algo ga;

	public AutoGame(game_support support, Arena arena) {
		this.Support = support;
		this.arena = arena;
		ga = new Graph_Algo(this.arena.getGraph());
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
		Thread t = new Thread(new move2ClosestFruit());
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
	
	private void moveSimultan() {
		double[][] matrix = new double[Support.robotsSize()][Support.fruitsSize()]; 
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				double dist = Arena_Algo.getFruitEdge(arena, Support.getFruits()[j]).getWeight() 
						+ ga.shortestPathDist(Support.getRobots()[i].getSrc(), Arena_Algo.getFruitEdge(arena, Support.getFruits()[j]).getSrc());
				matrix[i][j] = dist;
			}
		}
		
		boolean[] flags = new boolean[Support.fruitsSize()];
		robot_data[] robots = Support.getRobots();
		for (robot_data robot : robots) {
			if(robot.getDest() == -1)
				move2ClosestFruit(robot, flags);
		}
	}
	
	private void move2ClosestFruit(robot_data robot, boolean[] flags) {
		int dest = -1;
		fruit_data[] fruits = Support.getFruits();
		double dist = 0;
		int fruitPlace = 0;
		boolean first = true;
		for (int i = 0; i < fruits.length; i++) {
			if (flags[i]) {
				continue;
			}
			if (first) {
				first = false;
				fruitPlace = i;
				if (robot.getSrc() == Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc()) {
					dest = Arena_Algo.getFruitEdge(arena, fruits[i]).getDest();
					dist = Arena_Algo.getFruitEdge(arena, fruits[i]).getWeight();
				} else {
					LinkedList<node_data> Path = (LinkedList<node_data>) ga.shortestPath(robot.getSrc(), Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
					dist = Arena_Algo.getFruitEdge(arena, fruits[i]).getWeight() 
							+ ga.shortestPathDist(robot.getSrc(), Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
					Iterator<node_data> it = Path.iterator();
					boolean setDest = false;
					while (it.hasNext() && !setDest) {
						node_data node = it.next();
						if (arena.getGraph().getEdge(robot.getSrc(), node.getKey()) != null) {
							dest = node.getKey();
							setDest = true;
						}
					}
				}
			} else {
				double tempDist = Arena_Algo.getFruitEdge(arena, fruits[i]).getWeight() 
									+ ga.shortestPathDist(robot.getSrc(), Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
				if(tempDist < dist) {
					fruitPlace = i;
					if (robot.getSrc() == Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc()) {
						dest = Arena_Algo.getFruitEdge(arena, fruits[i]).getDest();
						dist = tempDist;
					} else {
						LinkedList<node_data> Path = (LinkedList<node_data>) ga.shortestPath(robot.getSrc(), Arena_Algo.getFruitEdge(arena, fruits[i]).getSrc());
						Iterator<node_data> it = Path.iterator();
						boolean setDest = false;
						while (it.hasNext() && !setDest) {
							node_data node = it.next();
							if (arena.getGraph().getEdge(robot.getSrc(), node.getKey()) != null) {
								dest = node.getKey();
								setDest = true;
							}
						}
					}
				}
			}
		}
		Support.RobotNextNode(robot.getId(), dest);
		flags[fruitPlace] = true;
	}

	private class randomWalk implements Runnable {

		@Override
		public void run() {
			while(Support.isRunning()) {
				robot_data[] robots = Support.getRobots();
				for (robot_data robot : robots) {
					if(robot.getDest() == -1);
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
	
	private class move2ClosestFruit implements Runnable {

		@Override
		public void run() {
			while(Support.isRunning()) {
				boolean[] flags = new boolean[Support.fruitsSize()];
				robot_data[] robots = Support.getRobots();
				for (robot_data robot : robots) {
					move2ClosestFruit(robot, flags);
				}
//				for (robot_data robot : robots) {
//					if(robot.getDest() == -1);
//						randomWalk(robot);
//				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
