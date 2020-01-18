package elements;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dataStructure.edge_data;
import utils.*;

public class Fruit {

	edge_data fruitEdge; //where the fruit at 
	double 		value; 
	double 		type; 
	Point3D 	location;
	String 		StartTime;
	double 		id;
	static int style_init =0;
	

	public Fruit() {}

	public Fruit(double type, double v, Point3D p, edge_data e) {
		this.type = type;
		this.value = v;
		this.location = new Point3D(p);
		this.fruitEdge = e;
		id = (p.x() +p.y())*type;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		this.StartTime = now.toString(); 
	}

	public double getID() {
		return this.id;
	}

	public edge_data getFruitEdge() {
		return fruitEdge;
	}

	public double getValue() {
		return value;
	}

	public double getType() {
		return type;
	}

	public Point3D getLocation() {
		return location;
	}	   

	public void setValue(double v) {
		this.value=v;
	}

	public String getStartTime() {
		return this.StartTime;
	}
	
	public static String init_Kml() {
		
		if(style_init >0) { return "";}
		style_init++;
		String temp = " <Style id=\"banana\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/plasticine/100/000000/banana.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"+
				" <Style id=\"apple\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/doodle/96/000000/apple.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"
				;
		
		return temp;
	}
	
	public String to_kml() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		String fruit;
		String name;
		if(type == -1) {
			fruit = "#banana";
			name = "banan";
		}else {
			fruit = "#apple";
			name = "apple";
		}
		
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
