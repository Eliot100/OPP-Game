package dataStructure;

import utils.Point3D;
/**
 * This interface represents the fruit of the game arena. 
 * @author Eli Ruvinov
 */
public interface fruit_data {
	/**
	 * @return the position (Point3D) of the fruit_data.
	 */
	public Point3D getPos() ;
	/**
	 * This function is set this fruit_data position to be new Point3D.
	 * @param pos - the new Point3D.
	 */
	public void setPos(Point3D pos);
	/**
	 * @return the type of the fruit_data (banana or apple).
	 */
	public FruitsType getType();
	/**
	 * This function is set this fruit_data type to be a fruit from enum fruits.
	 * @param type - a fruit from enum fruits (banana or apple).
	 */
	public void setType(FruitsType type);
	/**
	 * @return the score value (double) that robot gets after eating this fruit_data.  
	 */
	public double getValue();
	/**
	 * This function is set this fruit_data value to be a new score value (double).
	 * @param value - the new score that robot will get after eating this fruit_data from know.
	 */
	public void setValue(double value);
}
