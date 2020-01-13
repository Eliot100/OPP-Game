package gameClient;

import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Robot;
/**
 * This interface represents the connection between the GameServer and the rest of the code.
 * @author Eli Ruvinov
 */
public interface game_support {
	/**
	 * @return the fruits from the game.
	 */
	public Fruit[] getFruits() ;
	/**
	 * @return the robots from the game.
	 */
	public Robot[] getRobots() ;
	/**
	 * @return the graph (directed) of the game.
	 */
	public DGraph getGraph() ;
	/**
	 * This function is giving the game server the order 
	 * to move the robots in the directed graph of the game.
	 */
	public void moveRobots();
	/**
	 * This function is giving the game server the order 
	 * to put robot in node of the game graph.
	 * @param robotId - the id of the robot you want to put.
	 * @param nodeKey - the key of the node which you want to put the robot.
	 */
	public void placeRobot(int robotId, int nodeKey);
	/**
	 * This function is giving the game server the order 
	 * to change the robot destination by node key.
	 * @param robotId - the id of the robot you set its destination node.
	 * @param nodeKey - the key of the destination node 
	 * which you want the robot will move to.
	 */
	public void RobotNextNode(int robotId, int nodeKey);
	/**
	 * @return true if there is more time to the game else false. 
	 */
	public boolean isRunning();
	/**
	 * This function is giving the game server the order 
	 * to start the game.
	 * @return how much time left to the game.
	 */
	public long startGame();
	/**
	 * This function is giving the game server the order 
	 * to stop the game.
	 * @return how much time left to the game.
	 */
	public long stopGame();
	/**
	 * @return how much time left to the game.
	 */
	public long time2End();
}
