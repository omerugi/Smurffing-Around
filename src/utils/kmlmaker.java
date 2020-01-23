package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
/**
 * 
 * Class Kml Maker: 
 * 
 * this class is in charge of constructing generic Kml file from objects.
 * 
 * Kml maker is a singleton class. 
 * 
 * this class constructor opens up a kml object (File)
 * and a new String Buffer that will be holding all the kml code 
 * inside of it. 
 * 
 *  the construction of the kml code is sepperated in to 3 major parts: 
 *  begin, content (objects kml string code), end.
 *   
 * begin+contents:   
 *   
 * when adding a object kml code to the class the method "add_kml" 
 * the class validates that all the kml headlines (the start of the kml code)
 * is indeed presents in the kml buffer (if there was no use of the "add info" method case)
 * --> if no a defult kml start lines will be created and append to the string buffer before 
 * the object kml code. 
 * --> if yes the method just appends the kml object string to the buffer. 
 * 
 * end: 
 * when the user wish to save or wrap and close the kml code using the  "save_kml" method,
 * the class will wrap the code constructed in the String Buffer with an ending lines of the 
 * kml code syntax using "add_end" method.
 * 
 * now when all the code is wrapped up the function will save the kml file by the name 
 * given to the method by the user.  
 * 
 */
public class kmlmaker {

	private StringBuffer game_kml;					//will hold the kml buffer.
	private static int singletone = 0;		//Singleton indicator.	
	private static int info = 0;  			//Indicator for entering a default start
	private static kmlmaker kml;			//Kml file.
	private static boolean is_close = false;
	
	/**private constructor:
	 * in charge of initializing the Kml file fields. 
	 */
	private kmlmaker() {
		game_kml = new StringBuffer();
		kml = this;
		singletone++;
	}
/**
 * will be in charge of appending kml code of objects into the kml file. 
 * checks for info is presents in the buffer using the 'info' variable. 
 * if not present the method will call the defult_add_info before appending 
 * the incoming kml object string. 
 * @param kml_obj --> object Kml code to append.
 */
	public void add_kml(String kml_obj) {
		if(info == 0) {def_add_info();}
		game_kml.append(kml_obj);		
	}
/**
 * this method allow the user to adding customized description to the kml file.
 * takes the wanted description from the user and append the info in the kml file description
 * area. 
 * @param info_of_path --> description to be added to the kml file. 
 */
	public void add_info(String info_of_path) {
		if(info > 0 ) {return;}
		String headline = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>KML path</name>\r\n" + 
				"    <open>1</open>\r\n" + 
				"    <description>\r\n" + 
				info_of_path +"\n"+ 
				"    </description> \n"+
				" <LookAt>\r\n" + 
				"      <longitude>35.20734722222223</longitude>\r\n" + 
				"      <latitude>32.10453611111111</latitude>\r\n" + 
				"      <altitude>0</altitude>\r\n" + 
				"      <range>1131.110892010045</range>\r\n" + 
				"      <tilt>0</tilt>\r\n" + 
				"      <heading>-0.3840786059394472</heading>\r\n" + 
				"    </LookAt>\r\n" 
				;
		game_kml.append(headline);
		info++;

	}
/**
 * default start of a kml file. 
 * with no description added. \
 * the kml file will present "no info available" in the kml description section 
 */
	private void def_add_info() {
		String headline = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>KML path</name>\r\n" + 
				"    <open>1</open>\r\n" + 
				"    <description>\r\n" + 
				"no info avilable" +"\n"+ 
				"    </description> \n"+
				" <LookAt>\r\n" + 
				"      <longitude>35.20734722222223</longitude>\r\n" + 
				"      <latitude>32.10453611111111</latitude>\r\n" + 
				"      <altitude>0</altitude>\r\n" + 
				"      <range>1131.110892010045</range>\r\n" + 
				"      <tilt>0</tilt>\r\n" + 
				"      <heading>-0.3840786059394472</heading>\r\n" + 
				"    </LookAt>\r\n" 
				;
		game_kml.append(headline);
		info++;
	}
/**
 * in charge of appending a wrap to the kml String buffer. 
 * close up the Document scope and the kml scope.
 */
	private void add_end() {

		String temp = "\n"+
				"	</Document>\n"+
				"</kml>";
		game_kml.append(temp);
	}
/**
 * this methods saves the kml file.
 * first call the add_end methods for wrapping up all the code in the kml. 
 * than using ' print writer ' prints all the String Buffer of the kml to a txt 
 * file.  
 * @param filename --> wished file name by user
 */
	public void save_kml(String filename) {
		
		
		String file_name = filename;
		if(!is_close) {add_end();}
		
		try {
			PrintWriter w = new PrintWriter(file_name);
			w.println(game_kml.toString());
			w.close();
			System.out.println("File saved");

		} catch (Exception e) {System.out.println("problem");}
		is_close = true;
	}
	/** 
	 * this method return a string in a kml code format. 
	 */
	@Override
	public String toString() {
		
		if(!is_close) {add_end();}
		is_close = true;
		
		
		return game_kml.toString();
	}
	/**
	 * empty the String Buffer. 
	 */
	public void clean() {
		is_close = false;
		info = 0;
		game_kml = new StringBuffer();
	}
	
	
	
/**
 * Singleton constructor. 
 * checks first if an kml object is already constructed. 
 * if yes --> the method will return the kml file present in the class
 * if no  --> will return a new Kml Maker and construct it by the private constructor.
 * @return kml file. 
 */
	static public kmlmaker get_kmlmaker() {

		if(singletone ==1) {
			return kml;
		}
		
		return new kmlmaker();
	}
}