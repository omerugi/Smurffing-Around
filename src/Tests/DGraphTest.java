package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.NodeV;
import utils.kmlmaker;

class DGraphTest {

	DGraph g;

	@BeforeEach
	void BeforeEach() {

		g = new DGraph();
		for (int i = 0, j=1; i < 20; i+=2) {
			g.addNode(new NodeV(i,j,1));
			j*=2;
		}

		Iterator hit = g.getV().iterator();

		int j=0;
		while(hit.hasNext()) {
			Iterator hit2 = g.getV().iterator();
			node_data src = (node_data) hit.next();
			j=0;
			while (hit2.hasNext()) {
				node_data dest = (node_data) hit2.next();
				if(j%2 == 0) {
					g.connect(src.getKey(), dest.getKey(), 2);
				}
				j++;
			}

		}

	}


	@Test
	void testDGraphString() {
		
		DGraph gg = new DGraph("data\\A2");
		
		System.out.println(gg.getV());
		
		gg = new DGraph("data\\A1");
		System.out.println(gg.getV());
		
		gg = new DGraph("data\\A0");
		System.out.println(gg.getV());
		
	}
	
	@Test
	void testInitJson() {
		DGraph gg = new DGraph();
		
		try {
			Scanner scanner = new Scanner(new File("data\\A0"));
			String jsonString = scanner.useDelimiter("\\A").next();
			scanner.close();
			gg.init(jsonString);
		} catch (Exception e) {
			fail();
		}
		
		
	}
	
	@Test
	void testToKml() {
		
		kmlmaker kml = kmlmaker.get_kmlmaker();
		kml.add_kml(g.to_kml());
		kml.save_kml("testdgraphJunit.kml");
		/// to see need to open google earth
	}
	
	
	@Test
	void testGetNode() {

		Iterator hit = g.getV().iterator();
		ArrayList<node_data> to_print = new ArrayList<node_data>();
		while(hit.hasNext()) {
			to_print.add((node_data) hit.next());

		}
		System.out.print("[");
		for (node_data e : to_print) {
			System.out.print(" "+e.getKey()+" ");
		}
		System.out.println("]");

		graph g1 = new DGraph();

		node_data v1 = new NodeV(3, 4);
		node_data v2 = new NodeV(8, 7);
		g1.addNode(v1);
		g1.addNode(v2);
		assertEquals(g1.getNode(v1.getKey()), v1);
		assertEquals(g1.getNode(v2.getKey()), v2);
	}

	@Test
	void testGetEdge() {

		graph g1 = new DGraph();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.connect(v2.getKey(), v3.getKey(), 3);
		g1.connect(v3.getKey(), v4.getKey(), 3);
		g1.connect(v4.getKey(), v5.getKey(), 3);
		g1.connect(v5.getKey(), v6.getKey(), 3);

		assertEquals(g1.getEdge(v1.getKey(), v2.getKey()).getSrc(),v1.getKey());
		assertEquals(g1.getEdge(v1.getKey(), v2.getKey()).getDest(),v2.getKey());

		assertEquals(g1.getEdge(v2.getKey(), v3.getKey()).getSrc(),v2.getKey());
		assertEquals(g1.getEdge(v2.getKey(), v3.getKey()).getDest(),v3.getKey());



	}

	@Test
	void testAddNode() {

		graph g1 = new DGraph();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		assertEquals(g1.getNode(v1.getKey()), v1);
		assertEquals(g1.getNode(v2.getKey()), v2);
		assertEquals(g1.getNode(v3.getKey()), v3);
		assertEquals(g1.getNode(v4.getKey()), v4);
		assertEquals(g1.getNode(v5.getKey()), v5);
		assertEquals(g1.getNode(v6.getKey()), v6);

	}

	@Test
	void testConnect() {

		graph g1 = new DGraph();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.connect(v2.getKey(), v3.getKey(), 3);
		g1.connect(v3.getKey(), v4.getKey(), 3);
		g1.connect(v4.getKey(), v5.getKey(), 3);
		g1.connect(v5.getKey(), v6.getKey(), 3);

		assertEquals(g1.getEdge(v1.getKey(), v2.getKey()).getSrc(),v1.getKey());
		assertEquals(g1.getEdge(v1.getKey(), v2.getKey()).getDest(),v2.getKey());

		assertEquals(g1.getEdge(v2.getKey(), v3.getKey()).getSrc(),v2.getKey());
		assertEquals(g1.getEdge(v2.getKey(), v3.getKey()).getDest(),v3.getKey());


	}

	@Test
	void testGetV() {

		graph g1 = new DGraph();
		ArrayList<node_data> g2 = new ArrayList<node_data>();

		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g2.add(v1);		g2.add(v2);
		g2.add(v3);		g2.add(v4);
		g2.add(v5);		g2.add(v6);

		Collection<node_data> a = g1.getV();
		Iterator hit = a.iterator();
		for (int i = 0; i < g2.size(); i++) {

			assertTrue(g2.contains((node_data)hit.next()));

		}

	}

	@Test
	void testGetE() {
		graph g1 = new DGraph();
		ArrayList<Integer> g2 = new ArrayList<Integer>();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g2.add(v2.getKey());		g2.add(v3.getKey());
		g2.add(v4.getKey());		g2.add(v5.getKey());
		g2.add(v6.getKey());		

		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.connect(v1.getKey(), v3.getKey(), 3);
		g1.connect(v1.getKey(), v4.getKey(), 3);
		g1.connect(v1.getKey(), v5.getKey(), 3);
		g1.connect(v1.getKey(), v6.getKey(), 3);

		Collection<edge_data> e1 = g1.getE(v1.getKey());
		assertEquals(e1.size(), 5);

		Iterator<edge_data> hit = e1.iterator();

		while (hit.hasNext()) {

			edge_data temp = hit.next();
			assertTrue(g2.contains(temp.getDest()));

		}


		g1 = new DGraph();

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g1.connect(v1.getKey(), v2.getKey(), 3);
		Collection<edge_data> v2e = g1.getE(v1.getKey());
		hit = v2e.iterator();
		edge_data e = (edge_data) hit.next();
		assertEquals(e.getDest(), v2.getKey());

		g1.connect(v1.getKey(), v3.getKey(), 3);
		g1.connect(v1.getKey(), v4.getKey(), 3);
		v2e = g1.getE(v1.getKey());
		hit = v2e.iterator();

		ArrayList<node_data> temp = new ArrayList<node_data>();
		temp.add(v2);
		temp.add(v3);
		temp.add(v4);
		ArrayList<node_data> temp1 = new ArrayList<node_data>();
		while(hit.hasNext()) {
			e = (edge_data) hit.next();
			temp1.add(g1.getNode(e.getDest()));
		}

		for (int i = 0; i < temp.size(); i++) {

			if(!temp1.contains(temp.get(i))) {fail();}

		}

	}

	@Test
	void testRemoveNode() {
		graph g1 = new DGraph();
		ArrayList<Integer> g2 = new ArrayList<Integer>();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.connect(v1.getKey(), v3.getKey(), 3);
		g1.connect(v1.getKey(), v4.getKey(), 3);
		g1.connect(v1.getKey(), v5.getKey(), 3);
		g1.connect(v1.getKey(), v6.getKey(), 3);

		g1.removeNode(v1.getKey());

		assertNull(g1.getE(v2.getKey()));
		assertNull(g1.getE(v3.getKey()));
		assertNull(g1.getE(v4.getKey()));
		assertNull(g1.getE(v5.getKey()));
		assertNull(g1.getE(v6.getKey()));

	}

	@Test
	void testRemoveEdge() {

		graph g1 = new DGraph();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);


		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.removeEdge(v1.getKey(), v2.getKey());
		assertNull(g1.getEdge(v1.getKey(), v2.getKey()));

		g1.connect(v1.getKey(), v3.getKey(), 3);
		g1.removeEdge(v1.getKey(), v3.getKey());
		assertNull(g1.getEdge(v1.getKey(), v3.getKey()));


		g1.connect(v1.getKey(), v4.getKey(), 3);
		g1.removeEdge(v1.getKey(), v4.getKey());
		assertNull(g1.getEdge(v1.getKey(), v4.getKey()));



	}

	@Test
	void testNodeSize() {

		graph g1 = new DGraph();
		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		assertEquals(g1.nodeSize(), 6);

	}

	@Test
	void testEdgeSize() {

		graph g1 = new DGraph();

		node_data v1 = new NodeV(3, 4);		node_data v2 = new NodeV(7, 7);
		node_data v3 = new NodeV(4, 4);		node_data v4 = new NodeV(2, 7);
		node_data v5 = new NodeV(9, 4);		node_data v6 = new NodeV(1, 7);

		g1.addNode(v1);		g1.addNode(v2);
		g1.addNode(v3);		g1.addNode(v4);
		g1.addNode(v5);		g1.addNode(v6);

		g1.connect(v1.getKey(), v2.getKey(), 3);
		g1.connect(v1.getKey(), v3.getKey(), 3);
		g1.connect(v1.getKey(), v4.getKey(), 3);
		g1.connect(v1.getKey(), v5.getKey(), 3);
		g1.connect(v1.getKey(), v6.getKey(), 3);

		assertEquals(g1.edgeSize(), 5);
	}
	
	


}
