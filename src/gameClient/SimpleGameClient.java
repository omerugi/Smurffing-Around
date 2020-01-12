package gameClient;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
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
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Edge;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
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
public class SimpleGameClient implements Runnable {
	
	public static void main(String[] a) {
		//SimplePlayer play = new SimplePlayer("C:\\Users\\dorge\\eclipse-workspace\\OOP-Ex3\\music.mp3");
		//Thread t = new Thread(play); t.run();
		test1();
		}
	
	public static void test1() {
		JFrame frame = null; 

		String temp1; int Level_chooser = 0; int GameType_chooser = 0; 	boolean flag = true;


		//Choose Game Type : Auto or Menual\\ 
		while(flag) {
			try {	
				String Menual_Or_Auto = JOptionPane.showInputDialog(frame,"Enter 1 to Menual || Enter 2 to Auto ");
				if(Menual_Or_Auto.matches("1")||Menual_Or_Auto.matches("2")) { GameType_chooser =Integer.parseInt(Menual_Or_Auto);	flag =false;}
			}catch (Exception e) {}
		}flag =true;

		//Choose level\\
		while(flag) {
			try {	
				temp1 = JOptionPane.showInputDialog(frame,"Enter Level ");
				if(temp1.matches("\\d+")) { Level_chooser =Integer.parseInt(temp1);flag =false;	}
			}catch (Exception e) {}
		}flag =true;

		game_service game = Game_Server.getServer(Level_chooser); // you have [0,23] games this will pick what lvl we will play from the server

		String graph_string = game.getGraph(); // game.getgrapf will bring me a json of the graph that I can place in a string 

		DGraph dgraph = new DGraph();
		dgraph.init(graph_string);
		dgraph.EdgeInitTag();
		
		
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


		if(GameType_chooser == 1) {
			playMenual(dgraph,game); 
		}else {
			PlayAuto(dgraph,game);
		}
		
		String results = game.toString();
		System.out.println("Game Over: "+results);

	}

	private static void PlayAuto(DGraph dgraph, game_service game) {

		GUI gui = new GUI(dgraph,game,0);
		gui.setVisible(true);
		
		
		game.startGame();
		
		long first = System.currentTimeMillis();
		while(game.isRunning()) { // while there is more time in the game
			moveRobots(game, dgraph);
			gui.repaint();
			if(System.currentTimeMillis() - first >= 10000){
				first = System.currentTimeMillis();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	static HashMap<Integer, Queue<node_data>> robot_final_dest = new HashMap<Integer, Queue<node_data>>();

	static private void moveRobotsM(game_service game, DGraph gg) {
		Graph_Algo ga = new Graph_Algo(gg);
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

					if(!robot_final_dest.containsKey(src)) {
						robot_final_dest.put(src, null);
					}

				}catch (Exception e) {}
			}

			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest == -1 && robot_final_dest.get(rid) == null ) {

						boolean flag = true; Frame frame = null; int d_choosen =-1;
						while (flag==true) {
							try {
								String temp1= JOptionPane.showInputDialog(frame,"Enter Dest ID for ROBOT"+rid);
								if(temp1.matches("\\d+")) {
									d_choosen =Integer.parseInt(temp1);	
									if(gg.getNode(d_choosen)!=null && d_choosen!=src) {flag=false; continue;}
								}	System.out.println("dest Not Valid");

							}catch (Exception e) {}flag =true;

								
						}
						Queue<node_data> q = new LinkedList<node_data>(ga.shortestPath(src, d_choosen));

						robot_final_dest.put(rid,q);	
					}

					if(dest==-1 && robot_final_dest.get(rid) != null && robot_final_dest.get(rid).size() != 0 ) {

						dest = robot_final_dest.get(rid).remove().getKey();
						if(robot_final_dest.get(rid).size()==0) {
							robot_final_dest.put(rid,null);
						}

						game.chooseNextEdge(rid, dest);
						System.out.println(ttt);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));

					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}

	}



	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */

	static private void moveRobots(game_service game, DGraph gg) {
		
		
		HashMap<Integer, Queue<node_data>> robot_final = robot_final_dest;
		List<String> log = game.move();
		if(log!=null) {

			long t = game.timeToEnd();

			List<String> Fruit = game.getFruits();
			ArrayList<Edge> fruit_edges = new ArrayList<Edge>();

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

					Iterator hit = gg.getV().iterator();
					while(hit.hasNext()) {

						node_data v = (node_data) hit.next(); 
						Collection<edge_data> edges = gg.getE(v.getKey());
						if(edges == null) {continue;}

						Iterator hit2 = edges.iterator();

						while(hit2.hasNext()) {
							Edge dest = (Edge) hit2.next();
							if(dest.isOn(x, y,type)) {
								fruit_edges.add(dest);
							}
						}
					}
				} catch (JSONException e) {e.printStackTrace();}

			}


			for(int i=0;i<log.size();i++) {

				String robot_json = log.get(i);

				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(!robot_final_dest.containsKey(src)) {
						robot_final_dest.put(src, null);
					}

				}catch (Exception e) {}
			}


			Object [] min_path = null;
			Graph_Algo ga = new Graph_Algo();
			ga.init(gg);
			int id = 0;

			for(int j=0; j<fruit_edges.size();j++) {

				min_path = null;
				for(int i=0;i<log.size();i++) {

					String robot_json = log.get(i);

					try {
						JSONObject line = new JSONObject(robot_json);
						JSONObject ttt = line.getJSONObject("Robot");
						int rid = ttt.getInt("id");
						int src = ttt.getInt("src");
						int dest = ttt.getInt("dest");
						
					
						if(robot_final_dest.get(rid) != null) {continue;}

						Object [] temp_path = ga.shortestPath_Dist(src, fruit_edges.get(j).getSrc());

						if(min_path == null && temp_path != null) {
							min_path = temp_path;
							id =rid;
						}else if(src == fruit_edges.get(j).getSrc()) {

							min_path[0] = fruit_edges.get(j).getWeight();
							List<node_data> temp = new LinkedList<node_data>();
							temp.add( gg.getNode(fruit_edges.get(j).getSrc()));
							min_path[1] =  temp;

						}
						else if((double)min_path[0] > (double)temp_path[0]) {
							min_path = temp_path;
							id =rid;
						}
					}
					catch (JSONException e) {e.printStackTrace();}
				}

				if(min_path == null) {continue;}

				Queue<node_data> q = new LinkedList<node_data>((List<node_data>) min_path[1]);
				q.remove();
				q.add(gg.getNode(fruit_edges.get(j).getDest()));
				robot_final_dest.put(id, q);
			}

			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1 && robot_final_dest.get(rid) != null &&robot_final_dest.get(rid).size() != 0 ) {

						dest = robot_final_dest.get(rid).remove().getKey();
						if(robot_final_dest.get(rid).size()==0) {
							robot_final_dest.put(rid,null);
							System.out.println(src+" "+dest);
						}
						
						game.chooseNextEdge(rid, dest);
						System.out.println(ttt);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));

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

	static private ArrayList<edge_data> fruit_edges = new ArrayList<edge_data>();


	static private void get_fruit_edges(DGraph g, game_service game) {

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

				Iterator hit = g.getV().iterator();
				while(hit.hasNext()) {


					node_data v = (node_data) hit.next(); 


					Collection<edge_data> edges = g.getE(v.getKey());
					if(edges == null) {continue;}


					Iterator hit2 = edges.iterator();

					while(hit2.hasNext()) {

						Edge dest = (Edge) hit2.next();
						if(dest.isOn(x, y,type)) {
							fruit_edges.add(dest);
						}
					}
				}


			} catch (JSONException e) {e.printStackTrace();}

		}

	}


	static private HashMap<Integer, MinHeap> robot_edges = new HashMap<Integer, SimpleGameClient.MinHeap>();


	static private void robot_edges(DGraph g, game_service game) {


		Graph_Algo ga = new Graph_Algo();
		ga.init(g);
		List<String> ArnoldSchwarzenegge = game.getRobots();
		for (int j = 0; j < ArnoldSchwarzenegge.size(); j++) {

			try {

				JSONObject obj = new JSONObject(ArnoldSchwarzenegge.get(j));
				JSONObject ff = obj.getJSONObject("Robot");
				int src = ff.getInt("src");

				MinHeap temp = new MinHeap();
				for (int i = 0; i < fruit_edges.size(); i++) {
					temp.add(fruit_edges.get(i), ga.shortestPath_Dist(src,fruit_edges.get(i).getSrc()));
				}

				robot_edges.put(src, temp);

			} catch (JSONException e) {e.printStackTrace();}


		}

	}


	static private void PickAmove(DGraph g, game_service game) {

		Set setofrobos = robot_edges.keySet();
		Iterator hit = setofrobos.iterator();
		List<String> robo_list = game.getRobots();
		int i =0;
		int j =0;
		boolean should_next = true;
		int robot_id = 0;

		while (hit.hasNext() ) {

			if(should_next) {
				robot_id = (int) hit.next();
			}

			Set setofrobos2 = robot_edges.keySet();
			Iterator hit2 = setofrobos.iterator();
			j=0;
			if (robo_list.size()==1) {return;}
			while (j != robo_list.size() && hit2.hasNext() ) {


				int robot_id2 = (int) hit2.next();

				if(robot_id == robot_id2) {continue;}

				node robot_path = robot_edges.get(robot_id).peek();
				node robot_path2 = robot_edges.get(robot_id2).peek();


				if( robot_path.getE() == robot_path2.getE()) {

					if(robot_path.getPathDist() < robot_path2.getPathDist()) {
						robot_edges.get(robot_path2).pop();
						j++;
						should_next =true;
					}else if(robot_path.getPathDist() > robot_path2.getPathDist()) {

						robot_edges.get(robot_path).pop();
						j = robo_list.size();
						should_next =false;
					}
				}

			}
		}


	}



	static private void final_jorney(DGraph g, game_service game) {

		Set setofrobos = robot_edges.keySet();
		Iterator hit = setofrobos.iterator();
		List<String> robo_list = game.getRobots();
		int robot_id;

		while (hit.hasNext()) {	
			robot_id = (int) hit.next();

			Queue<node_data> temp = new LinkedList<node_data>();
			List<node_data> temp2 = robot_edges.get(robot_id).peek().getPath();

			for (int i = 0; i < temp2.size(); i++) {
				temp.add(temp2.get(i));
			}

			robot_final_dest.put(robot_id, temp);
		}

	}



	static private class node{
		edge_data e;
		Object [] path;	
		public node(edge_data e, Object [] path) {
			this.e = e;
			this.path = path;	
		}

		public double getPathDist() {
			return (double) path[0];
		}

		public edge_data getE() {
			return e;
		}

		public List<node_data> getPath() {
			return (List<node_data>) path[1];
		}

	}




	static private class MinHeap{




		ArrayList<node> heap;

		public MinHeap () {
			heap = new ArrayList<node>();
		}

		public MinHeap (MinHeap a) {
			heap = new ArrayList<node>();
			ArrayList<node> temp = a.getList();
			for (int i = 0; i < temp.size(); i++) {
				heap.add(temp.get(i));
			}
		}

		private ArrayList<node> getList() {
			return heap;
		}

		public void add(edge_data e, Object [] path) {

			node new_e = new node(e, path);	
			add_heapfyup(new_e);
		}

		private void add_heapfyup(node new_e) {

			heap.add(new_e);
			int index = heap.size()-1;

			while(index > 0) {
				int parent = (index-1)/2;

				if(heap.get(index).getPathDist() < heap.get(parent).getPathDist()) {
					node temp = heap.get(parent);
					heap.set(parent, heap.get(index));
					heap.set(index, temp);
					index = parent;
				}
				else {
					return;
				}
			}

		}

		public node pop() {

			node temp = heap.get(0);
			heap.set(0, heap.get(heap.size()-1));
			heap.set(heap.size()-1, temp);
			node pop = heap.remove(heap.size()-1);
			heapfy_down();
			return pop;
		}

		private void swap(int a ,int b) {
			node temp = heap.get(a);
			heap.set(a, heap.get(b));
			heap.set(b, temp);
		}

		private void heapfy_down() {

			int right 		=2;
			int left 		=1;
			int movingindex =0;
			int end 		=heap.size()-1; 


			while(movingindex != end && right < end && left < end) {

				if(heap.get(movingindex).getPathDist() > heap.get(left).getPathDist())
				{
					if(heap.get(left).getPathDist() < heap.get(right).getPathDist())
					{
						swap(movingindex,left);
						movingindex = left; 
						left = (movingindex*2)+1;
						right= (movingindex*2)+2;
					}
					else if(heap.get(right).getPathDist() < heap.get(left).getPathDist())
					{
						swap(movingindex,right);
						movingindex = right; 
						left = (movingindex*2)+1;
						right= (movingindex*2)+2;
					}
					else 
					{
						swap(movingindex,left);
						movingindex = left; 
						left = (movingindex*2)+1;
						right= (movingindex*2)+2;
					}

				} else return; 
			}

			if(left<end) 
			{
				if(heap.get(left).getPathDist() < heap.get(movingindex).getPathDist())
				{swap(movingindex,left);}
			}
		}

		public String toString() {
			String print = "[";
			for (int i = 0; i <heap.size(); i++) {
				print+=heap.get(i).path;
				if(i != heap.size()-1) {
					print+=",";
				}
			}
			print+="]";
			return print;
		}

		public boolean isEmpty() {
			return heap.isEmpty();
		}

		public node peek() {
			return heap.get(0);
		}
	}




	@Override
	public void run() {
		test1();
		
	}




}
