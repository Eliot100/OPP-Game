package gameClient;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.fruit_data;
import dataStructure.robot_data;
/**
 * This interface represents the connection between the GameServer and the rest of the code.
 * @author Eli Ruvinov
 */
public interface game_support {
	/**
	 * @return the fruits from the game.
	 */
	public game_service getGameService() ;
	/**
	 * @return the fruits from the game.
	 */
	public fruit_data[] getFruits() ;
	/**
	 * @return how many fruit_data there is in the game.
	 */
	public int fruitsSize();
	/**
	 * @return the robots from the game.
	 */
	public robot_data[] getRobots();
	/**
	 * @return how many robot_data there is in the game.
	 */
	public int robotsSize();
	/**
	 * @return the graph (directed) of the game.
	 */
	public DGraph getGraph() ;
	/**
	 * @return how many moves was playd.
	 */
	public int getMoves() ;
	/**
	 * @return the grade of the game.
	 */
	public double getGrade() ;
	/**
	 * This function is giving the game server the order 
	 * to move the robots in the directed graph of the game.
	 */
	public void moveRobots();
	/**
	 * This function is giving the game server the order 
	 * to put robot in node of the game graph.
	 * @param nodeKey - the key of the node which you want to put the robot.
	 */
	public void placeRobot(int nodeKey);
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
