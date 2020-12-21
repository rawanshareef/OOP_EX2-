package gameClient;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class Ex2Test {

	@Test
	void test_loadGraph() {
		File f = new File("data/A0");
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String s = r.readLine();
			r.close();
			Ex2 ex = new Ex2(1, 1);
			ex.loadGraph(s);
			assertEquals(11, ex.getG().getV().size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
