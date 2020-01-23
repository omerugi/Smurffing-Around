package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gameClient.SimpleDB;
/**
 * result window class:
 * 
 * this class in charge of representing the scores as separated thread from the 
 * GUI thread which allows to keep the game run smoothly.
 * 
 * gets the level Id and the Score presentation of the user choice using the simpleDB
 * class method 'StringForGui'. 
 */
public class resultwindow implements Runnable{

	private int level;
	private int Id;  
	private int choice; 
	private JFrame frame =null; 
	private JOptionPane pan = new JOptionPane();
	
	/**
	 * 
	 * @param level - game level
	 * @param Id	- player ID
	 * @param choice- represents the specifications to display
	 */
	public resultwindow(int level , int Id,  int choice) {
		this.level=level;
		this.Id = Id; 
		this.choice = choice; 
	}
/**
 * call simpleDB method 'StringForGUI' 
 * with the user specifications.
 */
	private void print() {
		
		System.out.println(level+"  " + Id+"  "+   choice);
		JOptionPane.showConfirmDialog(frame,SimpleDB.StringForGUI(level,Id,choice));
		
		
	}
	
	
	
	@Override
	public void run() {
		print();
	}

	
	
}
