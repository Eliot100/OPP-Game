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
/**
 * This class use to comunicte with the server esaly
 * @author Eli Ruvinov
 */
public class MyServer implements game_server {
	private game_service game;
	/**
	 * 
	 * @param game - the game_service that I comunicte with.
	 */
	public MyServer(game_service game){
		this.game = game;
	}
	/**
	 * This function is giving the game server the order 
	 * to move the robots in the directed graph of the game.
	 */
	@Override
	public void moveRobots() {
		boolean b = true;
		while (b) {
			try {
				game.move();
				b = false;
			} catch (Exception e) {b = true;}
		}
	}
	/**
	 * This function is giving the game server the order 
	 * to put robot in node of the game graph.
	 * @param nodeKey - the key of the node which you want to put the robot.
	 */
	@Override
	public void placeRobot(int nodeKey) {
		game.addRobot(nodeKey);
	}
	/**
	 * This function is giving the game server the order 
	 * to change the robot destination by node key.
	 * @param robotId - the id of the robot you set its destination node.
	 * @param nodeKey - the key of the destination node 
	 * which you want the robot will move to.
	 */
	@Override
	public void RobotNextNode(int robotId, int destKey) {
		game.chooseNextEdge(robotId, destKey);
	}
	/**
	 * @return true if there is more time to the game else false. 
	 */
	@Override
	public boolean isRunning() {
		try {
			return game.isRunning();
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * This function is giving the game server the order 
	 * to start the game.
	 * @return how much time left to the game.
	 */
	@Override
	public long startGame() {
		return game.startGame();
	}
	/**
	 * This function is giving the game server the order 
	 * to stop the game.
	 * @return how much time left to the game.
	 */
	@Override
	public long stopGame() {
		return game.stopGame();
	}
	/**
	 * @return how much time left to the game.
	 */
	@Override
	public long time2End() { 
		try {
		return game.timeToEnd();
		} catch (Exception e) {
			return 0;
		}
	}
	/**
	 * @return the fruits from the game.
	 */
	@Override
	public fruit_data[] getFruits() {
		boolean b = true;
		fruit_data[] fruits = null;
		while (b) {
			b = false;
			fruits = new Fruit[fruitsSize()];
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
			for (fruit_data fruit : fruits) {
				if(fruit == null)
					b = true;
			}
		}
		return fruits;
	}
	/**
	 * @return the robots from the game.
	 */
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
	/**
	 * @return the graph (directed) of the game.
	 */
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
			graph.addNode( new Node(node.getId(), node.getPos()));
		}
		for (microDGraph.microEdge edge : collections.getEdges()) {
			graph.connect(edge.getSrc(), edge.getDest(), edge.getW());
		}
		return graph;
	}
	/**
	 * @return the game_service from the game.
	 */
	@Override
	public game_service getGameService() {
		return game;
	}
	/**
	 * @return how many fruit_data there is in the game.
	 */
	@Override
	public int fruitsSize() {
		try {
			String info = game.toString();
			JSONObject JSONgame;
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("fruits");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in robotsSize Function ");
		}
	}
	/**
	 * @return how many robot_data there is in the game.
	 */
	@Override
	public int robotsSize() {
		try {
			String info = game.toString();
			JSONObject JSONgame;
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("robots");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in robotsSize Function ");
		}
	}
	/**
	 * @return how many moves was playd.
	 */
	@Override
	public int getMoves() {
		try {
			String info = game.toString();
			JSONObject JSONgame;
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getInt("moves");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in getMoves Function ");
		}
	}
	/**
	 * @return the grade of the game.
	 */
	@Override
	public double getGrade() {
		try {
			String info = game.toString();
			JSONObject JSONgame;
			JSONgame = new JSONObject(info).getJSONObject("GameServer");
			return JSONgame.getDouble("grade");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("JSONException in getMoves Function ");
		}
	}
	/**
	 * Sending the content (of the kml file) to the server
	 * @param content
	 */
	@Override
	public void sendKML(String content) {
		try {
			game.sendKML(content);
		} catch (Exception e) {
			System.out.println("didnt send the log.");
		}
	}
}
