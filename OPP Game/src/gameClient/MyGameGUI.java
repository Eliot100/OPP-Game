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
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import dataStructure.Arena;
import dataStructure.edge_data;
import dataStructure.fruit_data;
import dataStructure.node_data;
import dataStructure.robot_data;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1;
	private game_support gameSupport;
	private Arena arena;
	public Point3D pivot;
	private BufferedImage bi;
	private Insets Insets;
	private AutoGame AutoGame;
	
	private Color[] Colors = { 
			Color.GREEN,
			Color.ORANGE,
			Color.RED,
			Color.YELLOW
	};
	public boolean newPivot;

	private static final Color edgeColor = new Color(80, 80, 80);
	private static final Color edgeTextColor = new Color(50, 50, 200);
	private static final int YUPborder = 35;
	private static final int border = 35;
	private int nodeRad = 10;
	private boolean isGameBegin;
	private boolean firstPaint;
	private boolean isAuto;
	private static boolean isRobotSets;
	private static double minY;
	private static double minX;
	private static double maxY;
	private static double maxX;
	/**
	 * this is an example how to make the GUI_Window object.
	 */
	public static void main(String[] args) {
		new MyGameGUI();
	}
	/**
	 * Constructor for GUI_Window with a graph in it.
	 */
	public MyGameGUI() {
		String userAns = JOptionPane.showInputDialog("Enter A for Aotomatic gui, else the window will be manual." );
		if (userAns.equals("A")) {
			isAuto = true;
		}
		firstPaint = true;
		init(); 
		bi = new BufferedImage(1500, 1000, BufferedImage.TYPE_INT_RGB );
		Insets = getInsets();
	}
	/**
	 * @return this game_support.
	 */
	public game_support getSupport() {
		return gameSupport;
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
		}
		g.drawImage(bi, Insets.left, Insets.top, this);
		} else firstPaint = false;
	}

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
	private int ScaleY(double y) {
		return upsideDownY((int) scale(y, minY, maxY, YUPborder+border , this.getHeight()-border ));
	}

	private int upsideDownY(int y) {
		int midY = (int) ((this.getHeight())/2);
		return (int) ((y - midY)*(-1)+midY);
	}

	private int ScaleX(double x) {
		return (int) scale(x, minX, maxX, border, this.getWidth()-border );
	}

	private Point3D ScaledLoc(Point3D location) {
		return new Point3D(ScaleX(location.x()), ScaleY(location.y()));
	}

	/**
	 * 
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
	private void drowNode(node_data node, Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillOval(ScaleX(node.getLocation().x())-9, ScaleY(node.getLocation().y())-(int)(nodeRad/2), (int)nodeRad, (int)nodeRad);
		g.setColor(edgeColor);
		g.drawString(""+node.getKey(), ScaleX(node.getLocation().x()), ScaleY( node.getLocation().y()));
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

		//
		Menu menu1 = new Menu(" Game Options ");
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		menuBar.add(menu1); 
		if (isAuto) {
			String[] gameFunctions = {" init stage ", " start game "}; 
			for (int i = 0; i < gameFunctions.length; i++) {
				MenuItem Item = new MenuItem(gameFunctions[i]);
				Item.addActionListener(this);
				menu1.add(Item);
			}
		} else {
			String[] gameFunctions = {" init stage ", " start game ", " move robot "}; 
			for (int i = 0; i < gameFunctions.length; i++) {
				MenuItem Item = new MenuItem(gameFunctions[i]);
				Item.addActionListener(this);
				menu1.add(Item);
			}
		}
		

		this.addMouseListener(this);
		this.setSize(800, 500);
		this.setVisible(true);
	}

	private void paintTime(Graphics g) {
		if (arena != null && gameSupport.time2End() != -1) {
			g.drawString("time to end : "+String.format("%.2f", (double) gameSupport.time2End()/360), 20, 20);
		} else if (arena != null) {
			g.drawString("time to end : ", 20, 20);
		}
	}
	
	private void paintScore(Graphics g) {
		if (arena != null ) {
			g.drawString("Score : "+String.format("%.2f", (double) gameSupport.getGrade()), getWidth()/2, 20);
		} 
	}

	private void paintFruit(Graphics g) {
		for (int i = 0; i < arena.getFruits().length; i++) {
			g.setColor(Color.PINK);
			g.fillOval(ScaleX(arena.getFruits()[i].getPos().x())-8, ScaleY(arena.getFruits()[i].getPos().y())-8, 16, 16);
			g.setColor(Color.black);
			g.drawString(""+arena.getFruits()[i].getType(), ScaleX(arena.getFruits()[i].getPos().x())-4, ScaleY(arena.getFruits()[i].getPos().y())+2);
		}
	}

	private void paintRobot(Graphics g) {
		if( isRobotSets)
			for (int i = 0; i < arena.getRobots().length; i++) {
				g.setColor(Colors[i%Colors.length]);
				g.fillOval(ScaleX(arena.getRobots()[i].getPos().x())-11, ScaleY(arena.getRobots()[i].getPos().y())-13, 25, 25);
				g.setColor(Color.black);
				g.drawString(""+arena.getRobots()[i].getId(), ScaleX(arena.getRobots()[i].getPos().x())-1, ScaleY(arena.getRobots()[i].getPos().y())+1);
			}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x0 = arg0.getX();
		int y0 = arg0.getY();
		pivot = new Point3D(x0, y0-YUPborder);
		newPivot = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case " init stage " :
			String userAns = JOptionPane.showInputDialog("Enter the stage you want to play." );
			isRobotSets = false;
			isGameBegin = false;
			try {
				int stage = Integer.parseInt(userAns);
				if (stage < 0 || stage > 23) {
					JOptionPane.showMessageDialog(null, "You didnt a valid stage.");
				} else {
					game_service game = Game_Server.getServer(stage);
					gameSupport = new MainGameClient(game);
					arena = new Arena(gameSupport.getGraph(), gameSupport.getFruits(), gameSupport.getRobots());
					RefreshMinMaxFrame();
					repaint();

					if (isAuto) {
						AutoGame = new AutoGame(gameSupport, arena);
						AutoGame.setRobots();
						isRobotSets = true;
						this.arena.setRobots(gameSupport.getRobots());
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
			break;
		case " start game " :
			if (!isGameBegin) {
				isGameBegin = true;
				System.out.println("The game as begin .");
				runGame RG = new runGame(this);
				Thread t1 = new Thread(RG);
				t1.start();
				gameSupport.startGame();
				if(isAuto)
					AutoGame.moveRobots();
			}
			else 
				System.out.println("The game was start alredy.");
			break;
//		case " stop game " : // Open game to cheats .
//			if (isGameBegin) { 
//				gameSupport.stopGame();
//				isGameBegin = false;
//			}
//			break;
		case " move robot " :
			if (gameSupport.isRunning()) {
				moveRobot MR = new moveRobot(this);
				Thread t2 = new Thread(MR);
				t2.start();
			}
			break;
		}
		
	}
	
	private class moveRobot implements Runnable {
		MyGameGUI GameGUI;
		
		public moveRobot (MyGameGUI myGameGUI) {
			this.GameGUI = myGameGUI;
		}
		
		@Override
		public void run() {
			GameGUI.newPivot = false;
			
			robot_data theRobot = setRobot();
			System.out.println("loc robot : "+theRobot.getPos());
			
			node_data target = setTarget(GameGUI, theRobot);
			System.out.println("loc target : "+target.getLocation());
			
			theRobot.setDest(target.getKey());
			GameGUI.gameSupport.RobotNextNode(theRobot.getId(), target.getKey());
			GameGUI.gameSupport.moveRobots();
		}

		private robot_data setRobot() {
			robot_data[] robots = arena.getRobots();
			robot_data theRobot = null;
			Point3D pivotLoc = setPoint(GameGUI);
			double minDis = 0;
			for (int i = 0; i < robots.length; i++) { 
				Point3D ScaleRobotLoc = GameGUI.ScaledLoc( robots[i].getPos());
				if (i == 0) {
					theRobot = robots[i];
					minDis = ScaleRobotLoc.distance2D(pivotLoc);
				} else if( ScaleRobotLoc.distance2D(pivotLoc) < minDis ) {
					theRobot = robots[i];
					minDis = ScaleRobotLoc.distance2D(pivotLoc);
				}
			}
			return theRobot;
		}

		private node_data setTarget(MyGameGUI gameGUI, robot_data theRobot) {
			node_data robotNode = arena.getGraph().getNode(theRobot.getSrc());
			while (true) {
				Point3D targetLoc = setPoint(GameGUI);
				for (edge_data edge : arena.getGraph().getE(robotNode.getKey())) {
					Point3D ScaleLocation = GameGUI.ScaledLoc( arena.getGraph().getNode(edge.getDest()).getLocation());
					if (ScaleLocation.distance2D(targetLoc) < (GameGUI.nodeRad*4)) {
						return arena.getGraph().getNode(edge.getDest());
					}
				}
			}
		}
	}
	
	private class runGame implements Runnable {
		MyGameGUI GameGUI;
		
		public runGame (MyGameGUI myGameGUI) {
			this.GameGUI = myGameGUI;
		}
		
		@Override
		public void run() {
			while (GameGUI.gameSupport.time2End() > 0) {
				gameSupport.moveRobots();
				if (toRepaint(arena.getRobots(), arena.getFruits())){
					arena.setFruits(GameGUI.gameSupport.getFruits());
					arena.setRobots(GameGUI.gameSupport.getRobots());
					GameGUI.repaint();
					try {
						GameGUI.repaint();
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				} else {
					try {
						repaint();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			System.out.println("End of game.");
		}

		private boolean toRepaint(robot_data[] robots2, fruit_data[] fruits2) {
			robot_data[] robots = gameSupport.getRobots();
			fruit_data[] fruits = gameSupport.getFruits();
			try {
			for (int i = 0; i < fruits.length; i++) {
				if(!fruits[i].getPos().equals(fruits2[i].getPos()) )
					return true;
			}
			for (int i = 0; i < robots.length; i++) {
				if(!robots[i].getPos().equals(robots2[i].getPos()) )
					return true;
			}
			} catch (Exception e) {}
			return false;
		}

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
	
	private class SetsRonots implements Runnable {
		MyGameGUI GameGUI;
		
		public SetsRonots (MyGameGUI myGameGUI) {
			this.GameGUI = myGameGUI;
		}
		
		private node_data addNode(Point3D tempPivot, boolean first) {
			node_data Addnode = null;
			for (Iterator<node_data> iterator = GameGUI.arena.getGraph().getV().iterator(); iterator.hasNext();) {
				node_data node = iterator.next();
				Point3D ScaleLocation = GameGUI.ScaledLoc( node.getLocation());
				if (ScaleLocation.distance2D(tempPivot) < (GameGUI.nodeRad*4) ) {
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
			System.out.println("Place "+GameGUI.getSupport().robotsSize()+" robots");
			Point3D tempPivot = null;
			GameGUI.newPivot = false;
			for (int i = 0; i < GameGUI.getSupport().robotsSize(); i++) {
				tempPivot = setPoint(GameGUI);
				System.out.println(tempPivot);
				node_data Addnode = null;
				boolean first = true;
				Addnode = this.addNode(tempPivot, first);
				if (Addnode == null) {
					System.out.println("didnt secsed, try agina.");
					i--;
				} else {
					System.out.println("set Robot "+i);
					GameGUI.getSupport().placeRobot(Addnode.getKey());
				}
			}
			System.out.println("The robot is set.");
			GameGUI.arena.setRobots(gameSupport.getRobots()); 
			isRobotSets = true;
			GameGUI.repaint();
		}
	}

}
