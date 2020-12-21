package api;

import java.util.Objects;

public class Node implements node_data {
    private int key;
    private String info;
    private int tag;
    private double weight;
    private geo_location geo;

    public Node(int key) {
        this.key = key;
        this.tag = 0;
        this.weight = 0;
        this.geo = null;
        this.info="";
    }
    public Node() {
        this.key = 0;
        this.tag = 0;
        this.weight = 0;
        this.geo = null;
        this.info="";
    }

    public Node(int key, int tag, double weight, geo_location geo) {
        this.key = key;
        this.tag = tag;
        this.weight = weight;
        this.geo = geo;
    }
    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }
    /** Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return this.geo;
    }
    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.geo=p;
    }
    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight=w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
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
        return this.tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
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
        Node node = (Node) o;
        return key == node.key &&
                tag == node.tag &&
                Double.compare(node.weight, weight) == 0 &&
                Objects.equals(info, node.info) &&
                Objects.equals(geo, node.geo);
    }

}
