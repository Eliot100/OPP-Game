package gameClient;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Robot {
	private int id;
	private int src;
	private int dest;
	private int speed;
	private double value;
	private Point3D pos;
	
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
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public int getDest() {
		return dest;
	}
	public void setDest(int dest) {
		this.dest = dest;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
}
