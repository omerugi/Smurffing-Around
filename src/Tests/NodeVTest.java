package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import elements.NodeV;
import utils.Point3D;

class NodeVTest {

	@Test
	void testNodeVIntIntInt() {
		
		for (int i = 0,j=0; i < 10; i++) {
			NodeV a = new NodeV(i,j);
			j+=2;
			System.out.println(a.getInfo());
		}
		
	}

	@Test
	void testNodeVPoint3D() {
		
		for (int i = 0,j=0; i < 50; i++) {
			Point3D temp = new Point3D(i,j);
			NodeV a = new NodeV(temp);
			j+=2;
		}
		
	}


	@Test
	void testGetKey() {
		ArrayList<NodeV> list = new ArrayList<NodeV>();
		for (int i = 0,j=0; i < 10; i++) {
			list.add(new NodeV(i,j));
			j+=2;
		}
		System.out.print("[");
		for (int i = 0; i < 10 ; i++) {
			System.out.print(list.get(i).getKey());
			if(i != 9) {System.out.print(",");}
		}
		System.out.println("]");
	}

	@Test
	void testGetLocation() {
		ArrayList<NodeV> list = new ArrayList<NodeV>();
		for (int i = 0,j=1; i < 10; i++) {
			list.add(new NodeV(i,j));
			j*=2;
			System.out.println(list.get(i).getLocation());
		}

		
	}

	@Test
	void testSetLocation() {
		ArrayList<NodeV> list = new ArrayList<NodeV>();
		for (int i = 0,j=1; i < 10; i++) {
			list.add(new NodeV(i,j));
			j*=2;
			list.get(i).setLocation(new Point3D(j,i));
			System.out.println(list.get(i).getLocation());
		}
	}

}
