package api;

import java.util.Objects;

public class Edge implements edge_data{
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Edge(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = "";
        this.tag = 0;
    }
    public Edge() {
        this.src = 0;
        this.dest = 0;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
    }
    public Edge(double w) {
        this.src = 0;
        this.dest = 0;
        this.weight = w;
        this.info = "";
        this.tag = 0;
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return src;
    }
    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return dest;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info=s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }
    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag=t;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return src == edge.src &&
                Double.compare(edge.weight, weight) == 0 &&
                tag == edge.tag &&
                dest == edge.dest &&
                Objects.equals(info, edge.info);
    }
}
