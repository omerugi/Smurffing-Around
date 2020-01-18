package elements;


import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sun.glass.ui.Robot;

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
	StringBuffer robotBuffer;
	int folder=0;
	private static int style_init =0;
	
	//defult constructor. 
	public robot(){
		robotBuffer= new StringBuffer(); 
	}

	public robot(int id, Point3D location, double speed, int src, int dest) {
		this.id = id;
		this.location = location;
		Speed = speed;
		this.src = src;
		this.dest = dest;
		path = new LinkedList<Edge>();
		robot_Heap = new MinHeap();
		robotBuffer= new StringBuffer(); 

	}
	
	public robot(int id){
		this.id = id;
		robotBuffer= new StringBuffer(); 
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
	
	static public String init_Kml() {
		
		if(style_init >0) { return "";}
		style_init++;
		String temp = " <Style id=\"robot_icon\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/color/128/000000/transformer.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>";
		
		return temp;
	}
	
	public void add_start() {
		
		String temp = "<Folder>\r\n" + 
				"      <name>robot"+this.id+" folder</name>\r\n" + 
				"      <open>1</open>\r\n" + 
				"      <Style>\r\n" + 
				"        <ListStyle>\r\n" + 
				"          <listItemType>"+"robot-"+this.id+" path"+"</listItemType>\r\n" + 
				"        </ListStyle>\r\n" + 
				"      </Style>/n";
		this.robotBuffer.append(temp);
		
	}

	public void add_kml_loc() {
		if(folder == 0) {add_start();folder++;}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
				
		String temp  = "      "
				+ "	   <Placemark>\r\n" + 
				"        <TimeStamp>\r\n" + 
				"          <when>"+now+"</when>\r\n" + 
				"        </TimeStamp>\r\n" + 
				"        <styleUrl>#robot_icon</styleUrl>\r\n" + 
				"        <Point>\r\n" + 
				"			<coordinates>"+ this.location.x()+","+this.location.y()+","+"0"+"</coordinates>\r\n" + 
				"        </Point>\r\n" + 
				"      </Placemark>\n";
		this.robotBuffer.append(temp);
	}
	
	public String stringTokml() {
		this.robotBuffer.append("</Folder>\r\n");
	    
		return robotBuffer.toString();
	}
	
	
	
}
