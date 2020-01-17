package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;

public class kmlmaker {

	StringBuffer game_kml;
	DGraph gg;
	game_service game;

	public kmlmaker(DGraph gg, game_service game) {
		this.gg = gg;
		this.game = game;
		game_kml = new StringBuffer();
	}

	public void add_game_set() {

		String headline = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>Gmae path</name>\r\n" + 
				"    <open>1</open>\r\n" + 
				"    <description>\r\n" + 
				game.toString() +"\n"+ 
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

		add_graph();

	}

	private void add_graph() {

		Iterator hit = gg.getV().iterator();
		StringBuffer edges_kml = new StringBuffer();
		edges_kml.append(add_headline_edges());
		
		while(hit.hasNext()) {
			node_data v = (node_data) hit.next();
			int i=0;

			add_vertex(v);

			Collection<edge_data>edges = gg.getE(v.getKey());
			if(edges == null) {continue;}
			//go over the edges that come out of the specific vertex\\ 
			Iterator hit2 = edges.iterator();

			while(hit2.hasNext()) {
				
				edge_data dest = (edge_data) hit2.next();
				edges_kml.append(add_edge(dest));
			}


		}
	}

	private String add_headline_edges() {
		
		String temp = "<Placemark>\r\n" + 
				"		<name>Graph</name>\r\n" + 
				"		<styleUrl>#m_ylw-pushpin</styleUrl>\r\n" + 
				"		<LineString>\r\n" + 
				"			<tessellate>1</tessellate>\r\n" + 
				"			<coordinates>\n";
		return temp;
		
	}

	private String add_edge(edge_data e) {
		
		String temp = ""+
		gg.getNode(e.getSrc()).getLocation().y()+","+gg.getNode(e.getSrc()).getLocation().x()+",0"+" "+
		gg.getNode(e.getDest()).getLocation().y()+","+gg.getNode(e.getDest()).getLocation().x()+",0"+" "
		;
		return temp;
	}

	private void add_vertex(node_data v) {

		String temp = " <Placemark>\r\n" + 
				"		<name>Vertex:" +v.getKey() +"</name>\r\n" +  
				"		<description> Graph's vertex	</description>\n"+			
				"		<Point>\r\n" +  
				"			<coordinates>"+v.getLocation().y() +","+v.getLocation().x()+",1</coordinates>\r\n" + 
				"		</Point>\r\n" + 
				"	</Placemark>\n";
		
		game_kml.append(temp);
		
	}
	
	private void add_end() {

		String temp = "\n"+
		"	</Document>\n"+
		"</Kml>";
		
	}
	
	
	public void save_kml(String filename) {
		
		String file_name = filename;
		add_end();
		
		try {
			
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			out.writeChars(this.game_kml.toString());
			out.close();
			System.out.println("File saved");
			
		} catch (Exception e) {
			System.out.println("problem");
		}
		
	}

	
}