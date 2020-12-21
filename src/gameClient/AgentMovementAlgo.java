package gameClient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.node_data;

/**
 * This class is used for calculate algorithms for agents movements, and picking the next good pokemon.
 *
 */
public class AgentMovementAlgo {
	private DWGraph_Algo algo;
	private HashMap<CL_Agent, HashMap<CL_Pokemon, Double>> dists;

	/**
	 * constructor for init graph algorithms and game info for algorithms.
	 * @param game
	 * @param g
	 */
	public AgentMovementAlgo(directed_weighted_graph g) {
		this.algo = new DWGraph_Algo();
		algo.init(g);
	}

	/**
	 * The method initializes the routes of all the agents later to the location of the Pokemon 
	 * so that when calling nextNode the next corresponding node is returned.
	 * The method produces for each agent the series of Pokemon that he needs to catch in order from near to far 
	 * and then commands him to the nearest Pokemon that has not been taken by a closer agent.
	 * @param agents
	 * @param pokemons
	 */
	public void initNextNode(List<CL_Agent> agents, List<CL_Pokemon> pokemons) {
		dists = new HashMap<CL_Agent, HashMap<CL_Pokemon,Double>>();
		for (int i = 0; i < agents.size(); i++) {
			buildPathForallPokemons(agents.get(i), pokemons);
		}
		for (int i = 0; i < agents.size(); i++) {
			setNextPokemon(agents.get(i), agents, pokemons);
		}
	}

	private void setNextPokemon(CL_Agent a, List<CL_Agent> agents, List<CL_Pokemon> pokemons) {
		a.set_curr_fruit(null);
		double min = Double.POSITIVE_INFINITY;
		CL_Pokemon chosen = null;
		for(CL_Pokemon p : pokemons) {
			double d = dists.get(a).get(p);
			if(notTaken(agents, p) && bestAgent(agents, a, p,d) && d < min) {
				min = d;
				chosen = p;
			}
		}
		if(chosen == null) {
			min = Double.POSITIVE_INFINITY;
			for(CL_Pokemon p : pokemons) {
				double d = dists.get(a).get(p);
				if(notTaken(agents, p) && d < min) {
					min = d;
					chosen = p;
				}
			}	
		}
		a.set_curr_fruit(chosen);
	}

	private boolean bestAgent(List<CL_Agent> agents, CL_Agent me, CL_Pokemon p, double d) {
		for(CL_Agent a : agents) {
			if(a != me && dists.get(a).get(p)*1.2 < d) return false;
		}
		return true;
	}

	private boolean notTaken(List<CL_Agent> agents, CL_Pokemon p) {
		for(CL_Agent a : agents) {
			if(a.get_curr_fruit() == p) return false;
		}
		return true;
	}

	private void buildPathForallPokemons(CL_Agent a, List<CL_Pokemon> pokemons) {
		List<CL_Pokemon> pok = new LinkedList<CL_Pokemon>();
		HashMap<CL_Pokemon, Double> pdists = new HashMap<CL_Pokemon, Double>();
		for(CL_Pokemon p : pokemons) pok.add(p);
		int src = a.getSrcNode();
		double totalDist = 0;
		while(!pok.isEmpty()) {
			double min_dist = Double.POSITIVE_INFINITY;
			int min_index = -1;
			for (int i = 0; i < pok.size(); i++) {
				double d = algo.shortestPathDist(src, pok.get(i).get_edge().getSrc()) + pok.get(i).get_edge().getWeight();
				if(d < min_dist) {
					min_dist = d;
					min_index = i;
				}
			}
			src = pok.get(min_index).get_edge().getDest();
			totalDist += min_dist;
			pdists.put(pok.remove(min_index), totalDist);
		}
		dists.put(a, pdists);
	}

	/**
	 *  Return the first node on shortest path between the given agent and his closets pokemon.
	 * @param i
	 * @return thee next node to go to
	 */
	public int nextNode(CL_Agent a) {
		if(a == null || a.get_curr_fruit() == null) return -1;
		edge_data e = a.get_curr_fruit().get_edge();
		if(e.getSrc() == a.getSrcNode()) return e.getDest();
		List<node_data> p = algo.shortestPath(a.getSrcNode(), e.getSrc());
		if(p == null) return -1;
		return p.get(1).getKey();
	}
}
