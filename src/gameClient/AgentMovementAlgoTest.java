package gameClient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import api.DWGraph_DS;
import api.Node;
import api.directed_weighted_graph;

class AgentMovementAlgoTest {
	
	@Test
	void test_initNextNode() {
		List<CL_Agent> agents = new LinkedList<CL_Agent>();
		List<CL_Pokemon> pokemons = new LinkedList<CL_Pokemon>();
		directed_weighted_graph g = new DWGraph_DS();
		g.addNode(new Node(0));
		g.addNode(new Node(1));
		g.addNode(new Node(2));
		g.addNode(new Node(3));
		g.addNode(new Node(4));
		g.connect(0, 1, 1);g.connect(1, 0, 2);
		g.connect(0, 2, 8);g.connect(2, 0, 3);
		g.connect(0, 4, 5);g.connect(4, 0, 3);
		g.connect(1, 2, 2);g.connect(2, 1, 10);
		g.connect(2, 4, 6);g.connect(4, 2, 1);
		g.connect(4, 3, 2);g.connect(3, 4, 1);
		AgentMovementAlgo am = new AgentMovementAlgo(g);
		agents.add(new CL_Agent(g , 0));
		pokemons.add(new CL_Pokemon(null, -1, 5, 0, g.getEdge(2, 0)));
		am.initNextNode(agents, pokemons);
		assertEquals(1, am.nextNode(agents.get(0)));
	}
	
	
}
