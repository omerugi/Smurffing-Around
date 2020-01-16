package elements;

import dataStructure.edge_data;
import utils.*;

public class Fruit {

	edge_data fruitEdge; //where the fruit at 
	double 		value; 
	double 		type; 
	Point3D 	Location; 
	
	public Fruit() {}
	  
	  public Fruit(double type, double v, Point3D p, edge_data e) {
		this.type = type;
	    this.value = v;
	    this.Location = new Point3D(p);
	    this.fruitEdge = e;
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
		return Location;
	}	   
	  
	public void setValue(double v) {
		this.value=v;
	}
//	  public Fruit(String JsonFruit) {
//		    this._value = v;
//		    this._pos = new Point3D(p);
//		    this._edge = e;
//		  }
//	  
//	  public int getType() {
//	    int ans = 0;
//	    ans = this._edge.getDest() - this._edge.getSrc();
//	    return ans;
//	  }
//	  
//	  public Point3D getLocation() {
//	    return new Point3D(this._pos);
//	  }
//	  
//	  public String toJSON1() {
//	    String ans = "{\"Fruit\":{\"value\":10,\"type\":1,\"pos\":\"35.187615443099276,32.103800431932775,0.0\"}}";
//	    return ans;
//	  }
//	  
//	  public String toString() {
//	    return toJSON();
//	  }
//	  
//	  public String toJSON() {
//	    int d = 1;
//	    if (this._edge.getSrc() > this._edge.getDest())
//	      d = -1; 
//	    String ans = "{\"Fruit\":{\"value\":" + 
//	      this._value + "," + 
//	      "\"type\":" + d + "," + 
//	      "\"pos\":\"" + this._pos.toString() + "\"" + 
//	      "}" + 
//	      "}";
//	    return ans;
//	  }
//	  
//	  public double getValue() {
//	    return this._value;
//	  }
//	  
//	  public double grap(robot r, double dist) {
//	    double ans = 0.0D;
//	    if (this._edge != null && r != null) {
//	      int d = r.getNextNode();
//	      if (this._edge.getDest() == d) {
//	        Point3D rp = r.getLocation();
//	        if (dist > rp.distance2D(this._pos))
//	          ans = this._value; 
//	      } 
//	    } 
//	    return ans;
//	  }
}
