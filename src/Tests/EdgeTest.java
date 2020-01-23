package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.Edge;
import elements.NodeV;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.NodeV;

class EdgeTest {

	DGraph g; 
	int aID;
	int bID;
	
	
	@BeforeEach
	void BeforeAll() {
		
		g = new DGraph();
		NodeV a = new NodeV(100,100); 
		NodeV b = new NodeV(200,200); 
		g.addNode(a);
		g.addNode(b);
		aID = a.getKey();	bID = b.getKey();
		g.connect(a.getKey(), b.getKey(), 50);
	}

	@Test
	void testEdge() {
		edge_data ed =g.getEdge(aID, bID);
		assertEquals(ed.getSrc(), aID);
		assertEquals(ed.getDest(), bID);
		if(ed.getWeight() != 50.0) {fail();}
	}

	@Test
	void testGetSrc() {
		edge_data ed =g.getEdge(aID, bID);
		if(ed.getSrc() != aID) {fail();}
	}

	@Test
	void testGetDest() {
		edge_data ed =g.getEdge(aID, bID);
		if(ed.getDest() != bID) {fail();}
	}

	@Test
	void testGetWeight() {
		edge_data ed =g.getEdge(aID, bID);
		if(ed.getWeight() != 50) {fail();}
	
	}

	@Test
	void testGetTag() {
		edge_data ed =g.getEdge(aID, bID);
		if(ed.getTag() != 0) {fail();}
	}

	@Test
	void testSetTag() {
		g.getEdge(aID, bID).setTag(3);
		
		System.out.println(g.getEdge(aID, bID).getTag());
		if(g.getEdge(aID, bID).getTag() != 3) {fail();}
	}

}
