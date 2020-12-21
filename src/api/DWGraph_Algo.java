package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
             this.g=g;

    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.g;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph paste = new DWGraph_DS();
        for (node_data present : g.getV()) {
            paste.addNode(present);
        }
        for (node_data present : g.getV()) {
            for (edge_data my_edge : g.getE(present.getKey())) {
                paste.connect(my_edge.getSrc(), my_edge.getDest(),my_edge.getWeight());
            }
        }
        return paste;
    }

    private void reset_tag_to0(directed_weighted_graph g) {//reset tage for all vertex to zero
        for (node_data n : g.getV()) {
            n.setTag(0);
        }
    }
    private void change_graph_to_unvisited() {//change all the info to false

        for (node_data node : g.getV()) {
            node.setInfo("");
        }
    }

    private void change_dist_to_infinity() {// change all the tage to infinity
        double dist = Double.MAX_VALUE;
        for (node_data node : g.getV()) {
            node.setWeight(dist);
        }
    }

    private directed_weighted_graph revers(directed_weighted_graph graph){
        directed_weighted_graph new_g = new DWGraph_DS();
        for (node_data present : g.getV()) {
            new_g.addNode(present);
        }
        for (node_data present : g.getV()) {
            for (edge_data my_edge : g.getE(present.getKey())) {
                new_g.connect( my_edge.getDest(),my_edge.getSrc(),my_edge.getWeight());
            }
        }
        return new_g;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g.getV().isEmpty() || g.getV().size() == 1) {//if the graph empty or contain only one node
            return true;
        } else {//the graph not empty
            reset_tag_to0(g);
            node_data first = g.getV().iterator().next();
            Queue<node_data> queue = new LinkedList<>();
            List<Integer> sum=new ArrayList<>();
            queue.add(first);
            while (!queue.isEmpty()) {
                first = queue.poll();
                first.setTag(2);
                for (edge_data s : g.getE(first.getKey())) {
                    node_data check = g.getNode(s.getDest());
                    if (check.getTag() == 0) {
                        queue.add(check);
                        s.setTag(1);
                    }
                }
                sum.add(first.getKey());
            }
            if(sum.size()!=g.nodeSize()){
                return false;
            }
            else {

                directed_weighted_graph new_g = revers(g);
                reset_tag_to0(new_g);
                node_data first2 = g.getV().iterator().next();
                Queue<node_data> queue2 = new LinkedList<>();
                List<Integer> sum2=new ArrayList<>();
                queue2.add(first);
                while (!queue.isEmpty()) {
                    first2 = queue.poll();
                    first2.setTag(2);
                    for (edge_data s : g.getE(first2.getKey())) {
                        node_data check = g.getNode(s.getDest());
                        if (check.getTag() == 0) {
                            queue.add(check);
                            s.setTag(1);
                        }
                    }
                    sum.add(first2.getKey());
                }
                if(sum2.size()!=queue.size()){
                    return false;
                }
                    }

            }
                    return true;

}

    @Override
    public double shortestPathDist(int src, int dest) {
        node_data srcNode = g.getNode(src);
        node_data destNode = g.getNode(dest);
        if (g.getV().size() == 0 || g.getNode(src) == null || g.getNode(dest) == null) {
            return -1;
        }
        else if (src == dest) {
            return 0;
        }
        ShortPathCheck(src);
        //check if reached the dest
        if(destNode.getWeight()<0||destNode.getWeight()==Double.MAX_VALUE)return  -1;
            return destNode.getWeight();
        }

    private HashMap<node_data,node_data> ShortPathCheck (int src){
            node_data srcNode = g.getNode(src);
            change_dist_to_infinity();
            change_graph_to_unvisited();
            HashMap<node_data,node_data> par=new HashMap<>();
            PriorityQueue<node_data> queue = new PriorityQueue<node_data>(new Comparator<node_data>() {

				@Override
				public int compare(node_data o1, node_data o2) {
					if(o1.getWeight() < o2.getWeight()) return -1;
					return 1;
				}
			});
            srcNode.setWeight(0);
            queue.add(srcNode);
            par.put(srcNode,null);
            while (!queue.isEmpty()) {
                node_data first = queue.poll();
                if(!queue.contains(first)){
                for (edge_data n : g.getE(first.getKey())) {//check the first edges conecct
                    double weight = first.getWeight() + n.getWeight();
                    if (g.getNode(n.getDest()).getInfo() == "" && weight < g.getNode(n.getDest()).getWeight()) {
                        g.getNode(n.getDest()).setWeight(weight);
                        par.put(g.getNode(n.getDest()),first);
                        queue.remove(g.getNode(n.getDest()));
                        queue.add(g.getNode(n.getDest()));
                    }
                }
                    first.setInfo("1");
            }
            }
            return par;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_Algo that = (DWGraph_Algo) o;
        return g.equals(that.g);
    }


    @Override
    public List<node_data> shortestPath(int src, int dest) {

        node_data srcNode = g.getNode(src);
        node_data destNode = g.getNode(dest);
        if (g.getV().size() == 0 || g.getNode(src) == null || g.getNode(dest) == null) {
            return null;
        }
        if (shortestPathDist(src, dest)<0) {
            return null;
        }
        HashMap<node_data, node_data> p = ShortPathCheck(src);
        if(p.get(g.getNode(dest)) == null) return null;
        List<node_data> road = new LinkedList<>();
        node_data loop = destNode;
        while (loop != null) {
            road.add(0,loop);
            loop = p.get(loop);
        }
        return road;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) throws IOException {

        //make json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json =gson.toJson(g);
        //write json to file
        try{
            PrintWriter pw=new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try{
            GsonBuilder builder=new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class,new newGraph());
            Gson gson=builder.create();
            FileReader reader=new FileReader(file) ;
            directed_weighted_graph gr=gson.fromJson(reader,directed_weighted_graph.class);
            System.out.println(gr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}