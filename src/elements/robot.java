package elements;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Server.fruits;
import dataStructure.MinHeap;
import dataStructure.MinHeap.node;
import dataStructure.node_data;
import utils.Point3D;

public class robot {

	Queue<Edge> path = null;
	int 	id;    			//Id of the robot.
	Point3D location;		//Robot x,y coordinates.
	double 	Speed; 			//Robot speed. 
	int 	src; 			//the robot current vertex .
	int 	dest;			//the next vertex of the robot (-1 means robot is wait to instructions).
	MinHeap robot_Heap;		//will contain a list of the shortest paths from the robot to a fruit.
	double path_len;		//the length of the path to the fruit. 
	Fruit fruit;			//the fruit the robot goes to.
	//defult constructor. 
	public robot(){}

	public robot(int id, Point3D location, double speed, int src, int dest) {
		this.id = id;
		this.location = location;
		Speed = speed;
		this.src = src;
		this.dest = dest;
		path = new LinkedList<Edge>();
		robot_Heap = new MinHeap();
	}
	
	public robot(int id){
		this.id = id;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////robot settings///////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * returns the robot id.
	 * @return Id 
	 */
	public int getId() {
		return id;
	}
	/**
	 * setting the robot Id
	 * @param id
	 */
	
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * returns the x,y coordinates of the robot.
	 * @return
	 */
	
	public Point3D getLocation() {
		return location;
	}

	/**
	 * setting the Robot location coordinates.
	 * @param location
	 */
	public void setLocation(Point3D location) {
		this.location = location;
	}

	/**
	 * getting the current speed of the robot. 
	 * @return speed
	 */
	public double getSpeed() {
		return Speed;
	}
	
	
	public void setSpeed(double speed) {
		this.Speed = speed;
	}
	
	/**
	 * returns the current vertex the robot is on.
	 * @return vertex id
	 */
	public int getSrc() {
		return src;
	}
		
	/**
	 * sets the src vertex the robot is on ?  
	 * @param src
	 *///??????????????????????
	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * 
	 * @return the next destination of the robot (vertex id); 
	 */
	public int getDest() {
		return dest;
	}
	
	/**
	 * sets the robot next destination (vertex id)
	 * @param dest
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////Heap Methods/////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void addHeap(Object [] path, Fruit fruit) {
		robot_Heap.add(path,fruit);
	}
	
	public void cleanHeap() {
		robot_Heap = new MinHeap();
	}
		
	public Fruit getFruit() {
		return robot_Heap.fruitpeek();
	}

	
	public double robot_Heap_Get_PathLen() {
		return robot_Heap.lenPeek();
	}
	
	public node robot_Heap_pop() {
		return robot_Heap.pop();
	}
	
			
	public Queue<Edge> getPath() {
		return path;
	}

	public void setPath(java.util.List<Edge> la) {
		path = new LinkedList<Edge>(la);
	}

	public MinHeap getrobot_Heap() {
		return robot_Heap;
	}

	public void setrobot_Heap(MinHeap robot_Heap) {
		this.robot_Heap = robot_Heap;
	}

	public double getPath_len() {
		return path_len;
	}
	
	public void setPath_len(double path_len) {
		this.path_len = path_len;
	}
	
	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}

}
