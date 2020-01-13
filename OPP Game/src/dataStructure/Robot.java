package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;
/**
 * This class is my implementation of my robot_data interface.
 * @author Eli Ruvinov
 */
public class Robot implements robot_data {
	/**
	 * This field represents this Robot id.	 
	 */
	private int id;
	/**
	 * This field represents the key of the last node_data which this Robot was on.	 
	 */
	private int src;
	/**
	 * This field represents the key of the node_data which this Robot is headed to.	 
	 */
	private int dest;
	/**
	 * This field represents this Robot speed.	 
	 */
	private int speed;
	/**
	 * This field represents this Robot score.	 
	 */
	private double value;
	/**
	 * This field represents this Robot location (Point3D).
	 */
	private Point3D pos;
	/**
	 * This function is a constructor of the Robot.
	 * @param jsonFruit - is a JSONObject which have 3 fields:
	 * 1) Double "value" - how much score this Fruit is worth.
	 * 2) Integer "type" - banana (-1) or apple (1).
	 * 3) String "pos" - String of the Point3D (x,y,z).
	 * @throws JSONException
	 */
	public Robot(JSONObject jsonRobot) throws JSONException {
		this.id = jsonRobot.getInt("id");
		this.src = jsonRobot.getInt("src");
		this.dest = jsonRobot.getInt("dest");
		this.value = jsonRobot.getDouble("value");
		this.speed = jsonRobot.getInt("speed");
		String pos  = jsonRobot.getString("pos");
		String[] xyz = pos.split(",");
		this.pos = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
	}
	/**
	 * @return the key of the node that this Robot last was on.
	 */
	public int getSrc() {
		return src;
	}
	/**
	 * This function is changing the Robot source node_data .
	 * @param dest - the key of the node_data which you want to be 
	 * the next node_data that this Robot will move to.
	 */
	public void setSrc(int src) {
		this.src = src;
	}
	/**
	 * @return the key of the node_data that this Robot is headed to.
	 */
	public int getDest() {
		return dest;
	}
	/**
	 * This function is changing the Robot destination node_data.
	 * @param dest - the key of the node_data which you want to be 
	 * the next node_data that this Robot will move to.
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}
	/**
	 * @return the id of this Robot.
	 */
	public int getId() {
		return id;
	}
	/**
	 * This function is changing the Robot id.
	 * @param id - the new id (Integer) for this Robot.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the score value of this Robot.
	 */
	public double getValue() {
		return value;
	}
	/**
	 * This function is changing the Robot value.
	 * @param value - the new score value (Double) for this Robot.
	 */
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * @return the speed value of this Robot.
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * This function is changing speed of the Robot.
	 * @param speed - the new Robot speed (Integer).
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	/**
	 * @return the position (Point3D) of the Robot.
	 */
	public Point3D getPos() {
		return pos;
	}
	/**
	 * This function is changing the Robot position.
	 * @param pos - the new position of the Robot.
	 */
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
}
