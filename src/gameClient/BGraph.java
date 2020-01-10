//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gameClient;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import oop_elements.OOP_Edge;
import oop_elements.OOP_NodeData;
import oop_utils.OOP_Point3D;
import org.json.JSONArray;
import org.json.JSONObject;

import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_dataStructure.oop_node_data;

public class BGraph  {
    public static final double EPS1 = 1.0E-4D;
    public static final double EPS2 = Math.pow(1.0E-4D, 2.0D);
    public static final double EPS;
    private HashMap<Integer, oop_node_data> _V;
    private HashMap<Integer, HashMap<Integer, oop_edge_data>> _E;
    public static final int UNDIRECTED = 0;
    public static final int DIRECTED = 1;
    private int _mc;
    private int _type;
    private int e_count;

    static {
        EPS = EPS2;
    }

    public BGraph() {
        this(1);
    }

    public BGraph(int type) {
        this.init();
        this._type = type;
        this.e_count = 0;
        this._mc = 0;
    }

    public BGraph(String file_name) {
        try {
            this.init();
            OOP_NodeData.resetCount();
            Scanner scanner = new Scanner(new File(file_name));
            String jsonString = scanner.useDelimiter("\\A").next();
            scanner.close();
            JSONObject graph = new JSONObject(jsonString);
            JSONArray nodes = graph.getJSONArray("Nodes");
            JSONArray edges = graph.getJSONArray("Edges");

            int i;
            int s;
            for(i = 0; i < nodes.length(); ++i) {
                s = nodes.getJSONObject(i).getInt("id");
                String pos = nodes.getJSONObject(i).getString("pos");
                OOP_Point3D p = new OOP_Point3D(pos);
                this.addNode(new OOP_NodeData(s, p));
            }

            for(i = 0; i < edges.length(); ++i) {
                s = edges.getJSONObject(i).getInt("src");
                int d = edges.getJSONObject(i).getInt("dest");
                double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    public int getType() {
        return this._type;
    }

    public int getMC() {
        return this._mc;
    }

    public int nodeSize() {
        return this._V.size();
    }

    public int edgeSize() {
        return this.e_count;
    }

    public Collection<oop_node_data> getV() {
        return this._V.values();
    }

    public oop_edge_data getEdge(int a, int b) {
        return (oop_edge_data)((HashMap)this._E.get(a)).get(b);
    }

    public void addNode(oop_node_data n) {
        HashMap<Integer, oop_edge_data> tt = new HashMap();
        this._V.put(n.getKey(), n);
        this._E.put(n.getKey(), tt);
        ++this._mc;
    }

    public oop_node_data getNode(int key) {
        return (oop_node_data)this._V.get(key);
    }

    public void connect(oop_node_data a, oop_node_data b, OOP_Edge e) {
        this.put(e);
    }

    public void connect(int a, int b, double w) {
        oop_node_data aa = this.getNode(a);
        oop_node_data bb = this.getNode(b);
        if (aa != null && bb != null) {
            OOP_Edge e = new OOP_Edge(a, b, w);
            this.put(e);
        } else {
            throw new RuntimeException("ERR: can NOT connect nodes which are not in the graph! got(" + a + "," + b + ")");
        }
    }

    private void put(oop_edge_data e) {
        HashMap<Integer, oop_edge_data> v_out = (HashMap)this._E.get(e.getSrc());
        v_out.put(e.getDest(), e);
        ++this.e_count;
        ++this._mc;
    }

    public oop_node_data getNode(oop_node_data n) {
        return (oop_node_data)this._V.get(n.getKey());
    }

    public oop_node_data removeNode(int key) {
        oop_node_data v = (oop_node_data)this._V.remove(key);
        int t = ((HashMap)this._E.remove(key)).size();
        this.e_count -= t;
        Iterator iter = this.getV().iterator();

        while(iter.hasNext()) {
            oop_node_data d = (oop_node_data)iter.next();
            int dd = d.getKey();
            HashMap<Integer, oop_edge_data> ni = (HashMap)this._E.get(dd);
            if (ni.containsKey(key)) {
                oop_edge_data e = (oop_edge_data)ni.remove(key);
                if (e != null) {
                    --this.e_count;
                }
            }
        }

        ++this._mc;
        return v;
    }

    public oop_edge_data removeEdge(int src, int dest) {
        oop_edge_data ans = (oop_edge_data)((HashMap)this._E.get(src)).remove(dest);
        if (ans != null) {
            --this.e_count;
            ++this._mc;
        }

        return ans;
    }

    oop_node_data getNodeByKey(int k) {
        return (oop_node_data)this._V.get(k);
    }

    public String toString() {
        return this.toJSON();
    }

    public String toString1() {
        String ans = this.getClass().getName() + "|V|=" + this.getV().size() + ", |E|=" + this.edgeSize() + "\n";

        for(Iterator iter = this.getV().iterator(); iter.hasNext(); ans = ans + " * new Node *\n") {
            oop_node_data c = (oop_node_data)iter.next();
            ans = ans + c.toString() + "\n";

            oop_edge_data ee;
            for(Iterator ei = this.getE(c.getKey()).iterator(); ei.hasNext(); ans = ans + ee.toString() + "\n") {
                ee = (oop_edge_data)ei.next();
            }
        }

        return ans;
    }

    public void init(String jsonSTR) {
        try {
            OOP_NodeData.resetCount();
            this.init();
            this.e_count = 0;
            JSONObject graph = new JSONObject(jsonSTR);
            JSONArray nodes = graph.getJSONArray("Nodes");
            JSONArray edges = graph.getJSONArray("Edges");

            int i;
            int s;
            for(i = 0; i < nodes.length(); ++i) {
                s = nodes.getJSONObject(i).getInt("id");
                String pos = nodes.getJSONObject(i).getString("pos");
                OOP_Point3D p = new OOP_Point3D(pos);
                this.addNode(new OOP_NodeData(s, p));
            }

            for(i = 0; i < edges.length(); ++i) {
                s = edges.getJSONObject(i).getInt("src");
                int d = edges.getJSONObject(i).getInt("dest");
                double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public String toJSON() {
        JSONObject allEmps = new JSONObject();
        JSONArray VArray = new JSONArray();
        JSONArray EArray = new JSONArray();
        Collection<oop_node_data> V = this.getV();
        Iterator<oop_node_data> iter = V.iterator();
        Collection<oop_edge_data> E = null;
        Iterator itr = null;

        try {
            while(iter.hasNext()) {
                oop_node_data nn = (oop_node_data)iter.next();
                int n = nn.getKey();
                String p = nn.getLocation().toString();
                JSONObject node = new JSONObject();
                node.put("id", n);
                node.put("pos", p);
                VArray.put(node);
                itr = this.getE(n).iterator();

                while(itr.hasNext()) {
                    oop_edge_data ee = (oop_edge_data)itr.next();
                    JSONObject edge = new JSONObject();
                    edge.put("src", ee.getSrc());
                    edge.put("dest", ee.getDest());
                    edge.put("w", ee.getWeight());
                    EArray.put(edge);
                }
            }

            allEmps.put("Nodes", VArray);
            allEmps.put("Edges", EArray);
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        return allEmps.toString();
    }

    private void init() {
        this._V = new HashMap();
        this._E = new HashMap();
    }

    public Collection<oop_edge_data> getE(int node_id) {
        return ((HashMap)this._E.get(node_id)).values();
    }


}