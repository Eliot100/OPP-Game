package dataStructure;
/**
 * This interface represents the game board.   
 * @author Eli Ruvinov
 */
public interface arena_data {
	/**
	 * @return the fruits from the arena.
	 */
	public fruit_data[] getFruits() ;
	/**
	 * This function is change the arena fruits to be new fruits array.
	 * @param fruits - the new fruits array.
	 */
	public void setFruits(fruit_data[] fruits) ;
	/**
	 * @return the robots from the arena.
	 */
	public robot_data[] getRobots() ;
	/**
	 * This function is change the arena robots to be new robot array.
	 * @param robots - the new robot array.
	 */
	public void setRobots(robot_data[] robots) ;
	/**
	 * @return the graph (directed) of the arena.
	 */
	public DGraph getGraph() ;
	/**
	 * This function is change the arena graph to be new directed graph.
	 * @param graph - the new directed graph.
	 */
	public void setGraph(DGraph graph) ;
}
