package dataStructure;

/**
 * This class is my implementation of my arena_data interface.
 * @author Eli Ruvinov
 */
public class Arena implements arena_data {
	/**
	 * This field represents the (directed) graph of the Arena.
	 */
	private DGraph graph;
	/**
	 * This field represents the fruits of the Arena.
	 */
	private fruit_data[] fruits;
	/**
	 * This field represents the robots of the Arena.
	 */
	private robot_data[] robots;
	/**
	 * This function is a constructor of the Arena.
	 * @param graph - the (directed) graph of the Arena.
	 * @param fruits - the fruits of the Arena.
	 * @param robots - the robots of the Arena.
	 */
	public Arena(DGraph graph, fruit_data[] fruits, robot_data[] robots) {
		this.graph = graph;
		this.fruits = fruits;
		this.robots = robots;
	}
	/**
	 * @return the fruits from the Arena.
	 */
	@Override
	public fruit_data[] getFruits() {
		return fruits;
	}
	/**
	 * This function is change the arena_data fruit_data array to be new fruit_data array.
	 * @param FruitsType - the new fruit_data array.
	 */
	@Override
	public robot_data[] getRobots() {
		return robots;
	}
	/**
	 * @return the graph of the Arena.
	 */
	@Override
	public DGraph getGraph() {
		return graph;
	}
	/**
	 * This function is change the Arena graph to be new directed graph.
	 * @param graph - the new directed graph.
	 */
	@Override
	public void setGraph(DGraph graph) {
		this.graph = graph;
	}
	/**
	 * This function is change the Arena fruit_data array to be new fruit_data array.
	 * @param fruits - the new fruit_data array.
	 */
	@Override
	public void setFruits(fruit_data[] fruits) {
		this.fruits = fruits;
	}
	/**
	 * This function is change the arena_data robot_data array to be new robot_data array.
	 * @param robots - the new robot_data array.
	 */
	public void setRobots(robot_data[] robots) {
		this.robots = robots;
	}
}
