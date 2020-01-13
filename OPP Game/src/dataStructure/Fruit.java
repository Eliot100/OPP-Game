package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;
/**
 * This class is my implementation of my fruit_data interface.
 * @author Eli Ruvinov
 */
public class Fruit implements fruit_data {
	/**
	 * This field represents the score value that this Fruit poses.
	 */
	private double value;
	/**
	 * This field represents this Fruit type (banana or apple).
	 */
	private fruits type;
	/**
	 * This field represents this Fruit location (Point3D).
	 */
	private Point3D pos;
	/**
	 * This function is a constructor of the Fruit.
	 * @param jsonFruit - is a JSONObject which have 3 fields:
	 * 1) Double "value" - how much score this Fruit is worth.
	 * 2) Integer "type" - banana (-1) or apple (1).
	 * 3) String "pos" - String of the Point3D (x,y,z).
	 * @throws JSONException
	 */
	public Fruit(JSONObject jsonFruit) throws JSONException {
		this.value = jsonFruit.getDouble("value");
		int typeNum = jsonFruit.getInt("type");
		if (typeNum == -1) {
			this.type = fruits.banana;
		} else if (typeNum == 1) {
			this.type = fruits.apple;
		}
		String pos  = jsonFruit.getString("pos");
		String[] xyz = pos.split(",");
		this.pos = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
	}
	/**
	 * @return the type of the fruit_data (banana or apple).
	 */
	@Override
	public fruits getType() {
		return type;
	}
	/**
	 * This function is set this fruit_data position to be new Point3D.
	 * @param pos - the new Point3D.
	 */
	@Override
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	/**
	 * This function is set this fruit_data type to be a fruit from enum fruits.
	 * @param type - a fruit from enum fruits (banana or apple).
	 */
	@Override
	public void setType(fruits type) {
		this.type = type;
	}
	/**
	 * This function is set this fruit_data value to be a new score value (double).
	 * @param value - the new score that robot will get after eating this fruit_data from know.
	 */
	@Override
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * @return the position (Point3D) of the fruit_data.
	 */
	@Override
	public Point3D getPos() {
		return pos;
	}
	/**
	 * @return the score value (double) that robot gets after eating this fruit_data.  
	 */
	@Override
	public double getValue() {
		return value;
	}
}
