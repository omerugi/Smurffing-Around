package gameClient;

import elements.*;
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
import utils.kmlmaker;

/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 49-50)
 * 5. Add a set of robots (line 52-53) // note: in general a list of robots should be added
 * 6. Starts game (line 57)
 * 7. Main loop (should be a thread) (lines 59-60)
 * 8. move the robot along the current edge (line 74)
 * 9. direct to the next edge (if on a node) (line 87-88)
 * 10. prints the game results (after "game over"): (line 63)
 *  
 * @author boaz.benmoshe
 *
 */
public class SimpleGameClient extends Observable implements Runnable {

	public static void test1() {


		//////////////////////Variables hold the game setting////////////////////////// 
		JFrame frame = null; int Level_chooser = 0; int GameType_chooser = 0;

		/////////////////////Choose Game Type : Auto or Manual////////////////////////
		Object[] possibilities1 = {"Automatic" , "Menual"};
		String s1 =  (String)JOptionPane.showInputDialog(frame,"Play automatic or Menual","Auto or Menual", JOptionPane.PLAIN_MESSAGE,null ,possibilities1,  "");
		if(s1 == "Automatic") {GameType_chooser  = 0 ; }
		if(s1 == "Menual")    {GameType_chooser  = 1 ; }

		///////////////////////////////Choose level//////////////////////////////////
		Object[] possibilities2 = {"0","1", "2", "3","4","5", "6", "7","8","9", "10", "11","12","13", "14", "15","16","17", "18", "19","20","21", "22", "23"};
		String s2 = (String)JOptionPane.showInputDialog(frame,"pick game","Choose Game Level:", JOptionPane.PLAIN_MESSAGE,null ,possibilities2,  "");
		Level_chooser =Integer.parseInt(s2);	


		game_service game = Game_Server.getServer(Level_chooser); //peek the level from server. 

		String graph_string = game.getGraph();					 //Getting the level map(graph). 

		/////////////////////////////initializing the graph//////////////////////////
		DGraph dgraph = new DGraph();							
		dgraph.init(graph_string);
		dgraph.EdgeInitTag();

		String info = game.toString();							//getting all the Level information from the server.(string)
		



		////////////////////////////Loading the information from the Json String given by the server/////////////////////////////
		JSONObject line;
		
		initRobots(game,dgraph);
		kmlmaker kml = new kmlmaker(dgraph,game);
		kml.save_kml("test.kml");

		////Open a manual Game/////
		if(GameType_chooser == 1) {
			playMenual(dgraph,game); 
			//game is running...
		}


		////Open a Automatic Game/////
		else {

			PlayAuto(dgraph,game);
			//game is running...
		}


			//Print the Game results//
			String results = game.toString();
			System.out.println("Game Over: "+results);
		}

		static private void initRobots(game_service game, DGraph gg) {

			String graph_string = game.getGraph();
			String info = game.toString();
			JSONObject line;
			try {

				line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				System.out.println(info);
				System.out.println(graph_string);
				// the list of fruits should be considered in your solution
				Iterator<String> f_iter = game.getFruits().iterator();

				fruits = new Fruit_Basket(game, gg);
				robots_list = new ArrayList<robot>();		 //creating the list of the robots.

				Fruit_Basket temp = new Fruit_Basket(game, gg);

				while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
				int src_node = 0;  // arbitrary node, you should start at one of the fruits

				for(int a = 0;a<rs;a++) {
					// will get an ID of node and place a robot on it.
					int src = temp.getMax().getFruitEdge().getSrc();
					game.addRobot(src);
					robots_list.add(new robot(a));
					final_dest_len.add(Double.MAX_VALUE);

				}
			}		catch (JSONException e) {e.printStackTrace();}	

		}




		static Fruit_Basket fruits;
		static ArrayList<robot> robots_list;
		static HashMap<Integer, Queue<Edge>> robot_final_dest1 = new HashMap<Integer, Queue<Edge>>();
		static HashMap<Integer, Queue<node_data>> robot_final_dest = new HashMap<Integer, Queue<node_data>>();
		static ArrayList<Double> final_dest_len = new ArrayList<Double>();
		static boolean flag = true;

		/**
		 * Main Method of the automatic game:
		 * 
		 * will play the game using algorithm for best score. 
		 * @param dgraph -the game graph.
		 * @param game 	 -game server.
		 */
		private static void PlayAuto(DGraph dgraph, game_service game) {

			//Open the Game window display (GUI)
			GUI gui = new GUI(dgraph,game,0);
			gui.setVisible(true);

			game.startGame();			 // Commend the game server to start the game. 

			int ind = 30; 		int delay = 0;
			long first = System.currentTimeMillis();
			while(  game.timeToEnd()/1000!=0) { //   game.isRunning()) { 

				new_moveRobots(game, dgraph);

				gui.repaint();
				if(System.currentTimeMillis() - first >= 10000){
					first = System.currentTimeMillis();
				}


			}

		}

		/**
		 * in charge of setting all the information of the robots in the game. 
		 * @param game
		 * @param gg
		 */
		static private void updateRobots(game_service game, DGraph gg) {

			List<String> log = game.move();		 // getting the game log information. 

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

					boolean flag = true;

					//--> update the robot fields.
					robots_list.get(i).setDest(dest);
					robots_list.get(i).setSrc(src);
					robots_list.get(i).setSpeed(speed);

					if(robots_list.get(i).getPath() != null) {
						//--> update the robot local path
						if(robots_list.get(i).robot_Heap_Get_PathLen() < robot_final_dest1.get(i).peek().getWeight()) {
							//need to be updated.
							for (int j = 0; j < robot_final_dest1.get(i).size(); j++) {

							} 

						}
					}

					//update the local path of the robot. 
					if(!robot_final_dest1.containsKey(robots_list.get(i).getId())) {
						robot_final_dest1.put(robots_list.get(i).getId(), null);
					}

				}catch (Exception e) {}
			}
		}


		static private void new_moveRobots(game_service game, DGraph gg) {

			List<String> log = game.move();

			if(log!=null) {

				long t = game.timeToEnd();

				fruits= new Fruit_Basket(game, gg);
				updateRobots(game,gg);

				Fruit_Basket f = fruits;
				ArrayList<robot> r=  robots_list;
				// --> update robot heap
				for (int i = 0; i < robots_list.size(); i++) {
					robots_list.get(i).cleanHeap();
					for (int j = 0; j < fruits.getLen(); j++) {	

						Object[] a = Graph_Algo.shortestPath_Dist(robots_list.get(i).getSrc(),fruits.getFruit(j).getFruitEdge().getSrc(),gg,fruits.getFruit(j),1);

						robots_list.get(i).addHeap(a,fruits.getFruit(j));
					}
				}

				for (int i = 0; i < robots_list.size(); i++) {

					for (int j = 0; j < robots_list.size(); j++) {
						if(i == j) continue;//same robot
						else if(robots_list.get(i).getFruit() == robots_list.get(j).getFruit()) {

							//-->same value of paths (choose random)
							if(robots_list.get(i).getPath_len() == robots_list.get(j).getPath_len()) {
								robots_list.get(j).robot_Heap_pop();
							}
							//
							else if (robots_list.get(i).getPath_len() < robots_list.get(j).getPath_len()) {
								robots_list.get(j).robot_Heap_pop();
							}
							else if (robots_list.get(i).getPath_len() > robots_list.get(j).getPath_len()) {
								robots_list.get(i).robot_Heap_pop();

								i=-1; j= robots_list.size(); continue;
							}
						}

					}

				}

				for (int i = 0; i < robots_list.size(); i++) {

					if(robot_final_dest1.get(i)== null) {			
						Queue<Edge> temp =  new LinkedList<Edge>(robots_list.get(i).getrobot_Heap().peek().getPath());
						robot_final_dest1.put(i,temp );
						final_dest_len.set(i,robots_list.get(i).getrobot_Heap().peek().getPathDist());

					}
					else if(final_dest_len.get(i)> robots_list.get(i).getrobot_Heap().peek().getPathDist()){			
						Queue<Edge> temp =  new LinkedList<Edge>(robots_list.get(i).getrobot_Heap().peek().getPath());
						robot_final_dest1.put(i,temp);
						final_dest_len.set(i,robots_list.get(i).getrobot_Heap().peek().getPathDist());
					}

				}
				HashMap<Integer, Queue<Edge>>robot_final = robot_final_dest1;

				for (int i = 0; i < robots_list.size(); i++) {

					if(robots_list.get(i).getDest() ==-1) {

						Edge e = robot_final_dest1.get(i).remove();
						if(robot_final_dest1.get(i).size() == 0) {
							robot_final_dest1.put(i, null);
						}
						double temp = final_dest_len.get(i);
						final_dest_len.set(i, temp-e.getWeight());
						int id_src = e.getSrc();
						int id_dest = e.getDest();
						System.out.println(id_src+" "+id_dest);
						game.chooseNextEdge(robots_list.get(i).getId(), id_dest);
						System.out.println(game.timeToEnd()/1000);

					}
				}	
			}
		}


		private static void playMenual(DGraph dgraph, game_service game) {
			GUI gui = new GUI(dgraph,game,1);
			gui.setVisible(true);
			game.startGame();

			long t = game.timeToEnd();
			while(game.isRunning()) { // while there is more time in the game
				moveRobotsM(game,dgraph);
				gui.repaint();
			}

		}




		static private void moveRobotsM(game_service game, DGraph gg) {

			List<String> log = game.move();
			Frame frame = null; 
			int d_choosen = -1;
			if(log!=null) {

				updateRobots(game,gg);

				for(int i=0;i<log.size();i++) {
					if(!robot_final_dest1.containsKey(i)) {
						robot_final_dest1.put(i, null);
					}

				}

				for (int i = 0; i < robots_list.size(); i++) {

					if(robot_final_dest.get(i)==null) {

						while (flag==true) {
							try {
								String temp1= JOptionPane.showInputDialog(frame,"Enter Dest ID for ROBOT "+i);
								if(temp1.matches("\\d+")) {
									d_choosen =Integer.parseInt(temp1);	
									if(gg.getNode(d_choosen)!=null) {flag=false; continue;}
								}	System.out.println("dest Not Valid");
							}catch (Exception e) {}		
						}flag=true;

						List<node_data> temp = Graph_Algo.shortestPath(robots_list.get(i).getSrc(), d_choosen, gg);
						Queue<node_data> e = new LinkedList<node_data>(temp);
						e.remove();
						e.add(gg.getNode(d_choosen));
						robot_final_dest.put(i, e);
					}
				}

				for (int i = 0; i < robots_list.size(); i++) {

					if(robots_list.get(i).getDest() ==-1) {

						node_data e = robot_final_dest.get(i).remove();
						
						if(robot_final_dest.get(i).size() == 0) {
							robot_final_dest.put(i, null);
						}
						
						double temp = final_dest_len.get(i);
					
						game.chooseNextEdge(robots_list.get(i).getId(), e.getKey());
						System.out.println(game.timeToEnd()/1000);
					}
				}


			}

		}


		@Override
		public void run() {
			test1();

		}




	}
