package gameClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.prism.Image;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gui.GUI_Window;
import oop_dataStructure.OOP_DGraph;
import utils.Point3D;

public class MainGameClient { // MainGameClien1t MyGameGU1I
	private DGraph graph;
	private Robot[] robots;
	private Fruit[] fruits;
	
	public MainGameClient(graph g, Robot[] robots) {
		graph = new DGraph(g);
		new MyGameGUI(graph, robots, fruits);
	}
	/**
	 * 
	 * @param bob
	 */
	public static void main(String[] bob) {
		DGraph graph2 = new DGraph();
		int scenario_num = 0;
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
			for (microNode node : collections.Nodes) {
				String[] xyz = node.pos.split(",");
				Point3D p = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
				graph2.addNode( new Node(node.id, p));
			}
			for (microEdge edge : collections.Edges) {
				graph2.connect(edge.src, edge.dest, edge.w);
			}
			
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			int i = 0;
			while(f_iter.hasNext()) {
				JSONObject JSONfruit = (new JSONObject(f_iter.next())).getJSONObject("Fruit");
				fruits[i] = new Fruit(JSONfruit) ;
				System.out.println("f : " + fruits[i].getPos()+  " " + fruits[i].getType());
				i++;
			}	
			
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for(int a = 0; a < rs; a++) {
				game.addRobot(src_node+a);
			}
			Iterator<String> r_iter = game.getRobots().iterator();
			i = 0;
			while(r_iter.hasNext()) {
				JSONObject JSONrobot = (new JSONObject(r_iter.next())).getJSONObject("Robot");
				robots[i] = new Robot(JSONrobot);
				System.out.println("r : " + robots[i].getPos());
			}
			new MyGameGUI(graph2, robots, fruits);
			
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
	
	private class microDGraph {
		microEdge[] Edges;
		microNode[] Nodes;
//		public microDGraph(microEdge[] Edges, microNode[] Nodes) {
//			this.Edges = Edges;
//			this.Nodes = Nodes;
//		}
	}
	private class microEdge {
		int src;
		double w;
		int dest;
//		public microEdge(int src, double w, int dest){ 
//			this.dest = dest;
//			this.src = src;
//			this.w = src;
//		}
	}
	private class microNode {
		int id;
		String pos;
//		public microNode(int id, String pos){ 
//			this.id = id;
//			this.pos = pos;
//		}
	}
//	private class microNode {
//		int id;
//		Point3D pos;
//		public microNode(int id, String pos){ 
//			this.id = id;
//			String[] xyz = pos.split(",");
//			this.pos = new Point3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]));
//		}
//	}
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
