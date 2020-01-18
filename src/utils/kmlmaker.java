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

public class kmlmaker {

	StringBuffer game_kml;
	private static int singletone = 0;
	private static int info = 0;
	private static kmlmaker kml;
		
	private kmlmaker() {
		game_kml = new StringBuffer();
		kml = this;
		singletone++;
	}

	public void add_kml(String kml_obj) {
		if(info == 0) {def_add_info();}
		game_kml.append(kml_obj);		
	}

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
	
	private void add_end() {

		String temp = "\n"+
		"	</Document>\n"+
		"</kml>";
		game_kml.append(temp);
	}
	
	public void save_kml(String filename) {
		
		String file_name = filename;
		add_end();
		
		System.out.println(game_kml);
		
		try {
			
			PrintWriter w = new PrintWriter(file_name);
			w.println(game_kml.toString());
			w.close();
			System.out.println("File saved");
			
		} catch (Exception e) {System.out.println("problem");}	
	}

	static public kmlmaker get_kmlmaker() {
		
		if(singletone ==1) {
			return kml;
		}
		
		return new kmlmaker();
		
	}
	
}