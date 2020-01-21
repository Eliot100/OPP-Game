package gameClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import dataStructure.fruit_data;
import dataStructure.microDGraph;
import dataStructure.robot_data;
import utils.Point3D;

public class MyServer implements game_server {
	private game_service game;
	
	public MyServer(game_service game){
		this.game = game;
	}
	
	@Override
	public void moveRobots() {
		game.move();
	}

	@Override
	public void placeRobot(int nodeKey) {
		game.addRobot(nodeKey);
	}

	@Override
	public void RobotNextNode(int robotId, int destKey) {
		game.chooseNextEdge(robotId, destKey);
	}

	@Override
	public boolean isRunning() {
		try {
			return game.isRunning();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public long startGame() {
		return game.startGame();
	}

	@Override
	public long stopGame() {
		return game.stopGame();
	}

	@Override
	public long time2End() { 
		try {
		return game.timeToEnd();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public fruit_data[] getFruits() {
		fruit_data[] fruits = new Fruit[fruitsSize()];
		Iterator<String> f_iter = game.getFruits().iterator();
		int i = 0;
		while(f_iter.hasNext()) {
			try {
				JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
				fruits[i] = new Fruit(JSONfruit) ;
			} catch (JSONException e) {
				throw new RuntimeException(e.toString());
			}
			i++;
		}
		return fruits;
	}

	@Override
	public robot_data[] getRobots() {
		robot_data[] robots = new Robot[robotsSize()];
		Iterator<String> r_iter = game.getRobots().iterator();
		for(int i = 0; r_iter.hasNext(); i++) {
			JSONObject JsonRobot;
			try {
				JsonRobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
				robots[i] = new Robot(JsonRobot);
			} catch (JSONException e) {
				throw new RuntimeException(e.toString());
			}
		}
		return robots;
	}

	@Override
	public DGraph getGraph() {
		DGraph graph = new DGraph();
		Gson gson = new Gson();
		String st0 = "";
		try {
			String info = game.toString();
			JSONObject line = new JSONObject(info);
			JSONObject JSONgame = line.getJSONObject("GameServer");
			String graph0 = JSONgame.getString("graph");
			File DGraph_JASON = new File(graph0);
			BufferedReader br = new BufferedReader(new FileReader(DGraph_JASON));
			String st;
			while((st = br.readLine()) != null) {
				st0 += st;
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		microDGraph collections = gson.fromJson(st0, microDGraph.class);
		for (microDGraph.microNode node : collections.getNodes()) {
			String[] xyz = node.getPos().split(",");
			Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
			graph.addNode( new Node(node.getId(), p));
		}
		for (microDGraph.microEdge edge : collections.getEdges()) {
			graph.connect(edge.getSrc(), edge.getDest(), edge.getW());
		}
		return graph;
	}

	@Override
	public game_service getGameService() {
		return game;
	}

	@Override
	public int fruitsSize() {
		String info = game.toString();
		JSONObject JSONgame;
		try {
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("fruits");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in robotsSize Function ");
		}
	}

	@Override
	public int robotsSize() {
		String info = game.toString();
		JSONObject JSONgame;
		try {
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("robots");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in robotsSize Function ");
		}
	}

	@Override
	public int getMoves() {
		String info = game.toString();
		JSONObject JSONgame;
		try {
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("moves");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in getMoves Function ");
		}
	}

	@Override
	public double getGrade() {
		String info = game.toString();
		JSONObject JSONgame;
		try {
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getDouble("grade");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in getMoves Function ");
		}
	}
	
	

}
