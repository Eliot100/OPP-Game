package gameClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import dataStructure.graph;
import dataStructure.microDGraph;
import dataStructure.microEdge;
import dataStructure.microNode;
import dataStructure.node_data;
import gui.manualGameGUI;
import oop_dataStructure.OOP_DGraph;
import utils.Point3D;

public class MainGameClient { // MainGameClien1t MyGameGU1I
	private DGraph graph;
	private Robot[] robots;
	private Fruit[] fruits;


	public static String getGraphStr(game_service game) {
		return game.getGraph();
	}
	
	public static int getFruitsNum(String game) throws JSONException {
		JSONObject JSONgame = new JSONObject(game).getJSONObject("GameServer");
		return JSONgame.getInt("fruits");
	}
	
	public static int getRobotsNum(String game) throws JSONException {
		JSONObject JSONgame = new JSONObject(game).getJSONObject("GameServer");
		return JSONgame.getInt("robots");
	}
	
	public static void placeRobot(game_service game, int nodeKey) {
		game.addRobot(nodeKey);
	}
	
	public Robot[] getRobots(game_service game) throws JSONException {
		Robot[] robots = new Robot[getRobotsNum(game.toString())];
		Iterator<String> r_iter = game.getRobots().iterator();
		for(int i = 0; r_iter.hasNext(); i++) {
			JSONObject JsonRobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
			robots[i] = new Robot(JsonRobot);
		}
		return robots;
	}
	
	public Fruit[] getFruits(game_service game) throws JSONException {
		Fruit[] fruits = new Fruit[getFruitsNum(game.toString())];
		Iterator<String> f_iter = game.getFruits().iterator();
		for(int i = 0; f_iter.hasNext(); i++) {
			JSONObject JsonFruit = (new JSONObject(f_iter.next())).getJSONObject("Robot");
			fruits[i] = new Fruit(JsonFruit);
		}
		return fruits;
	}
	
	public static void main(String[] bob) {
		DGraph graph2 = new DGraph();
		int scenario_num = 20;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		OOP_DGraph gg = new OOP_DGraph();
		gg.init(g);
		String info = game.toString();
		JSONObject line = null;
		try {
			line = new JSONObject(info);
			JSONObject JSONgame = line.getJSONObject("GameServer");
			String graph0 = JSONgame.getString("graph");
			File DGraph_JASON = new File(graph0);
			BufferedReader br = new BufferedReader(new FileReader(DGraph_JASON));
			String st0 = "";
			String st;
			while((st = br.readLine()) != null) {
				st0 += st;
			}
			br.close();
			//			Image Image = Image.
			line = new JSONObject(info);
			int rs = JSONgame.getInt("robots");
			int fs = JSONgame.getInt("fruits");
			System.out.println(info);
			Fruit[] fruits = new Fruit[fs];
			Robot[] robots = new Robot[rs];
			System.out.println(st0);
			Gson gson = new Gson();
			microDGraph collections = gson.fromJson(st0, microDGraph.class);
			for (microNode node : collections.getNodes()) {
				String[] xyz = node.getPos().split(",");
				Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
				graph2.addNode( new Node(node.getId(), p));
			}
			for (microEdge edge : collections.getEdges()) {
				graph2.connect(edge.getSrc(), edge.getDest(), edge.getW());
			}

			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			
			for (int i = 0; f_iter.hasNext(); i++) {
				JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
				fruits[i] = new Fruit(JSONfruit) ;
				System.out.println("f : " + fruits[i].getPos()+  " " + fruits[i].getType());
				
			}	

			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for(int a = 0; a < rs; a++) {
				game.addRobot(src_node+a);
			}
			Iterator<String> r_iter = game.getRobots().iterator();
			for(int i = 0; r_iter.hasNext(); i++) {
				JSONObject JsonRobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
				robots[i] = new Robot(JsonRobot);
				System.out.println("r : " + robots[i].getPos());
				
			}
//						new manualGameGUI(graph2, robots, fruits);

			////			System.out.println(g);

		}
		catch (JSONException | IOException e) {e.printStackTrace();}
		//		game.startGame();
		// should be a Thread!!!
		//		while(game.isRunning()) {
		//			moveRobots(game, gg);
		//		}
		//		String results = game.toString();
		//		System.out.println("Game Over: "+results);
	}
	
	public DGraph getGraph() {
		return graph;
	}
	public void setGraph(DGraph graph) {
		this.graph = graph;
	}
	public Robot[] getRobots() {
		return robots;
	}
	public void setRobots(Robot[] robots) {
		this.robots = robots;
	}
	public Fruit[] getFruits() {
		return fruits;
	}
	public void setFruits(Fruit[] fruits) {
		this.fruits = fruits;
	}

}
