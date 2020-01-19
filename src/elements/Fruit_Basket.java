package elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;


/**
 * Class Fruit Basket: 
 * 
 * This class will hold the fruit objects in a data Structure.  
 * Will be use as a container of fruit objects. 
 *
 */
public class Fruit_Basket {

	ArrayList <Fruit> fruits = new ArrayList<Fruit>(); // Array list of fruits ( the container )  
	StringBuffer fruitBuffer = new StringBuffer();    // String buffer - will contain the fruits in a KML String. 
	
	//
	public Fruit_Basket () {
		start_kml();
	}
	public Fruit_Basket (game_service game, DGraph gg) {
		
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
				Point3D fruit_pos = new Point3D(x, y,0);
				
				fruits.add(new Fruit(type,value,fruit_pos,Graph_Algo.EdgeForFruit(gg, fruit_pos, type)));
				
			} catch (JSONException e) {e.printStackTrace();}
			
		}	
		start_kml();
	}
	/**
	 * Private method - used for constructing the head 
	 * lines of the fruits folder for the Kml file
	 */
	private void start_kml() {
		
		fruitBuffer.append(Fruit.init_Kml());
		String temp = "<Folder>\r\n" + 
				"      <name>fruits</name>";
		fruitBuffer.append(temp);
	}
	/**
	 * Public method - used for constructing the tail 
	 * lines of the fruits folder for the Kml file.
	 */
	public String end_kml() {
		
		for (int i = 0; i < fruits.size(); i++) {
			fruitBuffer.append(fruits.get(i).to_kml());
		}
		
		String temp = "</Folder>\r\n";
		fruitBuffer.append(temp);
		
		return fruitBuffer.toString();
		
	}
	
	/**
	 * During the game this method will refresh the fruits container. 
	 * add or remove new / old fruits. 
	 * @param game	- the game service 
	 * @param gg	- game graph. 
	 */
	public void update(game_service game, DGraph gg) {
		
		Fruit_Basket temp = new Fruit_Basket(game,gg);
		ArrayList <Fruit> temp_fruits = temp.gerFruitList();
			
		for (int i = 0; i  < temp_fruits.size(); i++) {
			
			if(!contains(temp_fruits.get(i).getLocation(), temp_fruits.get(i).getType(), temp_fruits.get(i).getValue())) {
				this.fruits.add(temp_fruits.get(i));
			}
		}
		
		boolean flag = false;
		for (int i = 0; i  < fruits.size(); i++) {
		
			for (int j =0; j < temp_fruits.size() ; j++) {
				
				if(fruits.get(i).getID() == temp_fruits.get(j).getID()) {
					flag = true; j = temp_fruits.size();
				}
			}
			
			if(!flag) {
				fruitBuffer.append(fruits.get(i).to_kml());
				fruits.remove(i);
			}
			flag = false;
		}
		
	}

	/**
	 * simple method used for check if a fruit is contained in the fruit container. 
	 * @param fruit_pos	- x,y coordinates
	 * @param type		- fruit type
	 * @param value		- fruit value
	 * @return	true/false if the fruit is contained in the fruit container. 
	 */
	private boolean contains(Point3D fruit_pos, double type, double value ) {
		for (int i = 0; i < this.fruits.size(); i++) {
			if(fruits.get(i).getLocation().x()==fruit_pos.x() 
					&& fruits.get(i).getLocation().y() ==fruit_pos.y()  
					&& fruits.get(i).type==type && fruits.get(i).value==value) 
			{return true;}
		}
		return false;
	}
	/**
	 * @return the content of the fruit container
	 */
	public ArrayList <Fruit> gerFruitList() {
		return fruits;
	}
	/**
	 * @return how many fruits are in the container 
	 */
	public int getLen() {
		return fruits.size();
	}
	/**
	 * @param i - position in the container .
	 * @return -returns the fruit in the i'th position in the container
	 */
	public Fruit getFruit(int i) {
		if(i>=fruits.size()) {return null;}
		return fruits.get(i);
	}
	
	/**
	 * @return fruit with the maximum value in the container 
	 */
	public Fruit getMax() {
		
		Fruit temp= new Fruit();
		temp.setValue(-100.0);
		int index = 0;
		for (int i = 0; i < fruits.size(); i++) {		
			if(fruits.get(i).getValue() > temp.getValue() ) {
				temp = fruits.get(i);
				index = i;
			}
		}
		
		fruits.remove(index);
		return temp;
		
	}
	
	
	
}
