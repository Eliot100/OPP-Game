package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Fruit {
	private double value;
	private int type;
	private Point3D pos;
	
	public Fruit(JSONObject jsonFruit) throws JSONException {
		this.value = jsonFruit.getDouble("value");
		this.type = jsonFruit.getInt("type");
		String pos  = jsonFruit.getString("pos");
		String[] xyz = pos.split(",");
		this.pos = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
	}
	
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
