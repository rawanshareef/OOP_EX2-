package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> vertex;
    private HashMap<Integer, HashMap<Integer, edge_data>> parents;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private int numberOfEdges;
    private int mc;


    public DWGraph_DS() {
        this.vertex = new HashMap<>();
        this.edges = new HashMap<>();
        this.parents = new HashMap<>();
        this.numberOfEdges = 0;
        this.mc = 0;
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (vertex.containsKey(key)) {
            return vertex.get(key);
        }
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (src != dest) {
            if (vertex.containsKey(src) && vertex.containsKey(dest)) {
                if (edges.get(src) != null)
                    return edges.get(src).get(dest);
            }
        }
        return null;

    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!vertex.containsKey(n.getKey())) {
            this.vertex.put(n.getKey(), n);
            this.edges.put(n.getKey(), new HashMap<>());
            this.parents.put(n.getKey(), new HashMap<>());
            mc++;
        } else {
            throw new NullPointerException("This key node is already available ");
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(w>0&&src!=dest) {
            if (vertex.containsKey(src) && vertex.containsKey(dest)) {
                edge_data new_edge=new Edge(getNode(src).getKey(), getNode(dest).getKey(), w);

                if (!edges.containsKey(src)) {
                    HashMap<Integer, edge_data> new_hash = new HashMap<>();
                    new_hash.put(dest,new_edge );
                    edges.put(src, new_hash);
                } else {
                    if (edges.get(src).containsKey(dest)) {
                        HashMap<Integer, edge_data> new_hash = new HashMap<Integer, edge_data>();
                        new_hash.replace(dest, new_edge);
                        edges.replace(src, new_hash);
                    } else {
                        HashMap<Integer, edge_data> new_hash = new HashMap(edges.get(src));
                        new_hash.put(dest, new_edge);
                        edges.replace(src, new_hash);
                    }
                }
            }
            numberOfEdges++;
            mc++;
        }

    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return vertex.values();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (edges.containsKey(node_id)) {
            return edges.get(node_id).values();
        }
        else {
            return new HashMap<Integer, edge_data>().values();
        }
    }
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        node_data remove = vertex.get(key);
        if (vertex.containsKey(key)) {
            if (vertex.size() == 1) {
                vertex.remove(key);
                edges.remove(key);
                parents.remove(key);
            } else {
                Collection<edge_data> e=getE(key);
                Iterator<edge_data> itr = e.iterator();
                while(itr.hasNext()){
                    edge_data ed=itr.next();
                    itr.remove();
                    removeEdge(key,ed.getDest());
                }
                edges.remove(key);
                for (int source : parents.get(key).keySet()){
                    removeEdge(source, key);
                }
                vertex.remove(key);
                mc++;
            }
        }
        else{
            throw new IllegalArgumentException("ITS Not Exist In Graph");
        }
        return remove;

    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(vertex.containsKey(src)&&vertex.containsKey(dest)){
            if(edges.containsKey(src)&&edges.containsKey(dest)){
                mc++;
                numberOfEdges--;
                parents.get(dest).remove(src);
                return  edges.get(src).remove(dest);
            }
        }
        return null;
    }
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return vertex.size();
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return numberOfEdges;
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return numberOfEdges == that.numberOfEdges &&
                mc == that.mc &&
                vertex.equals(that.vertex) &&
                edges.equals(that.edges);
    }

}
