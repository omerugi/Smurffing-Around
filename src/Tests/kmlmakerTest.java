package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import utils.kmlmaker;

class kmlmakerTest {
	
	
	@Test
	void testAdd_kml() {
		kmlmaker kml = kmlmaker.get_kmlmaker();
		DGraph gg = new DGraph("data\\A0");
		kml.add_kml(gg.to_kml());
		kml.save_kml("junit1.kml");
		kml.clean();
	}

	@Test
	void testAdd_info() {
		kmlmaker kml = kmlmaker.get_kmlmaker();	
		kml.add_info("This is a info String to test the kmlmaker un Junit");
		kml.save_kml("junit2.kml");
		kml.clean();
	}

	@Test
	void testSave_kml() {
		kmlmaker kml = kmlmaker.get_kmlmaker();
		DGraph gg = new DGraph("data\\A0");
		kml.add_kml(gg.to_kml());
		kml.save_kml("junit3.kml");
		kml.clean();
	}

	@Test
	void testToString() {
		kmlmaker kml = kmlmaker.get_kmlmaker();
		DGraph gg = new DGraph("data\\A0");
		kml.add_kml(gg.to_kml());
		System.out.println(kml.toString());
		kml.clean();
	}

	@Test
	void testClean() {

		kmlmaker kml = kmlmaker.get_kmlmaker();
		DGraph gg = new DGraph("data\\A0");
		kml.add_kml(gg.to_kml());
		
		kml.clean();
		System.out.println(kml.toString());
	}

	@Test
	void testGet_kmlmaker() {
		kmlmaker kml = kmlmaker.get_kmlmaker();
		if(kml == null) fail();
	}

}
