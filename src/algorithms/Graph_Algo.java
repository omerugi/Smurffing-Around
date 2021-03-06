package algorithms;


import dataStructure.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import elements.Edge;
import elements.Fruit;
import elements.NodeV;
import utils.Point3D;
/**
 * This class holds implementations of Algorithems interface.
 * Algorithms: 
 * -----------
 * 			1.init(file.txt) 	 graph from file.txt.
 * 			2.save			 	 deserialized to graph object from txt file. 
 * 								 and build a new graph from it.
 * 			3.isConnected  		 checks if the graph is strongly Connected. 
 *			4.shortest_path		 return the shortest path between two Vertexes. (Using Dijakstra)
 *			5.shortest_path		 sum the shortest path weight between two vertxes. (Using Dijakstra) 			
 * 			6.TSP				 return the relatively shortest path that pass on all the targets 
 * 								 vertexes input to function.
 * 
 * 
 *Secondery Algorithems + Methods: 
 *----------------------------------			
 * 			1.BFS			 Breath first check algorithm Used to check isConnected.
 * 			2.Dijkstra		 calculate the shortest path between src --> dest.
 * 
 * @author Dor Getter && Omer Rugi  
 *
 */
public class Graph_Algo  {


	////////////////////////////////////////////
	//////////////    fields     ///////////////
	////////////////////////////////////////////

	private static final double EPS = 0.0000001;


	///////////////////////////////////////////////////////////////////////////
	////////////////////////////       methods        /////////////////////////
	///////////////////////////////////////////////////////////////////////////


	/**
	 * 			This method checks if a graph is "Strongly connected".
	 * Strong Connected Directed Graph definition:  
	 * 		A directed graph is called strongly connected if there is a path
	 *  	between each pair of vertices of the graph.
	 * 
	 * 
	 * Using BFS algorithm the function checks if between random points of the graph
	 * there is a path ad in which all vertexes have been visited. 
	 * . 
	 * if all vertexes been visited it returns true;   
	 */

	static public boolean isConnected(DGraph g) {

		if(g == null) { System.out.println("Graph is empty"); return false;}

		Collection<node_data> vertex = g.getV();
		if(BFS(vertex,g)) { Collection<node_data> reverse_vertex = rev_collection(vertex.toArray());	return BFS(reverse_vertex,g);	}
		return false;
	}
	/**
	 * This method reverse a given Collection. 
	 * @param array Obj array; 
	 * @return reverse node_data Collection.  
	 */
	static private Collection<node_data> rev_collection(Object [] array) {

		Collection<node_data> rev = new ArrayList<node_data>();
		for (int i = array.length-1; i >= 0; i--) {
			rev.add((node_data) array[i]);}
		return rev;
	}
	/**
	 * Breath First search: 
	 * 
	 * 
	 * In given Collection of vertexes from a given graph,
	 * the algorithem  will check if the graph is strongly Connected.
	 * Breadth first search is a graph traversal algorithm that starts traversing the graph from random node and explores
	 * all the neighboring nodes. Then, it selects the nearest node and explore all the unexplored nodes. 
	 * The algorithm follows the same process for each of the nearest node using a queue until queue is empty which indicates all the neighboring 
	 * that are connected to the random first node are connected by path. 
	 * then using a 'check_all_visited' method check if all vertexes marked visited. 
	 * 
	 * @param vertex collections of vertexes of the graph.
	 * @return boolean , true: if all vertexes been visited, false otherwise;  
	 */
	static private boolean BFS(Collection<node_data> vertex, DGraph g) {

		Queue<node_data> q = new LinkedList<node_data>();
		if(vertex.isEmpty()) {
			return false;
		}
		all_Zero(g.getV()); //set tag of vertexes to 0 (unvisited); 
		Iterator hit = vertex.iterator();
		node_data a = (node_data) hit.next();
		a.setTag(1); //sets the first vertex to visited.
		q.add(a);	//adds it to queue 

		while(!q.isEmpty()) {

			node_data first_out = q.remove(); //dequeue the first in queue.

			Collection<edge_data> e = g.getE(first_out.getKey()); //getting all the neighboring.
			if(e == null) {return false;} //if there are no edges coming out return false (indicates the graph cannot be connected).   
			Iterator bgu = e.iterator();
			while(bgu.hasNext()) {  //iterating over the vertex edges
				edge_data ed	= (Edge) bgu.next(); 
				int nver 		= ed.getDest();	//gets the next neighbor. 
				if(g.getNode(nver).getTag() == 0) {	//checks if already visited. 
					g.getNode(nver).setTag(1);	//if not visited set to visited. 
					q.add(g.getNode(nver));	//add the vertex to the queue. 
				}
			}
		}
		return check_all_visited(vertex); //checks the tag on all vertexes. 
	}
	/**
	 * In given node_data Collection iterates all vertexes and validate all tags marked as visited (visited==1). 
	 * @param vertex
	 * @return true: all marked 1 , false: if at least one vertex tag 0 ;
	 */
	private static boolean check_all_visited(Collection<node_data> vertex) {

		Iterator idc = vertex.iterator();
		while(idc.hasNext()) {
			node_data check = (node_data) idc.next();
			if(check.getTag()==0)     {return false;}
		}
		return true;
	}
	/**
	 * Iterates Over all vertexes and set tag to 0; (unvisited);
	 * @param vertex
	 */
	static private void all_Zero(Collection<node_data> vertex) {
		Iterator tau = vertex.iterator();
		while(tau.hasNext()) {node_data check = (node_data) tau.next();	check.setTag(0);}
	}
	/**
	 * Iterates Over all vertexes and set weight to infinity; (unvisited);
	 * @param vertex
	 */
	static private void all_inf(Collection<node_data> vertex) {
		Iterator tau = vertex.iterator();
		while(tau.hasNext()) {	node_data check = (node_data) tau.next();	check.setWeight(Double.MAX_VALUE);}
	}
	/**
	 * Dijkstra:
	 * Given a graph and a source vertex in the graph, find shortest paths from source to all vertices in the given graph.
	 * Base:
	 * setting all vertexes weight to INF and tag them 0 - Unvisited; 
	 * --------------------------------------------------------------
	 * Algo: 
	 * Extract the first src node and set his weight to 0 (The cost of path from src to src is zero); 
	 * Using a Min heap add src to heap; 
	 * 
	 * The Algo Will Resume as followed: 
	 * for each iteration the top off the Min heap node will represents the shortest path from src by weight. 
	 * the algorithm works as a greedy algorithm which will move forwards in the least "expensive" path until reach the 
	 * Destination node. 
	 * in the end the shortest path will send to create path method and returns a Array List contained the path itself. 
	 * @param src start point.
	 * @param dest end point . 
	 * @return shortest path between src & dest. 
	 */
	private static ArrayList<node_data> DijkstraNode(int src, int dest,DGraph g){

		Collection<node_data> vertex = g.getV(); // getting all vertexes.

		doescontain(src,dest,vertex);

		all_Zero(vertex); //sets tag to 0; )Unvisited; 
		all_inf(vertex); // sets the path cost to 0; 

		node_data a = g.getNode(src); //getting the start node. 
		a.setWeight(0); //set star node path cost to 0; 
		int counter=0; //Counter usage for stopping condition counts vertexes; 

		MinHeap heap = new MinHeap(); //Will store the least valued path next vertex; 

		HashMap<Integer, Integer> prev_id = new HashMap<Integer, Integer>(); //Will store the previous node in the path. 

		heap.add((node_data) a, 0, a.getKey(),prev_id); //adding the start node to heap; 
		if(src == dest) { //stoping condition. 
			return createpath(src,dest,prev_id,g);
		}

		while(counter != vertex.size() && !heap.isEmpty()) {
			node_data pop = heap.pop(); //pop the least valued path node. 
			if(pop.getTag()==1) {continue;}	//if visited already just continue;
			pop.setTag(1); // visited pop

			if(pop.getKey() == dest) {counter = vertex.size(); continue;} //another stopping condition.(reached destination) 

			Collection<edge_data> e = g.getE(pop.getKey());		
			if(e==null) {continue;}
			Iterator<edge_data> edges =  e.iterator(); 

			while(edges.hasNext()) //adding all sons of the node.
			{
				edge_data temp = edges.next(); 
				if(g.getNode(temp.getDest()).getTag()==1) {continue;} //if visited continue;
				heap.add((NodeV) g.getNode(temp.getDest()) , temp.getWeight()+pop.getWeight(), pop.getKey(),prev_id); //else add the node to the heap; 
			}
			counter++; 
		}


		return createpath(src,dest,prev_id,g); //rearrange the order of the path from end-->start to start-->end; 

	}


	/**
	 * Dijkstra:
	 * Given a graph and a source vertex in the graph, find shortest paths from source to all vertices in the given graph.
	 * Base:
	 * setting all vertexes weight to INF and tag them 0 - Unvisited; 
	 * --------------------------------------------------------------
	 * Algo: 
	 * Extract the first src node and set his weight to 0 (The cost of path from src to src is zero); 
	 * Using a Min heap add src to heap; 
	 * 
	 * The Algo Will Resume as followed: 
	 * for each iteration the top off the Min heap node will represents the shortest path from src by weight. 
	 * the algorithm works as a greedy algorithm which will move forwards in the least "expensive" path until reach the 
	 * Destination node. 
	 * in the end the shortest path will send to create path method and returns a Array List of edges contained the path itself. 
	 * @param src start point.
	 * @param dest end point . 
	 * @return shortest path between src & dest. 
	 */
	private static ArrayList<Edge> DijkstraEdge(int src, int dest,DGraph g){

		Collection<node_data> vertex = g.getV(); // getting all vertexes.

		doescontain(src,dest,vertex);

		all_Zero(vertex); //sets tag to 0; )Unvisited; 
		all_inf(vertex); // sets the path cost to 0; 

		node_data a = g.getNode(src); //getting the start node. 
		a.setWeight(0); //set star node path cost to 0; 
		int counter=0; //Counter usage for stopping condition counts vertexes; 

		MinHeap heap = new MinHeap(); //Will store the least valued path next vertex; 

		HashMap<Integer, Integer> prev_id = new HashMap<Integer, Integer>(); //Will store the previous node in the path. 

		heap.add((node_data) a, 0, a.getKey(),prev_id); //adding the start node to heap; 
		if(src == dest) { //stoping condition. 
			return createpath_edge(src,dest,prev_id,g);
		}

		while(counter != vertex.size() && !heap.isEmpty()) {
			node_data pop = heap.pop(); //pop the least valued path node. 
			if(pop.getTag()==1) {continue;}	//if visited already just continue;
			pop.setTag(1); // visited pop

			if(pop.getKey() == dest) {counter = vertex.size(); continue;} //another stopping condition.(reached destination) 

			Collection<edge_data> e = g.getE(pop.getKey());		
			if(e==null) {continue;}
			Iterator<edge_data> edges =  e.iterator(); 

			while(edges.hasNext()) //adding all sons of the node.
			{
				edge_data temp = edges.next(); 
				if(g.getNode(temp.getDest()).getTag()==1) {continue;} //if visited continue;
				heap.add((NodeV) g.getNode(temp.getDest()) , temp.getWeight()+pop.getWeight(), pop.getKey(),prev_id); //else add the node to the heap; 
			}
			counter++; 
		}

		return createpath_edge(src,dest,prev_id,g); //rearrange the order of the path from end-->start to start-->end; 

	}


	/**
	 * 
	 * iterates from destination node backwards by using the prev_id to determine the 
	 * father of the node in the path. 
	 * when its reaches src as the prev_id it returns the path as Array List of Edges; 
	 * 
	 * @param src
	 * @param dest
	 * @param prev_id data structure to hold the edges in a path. 
	 * @return the path src --> destination
	 */
	private static ArrayList<Edge> createpath_edge(int src, int dest, HashMap<Integer, Integer> prev_id,DGraph g) {


		if(g.getNode(dest).getWeight() == Double.MAX_VALUE) { return null;}
		ArrayList<Edge> path = new ArrayList<Edge>();

		node_data a = (node_data) g.getNode(dest);
		while(a.getKey() != src) {
			
			path.add((Edge) g.getEdge(prev_id.get(a.getKey()),a.getKey() ));
			int prev = prev_id.get(a.getKey()) ;
			a = (node_data) g.getNode(prev);
		}

		ArrayList<Edge> new_path = new ArrayList<Edge>();

		for (int i = path.size()-1; i >=0; i--) {
			new_path.add(path.get(i));
		}

		return new_path;
	}


	/**
	 * 
	 * iterates from destination node backwards by using the prev_id to determine the 
	 * father of the node in the path. 
	 * when its reaches src as the prev_id it returns the path as Array List of Node_data; 
	 * 
	 * @param src
	 * @param dest
	 * @param prev_id data structure to hold the father of a node in a path. 
	 * @return the path src --> destination
	 */
	static private ArrayList<node_data> createpath(int src,int dest,HashMap<Integer, Integer> prev_id,DGraph g) {

		if(g.getNode(dest).getWeight() == Double.MAX_VALUE) { return null;}
		ArrayList<node_data> path = new ArrayList<node_data>();

		node_data a = (node_data) g.getNode(dest);

		while(a.getKey() != src) {
			path.add(a);
			int prev = prev_id.get(a.getKey()) ;
			a = (node_data) g.getNode(prev);
		}

		path.add(a);
		path = (ArrayList<node_data>) rev_collection(path.toArray());
		return path;
	}
	/**
	 * Using a Dijkstra method
	 * which creating the shortest path between src --> dest extracting the weight of the path.
	 * @return the cost value (double) of the shortest path src --> dest.   
	 */
	static public double shortestPathDist(int src, int dest,DGraph g,int pick) {	

		if(g == null) { System.out.println("Graph is empty"); return -1;}

		if(pick == 0) {
			List<node_data> temp = DijkstraNode(src, dest, g);
			if(temp == null ) {
				return -1; 
			}
			return temp.get(temp.size()-1).getWeight();
		}else {
			List<Edge> temp = DijkstraEdge(src, dest, g);
			if(temp == null ) {
				return -1; 
			}
			return g.getNode(temp.get(temp.size()-1).getDest()).getWeight();
		}
	}
	/**
	 * Using a Dijkstra method
	 * which creating the shortest path between src --> dest extracting the path.
	 * @return the shortest path src --> dest.   
	 */
	static public List<node_data> shortestPath(int src, int dest,DGraph g) {
		return DijkstraNode(src, dest,g);
	}
	
	/**
	 * Using a Dijkstra method
	 * which creating the shortest path between src --> dest extracting the path.
	 * @return the shortest path src --> dest.   
	 */
	static public List<Edge> shortestPathEdges(int src, int dest,DGraph g) {
		return DijkstraEdge(src, dest,g);
	}
	
	
	/**
	 * Using a Dijkstra method
	 * which creating the shortest path between src --> dest extracting the weight & path.
	 * @return the cost value (double) && the shortest path src --> dest    
	 */
	public static Object [] shortestPath_Dist(int src, int dest,DGraph g,Fruit fruit, int pick) {

		if(src == dest) {
			if(pick ==0) {
			List<node_data> temp = new ArrayList<node_data>();
			temp.add(g.getNode(src)); 
			temp.add(g.getNode(fruit.getFruitEdge().getDest()));
			Object [] arr = {0.0,temp};
			return arr;}
			else if(pick == 1) {
				List<Edge> temp = new ArrayList<Edge>();
				Edge e =  (Edge) g.getEdge(src, fruit.getFruitEdge().getDest());
				temp.add(e);
				Object [] arr = {0.0,temp};
				return arr;	
			}
		}
		
		if(pick == 0) {

		List<node_data> temp = DijkstraNode(src, dest, g);
		if(temp == null ) {
			return null;
		}

		Object [] arr = {temp.get(temp.size()-1).getWeight(),temp};

		return arr;}
		else {
			
			List<Edge> temp = DijkstraEdge(src, dest, g);
			if(temp == null ) {
				return null;
			}
			Object [] arr = {g.getNode(temp.get(temp.size()-1).getDest()).getWeight(),temp};
			return arr;	
		}
	}



	/**
	 * 
	 * Tsp: 
	 * The method calculates the relatively short path between given targets. 
	 * 
	 * Algo: 
	 * in given targets list each iteration will shuffle the targets visiting positions and calculate using the Dijkstra algorithm 
	 * the shortest path between t1-->t2 t2-->t3 ... tn-1-->tn (t==target), and iterates for 68 times which will statistically 
	 * will compute the shortest path.  
	 * @return the 'shortest' path. 
	 */

	static public List<node_data> TSP(List<Integer> targets, DGraph g) {

		if(targets==null) {System.out.println("No targets Entered..");return null;}
		targets=removeDuplicates(targets); // removing Duplicates.
		if(!checkPathOfTargets(targets,g)) {return null;}

		targets=removeDuplicates(targets); // removing Duplicates. 

		double min_path = Double.MAX_VALUE;

		double min_path_temp =-1; //base 
		Object [] arr_temp; //will hold the path between ti-->ti+1 & cost.	 
		ArrayList<node_data> arr = new ArrayList<node_data>();	//sum up the path between t1 ----> tn (if exists).
		ArrayList<node_data> ans = new ArrayList<node_data>();  //will hold the shortest path. 
		for (int i = 0; i < 64; i++) {  // how many checks. 
			arr = new ArrayList<node_data>();
			List<Integer> tmp = shuffleTargets(targets,g); //shuffle the targets list. 
			min_path_temp =0;							//setting the min path temp to hold the current path cost; 
			for (int j = 0; j < targets.size()-1; j++) {	//go over all the targets and try to create path ti-->ti+1; 
				arr_temp = shortestPath_Dist(tmp.get(j),tmp.get(j+1),g,null,0);
				if(arr_temp==null) { // if there is no path; 
					min_path_temp = -1;
					j=targets.size(); 
					continue;}
				min_path_temp += (double)arr_temp[0]; //add the cost of ti-->ti+1 + t1--------->ti-1;
				List<node_data> a = (List<node_data>) arr_temp[1];  
				for ( int k = 0; k < a.size(); k++) {  //add the path of ti-->ti+1 + t1--------->ti-1;
					if(arr.size() != 0 && arr.get(arr.size()-1).getKey() == a.get(k).getKey() ) {continue;}
					arr.add(a.get(k));

				}
			}

			if(min_path_temp < min_path && min_path_temp != -1 ) { //checks if the new checked path is least "expensive" than the minimum path stores.
				min_path = min_path_temp;
				ans = new ArrayList<node_data>();
				for (int j = 0; j < arr.size(); j++) {
					ans.add(arr.get(j));
				}
			}

		}

		if(ans.size() == 0) {return ans;} 
		return ans;
	}

	/**
	 * sub method to check if between all targets of tsp there is a path.
	 * @param targets
	 * @return if the targets subgraph is strongly connected.
	 */
	static private boolean checkPathOfTargets(List<Integer> targets,DGraph g) {
		Collection<node_data> t_check = new ArrayList<node_data>();
		try {
			for (int i = 0; i < targets.size(); i++) {
				t_check.add(g.getNode(targets.get(i))); }
		}catch (Exception e) {
			return false;
		}
		if(BFS(t_check, g)) {return true;}

		return false;
	}
	/**
	 * takes a list and shuffle randomly the elements positions inside the array.
	 * @param targets
	 * @return rearrange form of targets
	 */
	static private List<Integer> shuffleTargets(List<Integer> targets,DGraph g) {

		int t1 = new Random().nextInt(targets.size()-1);
		int t2 = new Random().nextInt(targets.size()-1);


		if(targets.size()==1) return targets;
		else if(targets.size()==2) {
			int temp = targets.get(0);
			targets.set(0, targets.get(1));
			targets.set(1,temp);
		}
		else if(t1==t2)
		{
			while(t1==t2)
			{
				t2 = new Random().nextInt(targets.size()-1);
			}
		}

		int temp = targets.get(t1);
		targets.set(t1, targets.get(t2));
		targets.set(t2,temp);

		return targets;
	}
	
	/**
	 * Used by Dijkstra. removing duplicated nodes in the Array List.
	 * @param list
	 * @return array without duplications. 
	 */

	static private ArrayList<Integer> removeDuplicates(List<Integer> list) 
	{ 

		// Create a new ArrayList 
		ArrayList<Integer> newList = new ArrayList<Integer>(); 

		// Traverse through the first list 
		for (Integer element : list) { 

			// If this element is not present in newList 
			// then add it 
			if (!newList.contains(element)) { 

				newList.add(element); 
			} 
		} 

		// return the new list 
		return newList; 
	} 

	
	/**
	 * @param src 	 - edge source node. 
	 * @param dest 	 - edge destination node.
	 * @param vertex - Coleection of all the Vertexes in the graph. 
	 * @return true/false if the there is a edge between source and destination vertexes.
	 */
	static private boolean doescontain(int src, int dest, Collection<node_data> vertex) {

		Iterator hit = vertex.iterator();
		boolean a=false; boolean b=false; 
		while (hit.hasNext())
		{
			node_data nd = (node_data) hit.next();
			if(nd.getKey()==src) 	{a =true;}
			if(nd.getKey()==dest) 	{b =true;}
		}
		if(a == false || b==false)
			return false;

		else
			return true;
	}
/**
 * calculate the length of an edge by the coordinates of x,y of the two vertexes
 * in the edge.      
 * @param x1 - source		 x1 coordinate
 * @param x2 - destination 	 Y1 coordinate
 * @param y1 - source		 x2 coordinate
 * @param y2 - destination 	 Y2 coordinate
 * @return the length between the vertexes
 */
	public static double CalcLen(double x1, double x2, double y1,double y2) {
		//d=sqrt((x1-x2)^2+(y1-y2)^2)
		return Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
		
	}
	
	
	
	/**
	 * Going through the Graph edges. 
	 * the method returns the edge which the fruit is on it. 
	 * @param gg 		- 			graph
	 * @param fruit_pos -			fruit 3D position (x,y,z coordinates); 	
	 * @param type      - 			fruit type. 
	 * @return			-			the edge which the fruit is. 
	 */
	public static Edge EdgeForFruit(DGraph gg, Point3D fruit_pos, double type) {

		Iterator hit = gg.getV().iterator();
		while(hit.hasNext()) {

			node_data v = (node_data) hit.next(); 
			Collection<edge_data> edges = gg.getE(v.getKey());
			if(edges == null) {continue;}

			Iterator hit2 = edges.iterator();

			while(hit2.hasNext()) {
				Edge dest = (Edge) hit2.next();
				if(isOn(dest,fruit_pos,type)) {
					return dest;
				}
			}
		}

		return null;
	}

	/**
	 * sub method to determined which edge the fruit is on. 
	 * @param dest		- edge from the graph
	 * @param fruit_pos - fruit 3D position (x,y,z coordinate)
	 * @param type		- fruit type
	 * @return			- boolean true/false if the fruit is on this edge.
	 */
	private static boolean isOn(Edge dest ,Point3D fruit_pos, double type) {

		if(dest.getSrcNode().getKey() < dest.getDestNode().getKey() &&  type == -1) {
			return false;
		}else if(dest.getSrcNode().getKey() > dest.getDestNode().getKey() &&  type == 1) {
			return false;
		}

		double x1 = dest.getSrcNode().getLocation().x(); double y1 = dest.getSrcNode().getLocation().y();
		double x2 = dest.getDestNode().getLocation().x(); double y2 = dest.getDestNode().getLocation().y();
		double x = fruit_pos.x(); double y = fruit_pos.y();

		if((CalcLen(x, x1, y, y1)+CalcLen(x, x2, y, y2)) <= dest.getlength()+EPS 
				&&(CalcLen(x, x1, y, y1)+CalcLen(x, x2, y, y2)) >= dest.getlength()-EPS)
		{
			return true;
		}
		return false;
	}



	//|--------------------------------------------------------------------------------------------------------------------------------|
	//|----------------------------------------------------SUB-CLASS-------------------------------------------------------------------|
	//|--------------------------------------------------------------------------------------------------------------------------------|
	/**
	 * MinHeap Data Structure: 
	 * 
	 * this data structure will contain node_data obj. and sort them by the node_data weight. 
	 * 
	 * will support the MinHeap methods: 
	 * push , pop . (not include peek).
	 * uses heapyup & heapyfudown for rearranging positions in the tree. 
	 * 
	 */

	static private class MinHeap{

		////////////////////////////////////////////
		//////////////    fields     ///////////////
		////////////////////////////////////////////


		ArrayList<node_data> heap;


		/////////////////////////////////////////////////////////////////
		///////////////////     Constructor     /////////////////////////
		/////////////////////////////////////////////////////////////////


		public MinHeap () {
			heap = new ArrayList<node_data>();}

		///////////////////////////////////////////////////////////////////////////
		////////////////////////////       methods        /////////////////////////
		///////////////////////////////////////////////////////////////////////////


		public void add(node_data v, double w, int prev_id,HashMap<Integer, Integer> prev) {

			if(heap.contains(v)) {
				updatew(v,w,prev_id,prev);
				return;
			}
			v.setWeight(w);
			prev.put(v.getKey(), prev_id);
			add_heapfyup(v);
		}
		/**
		 * adding to heap. 
		 * @param v
		 */
		private void add_heapfyup(node_data v) {
			heap.add(v);
			int index = heap.size()-1;
			while(index > 0) {
				int parent = (index-1)/2;

				if(heap.get(index).getWeight()< heap.get(parent).getWeight()) {
					node_data temp = heap.get(parent);
					heap.set(parent, heap.get(index));
					heap.set(index, temp);
					index = parent;
				}
				else {
					return;
				}
			}

		}
/**
 * updating the edge weight.
 * @param v
 * @param w
 * @param prev_id
 * @param prev
 */
		private void updatew(node_data v, double w,int prev_id,HashMap<Integer, Integer> prev) {
			if(w < v.getWeight()) {
				v.setWeight(w);
				prev.replace(v.getKey(), prev_id);
			}
		}

		public node_data pop() {

			node_data temp = heap.get(0);
			heap.set(0, heap.get(heap.size()-1));
			heap.set(heap.size()-1, temp);
			node_data pop = heap.remove(heap.size()-1);

			heapfy_down();
			return pop;
		}
/**
 * swap method.
 * @param a
 * @param b
 */
		private void swap(int a ,int b) {
			node_data temp = heap.get(a);
			heap.set(a, heap.get(b));
			heap.set(b, temp);
		}

		private void heapfy_down() {

			int right 		=2;
			int left 		=1;
			int movingindex =0;
			int end 		=heap.size()-1; 


			while(movingindex != end && right < end && left < end) {

				if(heap.get(movingindex).getWeight() > heap.get(left).getWeight())
				{
					if(heap.get(left).getWeight() < heap.get(right).getWeight())
					{
						swap(movingindex,left);
						movingindex = left; 
						left = (movingindex*2)+1;
						right= (movingindex*2)+2;
					}
					else if(heap.get(right).getWeight() < heap.get(left).getWeight())
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
				if(heap.get(left).getWeight() < heap.get(movingindex).getWeight())
				{swap(movingindex,left);}
			}
		}

		public String toString() {
			String print = "[";
			for (int i = 0; i <heap.size(); i++) {
				print+=heap.get(i).getWeight();
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
	}




}
