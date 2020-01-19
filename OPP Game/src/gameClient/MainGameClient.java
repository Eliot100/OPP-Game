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
import dataStructure.microEdge;
import dataStructure.microNode;
import dataStructure.robot_data;
import utils.Point3D;

public class MainGameClient implements game_support {
	private game_service game;
	
	public MainGameClient(game_service game){
		this.game = game;
	}

//	public static void main(String[] bob) {
//		int scenario_num = 20;
//		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
//		MainGameClient supGame = new MainGameClient(game);
//		DGraph graph = supGame.getGraph();
//		fruit_data[] fruits = supGame.getFruits();
//		robot_data[] robots = supGame.getRobots();
//		
//		
//		
//		
////		String g = game.getGraph();
////		OOP_DGraph gg = new OOP_DGraph();
////		gg.init(g);
////		String info = game.toString();
////		JSONObject line = null;
////		try {
////			line = new JSONObject(info);
////			JSONObject JSONgame = line.getJSONObject("GameServer");
////			String graph0 = JSONgame.getString("graph");
////			File DGraph_JASON = new File(graph0);
////			BufferedReader br = new BufferedReader(new FileReader(DGraph_JASON));
////			String st0 = "";
////			String st;
////			while((st = br.readLine()) != null) {
////				st0 += st;
////			}
////			br.close();
////			//			Image Image = Image.
////			line = new JSONObject(info);
////			int rs = JSONgame.getInt("robots");
////			int fs = JSONgame.getInt("fruits");
////			System.out.println(info);
////			Fruit[] fruits = new Fruit[fs];
////			Robot[] robots = new Robot[rs];
////			System.out.println(st0);
////			Gson gson = new Gson();
////			microDGraph collections = gson.fromJson(st0, microDGraph.class);
////			for (microNode node : collections.getNodes()) {
////				String[] xyz = node.getPos().split(",");
////				Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
////				graph2.addNode( new Node(node.getId(), p));
////			}
////			for (microEdge edge : collections.getEdges()) {
////				graph2.connect(edge.getSrc(), edge.getDest(), edge.getW());
////			}
////
////			// the list of fruits should be considered in your solution
////			Iterator<String> f_iter = game.getFruits().iterator();
////			
////			for (int i = 0; f_iter.hasNext(); i++) {
////				JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
////				fruits[i] = new Fruit(JSONfruit) ;
////				System.out.println("f : " + fruits[i].getPos()+  " " + fruits[i].getType());
////				
////			}	
////
////			int src_node = 0;  // arbitrary node, you should start at one of the fruits
////			for(int a = 0; a < rs; a++) {
////				game.addRobot(src_node+a);
////			}
////			Iterator<String> r_iter = game.getRobots().iterator();
////			for(int i = 0; r_iter.hasNext(); i++) {
////				JSONObject JsonRobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
////				robots[i] = new Robot(JsonRobot);
////				System.out.println("r : " + robots[i].getPos());
////				
////			}
//////			new manualGameGUI(graph2, robots, fruits);
////
////
////		}
////		catch (JSONException | IOException e) {e.printStackTrace();}
////		game.startGame();
//////		should be a Thread!!!
////		while(game.isRunning()) {
////			moveRobots(game, gg);
////		}
////		String results = game.toString();
////		System.out.println("Game Over: "+results);
//	}

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
		return game.isRunning();
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
		for (microNode node : collections.getNodes()) {
			String[] xyz = node.getPos().split(",");
			Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
			graph.addNode( new Node(node.getId(), p));
		}
		for (microEdge edge : collections.getEdges()) {
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
