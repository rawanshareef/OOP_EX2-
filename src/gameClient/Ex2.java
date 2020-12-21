package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.newGraph;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Ex2 implements Runnable{
	private MyFrame _win;
	private Arena _ar;
	private game_service game;
	private directed_weighted_graph g;
	private AgentMovementAlgo movement;
	private int scenario_num, id;

	/**
	 * Main method to enter the id and the level and starts the thread of the game.
	 * @param a
	 */
	public static void main(String[] a) {
		int id = -1, scenario_num = -1;
		try {
			if(a.length > 0) Integer.parseInt(a[0]);
			if(a.length > 1) Integer.parseInt(a[1]);
			if(id == -1 || scenario_num == -1) {
				id = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter id")); 
				scenario_num = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter level")); 
			}
		} catch (Exception e) {
			id = 0;
			scenario_num = 0;
		}
		Thread client = new Thread(new Ex2(id, scenario_num)); // 316580414
		client.start();
	}

	/**
	 * constructor. login into the server with id and level and inits the main components: the game graph, agents, pokemons and the gui
	 * @param id
	 * @param scenario_num
	 */
	public Ex2(int id, int scenario_num) {
		this.id = id;
		this.scenario_num = scenario_num;
		game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		game.login(id);
		String gg = game.getGraph();
		loadGraph(gg);
		movement = new AgentMovementAlgo(getG());
		init();
	}

	/**
	 * Game process Thread. running the game and stops when game is over.
	 * updating the game's frame and movements.
	 */
	@Override
	public void run() {
		game.startGame();
		long dt=100;
		while(game.isRunning()) {
			moveAgants();
			try {
				_win.repaint();
				_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
				dt = getDelayTime();
				Thread.sleep(dt);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();
		System.out.println(res);
		System.exit(0);
	}

	/**
	 * Calculates the delay time between calls of the move() mathod of game server.
	 * Calculates the time according the agents speed, position and pokemons position to the next event: 
	 * getting into next node or next pokemon.
	 * @return delay time
	 */
	private long getDelayTime() {
		//		System.out.println(g.getEdge(21, 32).getWeight());
		List<CL_Agent> agents = _ar.getAgents();
		List<CL_Pokemon> pokemons = _ar.getPokemons();
		boolean pok = false;
		int speed = 1;
		double min = Double.POSITIVE_INFINITY;
		for(CL_Agent a : agents) {
			if(a.getSpeed() > speed) speed = (int) a.getSpeed();
			for(CL_Pokemon p : pokemons) {
				//				System.out.println(p.get_edge());
				if(p.get_edge() == a.get_curr_edge()) {
					edge_data e = a.get_curr_edge();
					double d = getG().getNode(e.getSrc()).getLocation().distance(getG().getNode(e.getDest()).getLocation());
					double pDist = p.getLocation().distance(a.getLocation()) / d;
					pDist = 1000 * e.getWeight() * pDist/(a.getSpeed()) - 5;
					if(pDist < min) min = pDist;
				}
			}
			if(a.getNextNode() != -1) {
				edge_data e = a.get_curr_edge();
				double d = getG().getNode(e.getSrc()).getLocation().distance(getG().getNode(e.getDest()).getLocation());
				double dist = 1 - a.getLocation().distance(getG().getNode(e.getSrc()).getLocation()) / d;
				dist = 1000 * e.getWeight() * dist/(a.getSpeed());
				if(dist < min) min = dist;
			}
		}
		//		System.out.println(min);
		if(min != Double.POSITIVE_INFINITY) {
			return Math.min(180, (long) Math.ceil(min));
		}
		else return 0;
	}

	/** 
	 * Moves each of the agents along the edge,
	 * if agent has no destination then choosing new next node by using the algoithms from AgentMevementAlgo class.
	 */
	private void moveAgants() {
		String lg = game.move();
		List<CL_Agent> agents = Arena.getAgents(lg, getG());
		_ar.setAgents(agents);
		String pokemons_json =  game.getPokemons();
		List<CL_Pokemon> pokemons = Arena.json2Pokemons(pokemons_json);
		for(int a = 0;a<pokemons.size();a++) { Arena.updateEdge(pokemons.get(a),getG());}
		_ar.setPokemons(pokemons);
		movement.initNextNode(agents, pokemons);
		for (int i = 0; i < agents.size(); i++) {
			CL_Agent a = agents.get(i);
			if(a.getNextNode() == -1) {
				int next = movement.nextNode(a);
				game.chooseNextEdge(a.getID(), next); 
				System.out.println("Agent: "+ a.getID() +", val: "+ a.getValue() +"   turned to node: "+ next);
			}
		}
	}

	/**
	 * Inits gui frame of the game and inits the game pokemons and agents.
	 * put the pokemons from the game info and then put the agents.
	 * The place of agents are the node near to pokemons with highest values.
	 */
	private void init() {
		String fs = game.getPokemons();
		_ar = new Arena();
		_ar.setGraph(getG());
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2", game);
		_win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_win.setSize(1000, 700);
		_win.update(_ar);
		_win.setVisible(true);

		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int numOfAgents = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<pokemons.size();a++) { Arena.updateEdge(pokemons.get(a),getG());}
			Collections.sort(pokemons, new Comparator<CL_Pokemon>() {

				@Override
				public int compare(CL_Pokemon o1, CL_Pokemon o2) {
					if(o1.getValue() > o2.getValue()) return -1;
					return 1;
				}

			});
			for(int a = 0;a<numOfAgents;a++) {
				int ind = a%pokemons.size();
				CL_Pokemon c = pokemons.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				game.addAgent(nn);
			}

		}
		catch (JSONException e) {e.printStackTrace();}
	}

	/**
	 * Inits the game graph from Json string.
	 * @param json
	 */
	public void loadGraph(String json) {
		try{
			GsonBuilder builder=new GsonBuilder();
			builder.registerTypeAdapter(directed_weighted_graph.class,new newGraph());
			Gson gson=builder.create();
			StringReader reader=new StringReader(json);
			g = gson.fromJson(reader,directed_weighted_graph.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public directed_weighted_graph getG() {
		return g;
	}
}
