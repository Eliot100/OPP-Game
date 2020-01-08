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
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import gui.GUI_Window;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private Graph_Algo Graph_Algo ;
	private LinkedList<node_data> BoltedPath; 
	private static final Color edgeColor = new Color(80, 80, 80);
	private static final Color edgeTextColor = new Color(50, 50, 200);
	private static final int YUPborder = 50;
	private static final int border = 25;
	private static int minY;
	private static int minX;
	private static int maxY;
	private static int maxX;
	private static Scanner s = new Scanner(System.in);
	/**
	 * this is an example how to make the GUI_Window object.
	 */
	public static void main(String[] args) throws InterruptedException {
		DGraph d = DGraph.makeRandomGraph(6, 100);
		new GUI_Window(d);
	}
	/**1
	 * Constructor for empty GUI_Window.
	 */
	public MyGameGUI() {
		init();
	}
	/**
	 * Constructor for GUI_Window with a graph in it.
	 * @param g - the graph for the GUI_Window.
	 */
	public MyGameGUI( DGraph g) {
		init();
		Graph_Algo.graph = g ;
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
					minX = node.getLocation().ix();
					maxX = node.getLocation().ix();
					minY = node.getLocation().iy();
					maxY = node.getLocation().iy();
					b = false;
				} else {
					if (node.getLocation().ix() < minX ) {
						minX = node.getLocation().ix();
					} else if (node.getLocation().ix() > maxX) {
						maxX = node.getLocation().ix();
					}
					if (node.getLocation().iy() < minY ) {
						minY = node.getLocation().iy();
					} else if (node.getLocation().iy() > maxY) {
						maxY = node.getLocation().iy();
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
	private static double scale(double data, double r_min, double r_max,double t_min, double t_max){
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	private int ScaleY(double y) {
		return (int) scale(y, minY, maxY, YUPborder+border, this.get_Height()-border) ;
	}

	private int ScaleX(double x) {
		return (int) scale(x, minX, maxX, border, this.get_Width()-border) ;
	}

	private void drowEdge(edge_data edge, Graphics g) {
		Point3D sorce = Graph_Algo.graph.getNode(edge.getSrc()).getLocation();
		Point3D dest = Graph_Algo.graph.getNode(edge.getDest()).getLocation();
		g.setColor(edgeColor);
		g.drawLine(ScaleX(sorce.ix()), ScaleY(sorce.iy()), ScaleX(dest.ix()), ScaleY(dest.iy()));
		drowBoltedPath(BoltedPath, g);
		g.setColor(edgeColor);
		Point3D DirectionPoint = edgeDirectionPoint( sorce, dest);
		g.fillOval(ScaleX(DirectionPoint.x())-5, ScaleY(DirectionPoint.y())-5, 8, 8);
		g.setColor(edgeTextColor);  
		Point3D TextPoint = edgeTextPoint( sorce, dest);
		g.drawString(String.format("%.2f", edge.getWeight()), ScaleX(TextPoint.ix()), ScaleY(TextPoint.iy()));
	}
	private void drowNode(node_data node, Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillOval(ScaleX(node.getLocation().x())-9, ScaleY(node.getLocation().y())-12, 18, 18);
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
					g.drawLine(ScaleX(sorce.ix())-2+i, ScaleY(sorce.iy())-2+i, ScaleX(dest.ix())-2+i, ScaleY(dest.iy())-2+i);
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

		Menu menu1 = new Menu("DGraph Algorithems");
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		menuBar.add(menu1);
		String[] algoFunctions = {" Save DGraph to file ", " init DGraph from file ", " shortestPath ", " TSP "};
		for (int i = 0; i < algoFunctions.length; i++) {
			MenuItem Item = new MenuItem(algoFunctions[i]);
			Item.addActionListener(this);
			menu1.add(Item);
		}
		

		this.addMouseListener(this);
		this.setSize(800, 500);
		this.setVisible(true);
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		BoltedPath = null;
		this.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String str = event.getActionCommand();
		if (str.equals(" Save DGraph to file ")) {
			try {
				System.out.print("Enter the output file name : ");
				String fileName = s.next();
				this.Graph_Algo.save(fileName );
			} catch (Exception e) {
				System.out.println("fail to Save DGraph.");
			}
		} else if (str.equals(" init DGraph from file ")) {
			try {
				System.out.print("Enter the input file name : ");
				String fileName = s.next();
				this.Graph_Algo.init(fileName );
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to init DGraph.");
			}
		} else if (str.equals(" shortestPath ")) {
			try {
				System.out.print("Plase insert the key of the sorce node : ");
				int srcKey = s.nextInt();
				System.out.print("Plase insert the key of the destination node : ");
				int destKey =  s.nextInt();
				if (this.Graph_Algo.shortestPath(srcKey, destKey) != null) {
					BoltedPath = (LinkedList<node_data>) this.Graph_Algo.shortestPath(srcKey, destKey);
					this.repaint();
				} else { //exist
					System.out.println("I am sorry. Such path dosen't exist.");
				}
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to find the shortest path.");
			}
		} else if (str.equals(" TSP ")) {
			try {
				List<Integer> targets = new LinkedList<Integer>();
				System.out.print("Insert who meny node you want in the TSP : ");
				int nodeNum = s.nextInt();
				for (int i = 0; i < nodeNum; i++) {	
					System.out.print("Insert key of the node you want to add : ");
					int nodeKey = s.nextInt();
					targets.add(nodeKey);
				}
				BoltedPath = (LinkedList<node_data>) this.Graph_Algo.TSP(targets);
				if (BoltedPath == null) {
					System.out.println("Didn't meng to find such path.");
				}
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to find the shortest path.");
				e.printStackTrace();
			}
		} else if (str.equals(" New DGraph ")) {
			try {
				this.Graph_Algo.graph = new DGraph();
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to genret new DGraph.");
			}
		}  else if (str.equals(" Add Node ")) {
			try {
				System.out.print("Enter the x Value : ");
				int x = Integer.parseInt(s.next());
				System.out.print("Enter the y Value : ");
				int y = Integer.parseInt(s.next());
				this.Graph_Algo.graph.addNode( new Node(Graph_Algo.graph.newId(), new Point3D(x, y)));
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to add a node.");
			}
		}  else if (str.equals(" Remove Node ")) {
			try {
				System.out.print("Enter the key Value : ");
				int key = s.nextInt();
				Graph_Algo.graph.removeNode(key);
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to remove a node.");
			}
		}  else if (str.equals(" Add Edge ")) {
			try {
				System.out.print("Enter the suorce key : ");
				int src = s.nextInt();
				System.out.print("Enter the dest key : ");
				int dest = s.nextInt();
				System.out.print("Enter the edge wight : ");
				double w = s.nextDouble();
				Graph_Algo.graph.connect(src, dest, w);
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to add an edge.");
			}
		}  else if (str.equals(" Remove Edge ")) {
			try {
				System.out.print("Enter the suorce key : ");
				int src = s.nextInt();
				System.out.print("Enter the dest key : ");
				int dest = s.nextInt();
				Graph_Algo.graph.removeEdge(src, dest);
				this.repaint();
			} catch (Exception e) {
				System.out.println("fail to remove an edge.");
			}
		} 
	}
	
	// for now do nothing.
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent event) {}
	@Override
	public void mouseEntered(MouseEvent event) {}
	@Override
	public void mouseExited(MouseEvent event) {}
	@Override
	public void mouseReleased(MouseEvent event) {}

}
