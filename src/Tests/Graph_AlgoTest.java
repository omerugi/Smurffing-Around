package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import elements.Edge;
import elements.NodeV;
import utils.Point3D;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.Unmarshaller.Listener;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.NodeV;

class Graph_AlgoTest {

	DGraph 		graph;
	Graph_Algo ga;
	node_data a;		node_data b;
	node_data c;		node_data d ;
	node_data e;		node_data f;
	node_data g;		node_data h;
	node_data i;		node_data j;


	@BeforeEach
	void BeforeEach( ) {

		graph = new DGraph();
		ga= new Graph_Algo();

		a = new NodeV(100,100);		b =new NodeV(100,400);
		c =new NodeV(200,700);		d =new NodeV(400,500);
		e =new NodeV(400,900);		f =new NodeV(600,700);
		g =new NodeV(700,900);		h =new NodeV(700,200);
		i =new NodeV(900,600);		j =new NodeV(800,500);
		


		graph.addNode(a);
		graph.addNode(b);
		graph.addNode(c);
		graph.addNode(d);
		graph.addNode(e);
		graph.addNode(f);
		graph.addNode(g);
		graph.addNode(h);
		graph.addNode(i);
		graph.addNode(j);

		graph.connect(a.getKey(),b.getKey(),4.0);
		graph.connect(b.getKey(),c.getKey(),5.0);
		graph.connect(a.getKey(),h.getKey(),50.0);
		graph.connect(d.getKey(),a.getKey(),6.0);
		graph.connect(d.getKey(),c.getKey(),50.0);
		graph.connect(d.getKey(),f.getKey(),1.0);
		graph.connect(e.getKey(),d.getKey(),4.0);
		graph.connect(e.getKey(),g.getKey(),5.0);
		graph.connect(f.getKey(),c.getKey(),50.0);
		graph.connect(g.getKey(),f.getKey(),6.0);
		graph.connect(h.getKey(),d.getKey(),50.0);
		graph.connect(i.getKey(),j.getKey(),1.0);
		graph.connect(j.getKey(),d.getKey(),4.0);
		graph.connect(c.getKey(),i.getKey(),5.0);
		graph.connect(j.getKey(),h.getKey(),5.0);
		graph.connect(f.getKey(),e.getKey(),5.0);
		
	}

	
	@Test
	void testIsConnected() {
		
		assertTrue(Graph_Algo.isConnected(graph));
	}

	@Test
	void testShortestPathDist() {
		if(Graph_Algo.shortestPathDist(c.getKey(), f.getKey(), graph, 0)  != 11.0 ) fail();
		if(Graph_Algo.shortestPathDist(a.getKey(), g.getKey(), graph, 0)  != 30.0)  fail();		
	}

	@Test
	void testShortestPath() {
		ArrayList<node_data> test = new ArrayList<node_data>();
		test.add(a);		test.add(b);
		test.add(c);		test.add(i);
		test.add(j);		test.add(d);
		test.add(f);		test.add(e);	test.add(g);
		ArrayList<node_data> test2 = (ArrayList<node_data>) Graph_Algo.shortestPath(a.getKey(), g.getKey(), graph);
		if(test.size()!=test2.size()) {fail();}
		for (int i = 0; i < test.size(); i++) {
			if(test.get(i)!=test2.get(i)) {fail();}
		}
	}

	@Test
	void testTSP() {

		ArrayList<Integer> tar = new ArrayList<Integer>();
		tar.add(a.getKey());		tar.add(f.getKey());
		if(Graph_Algo.TSP(tar, graph)== null) fail();
	}
	
	@Test
	void testCalcLen() {
		
		if(Graph_Algo.CalcLen(0, 10, 0, 0) != 10){ fail();}
		
		if(Graph_Algo.CalcLen(0, 0, 10, 0) != 10){ fail();}
	}
	
	@Test
	void testEdgeForFruit() {
		
		Edge e = Graph_Algo.EdgeForFruit(graph, new Point3D(100,300), 1);
		
		if(!(e.getDestNode().getLocation().x() == 100 &&
				e.getDestNode().getLocation().y()==400 &&
				e.getSrcNode().getLocation().x() == 100 &&
				e.getSrcNode().getLocation().y() == 100 )) fail();
		
	}



}
