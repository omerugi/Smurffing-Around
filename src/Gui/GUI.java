package Gui;
import algorithms.Graph_Algo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler;

import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.SimpleDB;
import utils.resultwindow;

import java.text.DecimalFormat;


public class GUI extends JFrame implements ActionListener, MouseListener, Runnable, Observer {

	/////////////////////////////////////////////////////////////////
	//////////////////////////GUI_fields/////////////////////////////
	////////////////////////////////////////////////////////////////


	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private int Level; private int UserId; private int ResChoice;
	private Graphics2D g4; 
	/////////////////////////////////////////////////////////////////
	/////////////////////GUI_window_fields//////////////////////////
	////////////////////////////////////////////////////////////////

	private double min_x = 3000;		private double min_y = 3000;
	private double max_x = 0;			private double max_y = 0;

	//private int X_Axis = 1750;		private int Y_Axis = 870;


	private int defultx = 1800;	private int defulty = 900;


	public Collection<node_data> vertex;

	// contains all the edges by ID(src ver) and edge_data. 
	public Collection<edge_data> edges;
	private graph graph;
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
		((Observable) graph).addObserver(this);
	}

	public GUI(graph dg, game_service game,int gamechooser,int UserId)
	{
		initGUI();
		this.game = game; 
		this.vertex	= dg.getV();
		this.graph = dg;
		((Observable) graph).addObserver(this);
		this.Level= gamechooser;
		this.UserId=UserId;
	}

	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////   window settings  \\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	private void initGUI() {


		this.setSize(defultx,defulty);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon ImageIcon5 = new ImageIcon("data\\GameIcon.jpg");
		Image GameIcon  =	ImageIcon5.getImage();
		this.setIconImage(GameIcon);
		


		//creating menu bar//
		MenuBar menuBar	 = new MenuBar();
		Menu menu_file1	 = new Menu("Score Board");


		//adding the file section to the menu bar//
		menuBar.add(menu_file1);

		this.setMenuBar(menuBar);

		//creating a item in bar for short path
		MenuItem Score = new MenuItem("MyBestScores"); 
		Score.addActionListener(this);

		MenuItem Score1 = new MenuItem("WorldwideBestScores"); 
		Score1.addActionListener(this);

		//adding to menues:\\
		//file
		menu_file1.add(Score);
		menu_file1.add(Score1);


		//listen to the mouse\\
		this.addMouseListener(this);




	}

	private BufferedImage buff;
	private  Graphics2D g2;
	private BufferedImage buff3;

	JLabel background = null;
	
	//////////////////////////////////////////////Paint///////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void paint(Graphics g)
	{	

		/**
		 * double buffering section---
		 */
		if(buff == null || g2 == null || (this.WIDTH != defultx || this.HEIGHT != defulty )) {

			if((this.WIDTH != defultx || this.HEIGHT != defulty) && background != null ) {
				remove(background);
			}
			//set background
			setLayout(null);
			ImageIcon img = new ImageIcon("data\\background.jpg");
			background = new JLabel("", img ,JLabel.CENTER);
			background.setBounds(0,0, defultx-17, defulty-40);

			add(background);

			defultx = this.getWidth();
			defulty = this.getHeight();
			settings();
			buff = new BufferedImage(defultx, defulty, BufferedImage.TYPE_INT_ARGB);
			g2 = buff.createGraphics();
			super.paint(g2);
			paintgraph(g2);
		}

		g4 = (Graphics2D)g; 	g4.drawImage(buff,0,0,null);

		paintsmurfs();		paintGargamels();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////EndPaint//////////////////////////////////////////////////////////////

	/**
	 * this function used to reallocate the real y position to the window size position. 
	 * @param yv y coordinate
	 * @return
	 */
	private int reallocY(double yv) {

		double PresentOfScreen = (yv-min_y)/(max_y-min_y);
		yv= (defulty)-(PresentOfScreen*(defulty-500)+250);
		return (int) yv;
	}

	/**
	 * this function used to reallocate the real x position to the window size position. 
	 * @param xv x coordinate
	 * @return
	 */
	private int reallocX(double xv) {

		double PresentOfScreen = (xv-min_x)/(max_x-min_x);
		xv= (PresentOfScreen*(defultx-500)+250);
		return (int) xv;
	}
	/**
	 * this function sets the gui window scale.
	 */
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
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e){

		
		JFrame frame =null; 
		JOptionPane pan = new JOptionPane();
		
		if(e.getActionCommand()=="MyBestScores") {
			ResChoice=1;
			resultwindow reswind = new  resultwindow(this.Level, UserId, ResChoice);
			Thread t = new Thread(reswind); 
			t.start();
		}



		if(e.getActionCommand()=="WorldwideBestScores") {
			
			ResChoice=2;
			resultwindow reswind = new  resultwindow(this.Level, UserId, ResChoice);
			Thread t = new Thread(reswind); 
			t.start();
		}







	}

	public void run() {
		repaint();}
	@Override
	public void update(Observable o, Object arg) {
		repaint();run();}

	////////////////////////////////////////////Graph Paint////////////////////////////////////////////////////////////////////////
	private void paintgraph(Graphics g) {

		super.paintComponents(g);
		Graphics2D g4 = (Graphics2D) g;


		g.setColor(Color.blue);

		Iterator hit = vertex.iterator();
		while(hit.hasNext()) {
			//sets the line thickness
			g4.setStroke(new BasicStroke(5));
			//sets the txt size 
			float f=13.0f; // font size.
			g4.setFont(g4.getFont().deriveFont(f));


			/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			////////////////creating the vertex on Screen \\\\\\\\\\\\\\\\\\\\\\\\\
			node_data v = (node_data) hit.next();
			int i=0;

			int xv = reallocX(graph.getNode(v.getKey()).getLocation().x());
			int yv = reallocY(graph.getNode(v.getKey()).getLocation().y());
			/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			////////////////creating edges to the vertex \\\\\\\\\\\\\\\\\\\\\\\\\\
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
		/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		//////////////////////Painting the Vertexes\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		Iterator hit3 = vertex.iterator();
		while(hit3.hasNext()) {

			node_data v = (node_data) hit3.next();
			int i=0;
			int xv = reallocX(graph.getNode(v.getKey()).getLocation().x());
			int yv = reallocY(graph.getNode(v.getKey()).getLocation().y());
			ImageIcon SmurfsHouse = new ImageIcon("data\\SmurfsHouse.png");
			Image  SmurfsHouse1  = SmurfsHouse.getImage();
			g.drawImage(SmurfsHouse1, xv-25 ,yv-25,35,35, this);

			g.setColor(Color.BLACK);
			float f=20.0f; // font size.

			g4.setFont(g4.getFont().deriveFont(f));
			g.drawString(""+v.getKey(),xv-9,yv+3);

		}

		ImageIcon Score = new ImageIcon("data\\Score.png");
		Image  Score1  = Score.getImage();

		g.drawImage(Score1, (int)(defultx*0.0313), (int)(defulty*0.075),
				(int)(defulty*0.273),(int)(defulty*0.273), this);


		ImageIcon sd = new ImageIcon("data\\GameBoard.png");
		Image  sd1  = sd.getImage();
		g.drawImage(sd1, (int)(defultx*0.85),(int)(defulty*0.76),
				(int)(defulty*0.233),(int)(defulty*0.233), this);


		Graphics2D G = (Graphics2D)g;
		float f=26.0f; // font size.
		G.setFont(G.getFont().deriveFont(f));
		G.drawString("Time-Left    "+ game.timeToEnd()/1000 , (int)(defultx*0.857),(int)(defulty*0.842));

		G.drawString("Level:       "+ this.Level			, (int)(defultx*0.857),(int)(defulty*0.865));

		g4.setColor(Color.BLACK);
		f=35.0f; // font size.
		g4.setFont(g4.getFont().deriveFont(f));
		g4.drawString("Score:", (int)(defultx*0.070), (int)(defulty*0.24));




	}

	///////////////////////////////////////////////////Paint Graph End////////////////////////////////////////////////////////

	///////////////////////////////////////////////////Paint smurfs///////////////////////////////////////////////////////////
	private void paintsmurfs() {
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

				ImageIcon DardasAba = new ImageIcon("data\\Dardasaba.png");
				Image  DardasAba1  = DardasAba.getImage();
				ImageIcon Dardasit = new ImageIcon("data\\Dardasit.png"); 
				Image  Dardasit1   =	Dardasit.getImage();

				if(type == -1) {
					g4.drawImage(DardasAba1, reallocX(x)-30, reallocY(y)-30,50,50, this);
				}
				else {
					g4.drawImage(Dardasit1, reallocX(x)-30, reallocY(y)-30,50,50, this);
				}
			} catch (JSONException e) {e.printStackTrace();}
		}
	}
	///////////////////////////////////////////////////Paint smurfs End//////////////////////////////////////////////////////
	private void paintGargamels() {
		List<String> ArnoldSchwarzenegge = game.getRobots();
		int _score =0;
		for (int j = 0; j < ArnoldSchwarzenegge.size(); j++) {

			try {
				JSONObject obj = new JSONObject(ArnoldSchwarzenegge.get(j));
				JSONObject ff = obj.getJSONObject("Robot");

				String pos = ff.getString("pos");
				int rid = ff.getInt("id");
				int score = ff.getInt("value");
				_score += score;
				StringTokenizer st1 = new StringTokenizer(pos, ","); 

				double x = Double.parseDouble(st1.nextToken());
				double y = Double.parseDouble(st1.nextToken());

				ImageIcon Gargamel = new ImageIcon("data\\Gargamel.png");
				Image  Gargamel1  = Gargamel.getImage();
				g4.drawImage(Gargamel1, reallocX(x)-30, reallocY(y)-30,80,80, this);
				g4.setColor(Color.RED);
				g4.drawString(""+rid, reallocX(x)-20, reallocY(y)-20);
			} catch (JSONException e) {e.printStackTrace();}
		}

		g4.setColor(Color.RED);
		float f1=35.0f; // font size.
		g4.setFont(g4.getFont().deriveFont(f1));
		g4.drawString(""+_score,  (int)(defultx*0.070),  (int)(defulty*0.29));


	}

}





















