package dataStructure;

import utils.Point3D;
/**
 * This interface represents the game robot.   
 * @author Eli Ruvinov
 */
public interface robot_data {
	/**
	 * @return the key of the node_data that this robot_data last was on.
	 */
	public int getSrc();
	/**
	 * @return the key of the node_data that this robot_data headed to.
	 */
	public int getDest();
	/**
	 * This function is changing the robot_data destination node_data.
	 * @param dest - the key of the node_data which you want to be 
	 * the next node that this robot_data will move to.
	 */
	public void setDest(int dest);
	public void setPos(Point3D p); 
	/**
	 * @return the id of this robot_data
	 */
	public int getId();
	/**
	 * @return the score value of this robot_data.
	 */
	public double getValue();
	/**
	 * @return the speed value of this robot_data.
	 */
	public int getSpeed();
	public void setSpeed(int speed);
	/**
	 * @return the position (Point3D) of the robot_data.
	 */
	public Point3D getPos();
}
