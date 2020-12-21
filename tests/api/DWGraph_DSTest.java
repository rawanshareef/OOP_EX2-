package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest extends Node {

    @Test
    void getNode() {
        DWGraph_DS g = new DWGraph_DS();
        g.addNode(new Node(5));
        g.addNode(new Node(1));
        int s = g.nodeSize();
        assertEquals(2,s);
        g.addNode(new Node(9));
        g.addNode(new Node(8));
        int x = g.nodeSize();
        assertEquals(4,x);
        g.removeNode(9);
        int z = g.nodeSize();
        assertNotEquals(4,z);
        assertEquals(3,z);
        assertNull( g.getNode(20));


    }
    @Test
    void getEdge() {
        DWGraph_DS g = new DWGraph_DS();
        assertNull(g.getEdge(5,6));
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.connect(0,1,1);
        edge_data check=new Edge(1);
        assertEquals(check.getWeight(),g.getEdge(0,1).getWeight());
        assertEquals(g.getEdge(1,3),null);
        assertEquals(g.getEdge(1,0),null);
        g.connect(1,0,3);
        assertEquals(3,g.getEdge(1,0).getWeight());
        g.addNode(new Node(5));
        g.addNode(new Node(30));
        edge_data check2=new Edge(7);
        g.connect(5,30,3);
        assertNotEquals(check2.getWeight(),g.getEdge(5,30).getWeight());


    }

    @Test
    void addNode() {
        DWGraph_DS g = new DWGraph_DS();
        g.addNode(new Node(5));
        g.addNode(new Node(1));
        assertTrue(g.nodeSize()==2);
        g.addNode(new Node(9));
        g.addNode(new Node(8));
        assertFalse(g.nodeSize()==2);
        assertTrue(g.nodeSize()==4);

    }
    @Test
    void connect() {
//        DWGraph_DS g = new DWGraph_DS();
//        Node src=new Node(0);
//        Node des=new Node(1);
//        g.addNode(src);
//        g.addNode(des);
//        g.connect(src.getKey(),des.getKey(),2);
//        edge_data edge=new Edge(src.getKey(),des.getKey(),2);
//        assertEquals(edge.getWeight(),g.getEdge(src.getKey(),des.getKey()).getWeight());
        DWGraph_DS g = new DWGraph_DS();
       Node src=new Node(0);
       Node des=new Node(1);
        g.addNode(src);
        g.addNode(des);
        g.connect(src.getKey(),des.getKey(),1);
        double x=g.getEdge(src.getKey(), des.getKey()).getWeight();
        assertTrue(x==1.0);
//////////////////////////////////////////////////////////////////////////////////////
        g.addNode(new Node(55));
        g.addNode(new Node(11));
        g.addNode(new Node(22));
        g.addNode(new Node(33));
        g.connect(55,11,3);
        assertEquals(3,g.getEdge(55,11).getWeight());
        g.connect(55,22,4);
        assertEquals(4,g.getEdge(55,22).getWeight());
        g.removeNode(55);
        assertNull(g.getEdge(55,11));
        assertNull(g.getEdge(55,22));
        assertEquals(g.edgeSize(),1);


    }

    @Test
    void getV() {
        DWGraph_DS g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        Collection<node_data> v = g.getV();
        Iterator<node_data> iter = v.iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            assertNotNull(n);
        }

    }

    @Test
    void getE() {
        DWGraph_DS g = new DWGraph_DS();
        node_data n1=new Node(0);
        node_data n2=new Node(5);
        node_data n3=new Node(9);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.connect(0,5,1);
        g.connect(0,9,2);
        Collection<edge_data> v = g.getE(n1.getKey());
        Iterator<edge_data> iter = v.iterator();
        while (iter.hasNext()) {
            edge_data n = iter.next();
            assertNotNull(n);
        }

    }

    @Test
    void removeNode() {
        DWGraph_DS g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.removeNode(0);
        assertTrue(g.nodeSize()==3);
        g.addNode(new Node(9));
        g.addNode(new Node(11));
        assertTrue(g.nodeSize()==5);

    }

    @Test
    void removeEdge() {
        DWGraph_DS g = new DWGraph_DS();
        assertTrue(g.edgeSize()==0);
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.connect(0,1,1);
        g.connect(0,2,1);
        assertTrue(g.edgeSize()==2);
        g.removeEdge(0,1);
        assertTrue(g.edgeSize()==1);
        g.removeEdge(0,2);
        assertTrue(g.edgeSize()==0);


    }

    @Test
    void nodeSize() {
        DWGraph_DS g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(2));
        g.removeNode(2);
        int s = g.nodeSize();
        assertEquals(1,s);
        g.addNode(new Node(8));
        g.addNode(new Node(9));
        int h = g.nodeSize();
        assertNotEquals(1,h);
        assertEquals(3,h);
        g.removeNode(8);
        g.removeNode(9);
        g.removeNode(0);
        int f = g.nodeSize();
        assertEquals(0,f);

    }

    @Test
    void edgeSize() {
        DWGraph_DS g = new DWGraph_DS();
        assertTrue(g.edgeSize()==0);
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.connect(0,1,1);
        assertTrue(g.edgeSize()==1);
        g.connect(0,2,1);
        assertTrue(g.edgeSize()==2);
        g.removeEdge(0,2);
        assertTrue(g.edgeSize()==1);
        g.connect(0,3,5);
        assertTrue(g.edgeSize()==2);
        g.removeNode(0);
       // assertTrue(g.edgeSize()==0);






    }

}