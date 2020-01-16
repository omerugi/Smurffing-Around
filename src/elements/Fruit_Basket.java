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

public class Fruit_Basket {

	ArrayList <Fruit> fruits = new ArrayList<Fruit>();; 

	public Fruit_Basket () {}
	
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
		
	}
	
	public void update(game_service game, DGraph gg) {
		
		Fruit_Basket temp = new Fruit_Basket(game,gg);
		ArrayList <Fruit> temp_fruits = temp.gerFruitList();
			
		for (int i = 0; i  < temp_fruits.size(); i++) {
			
			if(!contains(temp_fruits.get(i).Location, temp_fruits.get(i).getType(), temp_fruits.get(i).getValue())) {
				this.fruits.add(temp_fruits.get(i));
			}
		}
		
		if(this.fruits.size() != temp_fruits.size()) {
			RemoveUnusedFruits(temp_fruits); 
		}	
	}

	private void RemoveUnusedFruits(ArrayList<Fruit> temp_fruits) {
		
		for (int i = 0; i < temp_fruits.size(); i++) {
			int [] check= contains2(temp_fruits.get(i).Location, temp_fruits.get(i).type ,temp_fruits.get(i).value); 
			if(check [0] == 0) { // fruit need to be deleted;
				this.fruits.remove(check[1]);
			}
		}
	}
	
	private int [] contains2(Point3D fruit_pos, double type, double value ) {
		int [] re = new int [2];
		for (int i = 0; i < this.fruits.size(); i++) {
			if(!(fruits.get(i).Location==fruit_pos && fruits.get(i).type==type && fruits.get(i).value==value)) {re[0]=0; re[1]=i;}
		}
		re [0]= 1;		re [1]=-1;		return  re;
	}
	
	
	
	
	
	private boolean contains(Point3D fruit_pos, double type, double value ) {
		for (int i = 0; i < this.fruits.size(); i++) {
			if(fruits.get(i).Location==fruit_pos && fruits.get(i).type==type && fruits.get(i).value==value) {return true;}
		}
		return false;
	}
	
	public ArrayList <Fruit> gerFruitList() {
		return fruits;
	}
	
	public int getLen() {
		return fruits.size();
	}
	
	public Fruit getFruit(int i) {
		if(i>=fruits.size()) {return null;}
		return fruits.get(i);
	}
	
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
