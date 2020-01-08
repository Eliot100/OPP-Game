package gameClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFrame;

import com.sun.prism.Image;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import gui.GUI_Window;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -5660315569509224336L;
	private DGraph graph;
	private Robot[] robots;
	private Fruit[] fruits;
	private Color[] Colors = { 
			Color.GREEN,
			Color.ORANGE,
			Color.RED,
			Color.YELLOW
	};
	
	private static Image map;
	
	
	private Graph_Algo Graph_Algo ;
	private LinkedList<node_data> BoltedPath; 
	private static final Color edgeColor = new Color(80, 80, 80);
	private static final Color edgeTextColor = new Color(50, 50, 200);
	private static final int YUPborder = 25;
	private static final int border = 25;
	private static double minY;
	private static double minX;
	private static double maxY;
	private static double maxX;
	private static Scanner s = new Scanner(System.in);
//	/**
//	 * this is an example how to make the GUI_Window object.
//	 */
//	public static void main(String[] args) throws InterruptedException {
//		DGraph d = DGraph.makeRandomGraph(6, 200);
//		new GUI_Window(d);
//	}
	/**
	 * Constructor for GUI_Window with a graph in it.
	 * @param g - the graph for the GUI_Window.
	 */
	public MyGameGUI( DGraph g, Robot[] robots, Fruit[] fruits) {
		init(); 
		Graph_Algo.graph = g ;
		this.fruits = fruits;
		this.robots = robots;
		repaint();
	}
	/**
	 * This is the function that paint the GUI_Window. 
	 */
	public void paint(Graphics g)  {
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, get_Width(), get_Height());
		minX = 0;
		maxX = 0;
		minY = 0;
		maxY = 0;
		boolean b = true;
		if ( Graph_Algo.graph.nodeSize() != 0 ) {
			for ( Iterator<node_data> iterator = Graph_Algo.graph.getV().iterator() ; iterator.hasNext();) {
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
		for ( Iterator<node_data> iterator = this.Graph_Algo.graph.getV().iterator() ; iterator.hasNext();) {
			node_data node = iterator.next();
			if (Graph_Algo.graph.getE(node.getKey()) != null)
				for (Iterator<edge_data> iterator2 = Graph_Algo.graph.getE(node.getKey()).iterator() ; iterator2.hasNext();) {
					edge_data edge = iterator2.next();
					drowEdge(edge, g);
				}
		}
		for ( Iterator<node_data> iterator = this.Graph_Algo.graph.getV().iterator() ; iterator.hasNext();) {
			node_data node = iterator.next();
			drowNode(node, g);
		}
		// Once
		paintPic(g);
		
		// always
		paintFruit(g);
		paintRobot(g);
		paintTime(g);
		
	}

	private int ScaleY(double y) {
		return (int) scale(y, minY, maxY, YUPborder+border, this.get_Height()-border );
	}

	private int ScaleX(double x) {
		return (int) scale(x, minX, maxX, border, this.get_Width()-border );
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
		Point3D sorce = Graph_Algo.graph.getNode(edge.getSrc()).getLocation();
		Point3D dest = Graph_Algo.graph.getNode(edge.getDest()).getLocation();
		g.setColor(edgeColor);
		g.drawLine(ScaleX(sorce.x()), ScaleY(sorce.y()), ScaleX(dest.x()), ScaleY(dest.y()));
		drowBoltedPath(BoltedPath, g);
		g.setColor(edgeColor);
		Point3D DirectionPoint = edgeDirectionPoint( sorce, dest);
		g.fillOval(ScaleX(DirectionPoint.x())-5, ScaleY(DirectionPoint.y())-5, 8, 8);
		g.setColor(edgeTextColor);  
		Point3D TextPoint = edgeTextPoint( sorce, dest);
		g.drawString(String.format("%.2f", edge.getWeight()), ScaleX(TextPoint.x()), ScaleY(TextPoint.y()));
	}
	private void drowNode(node_data node, Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillOval(ScaleX(node.getLocation().x())-9, ScaleY(node.getLocation().y())-12, 22, 22);
		g.setColor(edgeColor);
		g.drawString(""+node.getKey(), ScaleX(node.getLocation().x()), ScaleY( node.getLocation().y()));
	}
	private void drowBoltedPath(LinkedList<node_data> BoltedPath, Graphics g) {
		g.setColor(Color.RED);
		if (BoltedPath != null) {
			Iterator<node_data> iterator = BoltedPath.iterator();
			node_data lastNode = iterator.next();
			for (; iterator.hasNext();) {
				node_data node = iterator.next();
				Point3D sorce = lastNode.getLocation();
				Point3D dest = node.getLocation();
				for (int i = 0; i < 4; i++) {
					g.drawLine(ScaleX(sorce.x())-2+i, ScaleY(sorce.y())-2+i, ScaleX(dest.x())-2+i, ScaleY(dest.y())-2+i);
				}
				lastNode = node;
			}
		}
	}

	private Point3D edgeDirectionPoint(Point3D srcPoint, Point3D destPoint) {
		return new Point3D((srcPoint.x()+7*destPoint.x())/8, (srcPoint.y()+7*destPoint.y())/8);
	}

	private Point3D edgeTextPoint( Point3D srcPoint, Point3D destPoint) {
		return new Point3D((srcPoint.x()*3+7*destPoint.x())/10, (srcPoint.y()*3+7*destPoint.y())/10);
	}
	
	private int get_Height() {
		return this.getHeight();
	}

	private int get_Width() {
		return this.getWidth();
	}

	private void init() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		this.setTitle(" GUI ");

		this.Graph_Algo = new Graph_Algo();
		this.Graph_Algo.graph = new DGraph();
//
//		Menu menu1 = new Menu("DGraph Algorithems");
//		Menu menu2 = new Menu(" DGraph Actions");
//		MenuBar menuBar = new MenuBar();
//		this.setMenuBar(menuBar);
//
//		menuBar.add(menu2);
//		menuBar.add(menu1);
//		String[] algoFunctions = {" Save DGraph to file ", " init DGraph from file ", " shortestPath ", " TSP "};
//		String[] DGraphFunctions = {" New DGraph ", " Add Node ", " Remove Node ", " Add Edge ", " Remove Edge "};
//		for (int i = 0; i < algoFunctions.length; i++) {
//			MenuItem Item = new MenuItem(algoFunctions[i]);
//			Item.addActionListener(this);
//			menu1.add(Item);
//		}
//		for (int i = 0; i < DGraphFunctions.length; i++) {
//			MenuItem Item = new MenuItem(DGraphFunctions[i]);
//			Item.addActionListener(this);
//			menu2.add(Item);
//		}

		this.addMouseListener(this);
		this.setSize(800, 500);
		this.setVisible(true);
	}
	
	public static void main(String bob[]) {
		
	}
	
	
	private void paintPic(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	private void paintTime(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	private void paintFruit(Graphics g) {
		for (int i = 0; i < fruits.length; i++) {
			g.setColor(Color.PINK);
			g.fillOval(ScaleX(fruits[i].getPos().x())-8, ScaleY(fruits[i].getPos().y())-8, 16, 16);
			g.setColor(Color.black);
			g.drawString(""+fruits[i].getType(), ScaleX(fruits[i].getPos().x())-4, ScaleY(fruits[i].getPos().y())+2);
		}
	}

	private void paintRobot(Graphics g) {
		for (int i = 0; i < robots.length; i++) {
			g.setColor(Colors[i%Colors.length]);
			g.fillOval(ScaleX(robots[i].getPos().x())-11, ScaleY(robots[i].getPos().y())-13, 25, 25);
			g.setColor(Color.black);
			g.drawString(""+robots[i].getId(), ScaleX(robots[i].getPos().x())-1, ScaleY(robots[i].getPos().y())+1);
			
		}
	}

//	private void init() {
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.setResizable(false);
//		this.setBackground(Color.WHITE);
//		this.setTitle(" GUI ");
//
//		this.addMouseListener(this);
//		this.setSize(800, 500);
//		this.setVisible(true);
//	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setFruits(Fruit[] fruits) {
		this.fruits = fruits;
	}

	public static Image getMap() {
		return map;
	}

	public static void setMap(Image map) {
		MyGameGUI.map = map;
	}

}
