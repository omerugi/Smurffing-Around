package gameClient;

import java.awt.FileDialog;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Gui.GUI;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
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
public class SimpleGameClient {
	public static void main(String[] a) {
		test1();}
	public static void test1() {
		JFrame frame = null; 
		int scenario_num = 2; // picking what lvl in numbers
		String temp1; int chooser = 0; 
		temp1 = JOptionPane.showInputDialog(frame,"Enter scenario ");
		if(temp1.matches("\\d+")) { chooser =Integer.parseInt(temp1);	}
		
		game_service game = Game_Server.getServer(chooser); // you have [0,23] games this will pick what lvl we will play from the server
		// I get an obj of game and I will use it to make the game work
		// game.toString will give me the "map" of the lvl -> all the objs in it.
		
		String graph_string = game.getGraph(); // game.getgrapf will bring me a json of the graph that I can place in a string 
		// I will build from this string a graph and print it to the screen.
		// Need to add thiss function to graph -> init from string
		
		// to get all the fruits in the game I will use game.getfruits.
		/**
		 * for ( String fruit : game.getfruit())
		 * {
		 * 	syso(fruit); -> will give me all the info of the fruit
		 * }
		 * 
		 */
		// better to open an obj of fruit that will inisilize with "json" string.
		// how do we know where to place the fruit? we can solve -> if we will check the the size from v1 to v1 is equal to -> ( fruit to v1) + (furit to v2)
		// if the fuit is on a cross? X? we can it to one of the edges.
		// all we need is to move the robot and place the fruits on the edges -> the server will know when we move next to the cortinats of the fruit to add it to the robot
		// but it's not only the cortinate need to move on the right edge to pick it.
		// type 1 : from low ID to greter ID apple....and -1 the other way around banana.
		
		DGraph dgraph = new DGraph();
		
		dgraph.init(graph_string);
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
			while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for(int a = 0;a<rs;a++) {
				// will get an ID of node and place a robot on it.
				game.addRobot(src_node+a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		
		GUI gui = new GUI(dgraph,game);
		gui.setVisible(true);
		// this will start the game and will start a clock of the game.
		    game.startGame();
		// should be a Thread!!!
		// in order to not print all the time can use:
		/**
		 * long first = system.currentTimeMille();
		 * 
		 * while(game.isRuning){
		 * 
		 * **** to move the robos we will use:
		 * game.choosNextEdge(robot_id,node_dest_id) // this will tell the robots where to move. need to make it as a loop to go over all robots. 
		 *  **** I can move a robot only if it's in a node, meaning -1 in dest.
		 * game.move // will make the robots move
		 * 
		 * ** every robot has speed -> so we will need to move it accurding to it's speed and the length of the edge.
		 * ** if we will wait long time we can skip over a fruit so we need to do move as much as possible.
		 * if(system.currentTimeMille() - first >= 1000){
		 * 	syso(game.timetoend()/1000)
		 * first = system.currentTimeMille()
		 * }
		 * }
		 * also have game.timetoend will give me how long until the end so need to dive in 1000 becuse its in ms.
		 */
		
		long first = System.currentTimeMillis();
		while(game.isRunning()) { // while there is more time in the game
			moveRobots(game, dgraph);
			gui.repaint();
			if(System.currentTimeMillis() - first >= 10000){
				
				first = System.currentTimeMillis();
				
			}
			
			
		}
		/**
		 * this will give me the robos and the details in them
		 * for(String robot : game.getRobots())
		 * 
		 * }
		 */
	
		String results = game.toString();
		System.out.println("Game Over: "+results);
		
		
		// we need to make two games -> manual(2) or auto(3).
		// all of it should be in the GUI.
		// 3 will just run.
		// 2 will make give you the option where to place the robots and to tell it where to go.
		
		
	}
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots(game_service game, DGraph gg) {
		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
				
					if(dest==-1) {	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

}
