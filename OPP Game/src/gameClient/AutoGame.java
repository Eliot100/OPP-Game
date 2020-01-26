package gameClient;

import java.util.Iterator;
import java.util.LinkedList;

import algorithms.Arena_Algo;
import algorithms.Graph_Algo;
import dataStructure.Arena;
import dataStructure.fruit_data;
import dataStructure.node_data;
import dataStructure.robot_data;
/**
 * This class is used to the automat state in the game 
 * @author Eli Ruvinov
 */
public class AutoGame {
	private game_server server;
	private Arena arena;
	private Graph_Algo ga;
	/**
	 * Constructor of AutoGame
	 * @param server - the game_server of the AutoGame
	 * @param arena - the Arena of the AutoGame
	 */
	public AutoGame(game_server server, Arena arena) {
		this.server = server;
		this.arena = arena;
		ga = new Graph_Algo(this.arena.getGraph());
	}
	/**
	 * Place all the robots on the arena
	 */
	public void setRobots() {
		int robotsNum = server.robotsSize();
		int fruitsNum = server.fruitsSize();
		fruit_data[] fruits = server.getFruits();
		if (robotsNum <= fruitsNum) {
			for (int i = 0; i < robotsNum; i++) 
				server.placeRobot(Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
		}
		else {
			for (int i = 0; i < fruitsNum; i++) 
				server.placeRobot(Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
//			for (int i = 0; i < robotsNum-fruitsNum; i++) 
//				server.placeRobot(randomPlace(arena.getGraph()));
		}
	}
	
//	private int randomPlace(DGraph graph) {
//		int ans = 0;
//		int r = (int)(1+(Math.random()*(graph.nodeSize()-1)));
//		Iterator<node_data> itr = graph.getV().iterator();
//		for(int i=0; i<r ;i++) 
//			ans = itr.next().getKey();
//		return ans;
//	}
	
//	public void randomWalk(robot_data robot) {
//		int dest = -1;
//		int r = (int)(Math.random()*(arena.getGraph().getE(robot.getSrc()).size()));
//		Iterator<edge_data> itr = arena.getGraph().getE(robot.getSrc()).iterator();
//		for(int i=0; i<r ;i++) 
//			dest = itr.next().getDest();
//		robot.setDest(dest);
//		server.RobotNextNode(robot.getId(), dest);
//	}
	/** 
	 * @param robot 
	 * @return the "best" destantion for this robot (given the server robots). 
	 */
	public int moveSimultan(robot_data robot) {
		int dest = 0;
		robot_data[] robots = server.getRobots();
		fruit_data[] fruits = server.getFruits();
		double[][] RobotFruitPath = new double[robots.length][fruits.length]; 
		double[] minDist = new double[robots.length];
		int[] minDistPlace = new int[robots.length];
		setRobotFruitPathDistMatrix(RobotFruitPath, fruits, robots, minDist, minDistPlace );
		boolean[] fruitsflags = new boolean[fruits.length];
		boolean[] robotsflags = new boolean[robots.length];
		for (int ii = 0; ii < robots.length; ii++) {
			int tempPlace = SetRobotNeerFruit(robots, robotsflags, minDist);
			robotsflags[tempPlace] = true;
			fruitsflags[minDistPlace[tempPlace]] = true;
			if(robots[tempPlace].getId() == robot.getId())
				dest = robotToFruitPlace(robots[tempPlace], minDistPlace[tempPlace]);
			SetRobotsNeerestFruit(RobotFruitPath, fruits, robots, robotsflags, fruitsflags, minDist, minDistPlace);
		}
		return dest;
	}
	
	private void SetRobotsNeerestFruit(double[][] robotFruitPath, fruit_data[] fruits, robot_data[] robots,
			boolean[] robotsflags, boolean[] fruitsflags, double[] minDist, int[] minDistPlace) {
		for (int i = 0; i < robots.length; i++) {
			if(robotsflags[i]) continue;
			boolean firstFruit = true;
			for (int j = 0; j < fruits.length; j++) {
				if (fruitsflags[j]) continue;
				if (firstFruit) {
					minDistPlace[i] = j;
					minDist[i] = robotFruitPath[i][j];
				} else if (minDist[i] > robotFruitPath[i][j] ) {
					minDistPlace[i] = j;
					minDist[i] = robotFruitPath[i][j];
				}
			}
		}
	}
	
	private int SetRobotNeerFruit(robot_data[] robots, boolean[] robotsflags, double[] minDist) {
		boolean firstDist = true; 
		int tempPlace = 0;
		double tempMin = 0;
		for (int i = 0; i < robots.length; i++) {
			if(robotsflags[i]) continue;
			if(firstDist){
				tempMin =  minDist[i];
				tempPlace = i;
				firstDist = false;
			} else if (tempMin >  minDist[i]) {
				tempMin =  minDist[i];
				tempPlace = i;
			}
		}
		return tempPlace;
	}
	
	private void setRobotFruitPathDistMatrix(double[][] robotFruitPath, fruit_data[] fruits, robot_data[] robots,
			double[] minDist, int[] minDistPlace) {
		for (int j = 0; j < fruits.length; j++) {
			for (int i = 0; i < robots.length; i++) {
				robotFruitPath[i][j] = (Arena_Algo.getFruitEdge(server.getGraph(), fruits[j]).getWeight() + 
						ga.shortestPathDist(robots[i].getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[j]).getSrc()))
						/ robots[i].getSpeed();
				if(j==0) {
					minDist[i] = robotFruitPath[i][j];
					minDistPlace[i] = j; 
				}
				else if (minDist[i] > robotFruitPath[i][j]) {
					minDist[i] = robotFruitPath[i][j];
					minDistPlace[i] = j; 
				}
			}
		}
	}
	
	private int robotToFruitPlace(robot_data robot, int i) {
		fruit_data[] fruits = server.getFruits();
		int dest = 0;
		if (robot.getSrc() == Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc()) {
			dest = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getDest();
		} else {
			LinkedList<node_data> Path = (LinkedList<node_data>) ga.shortestPath(robot.getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
			Iterator<node_data> it = Path.iterator();
			if(it.hasNext())
				dest = it.next().getKey();
			if(it.hasNext())
				dest = it.next().getKey();
		}
		return dest;
	}
	
//	private void move2ClosestFruit(robot_data robot, boolean[] flags) {
//		int dest = -1;
//		fruit_data[] fruits = server.getFruits();
//		double dist = 0;
//		int fruitPlace = 0;
//		boolean first = true;
//		for (int i = 0; i < fruits.length; i++) {
//			if (flags[i]) {
//				continue;
//			}
//			if (first) {
//				first = false;
//				fruitPlace = i;
//				if (robot.getSrc() == Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc()) {
//					dest = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getDest();
//					dist = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getWeight();
//				} else {
//					LinkedList<node_data> Path = (LinkedList<node_data>) ga.shortestPath(robot.getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
//					dist = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getWeight() 
//							+ ga.shortestPathDist(robot.getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
//					Iterator<node_data> it = Path.iterator();
//					boolean setDest = false;
//					while (it.hasNext() && !setDest) {
//						node_data node = it.next();
//						if (server.getGraph().getEdge(robot.getSrc(), node.getKey()) != null) {
//							dest = node.getKey();
//							setDest = true;
//						}
//					}
//				}
//			} else {
//				double tempDist = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getWeight() 
//									+ ga.shortestPathDist(robot.getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
//				if(tempDist < dist) {
//					fruitPlace = i;
//					if (robot.getSrc() == Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc()) {
//						dest = Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getDest();
//						dist = tempDist;
//					} else {
//						LinkedList<node_data> Path = (LinkedList<node_data>) ga.shortestPath(robot.getSrc(), Arena_Algo.getFruitEdge(server.getGraph(), fruits[i]).getSrc());
//						Iterator<node_data> it = Path.iterator();
//						boolean setDest = false;
//						while (it.hasNext() && !setDest) {
//							node_data node = it.next();
//							if (arena.getGraph().getEdge(robot.getSrc(), node.getKey()) != null) {
//								dest = node.getKey();
//								setDest = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		robot.setDest(dest);
//		server.RobotNextNode(robot.getId(), dest);
//		flags[fruitPlace] = true;
//	}

//	private class randomWalk implements Runnable {
//
//		@Override
//		public void run() {
//			while(Support.isRunning()) {
//				robot_data[] robots = Support.getRobots();
//				for (robot_data robot : robots) {
//					if(robot.getDest() == -1);
//						randomWalk(robot);
//				}
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
	
//	private class move2ClosestFruit implements Runnable {
//
//		@Override
//		public void run() {
////			while(server.isRunning()) {
//				boolean[] flags = new boolean[server.fruitsSize()];
//				robot_data[] robots = server.getRobots();
//				for (robot_data robot : robots) {
//					move2ClosestFruit(robot, flags);
//				}
////				for (robot_data robot : robots) {
////					if(robot.getDest() == -1);
////						randomWalk(robot);
////				}
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
////		}
//
//	}
}
