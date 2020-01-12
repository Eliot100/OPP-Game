package dataStructure;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import Server.game_service;
import gameClient.MainGameClient;
import utils.Point3D;

public class Arena {
	private DGraph graph;
	private Fruit[] fruits;
	private Robot[] robots;
	
	public Arena(game_service game) throws JSONException {
		createDGraph(game);
		createFruits(game);
		createRobots(game);
	}
	
	private void createDGraph(game_service game) {
		DGraph graph = new DGraph();
		Gson gson = new Gson();
		microDGraph collections = gson.fromJson(MainGameClient.getGraphStr(game), microDGraph.class);
		for (microNode node : collections.getNodes()) {
			String[] xyz = node.getPos().split(",");
			Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
			graph.addNode( new Node(node.getId(), p));
		}
		for (microEdge edge : collections.getEdges()) {
			graph.connect(edge.getSrc(), edge.getDest(), edge.getW());
		}
		this.graph = graph;
	}
	
	private void createRobots(game_service game) throws JSONException {
		this.robots = new Robot[MainGameClient.getRobotsNum(game.toString())];
	}

	private void createFruits(game_service game) throws JSONException {
		Fruit[] fruits = new Fruit[MainGameClient.getFruitsNum(game.toString())];
		Iterator<String> f_iter = game.getFruits().iterator();
		int i = 0;
		while(f_iter.hasNext()) {
			JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
			fruits[i] = new Fruit(JSONfruit) ;
			i++;
		}
		this.fruits = fruits;
	}

	public DGraph getGraph() {
		return graph;
	}
	public void setGraph(DGraph graph) {
		this.graph = graph;
	}
	public Fruit[] getFruits() {
		return fruits;
	}
	public void setFruits(Fruit[] fruits) {
		this.fruits = fruits;
	}
	public Robot[] getRobots() {
		return robots;
	}
	public void setRobots(Robot[] robots) {
		this.robots = robots;
	}

	public void setFruits(game_service game) throws JSONException {
		Iterator<String> f_iter = game.getFruits().iterator();
		int i = 0;
		while(f_iter.hasNext()) {
			JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
			this.fruits[i] = new Fruit(JSONfruit) ;
			i++;
		}
	}

	public void setRobots(game_service game) throws JSONException {
		Iterator<String> r_iter = game.getRobots().iterator();
		for(int i = 0; r_iter.hasNext(); i++) {
			JSONObject jsonrobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
			this.robots[i] = new Robot(jsonrobot);
		}
	}

}
