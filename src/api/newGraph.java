package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class newGraph implements JsonDeserializer<directed_weighted_graph> {


    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        directed_weighted_graph g=new DWGraph_DS();
        JsonObject jsonObject= jsonElement.getAsJsonObject();
        JsonArray vertex=jsonObject.get("Nodes").getAsJsonArray();//[...]
        JsonArray edges=jsonObject.get("Edges").getAsJsonArray();
        //pass to all elements
        for(JsonElement n:vertex){

            int key_node=n.getAsJsonObject().get("id").getAsInt();//get value for the key
            node_data my_node=new Node(key_node);
            String[] point3d=n.getAsJsonObject().get("pos").getAsString().split(",");
             double _x=Double.parseDouble(point3d[0]);
            double _y=Double.parseDouble(point3d[1]);
            double _z=Double.parseDouble(point3d[2]);
            geo_location ge=new geolocation(_x,_y,_z);
            my_node.setLocation(ge);
            g.addNode(my_node);

        }
        for(JsonElement e:edges) {
            int src=e.getAsJsonObject().get("src").getAsInt();
            int des=e.getAsJsonObject().get("dest").getAsInt();
            double w=e.getAsJsonObject().get("w").getAsDouble();
            g.connect(src,des,w);
        }
        return g;
    }
}
