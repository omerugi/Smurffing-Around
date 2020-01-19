package elements;

import java.io.Serializable;
import java.util.Collection;
import java.util.StringTokenizer;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/**
 * Class Edge: 
 * 
 * this class will Constructs the Edges Objects of the graph. 
 */

public class Edge implements edge_data, Serializable{


	////////////////////////////////////////////
	//////////////    fields     ///////////////
	////////////////////////////////////////////

	node_data src;
	node_data dest;
	double w;
	int tag=0;
	double length = 0;
	double x1 ;	double y1 ;
	double x2 ;	double y2 ;
	private final double EPS = 0.0000001;
	
	
	
	/////////////////////////////////////////////////////////////////
	///////////////////     Constructor     /////////////////////////
	/////////////////////////////////////////////////////////////////

	public Edge(node_data src, node_data dest, double w) {
		
		if(w<=0) {System.out.println("weight must be greater then 0");return;}
		this.src = src; 
		this.dest	 = dest;
		this.w	=w;
		x1 = src.getLocation().x();
		y1 = src.getLocation().y();
		x2 = dest.getLocation().x();
		y2 = dest.getLocation().y();
		this.length = CalcLen(x1,x2,y1,y2);
		this.tag=tag;
	}
	private double CalcLen(double x1, double x2, double y1,double y2) {
		//d=((x1-x2)^2+(y1-y2)^2)
		return Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
	}
	///////////////////////////////////////////////////////////////////////////
	////////////////////////////       methods        /////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Will return the src of the edge.
	 */
	@Override
	public int getSrc() {
		return this.src.getKey();
	}
	/**
	 * Will return the dest of the edge.
	 */
	@Override
	public int getDest() {
		return this.dest.getKey();
	}
	/**
	 * @return the edge source Vertex
	 */
	public node_data getSrcNode() {
		return this.src;
	}
	/**
	 * Will return the dest of the edge.
	 */
	
	public node_data getDestNode() {
		return this.dest;
	}
	/**
	 * @return edge length. destance between source --> to destination .
	 */
	public double getlength() {
		return this.length;
	}
	
	
	/**
	 * Will return the weight of the edge.
	 */
	@Override
	public double getWeight() {
		return w;
	}
	/**
	 * Will return the starting point end and weight of the edge.
	 */
	@Override
	public String getInfo() {

		return "("+src.getKey()+","+dest.getKey()+","+this.getWeight()+")";
	}
	///////////////////////////////////////////////////////
	@Override
	public void setInfo(String s) {;}
	////////////////////////////////////////////////////////
	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;
	}
	/**
	 * calculates if a fruit is on the edge or not method. 
	 * using the fruit x , y coordinates calculates the length between the fruit 
	 * to the two vertexes constructing the edge.
	 * 
	 * if the sum of distance from the fruit to the two edge vertexes is equal to the 
	 * length of the edge in a slight deviation of EPSILON it returns true.  
	 * @param x			- fruit x coordinate
	 * @param y			- fruit y coordinate
	 * @param type		- fruit type
	 * @return 			- boolean if a point (fruit usage) is on the edge. 
	 */
	public boolean isOn(double x, double y, double type) {
		
		if(src.getKey() < dest.getKey() &&  type == -1) {
			return false;
		}else if(src.getKey() > dest.getKey() &&  type == 1) {
			return false;
		}
		
		if((CalcLen(x, x1, y, y1)+CalcLen(x, x2, y, y2)) <= this.length+EPS 
				&&(CalcLen(x, x1, y, y1)+CalcLen(x, x2, y, y2)) >= this.length-EPS)
		{
			return true;
		}
		return false;
	}
	/**
	 * returns a String of the coordinates of the edge .
	 */
	public String cord_kml() {
		
		String temp = ""+
		this.src.getLocation().x()+","+this.src.getLocation().y()+",0"+" "+
		this.dest.getLocation().x()+","+this.dest.getLocation().y()+",0"+" ";
		return temp;
	}
	
}
