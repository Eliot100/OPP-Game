package dataStructure;

/**
 * This interface represents the game board.   
 * @author Eli Ruvinov
 */
public interface arena_data {
	/**
	 * @return the fruits from the arena_data.
	 */
	public fruit_data[] getFruits() ;
	/**
	 * This function is change the arena_data fruit_data array to be new fruit_data array.
	 * @param fruits - the new fruit_data array.
	 */
	public void setFruits(fruit_data[] fruits) ;
	/**
	 * @return the robots from the arena_data.
	 */
	public robot_data[] getRobots() ;
	/**
	 * This function is change the arena_data robot_data array to be new robot_data array.
	 * @param robots - the new robot_data array.
	 */
	public void setRobots(robot_data[] robots) ;
	/**
	 * @return the graph (directed) of the arena_data.
	 */
	public DGraph getGraph() ;
	/**
	 * This function is change the arena_data graph to be new directed graph.
	 * @param graph - the new directed graph.
	 */
	public void setGraph(DGraph graph) ;
}
