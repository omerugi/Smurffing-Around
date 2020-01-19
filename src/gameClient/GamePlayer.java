package gameClient;

import elements.*;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import Gui.GUI;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import elements.Edge;
import elements.Fruit_Basket;
import utils.Point3D;
import utils.kmlmaker;

public class GamePlayer extends Observable implements Runnable {

	//////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////Class fields/////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	//fruit Basket object
	Fruit_Basket fruits;
	//Array list of the robots
	ArrayList<robot> robots_list;
	//Auto Game  - data structure of robots and paths in a hash, used for the robots paths.
	HashMap<Integer, Queue<Edge>> robot_final_dest1 = new HashMap<Integer, Queue<Edge>>();
	//Menual Game -  data structure of robots and paths in a hash, used for the robots paths.
	HashMap<Integer, Queue<node_data>> robot_final_dest = new HashMap<Integer, Queue<node_data>>();
	ArrayList<Double> final_dest_len = new ArrayList<Double>();
	//used for the user choices. 
	boolean flag = true;
	//KML file.
	kmlmaker kml;
	//the graph that will be used in the game
	DGraph dgraph = new DGraph();
	game_service game;
	JFrame frame = null; int Level_chooser = 0; int GameType_chooser = 0;
	String graph_string;


	public  void test1() {

		//////////////////////////////////////////////////Game set up/////////////////////////////////////////////////////////////////////////////////////////////////////// 
		GameType_GameLevel();		
		graph_string = game.getGraph();					 //Getting the level map(graph). 

		dgraph.init(graph_string);									      //initializing the graph		
		kml = kmlmaker.get_kmlmaker();		kml.add_kml(dgraph.to_kml()); //initialized KML file.
		initRobots(); 													  //creating the robots. 


		////Start Automatic Game/////
		if		(GameType_chooser == 0)		{	PlayAuto();   }//game is running...

		////Start   manual  Game/////
		else if	(GameType_chooser == 1) 	{	playMenual(); }//game is running...



		//---------------------------------------------------------------------------------|
		//will continued after the game is ended										   |
		//---------------------------------------------------------------------------------|

		Updating_kml();
		kml_save_framePOP();

		//Print the Game results//
		String results = game.toString();
		System.out.println("Game Over: "+results);
	}

	/**
	 * This method will initialized the robots from the server.
	 * Will open a robot object for each robot in robots_list 
	 * and place them next to the most valued fruits.
	 */
	private void initRobots() {

		//extracting robots information from the game server using Json.
		String info = game.toString();
		JSONObject line;
		try {

			line = new JSONObject(info);							
			JSONObject ttt = line.getJSONObject("GameServer");	//go to the Game server segment in the Json.
			int rs = ttt.getInt("robots");						//go to the    Robots   segment in the Json.		

			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();

			fruits = new Fruit_Basket(game, dgraph);
			robots_list = new ArrayList<robot>();		 //creating the list of the robots.
			kml.add_kml(robot.init_Kml());

			Fruit_Basket temp = new Fruit_Basket(game, dgraph);

			while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
			int src_node = 0;  // arbitrary node, you should start at one of the fruits

			for(int a = 0;a<rs;a++) {
				// will get an ID of node and place a robot on it.
				int src = temp.getMax().getFruitEdge().getSrc();
				game.addRobot(src);
				robot r = new robot(a);
				robots_list.add(r);
				final_dest_len.add(Double.MAX_VALUE);
			}

		}catch (JSONException e) {e.printStackTrace();}	

	}


	/**
	 * Main Method of the automatic game:
	 * 
	 * will play the game using algorithm for best score. 
	 * @param dgraph -the game graph.
	 * @param game 	 -game server.
	 */
	private  void PlayAuto() {

		//Open the Game window display (GUI)
		GUI gui = new GUI(dgraph,game,Level_chooser);	//open the GUI frame. 
		gui.setVisible(true);		

		game.startGame();			 		// Commend the game server to start the game. 

		int ind = 30; 		int delay = 0;
		long first = System.currentTimeMillis();
		while(  game.timeToEnd()/100!=0 ) {

			if((game.timeToEnd()/50)%1==0) {
				AutomoveRobots();
				gui.repaint();}

			if(System.currentTimeMillis() - first >= 10000){
				first = System.currentTimeMillis();
			}
		}
	}

	long update_time =System.currentTimeMillis();
	/**
	 * in charge of setting all the information of the robots in the game. 
	 * @param game
	 * @param dgraph
	 */
	private void updateRobots(game_service game, DGraph dgraph,  List<String> log) {

		//List<String> log = game.move();		 // getting the game log information. 

		String info = game.toString();		// getting the game information from the server. 
		JSONObject sline;

		//extract the number of the robots in the game. 
		int numberOfRobots = 0;
		try {sline = new JSONObject(info);
		JSONObject tttt = sline.getJSONObject("GameServer");
		numberOfRobots = tttt.getInt("robots"); //tell me how many robots there are in the game. 
		} catch (JSONException e1) {}

		//extract the robots meta data from the server.
		for(int i=0;i<numberOfRobots;i++) {
			String robot_json = log.get(i); // get the i'th robot string. 

			try {
				JSONObject line = new JSONObject(robot_json); // hold the specific robot from the string robot arr.
				JSONObject ttt = line.getJSONObject("Robot");
				//getting the robot info//
				int rid 		= ttt.getInt("id"); 			//robot unique Id	
				int src 		= ttt.getInt("src");			//robot source vertex Id
				int dest 		= ttt.getInt("dest");			//robot destination vertex ID 
				double speed 	= ttt.getDouble("speed");		//robot current speed

				//robot x,y coordinates// 
				String pos 		= ttt.getString("pos");		
				StringTokenizer st1 = new StringTokenizer(pos, ","); 

				double x 		= Double.parseDouble(st1.nextToken()); //robot x location
				double y 		= Double.parseDouble(st1.nextToken()); //robot y location


				//--> update the robot fields.
				robots_list.get(i).setDest(dest); 					
				robots_list.get(i).setSrc(src);
				robots_list.get(i).setSpeed(speed);
				robots_list.get(i).setLocation(new Point3D(x, y));

				//--> updating KML file
				if(System.currentTimeMillis() - update_time >= 90){
					update_time = System.currentTimeMillis();
					robots_list.get(i).add_kml_loc();
				}
			}catch (Exception e) {}
		}
	}
	/**
	 * This Method is in charge of the algorithm in which
	 *  the automatic player will move the robots. 
	 *  
	 *  the algorithm work as followed: 
	 *  
	 *  Using local path saver (hash <robot id, queue - 'the next nodes in the path'>
	 *  each iteration all the robots will calculate all the paths && costs to all the fruits. 
	 *  
	 *   each robot will save the paths in his structure using a minimum heap.
	 *   
	 *   the robots that the minimum cost to a same fruit will check who is less costly to go to this fruit. 
	 *   the robot in which the path to the fruit is cost more then the other robot will pop this path from his heap. 
	 *   
	 *   in the end each robot will hold a single fruit path. 
	 *   in the end the local hash will be updated to the new path chosen to the robot.
	 */
	private void AutomoveRobots() {

		List<String> log = game.move(); //check connection to server. 

		if(log!=null) {
			fruits.update(game, dgraph); 	//--> update fruit basket.
			updateRobots(game,dgraph,log);	//--> update robots


			// --> update robot heap
			for (int i = 0; i < robots_list.size(); i++) {
				robots_list.get(i).cleanHeap(); 				//clean the robot paths heap. 
				for (int j = 0; j < fruits.getLen(); j++) {		
					//Iterate all the fruits, for each robot build the heap of the paths to any fruit.  
					Object[] a = Graph_Algo.shortestPath_Dist(robots_list.get(i).getSrc(),fruits.getFruit(j).getFruitEdge().getSrc(),dgraph,fruits.getFruit(j),1);
					//add the path to the fruit to the robot heap.
					robots_list.get(i).addHeap(a,fruits.getFruit(j));
				}
			}
			//	Go over all the robots -> find which robot will go to which fruit. 
			//	the robot with the shortest path to fruit will win. and go to the particular fruit.
			for (int i = 0; i < robots_list.size(); i++) {

				for (int j = 0; j < robots_list.size(); j++) {
					if(i == j) continue;//same robot situation.
					else if(robots_list.get(i).getFruit() == robots_list.get(j).getFruit()) {

						//-->same value of paths (choose random)
						if(robots_list.get(i).getPath_len() == robots_list.get(j).getPath_len()) {
							robots_list.get(j).robot_Heap_pop();
						}
						//--> if robot i has a shorter path to the fruit remove the path to this fruit from robot j
						else if (robots_list.get(i).getPath_len() < robots_list.get(j).getPath_len()) {
							robots_list.get(j).robot_Heap_pop();
						}
						//--> if robot j has a shorter path to the fruit remove the path to this fruit from robot i
						else if (robots_list.get(i).getPath_len() > robots_list.get(j).getPath_len()) {
							robots_list.get(i).robot_Heap_pop();
							//if robot j has shorter path then robot i to the same fruit remove the path from robot i 
							//heap and check all over again with the other robots.
							i=-1; j= robots_list.size(); continue;
						}
					}

				}

			}

			//check the new path to the old path store in the class hash for each robot. 
			for (int i = 0; i < robots_list.size(); i++) {

				if(robot_final_dest1.get(i)== null) {			//if there is no path in the local hash path --> set the local path to the path in the robot heap.
					Queue<Edge> temp =  new LinkedList<Edge>(robots_list.get(i).getrobot_Heap().peek().getPath());
					robot_final_dest1.put(i,temp ); 
					final_dest_len.set(i,robots_list.get(i).getrobot_Heap().peek().getPathDist());

				}
				//if the local path is longer then the new path in the robot heap --> save in local the new faster path. 
				else if(final_dest_len.get(i)> robots_list.get(i).getrobot_Heap().peek().getPathDist()){			
					Queue<Edge> temp =  new LinkedList<Edge>(robots_list.get(i).getrobot_Heap().peek().getPath());
					robot_final_dest1.put(i,temp);
					final_dest_len.set(i,robots_list.get(i).getrobot_Heap().peek().getPathDist());
				}

			}
			HashMap<Integer, Queue<Edge>>robot_final = robot_final_dest1;
			//checks if robot reached his fruit. 
			for (int i = 0; i < robots_list.size(); i++) {
				
				if(robots_list.get(i).getDest() ==-1) {  //robot reached an edge destination  

					Edge e = robot_final_dest1.get(i).remove(); 
					if(robot_final_dest1.get(i).size() == 0) {  // if the queue of the local path of the robot is empty. 
						robot_final_dest1.put(i, null);			// update the path to null.
					}
					double temp = final_dest_len.get(i);		
					final_dest_len.set(i, temp-e.getWeight());  // if the queue of the local path isn't empty --> update the value of the path minus 
																// the weight of the previous edge the robot just passed.  
					int id_dest = e.getDest();					
					game.chooseNextEdge(robots_list.get(i).getId(), id_dest); // update the next move of the robot to the server. 
				}
			}	
		}
	}

/**
 * This method is in charge of the menual game operations. 
 * 
 * each time a robot is reached his destination a pop frame will appear and the next destination 
 * will be committed by the user.
 */
	private  void playMenual() {
		//open a GUI frame. 
		GUI gui = new GUI(dgraph,game,Level_chooser);
		gui.setVisible(true);
		
		game.startGame(); //start the game.

		while(game.isRunning()) { // while there is more time in the game move the robots.
			
			if((game.timeToEnd()/10)%2 ==0) {
			moveRobotsM();
			gui.repaint();
			}
		}

	}
/**
 * this method is in charge of the UI between the robots and the User. 
 * 
 */
	private void moveRobotsM() {

		List<String> log = game.move();
		
		int d_choosen = -1; 				//destination chosen by user.
		if(log!=null) {	
			
			updateRobots(game,dgraph,log);  //update --> robot info. 
			fruits.update(game, dgraph);	//update --> fruit basket. 

			for(int i=0;i<log.size();i++) {	//adding robots to the hash .	
				if(!robot_final_dest1.containsKey(i)) {
					robot_final_dest1.put(i, null); // if the robot does'nt have path in the hash just put null ;
				}

			}
			// go through all the robots in the hash.
			// if robot i does'nt have path open a new frame for user input. 
			for (int i = 0; i < robots_list.size(); i++) {
				if(robot_final_dest.get(i)==null) {
					//opening a frame dialog for the user <--
					while (flag==true) {
						try {
							String temp1= JOptionPane.showInputDialog(frame,"Enter Dest ID for ROBOT "+i);
							if(temp1.matches("\\d+")) {
								d_choosen =Integer.parseInt(temp1);	
								if(dgraph.getNode(d_choosen)!=null) {flag=false; continue;}
							}	System.out.println("dest Not Valid");
						}catch (Exception e) {System.out.println("Error");}		
					}flag=true;
					//after getting the user destination input the shortest path for this robot to the user
					//desire to a new queue.
					List<node_data> temp = Graph_Algo.shortestPath(robots_list.get(i).getSrc(), d_choosen, dgraph);
					Queue<node_data> e = new LinkedList<node_data>(temp);
					e.remove();
					e.add(dgraph.getNode(d_choosen));//get the node from the graph
					robot_final_dest.put(i, e); //put the path in the hash queue. 
				}
			}
			//advanced the robot in the path to the destination chosen by user 
			for (int i = 0; i < robots_list.size(); i++) {
				if(robots_list.get(i).getDest() ==-1) { //if the robot finished an edge
					
					node_data e = robot_final_dest.get(i).remove();	//remove the edge from the queue 
					
					if(robot_final_dest.get(i).size() == 0) { //if the queue is empty set the path to null
						robot_final_dest.put(i, null);
					}
					game.chooseNextEdge(robots_list.get(i).getId(), e.getKey()); //send to the server the robot next step. 
				}
			}
		}
	}

/**
 * thread method
	
 */
	@Override
	public void run() {
		test1();

	}
	
	/**
	 * Updating the kml file with all the robots kml's data.
	 */
	private void Updating_kml() {
		for (int i = 0; i < robots_list.size(); i++) {kml.add_kml(robots_list.get(i).stringTokml());} 
		kml.add_kml(fruits.end_kml());

	}

/*
 * pop a frame in the end of the game to save the game to a kml file. 
 */
	private void kml_save_framePOP() {
		Object[] possibilities21 = {"YES" , "NO"};
		String s4 =  (String)JOptionPane.showInputDialog(frame,"would you like to save?","Save Panel", JOptionPane.PLAIN_MESSAGE,null ,possibilities21,  "");
		boolean flag2=true;
		while(flag2) {
			if(s4 == "YES")   {
				FileDialog chooser = new FileDialog(frame, "Use a .kml extension", FileDialog.SAVE);
				chooser.setVisible(true);
				String filename =chooser.getDirectory()+chooser.getFile();
				if(filename.contains(".kml")) {	kml.save_kml(filename);	flag2 = false;}
				if (!filename.isEmpty()){kml.save_kml(filename+".kml"); flag2 = false;}
			}
			else flag2 =false;
		}
	}

/**
 * this method in charge of the UI choice of the game type :
 *  1.manual or automate.
 *  2.game Level.
 *  update the game <--
 */
	private void GameType_GameLevel() {
		boolean flag = true;
		while (flag) {
			/////////////////////Choose Game Type : Auto or Manual////////////////////////
			Object[] possibilities1 = {"Automatic" , "Menual"};
			String s1 =  (String)JOptionPane.showInputDialog(frame,"Play automatic or Menual","Auto or Menual", JOptionPane.PLAIN_MESSAGE,null ,possibilities1,  "");
			if(s1 == "Automatic") {GameType_chooser  = 0 ; }
			if(s1 == "Menual")    {GameType_chooser  = 1 ; }

			///////////////////////////////Choose level//////////////////////////////////
			Object[] possibilities2 = {"0","1", "2", "3","4","5", "6", "7","8","9", "10", "11","12","13", "14", "15","16","17", "18", "19","20","21", "22", "23"};
			String s2 = (String)JOptionPane.showInputDialog(frame,"pick game","Choose Game Level:", JOptionPane.PLAIN_MESSAGE,null ,possibilities2,  "");
			try {Level_chooser =Integer.parseInt(s2);flag = false;
			}catch (Exception e) {};
		}

		game = Game_Server.getServer(Level_chooser); 			 //peek the level from server. 
		String graph_string = game.getGraph();					 //Getting the level map(graph). 

	}
}
