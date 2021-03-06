package gameClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server.Game_Server;
import Server.game_service;
import algorithms.Arena_Algo;
import dataStructure.Arena;
import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.FruitsType;
import dataStructure.node_data;
import dataStructure.robot_data;
import utils.Point3D;
/**
 * This class is a window a game. 
 * @author Eli Ruvinov
 */
public class MyGameGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1;
	private game_server server;
	private Arena arena;
	private Insets Insets;
	private BufferedImage bi;
	private final BufferedImage AppleImage = ImageIO.read(new File("apple.jpg"));
	private final BufferedImage BananaImage = ImageIO.read(new File("banana.jpg"));;
	private final BufferedImage RobotImage = ImageIO.read(new File("evea.jpg"));
	private KML_Logger KML_Logger;
	private AutoGame AutoGame;
	/**
	 * The last point that Clicked on this MyGameGUI object.
	 */
	public Point3D pivot;
	private static final Color edgeColor = new Color(80, 80, 80);
	private static final Color edgeTextColor = new Color(50, 50, 200);
	private static final int YUPborder = 35;
	private static final int border = 45;
	private static final int nodeRad = 10;
	private static int gameStage; 
	private boolean newPivot;
	private boolean isGameBegin;
	private boolean firstPaint;
	private boolean isAuto;
	private boolean isEnded;
	private boolean isRobotSets;
	private static double minY;
	private static double minX;
	private static double maxY;
	private static double maxX;
	private static int[] my_res ;
	private final static int MyId = 206416687 ;
	/**
	 * The main
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		GUI();
	}
	/**
	 * This is an example how to make the MyGameGUI object.
	 * @throws IOException 
	 */
	public static void GUI() throws IOException {
		new MyGameGUI();
	}
	/**
	 * Constructor of MyGameGUI 
	 * @throws IOException 
	 */
	public MyGameGUI() throws IOException {
		int id = MyId;
		Game_Server.login(id);
		String userAns = JOptionPane.showInputDialog("Enter 'A' for Aotomatic gui, else the window will be manual." );
		isAuto = false;
		if (userAns.equals("A")) {
			isAuto = true;
		}
		firstPaint = true;
		init(); 
		my_res = new int[24];
		bi = new BufferedImage(1500, 1000, BufferedImage.TYPE_INT_RGB );
		Insets = getInsets();
	}
	/**
	 * This is the function that paint the GUI frame. 
	 */
	public void paint(Graphics g)  {
		if (!firstPaint) {
			Graphics g1 = null;
			g1 = bi.getGraphics();
			g1.setColor(Color.white);
			g1.fillRect(0, 0, this.getWidth(), this.getHeight());
			if (arena != null ) {
				paintDGraph(g1);
				paintFruit(g1);
				paintRobot(g1);
				paintTime(g1);
				paintScore(g1);
				paintMoves(g1);
			}
			g.drawImage(bi, Insets.left, Insets.top, this);
		} else firstPaint = false;
	}
	/**
	 * This is the function that paint the DGraph. 
	 */
	private void paintDGraph(Graphics g) {
		for ( Iterator<node_data> iterator = arena.getGraph().getV().iterator() ; iterator.hasNext();) {
			node_data node = iterator.next();
			if (arena.getGraph().getE(node.getKey()) != null)
				for (Iterator<edge_data> iterator2 = arena.getGraph().getE(node.getKey()).iterator() ; iterator2.hasNext();) {
					edge_data edge = iterator2.next();
					drowEdge(edge, g);
				}
		}
		for ( Iterator<node_data> iterator = arena.getGraph().getV().iterator() ; iterator.hasNext();) {
			node_data node = iterator.next();
			drowNode(node, g);
		}
	}
	/**
	 * This function is refresh the frame minimum maximum values. 
	 */
	private void RefreshMinMaxFrame() {
		boolean b = true;
		if (arena != null && arena.getGraph().nodeSize() != 0 ) {
			for ( Iterator<node_data> iterator = arena.getGraph().getV().iterator() ; iterator.hasNext();) {
				node_data node = iterator.next();
				if(b) {
					minX = node.getLocation().x() ;
					maxX = node.getLocation().x();
					minY = node.getLocation().y();
					maxY = node.getLocation().y();
					b = false;
				} else {
					if (node.getLocation().x() < minX ) {
						minX = node.getLocation().x();
					} else if (node.getLocation().x() > maxX) {
						maxX = node.getLocation().x();
					}
					if (node.getLocation().y() < minY ) {
						minY = node.getLocation().y();
					} else if (node.getLocation().y() > maxY) {
						maxY = node.getLocation().y();
					}
				}
			}
		}
	}
	/**
	 * This function is scale a given y and return the y value of the pixel in the given y. 
	 * @return the y value of the pixel
	 */
	private int ScaleY(double y) {
		return upsideDownY((int) scale(y, minY, maxY, YUPborder+border , this.getHeight()-border ));
	}
	/**
	 * This function is fliping a given y value. 
	 * @param y - the given y
	 * @return the flip given y
	 */
	private int upsideDownY(int y) {
		int midY = (int) ((this.getHeight())/2);
		return (int) ((y - midY)*(-1)+midY);
	}
	/**
	 * This function is scale a given x and return the x value of the pixel in the given x. 
	 * @param x - the given x
	 * @return the x value of the pixel
	 */
	private int ScaleX(double x) {
		return (int) scale(x, minX, maxX, border, this.getWidth()-border );
	}
	/**
	 * This function is scale a given point to point on the MyGameGUI object frame.
	 * @param location - the given point
	 * @return point on the MyGameGUI object frame
	 */
	private Point3D ScaledLoc(Point3D location) {
		return new Point3D(ScaleX(location.x()), ScaleY(location.y()));
	}
	/**
	 * This function is scale data from range.
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, double t_min, double t_max){
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	/**
	 * This is the function that paint an edge on graphics object. 
	 * @param edge - the painted edge_data
	 * @param g - the graphics object;
	 */
	private void drowEdge(edge_data edge, Graphics g) {
		Point3D sorce = arena.getGraph().getNode(edge.getSrc()).getLocation();
		Point3D dest = arena.getGraph().getNode(edge.getDest()).getLocation();
		g.setColor(edgeColor);
		g.drawLine(ScaleX(sorce.x()), ScaleY(sorce.y()), ScaleX(dest.x()), ScaleY(dest.y()));
		g.setColor(edgeColor);
		Point3D DirectionPoint = edgeDirectionPoint( sorce, dest);
		g.fillOval(ScaleX(DirectionPoint.x())-5, ScaleY(DirectionPoint.y())-5, 8, 8);
		g.setColor(edgeTextColor);  
		Point3D TextPoint = edgeTextPoint( sorce, dest);
		g.drawString(String.format("%.2f", edge.getWeight()), ScaleX(TextPoint.x()), ScaleY(TextPoint.y()));
	}
	/**
	 * This is the function that paint an node on graphics object. 
	 * @param node - the painted node_data
	 * @param g - the graphics object;
	 */
	private void drowNode(node_data node, Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval(ScaleX(node.getLocation().x())-(int)(nodeRad/2), ScaleY(node.getLocation().y())-(int)(nodeRad/2), (int)nodeRad, (int)nodeRad);
		g.setColor(Color.BLUE);
		g.drawString(""+node.getKey(), ScaleX(node.getLocation().x())-2, ScaleY( node.getLocation().y())-9);
	}

	private Point3D edgeDirectionPoint(Point3D srcPoint, Point3D destPoint) {
		return new Point3D((srcPoint.x()+7*destPoint.x())/8, (srcPoint.y()+7*destPoint.y())/8);
	}

	private Point3D edgeTextPoint( Point3D srcPoint, Point3D destPoint) {
		return new Point3D((srcPoint.x()*3+7*destPoint.x())/10, (srcPoint.y()*3+7*destPoint.y())/10);
	}

	private void init() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.setBackground(Color.WHITE);
		this.setTitle(" GUI ");
		
		Menu menu2 = new Menu(" Game pleys print ");
		Menu menu1 = new Menu(" Game Options ");
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		menuBar.add(menu1); 
		if (isAuto) {
			String[] gameFunctions = {" init stage ", " start game ", " send log ", " save log " }; 
			for (int i = 0; i < gameFunctions.length; i++) {
				MenuItem Item = new MenuItem(gameFunctions[i]);
				Item.addActionListener(this);
				menu1.add(Item);
			}
		} else {
			String[] gameFunctions = {" init stage ", " start game ", " move robot ",  " send log ", " save log "}; 
			for (int i = 0; i < gameFunctions.length; i++) {
				MenuItem Item = new MenuItem(gameFunctions[i]);
				Item.addActionListener(this);
				menu1.add(Item);
			}
		}
		menuBar.add(menu2);
		String[] gameFunctions = {" The number of games ", " My best resalts "}; 
		for (int i = 0; i < gameFunctions.length; i++) {
			MenuItem Item = new MenuItem(gameFunctions[i]);
			Item.addActionListener(this);
			menu2.add(Item);

		} 
		
		this.addMouseListener(this);
		this.setSize(1300, 700);
		this.setVisible(true);
	}

	private void paintTime(Graphics g) {
		g.setColor(Color.BLACK);
		if (arena != null && server.time2End() != -1) {
			g.drawString("time to end : "+String.format("%.1f", (double) server.time2End()/1000), 20, 20);
		} else if (isEnded) {
			g.drawString("time to end : 0", 20, 20);
		}
	}

	private void paintScore(Graphics g) {
		g.setColor(Color.BLACK);
		if (arena != null ) {
			g.drawString("Score : "+String.format("%.2f", (double) server.getGrade()), getWidth()/2, 20);
		} 
	}


	private void paintMoves(Graphics g) {
		g.setColor(Color.BLACK);
		if (arena != null ) {
			g.drawString("Moves : "+server.getMoves(), getWidth()/3, 20);
		}
	}

	private void paintFruit(Graphics g) {
		for (int i = 0; i < arena.getFruits().length; i++) {
			if (arena.getFruits()[i].getType() == FruitsType.apple) {
				g.drawImage(AppleImage, ScaleX(arena.getFruits()[i].getPos().x())-15, ScaleY(arena.getFruits()[i].getPos().y())-15, 30, 30, null);
			} else if (arena.getFruits()[i].getType() == FruitsType.banana) {
				g.drawImage(BananaImage, ScaleX(arena.getFruits()[i].getPos().x())-10, ScaleY(arena.getFruits()[i].getPos().y())-15, 20, 30, null);
			}
		}
	}

	private void paintRobot(Graphics g) {
		if( isRobotSets)
			for (int i = 0; i < arena.getRobots().length; i++) {
				g.drawImage(RobotImage,ScaleX(arena.getRobots()[i].getPos().x())-10, ScaleY(arena.getRobots()[i].getPos().y())-15, 20, 30, null);
			}
	}
	@Override
	/**
	 * Sets a new pivot Point.
	 */
	public void mouseClicked(MouseEvent arg0) {
		int x0 = arg0.getX();
		int y0 = arg0.getY();
		pivot = new Point3D(x0, y0-YUPborder);
		newPivot = true;
	}
	@Override
	/**
	 * Do noting
	 */
	public void mouseDragged(MouseEvent e) {}
	@Override
	/**
	 * Do noting
	 */
	public void mouseMoved(MouseEvent e) {}
	@Override
	/**
	 * Do noting
	 */
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	/**
	 * Do noting
	 */
	public void mouseExited(MouseEvent arg0) {}
	@Override
	/**
	 * Do noting
	 */
	public void mousePressed(MouseEvent arg0) {}
	@Override
	/**
	 * Do noting
	 */
	public void mouseReleased(MouseEvent arg0) {}//server.moveRobots();
	@Override
	/**
	 * Linking between the ActionEvent and the suitable action
	 */
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case " init stage " :
			initStage();
			break;
		case " start game " :
			startGame();
			break;
		case " move robot " :
			Thread t = new Thread(new MoveRobot());
			t.start();
			break;
		case " save log " :
			try {
				this.KML_Logger.newFile(""+gameStage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case " send log " :
			Thread t3 = new Thread(new SendLog());
			t3.start();
			break;
		case " The number of games " :
			JOptionPane.showMessageDialog(null, "The number of games you play in the server is: " + NumberOfGames());
			break;
		case  " My best resalts " :
			JOptionPane.showMessageDialog(null, printResalts());
			break;
		}
	}
	
	private class SendLog implements Runnable {
		private SendLog(){}
		public void run() {
			try {
				KML_Logger.newFile(""+gameStage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class MoveRobot implements Runnable {
		private MoveRobot(){}
		public void run() {
			moveRobot();
		}
	}
	
	private void moveRobot() {
		server.moveRobots();
		arena.setRobots(server.getRobots());
		newPivot = false;
		robot_data theRobot = setRobot();
		System.out.println("loc robot : "+theRobot.getPos());
		node_data target = setTarget( theRobot);
		System.out.println("loc target : "+target.getLocation());
		theRobot.setDest(target.getKey());
		server.RobotNextNode(theRobot.getId(), target.getKey());
		server.moveRobots();
		RobotMovement(theRobot);
	}
	
	private robot_data setRobot() {
		robot_data[] robots = arena.getRobots();
		robot_data theRobot = null;
		Point3D pivotLoc = setPoint(this);
		double minDis = 0;
		boolean first = true;
		for (robot_data robot: robots) {
			Point3D ScaleRobotLoc = ScaledLoc(robot.getPos());
			if (first) {
				first = false;
				theRobot = robot;
				minDis = ScaleRobotLoc.distance3D(pivotLoc);
			} else if( ScaleRobotLoc.distance3D(pivotLoc) < minDis ) {
				theRobot = robot;
				minDis = ScaleRobotLoc.distance3D(pivotLoc);
			}
		}
		return theRobot;
	}

	private node_data setTarget( robot_data theRobot) {
		node_data DestNode = null;
		node_data robotNode = arena.getGraph().getNode(theRobot.getSrc());
		Point3D targetLoc = setPoint(this);
		double minDistToTarget = 0;
		boolean first = true;
		for (edge_data edge : arena.getGraph().getE(robotNode.getKey())) {
			Point3D ScaleLocation = ScaledLoc( arena.getGraph().getNode(edge.getDest()).getLocation());
			if (first) {
				first = false;
				minDistToTarget = ScaleLocation.distance2D(targetLoc);
				DestNode = arena.getGraph().getNode(edge.getDest());
			}
			else if (ScaleLocation.distance2D(targetLoc) < minDistToTarget ) {
				minDistToTarget = ScaleLocation.distance2D(targetLoc);
				DestNode = arena.getGraph().getNode(edge.getDest());
			}
		}
		return DestNode;
	}

	private static Point3D setPoint(MyGameGUI GameGUI) {
		while(true) {
			if (GameGUI.newPivot) {
				GameGUI.newPivot = false;
				return GameGUI.pivot;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private void createLog() {
		try {
			this.KML_Logger = new KML_Logger(server , gameStage );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void initStage() {
		String userAns = JOptionPane.showInputDialog("Enter the stage you want to play." );
		isRobotSets = false;
		isGameBegin = false;
		try {
			gameStage = Integer.parseInt(userAns);
			if (gameStage < -1 || gameStage > 23) {
				JOptionPane.showMessageDialog(null, "You didnt a valid stage.");
			} else {
				game_service game = Game_Server.getServer(gameStage);
				server = new MyServer(game);
				arena = new Arena(server.getGraph(), server.getFruits(), server.getRobots());
				RefreshMinMaxFrame();
				repaint();
				if (isAuto) {
					AutoGame = new AutoGame(server, arena);
					AutoGame.setRobots();
					isRobotSets = true;
					this.arena.setRobots(server.getRobots());
					repaint();
				} else {
					SetsRonots SR = new SetsRonots(this);
					Thread t = new Thread(SR);
					t.start();
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You didnt enter an number.");
		} 
	}

	private void startGame() {
		if (!isGameBegin) {
			isGameBegin = true;
			System.out.println("The game as begin .");
			if(isAuto) {
				runGame RG = new runGame(this);
				Thread t1 = new Thread(RG);
				t1.start();
			}
			server.startGame();
			Thread t2 = new Thread(new PaintAllTheTime());
			t2.start();
			Thread t3 = new Thread(new EatAllTheTime());
			t3.start();
			createLog();
			Thread t4 = new Thread(this.KML_Logger);
			t4.start();
		}
		else 
			System.out.println("The game was start alredy.");
	}

	private class PaintAllTheTime implements Runnable {
		private PaintAllTheTime() {}
		public void run() {
			while (server.time2End() > 0) {
				repaint();
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
			repaint();
		}
	}
	
	private class EatAllTheTime implements Runnable {
		private EatAllTheTime() {}
		public void run() {
			while (server.time2End() > 0) {
				double eps = 0.0005;
				arena.setFruits(server.getFruits());
				for (robot_data robot : arena.getRobots()) {
					for (fruit_data fruit : arena.getFruits()) {
						edge_data fruitEdge = Arena_Algo.getFruitEdge(arena.getGraph(), fruit);
						if (robot.getPos().distance3D(fruit.getPos()) < eps && 
								fruitEdge.getSrc() == robot.getSrc() ) {//&& fruitEdge.getDest() == robot.getDest()
							server.moveRobots();
							arena.setFruits(server.getFruits());
							robot_data[] robots = server.getRobots();
							for (robot_data Robot : robots) {
								if(Robot.getId() == robot.getId())
									robot.setSpeed(Robot.getSpeed());
							}
							arena.setRobots(robots);
						}
					}
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	private void RobotToNext(robot_data robot, flagBol flag) {
		RobotToNext RobotEating = new RobotToNext(robot, flag);
		Thread t = new Thread(RobotEating);
		t.start();
	}
	
	private class RobotToNext implements Runnable {
		robot_data robot;
		flagBol flag;

		private RobotToNext(robot_data robot, flagBol flag) {
			this.robot = robot;
			this.flag = flag;
		}

		@Override
		public void run() {
			edge_data edge = server.getGraph().getEdge(robot.getSrc(), robot.getDest());
			long startTime = (new Date()).getTime();
			double moveTime = (edge.getWeight()/robot.getSpeed())*1000;
			while ((new Date()).getTime() - startTime -100 < moveTime ) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			flag.setFlag(true);
		}
	}
	
	private void RobotMovement(robot_data robot) {
		RobotMovement RM = new RobotMovement(robot);
		Thread t = new Thread(RM);
		t.start();
	}
	
	private class RobotMovement implements Runnable {
		robot_data robot;

		public RobotMovement(robot_data robot) {
			this.robot = robot;
		}

		@Override
		public void run() {
			Point3D src = server.getGraph().getNode(robot.getSrc()).getLocation();
			Point3D dest = server.getGraph().getNode(robot.getDest()).getLocation();
			edge_data edge = server.getGraph().getEdge(robot.getSrc(), robot.getDest());
			double moveTime = (edge.getWeight()/robot.getSpeed())*1000;
			long startTime = (new Date()).getTime();
			boolean flag = true;
			long timePast;
			while (flag) {
				timePast = (new Date()).getTime() - startTime;
				for (robot_data robot : arena.getRobots()) {
					if(robot.getId() == this.robot.getId()) {
						robot.setPos(new Point3D(src.x()*((moveTime - timePast)/moveTime)+dest.x()*(timePast/moveTime),
								 src.y()*((moveTime - timePast)/moveTime)+dest.y()*(timePast/moveTime)));
					}
				}
				robot.setPos(new Point3D(src.x()*((moveTime - timePast)/moveTime)+dest.x()*(timePast/moveTime),
										 src.y()*((moveTime - timePast)/moveTime)+dest.y()*(timePast/moveTime)));
//				System.out.println(robot.getPos());
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(timePast >= moveTime)
					flag = false;
			}
		}
	}

	private class runGame implements Runnable {
		MyGameGUI GameGUI;

		private runGame (MyGameGUI myGameGUI) {
			this.GameGUI = myGameGUI;
		}

		@Override
		public void run() {
			robot_data[] robots = server.getRobots();
			fruit_data[] fruits = server.getFruits();
			arena.setRobots(robots);
			arena.setFruits(fruits);
			flagBol flag = new flagBol(false);
			int i = 0;
			while (GameGUI.server.time2End() > 0) {
				if(flag.getFlag()) {
					flag.setFlag(false);
					server.moveRobots();
					arena.setRobots(server.getRobots());
				}
				AutoGame AutoGame = new AutoGame( GameGUI.server, GameGUI.arena);
				for (robot_data robot : robots) {
					if (robot.getDest() == -1) {
						int dest = AutoGame.moveSimultan(robot, robots, fruits);
						robot.setDest(dest);
						server.RobotNextNode(robot.getId(), dest);
						RobotMovement(robot);
						RobotToNext(robot, flag);
					}
				}
				fruits = server.getFruits();
				robots = server.getRobots();
				arena.setFruits(fruits);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				i++;
				if(i%120 == 0) {
					server.moveRobots();
				}
//				if(i%20 == 0) {
//				for (robot_data robot : robots) {
//					System.out.println((int) (i/20)+") robot id: "+robot.getId()+", robot pos: "+robot.getPos() +", robot src: "+robot.getSrc() + 
//							", robot dest: "+robot.getDest() +" .");
//				}
//			}
			}
			System.out.println("End of game.");
			isEnded = true;
			GameGUI.repaint();
		}
	}
	
	private class flagBol {
		private boolean flag;
		private flagBol(boolean flag) {
			this.setFlag(flag);
		}
		private boolean getFlag() {
			return flag;
		}
		private void setFlag(boolean flag) {
			this.flag = flag;
		}
	}

	private class SetsRonots implements Runnable {
		MyGameGUI GameGUI;

		private SetsRonots (MyGameGUI myGameGUI) {
			this.GameGUI = myGameGUI;
		}

		private node_data addNode(Point3D tempPivot) {
			node_data Addnode = null; 
			boolean first = true;
			for (Iterator<node_data> iterator = GameGUI.arena.getGraph().getV().iterator(); iterator.hasNext();) {
				node_data node = iterator.next();
				Point3D ScaleLocation = GameGUI.ScaledLoc( node.getLocation());
				if (ScaleLocation.distance2D(tempPivot) < (nodeRad*4) ) {
					if (first) {
						Addnode = node;
						first = false;
					}
					else if (ScaleLocation.distance2D(tempPivot) < Addnode.getLocation().distance3D(tempPivot) ) {
						Addnode = node;
					}
				}
			}
			return Addnode;
		}

		@Override
		public void run() {
			System.out.println("Place "+GameGUI.server.robotsSize()+" robots");
			Point3D tempPivot = null;
			GameGUI.newPivot = false;
			for (int i = 0; i < GameGUI.server.robotsSize(); i++) {
				tempPivot = setPoint(GameGUI);
				System.out.println(tempPivot);
				node_data Addnode = null;
				Addnode = addNode(tempPivot);
				if (Addnode == null) {
					System.out.println("didnt secsed, try agina.");
					i--;
				} else {
					System.out.println("set Robot "+i);
					GameGUI.server.placeRobot(Addnode.getKey());
				}
			}
			System.out.println("The robot is set.");
			GameGUI.arena.setRobots(server.getRobots()); 
			isRobotSets = true;
			GameGUI.repaint();
		}
	}
	/**
	 * this function returns number of games that were played in the server with my id.
	 */
	private int NumberOfGames() {
		int i =0 ;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(SimpleDB.jdbcUrl, SimpleDB.jdbcUser, SimpleDB.jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID = "+MyId+";";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()){
				i++;
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return i ;
	}
	/**
	 * This function goes through the data base games 
	 * gets all my id games
	 * @return String of my best games results 
	 */
	private static String printResalts() {
		String[] arr = new String[24];
		String temp = "";
//		int arr_moves[] = {290,580,0,580,0,500,0,0,0,580,0,580,0,580,0,0,290,0,0,580,290,0,0,1140};
//		int arr_scores[] = {125,436,0,713,0,570,0,0,0,480,0,1050,0,310,0,0,235,0,0,250,200,0,0,1000};
		int[] max = new int[24];
		boolean[] firstTime = new boolean[24];
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(SimpleDB.jdbcUrl, SimpleDB.jdbcUser, SimpleDB.jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID= "+MyId+";";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()){
				for (int i = 0; i < 24; i++) {
					if(resultSet.getInt("levelID") == i){
						if(!firstTime[i] ){//&&  resultSet.getInt("moves") <= arr_moves[resultSet.getInt("levelID")]
							max[i] = resultSet.getInt("score");
							arr[i] = "Game: "+i+"     score:"+max[i]+"    Moves:"+resultSet.getInt("moves");
							my_res[i] = max[i];
						}
						else if(resultSet.getInt("score") > max[i] ) {/**&& resultSet.getInt("moves") <= arr_moves[resultSet.getInt("levelID")] && 
								resultSet.getInt("score") >= arr_scores[resultSet.getInt("levelID")]*/

							max[i] = resultSet.getInt("score");
							arr[i] = "Game: "+i+"     score:"+max[i]+"    Moves:"+resultSet.getInt("moves");
							my_res[i] = max[i];
						}
						firstTime[i] = true;
					}
				}
			}
			for (int i = 0; i < arr.length; i++){
				if(arr[i] != null)
					temp += arr[i]+"\n";
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}
}

