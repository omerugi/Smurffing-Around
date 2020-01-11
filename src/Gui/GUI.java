package Gui;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.NodeV;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import utils.Point3D;

import java.text.DecimalFormat;


public class GUI extends JFrame implements ActionListener, MouseListener, Runnable, Observer {

	private static final Graphics Graphics = null;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	static JTextField textfield1, textfield2, textfield3;
	static JButton submit1,submit2;
	private static JFrame frame;
	boolean isConnected=false;
	double pathweight =-1;
	List<node_data> path;

	private Image i = null;
	Graphics doubleD; 

	private double min_x = 3000;		private double min_y = 3000;
	private double max_x = 0;			private double max_y = 0;

	private int X_Axis = 1750;		private int Y_Axis = 870;


	private int defultx = 2000;	private int defulty = 1050;


	private static final int DEFAULT_SIZE = 500;
	private static double penRadius;

	int action=0;

	//holds the vertex points.\\ 
	//ArrayList<NodeV> p_list = new ArrayList<NodeV>(); 
	public Collection<node_data> vertex;

	// contains all the edges by ID(src ver) and edge_data. 
	public Collection<edge_data> edges;
	private graph graph;
	Graph_Algo G = new Graph_Algo();
	private game_service game;


	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////   Constructor  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public GUI() {}
	public GUI(graph dg)
	{
		initGUI();
		this.vertex	= dg.getV();
		this.graph = dg;
		G.init(this.graph);
		((Observable) graph).addObserver(this);
	}

	public GUI(graph dg, game_service game)
	{
		initGUI();
		this.game = game; 
		this.vertex	= dg.getV();
		this.graph = dg;
		G.init(this.graph);
		((Observable) graph).addObserver(this);
	}


	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////   window settings  \\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	private void initGUI() {

		this.setSize(defultx,defulty);
		this.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//creating menu bar//
		MenuBar menuBar	 = new MenuBar();
		Menu menu_file1	 = new Menu("file");


		//adding the file section to the menu bar//
		menuBar.add(menu_file1);

		this.setMenuBar(menuBar);

		//creating a item in bar for short path
		MenuItem Save = new MenuItem("Save"); 
		Save.addActionListener(this);

		//creating a item in bar for short path
		MenuItem Load = new MenuItem("Load"); 
		Load.addActionListener(this);

		//creating a item in bar for clean graph
		MenuItem clean_all = new MenuItem("clean all"); 
		clean_all.addActionListener(this);

		//adding to menues:\\
		//file
		menu_file1.add(Save);
		menu_file1.add(Load);
		menu_file1.add(clean_all);


		//listen to the mouse\\
		this.addMouseListener(this);
		
		


	}

	private BufferedImage buff;
	private  Graphics2D g2;
	
	
	
	public void paint(Graphics g)
	{	
		//call paint func\\
		if(buff == null || g2 == null ) {
			settings();
			buff = new BufferedImage(defultx, defulty, BufferedImage.TYPE_INT_ARGB);
			g2 = buff.createGraphics();
			super.paint(g2);
			paintgraph(g2);
		}
		
		Graphics g2_comp = (Graphics2D)g;
		g2_comp.drawImage(buff,0,0,null);
		paintfruits(g2_comp);
		paintrobots(g2_comp);
		
	}


	private int reallocY(double yv) {

		double PresentOfScreen = (yv-min_y)/(max_y-min_y);
		yv= (PresentOfScreen*(defulty-500)+250);
		return (int) yv;
	}

	private int reallocX(double xv) {

		double PresentOfScreen = (xv-min_x)/(max_x-min_x);
		xv= (PresentOfScreen*(defultx-500)+250);
		return (int) xv;
	}

	private void settings() {

		Iterator hit = vertex.iterator();
		while(hit.hasNext()) {

			node_data v = (node_data) hit.next(); 

			double xv=graph.getNode(v.getKey()).getLocation().x();
			double yv=graph.getNode(v.getKey()).getLocation().y();

			if(xv > max_x) max_x =	xv;			if(yv > max_y) max_y =	yv;
			if(xv < min_x) min_x =	xv;			if(yv < min_y) min_y =	yv;
		}

	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println(x+"    "+y);

	}


	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) 
	{

		///////////////////////////////////////////////
		/////////////Saving Command\\\\\\\\\\\\\\\\\\\\\
		////////////////////////////////////////////////


		if(e.getActionCommand() == "Save") {
			action= 0;
			repaint();
			FileDialog chooser = new FileDialog(frame, "Use a .txt extension", FileDialog.SAVE);
			chooser.setVisible(true);
			String filename =chooser.getDirectory()+chooser.getFile();
			G.save(filename);
			System.out.println(G);

		}


		///////////////////////////////////////////////
		/////////////ShortPath Command\\\\\\\\\\\\\\\\\
		////////////////////////////////////////////////


		else if(e.getActionCommand() == "Short Path") {

			action= 0;
			repaint();
			JFrame f = null; 
			try {
				int src=0, Dest=0;
				boolean flag = true; 	
				String temp1="",temp2=""; 

				while (flag==true) {

					temp1 	= JOptionPane.showInputDialog(f,"Enter src ID: ");
					if(temp1.matches("\\d+")) {
						src =Integer.parseInt(temp1);	
						if(graph.getNode(src)!=null) {flag=false; continue;}
					}	System.out.println("src Not Valid");
				}flag =true;


				while (flag==true) {

					temp1 	= JOptionPane.showInputDialog(f,"Enter Dest ID: ");
					if(temp1.matches("\\d+")) {
						Dest =Integer.parseInt(temp1);	
						if(graph.getNode(Dest)!=null) {flag=false; continue;}
					}	System.out.println("dest Not Valid");
				}flag =true;

				graph_algorithms a = new Graph_Algo();
				a.init(graph);
				path =a.shortestPath(src,Dest);
				pathweight = a.shortestPathDist(src, Dest);
				if(path==null) {System.out.println("null");}
				else {		action = 1;
				repaint();}
			}catch (Exception es) {}
		}



		///////////////////////////////////////////////
		//////////////////TSP Command\\\\\\\\\\\\\\\\\\\
		////////////////////////////////////////////////



		else if(e.getActionCommand() == ("TSP")) {
			action= 0;
			repaint();
			JFrame f = null;  
			graph_algorithms a = new Graph_Algo();
			a.init(graph);
			boolean flag =true; String temp1="";
			int src=0,Dest=0;int size_of_tr=0;
			ArrayList<Integer> targets = new ArrayList<Integer>(); ;			

			try {
				//input targets\\
				while(flag==true) {

					if(targets.size()==vertex.size()) {flag=false; continue;}
					temp1 = JOptionPane.showInputDialog(f,"Enter targets src ID (enter: Done when finish)");
					if(temp1.equals("done") || temp1.equals("Done") ) {flag=false; continue;}
					else {
						if(temp1.matches("\\d+")) {
							int targ = Integer.parseInt(temp1);
							//containe allready
							if(targets.contains(targ)) {System.out.println("This vertex allready in the search path");}
							//isvalid < vertex.size
							else if(graph.getNode(targ)==null) {System.out.println("This vertex doesnt exists");}
							//adding the target
							else {	targets.add(targ); }
						}
					}
				}

				//copy the path for paint\\
				path = null;
				path = a.TSP(targets);
				System.out.println(path);
				//set action;
				action=3;
				repaint();
			}
			catch (Exception es) {}
		}


		///////////////////////////////////////////////
		//////////////// Clean All   \\\\\\\\\\\\\\\\\\\
		////////////////////////////////////////////////

		else if(e.getActionCommand() == ("clean all")) {

			action=0; 
			this.graph = null;
			repaint();
		}

	}

	@Override
	public void run() {
		repaint();

	}
	
	
	@Override
	public void update(Observable o, Object arg) {

		repaint();
		run();
	}
	
	
	private void paintgraph(Graphics g) {
		g.setColor(Color.blue);
		Iterator hit = vertex.iterator();
		while(hit.hasNext()) {

			/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			////////////////creating the vertex on Screen \\\\\\\\\\\\\\\\\\\\\\\\\
			//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

			node_data v = (node_data) hit.next(); 
			g.setColor(Color.BLUE);

			int i=0;


			//int xv=graph.getNode(v.getKey()).getLocation().x();
			//int yv=graph.getNode(v.getKey()).getLocation().y();

			int xv = reallocX(graph.getNode(v.getKey()).getLocation().x());
			int yv = reallocY(graph.getNode(v.getKey()).getLocation().y());

			g.drawRect(xv-10,yv-10,20,20);

			g.drawString(""+v.getKey(),xv-2,yv+5);


			/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			////////////////creating edges to the vertex \\\\\\\\\\\\\\\\\\\\\\\\\\
			//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

			edges = graph.getE(v.getKey());
			if(edges == null) {continue;}

			//go over the edges that come out of the specific vertex\\ 
			Iterator hit2 = edges.iterator();

			while(hit2.hasNext()) {


				g.setColor(Color.red);

				edge_data dest = (edge_data) hit2.next();
				//From\\

				int x1 = reallocX(graph.getNode(v.getKey()).getLocation().x());
				int y1 = reallocY(graph.getNode(v.getKey()).getLocation().y());
				//To\\

				int x2 = reallocX(graph.getNode(dest.getDest()).getLocation().x());
				int y2 = reallocY(graph.getNode(dest.getDest()).getLocation().y());

				//draw the line between the vertexes\\ 
				g.drawLine(x1, y1,x2,y2);
				g.setColor(Color.YELLOW);
				//Draw the circle indicates the direction of the edge,
				//by mark a oval in the 3/4 the line next to the dest vertex. 
				g.fillOval(((x1*1)/5)+((x2*4)/5),((y1*1)/5)+((y2*4)/5) , 15, 15);

				double w = dest.getWeight();

				g.setColor(Color.MAGENTA);

				g.drawString(df2.format(w),((x1*1)/3)+((x2*2)/3),((y1*1)/3)+((y2*2)/3));

			}
		}

	}
	
	
	
	private void paintfruits(Graphics g) {
		List<String> Fruit = game.getFruits();

		for (int j = 0; j < Fruit.size(); j++) {

			try {

				JSONObject obj = new JSONObject(Fruit.get(j));
				JSONObject ff = obj.getJSONObject("Fruit");
				double value = ff.getDouble("value");
				double type = ff.getDouble("type");
				String pos = ff.getString("pos");

				StringTokenizer st1 = new StringTokenizer(pos, ","); 

				double x = Double.parseDouble(st1.nextToken());
				double y = Double.parseDouble(st1.nextToken());

				g.setColor(Color.BLUE);
				g.fillOval(reallocX(x), reallocY(y), 20, 20);

			} catch (JSONException e) {e.printStackTrace();}


		}
		
	}


	private void paintrobots(Graphics g) {
		List<String> ArnoldSchwarzenegge = game.getRobots();
		for (int j = 0; j < ArnoldSchwarzenegge.size(); j++) {

			try {

				JSONObject obj = new JSONObject(ArnoldSchwarzenegge.get(j));
				JSONObject ff = obj.getJSONObject("Robot");

				String pos = ff.getString("pos");

				StringTokenizer st1 = new StringTokenizer(pos, ","); 

				double x = Double.parseDouble(st1.nextToken());
				double y = Double.parseDouble(st1.nextToken());

				g.setColor(Color.BLACK);
				g.fillOval(reallocX(x), reallocY(y), 20, 20);

			} catch (JSONException e) {e.printStackTrace();}


		}
		
	}


}





















