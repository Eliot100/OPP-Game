package gui;

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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;

import com.sun.prism.Image;

import Server.Game_Server;
import Server.game_service;
import dataStructure.Arena;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameClient.MainGameClient;
import utils.Point3D;

public class manualGameGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -5660315569509224336L;
	private game_service game;
	private Arena arena;
	private int robot2Place;
	private Color[] Colors = { 
			Color.GREEN,
			Color.ORANGE,
			Color.RED,
			Color.YELLOW
	};

	private static Image map;

	private static final Color edgeColor = new Color(80, 80, 80);
	private static final Color edgeTextColor = new Color(50, 50, 200);
	private static final int YUPborder = 50;
	private static final int border = 25;
	private static final double nodeRad = 6;
	private static boolean isRobotSets;
	private static double minY;
	private static double minX;
	private static double maxY;
	private static double maxX;
	/**
	 * this is an example how to make the GUI_Window object.
	 */
	public static void main(String[] args) {
		new manualGameGUI();
	}
	/**
	 * Constructor for GUI_Window with a graph in it.
	 */
	public manualGameGUI() {
		init(); 
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
		if (arena != null ) {
			if ( arena.getGraph().nodeSize() != 0 ) {
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

			paintPic(g);
			paintFruit(g);
			paintRobot(g);
			paintTime(g);
		}
	}

	private int ScaleY(double y) {
		return upsideDownY((int) scale(y, minY, maxY, YUPborder+border, this.get_Height()-border ));
	}

	private int upsideDownY(int y) {
		int midY = (int) ((this.get_Height()-YUPborder)/2)+YUPborder;
		return (int) ((y - midY)*(-1)+midY);
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
		g.fillOval(ScaleX(node.getLocation().x())-9, ScaleY(node.getLocation().y())-12, 22, 22);
		g.setColor(edgeColor);
		g.drawString(""+node.getKey(), ScaleX(node.getLocation().x()), ScaleY( node.getLocation().y()));
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
		this.setResizable(true);
		this.setBackground(Color.WHITE);
		this.setTitle(" GUI ");

		//
		Menu menu1 = new Menu(" Game Options ");
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		menuBar.add(menu1);
		String[] gameFunctions = {" init stage "," start game ", " set robots "};
		for (int i = 0; i < gameFunctions.length; i++) {
			MenuItem Item = new MenuItem(gameFunctions[i]);
			Item.addActionListener(this);
			menu1.add(Item);
		}

		this.addMouseListener(this);
		this.setSize(800, 500);
		this.setVisible(true);
	}

	private void paintPic(Graphics g) {
		// TODO Auto-generated method stub

	}

	private void paintTime(Graphics g) {
		// TODO Auto-generated method stub

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
		int x0 = arg0.getX();
		int y0 = arg0.getY();
		Point3D temp = new Point3D(x0, y0);
		for (Iterator<node_data> iterator = arena.getGraph().getV().iterator(); iterator.hasNext();) {
			node_data node = iterator.next();
			Point3D nodeScaledLoc = ScaledLoc(node.getLocation());
			if(temp.distance2D(nodeScaledLoc) < nodeRad) {
				MainGameClient.placeRobot(game, node.getKey());
				robot2Place--;
				System.out.println(" place Robot ");
			}
			else 
				System.out.println(" dont place Robot ");
		} 
	}

	
	private Point3D ScaledLoc(Point3D location) {
		return new Point3D(ScaleX(location.x()), upsideDownY(ScaleY(location.y())));
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
		switch (arg0.getActionCommand()) {
		case " init stage " :
			String userAns = JOptionPane.showInputDialog("Enter the stage you want to play." );
			try {
				int stage = Integer.parseInt(userAns);
				if (stage < 0 || stage > 23) {
					JOptionPane.showMessageDialog(null, "You didnt a valid stage.");
				} else {
					game = Game_Server.getServer(stage);
				}
				arena = new Arena(game);
				repaint();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "You didnt enter an number.");
			} 
			break;
		case " set robots " :
			SetsRonots SR = new SetsRonots(this);
			Thread t1 = new Thread(SR);
			t1.start();
			break;
		case " start game " :
			//				PaymentService paymentService1 = new PaymentService();
			//				Thread t1 = new Thread(paymentService1);
			//				t1.start();

			break;
		}
		
	}


	public static Image getMap() {
		return map;
	}

	public static void setMap(Image map) {
		manualGameGUI.map = map;
	}

}
