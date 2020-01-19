package elements;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dataStructure.edge_data;
import utils.*;
/**
 * Fruit Object class: 
 * 
 * Construct Fruit Objects. 
 */
public class Fruit {

	edge_data 	fruitEdge; 		//where the fruit at 
	double 		value; 			//value of the fruit
	double 		type; 			//fruit type (-1/1)
	Point3D 	location;		//fruit 3D coordinates
	String 		StartTime;		//time stamp of the fruit creations.
	double 		id;				//fruit id (fruit id is the sum of the multiplication of 
								//the x*y*type (x,y 3D coordinates of the fruit). 
	static int style_init =0;	//will indicates if the 'style' section already written to the KML
	
/**
 * Default Constructor; 
 */
	public Fruit() {}
/**
 *
 * @param type 	- fruit type 
 * @param v		- fruit value
 * @param p		- fruit coordinates
 * @param e		- fruit edge. (edge which the fruit on)
 */
	public Fruit(double type, double v, Point3D p, edge_data e) {
		this.type = type;
		this.value = v;
		this.location = new Point3D(p);
		this.fruitEdge = e;
		id = (p.x() +p.y())*type;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	//creating the time stamp of the fruit creation. 
		LocalDateTime now = LocalDateTime.now();
		this.StartTime = now.toString(); 
	}
/**
 * @return the fruit id. 
 */
	public double getID() {
		return this.id;
	}
/**
 * @return the edge which the fruit is on it. 
 */
	public edge_data getFruitEdge() {
		return fruitEdge;
	}
/**
 * @return the fruit value. 
 */
	public double getValue() {
		return value;
	}
/**
 * @return fruit type
 */
	public double getType() {
		return type;
	}
/**
 * 
 * @return fruit 3D coordinates
 */
	public Point3D getLocation() {
		return location;
	}	   
/**
 * @param v sets the value of the fruit.
 */
	public void setValue(double v) {
		this.value=v;
	}
/**
 * 
 * @return String containing the time the fruit has been created at. 
 */
	public String getStartTime() {
		return this.StartTime;
	}
	/**
	 * Constructs the head lines of the fruit 
	 * in Kml form .
	 * created as singleton method only happen once in fruit obj life. 
	 * @return String of the headlines of the fruit obj.  
	 */
	public static String init_Kml() {
		
		if(style_init >0) { return "";}
		style_init++;
		String temp = " <Style id=\"banana\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/plasticine/456/000000/banana.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"+
				" <Style id=\"apple\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/doodle/456/000000/apple.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"
				;
		
		return temp;
	}
	/**
	 * @return kml form of the fruit. 
	 * with all the data needed
	 */
	public String to_kml() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		String fruit;
		String name;
		
		// pick what type of style & the name to use banana/ apple
		if(type == -1) {
			fruit = "#banana";
			name = "banana";
		}else {
			fruit = "#apple";
			name = "apple";
		}
		
		// create the KML output
		String temp = "<Placemark>\r\n" + 
				"		<name>"+name+"</name>\r\n" + 
				"		<description> value of fruit: "+this.value+"</description>\r\n" + 
				"		<Point>\r\n" + 
				"			<coordinates>"+ this.location.x()+","+this.location.y()+","+"0"+"</coordinates>\r\n" + 
				"		</Point>\r\n" +
				"		<TimeSpan>\r\n" + 
				"    <begin>"+StartTime+"</begin>\r\n" + 
				"	 <end>"+ now.toString()+"</end>"+
				"  		</TimeSpan>\r\n	"+
				"<styleUrl>"+fruit+"</styleUrl>"+
				"	</Placemark>\n";
		return temp;
	}
}
