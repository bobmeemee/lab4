package Server;

import Utils.HashFunction;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



@RestController
public class NamingServer extends Thread{
    private final CustomMap nodeMap;
    private final HashMap<Integer, Integer> fileMap;

    public NamingServer() {
        nodeMap = new CustomMap();
        fileMap = new HashMap<Integer, Integer>();
    }

    @PostMapping("/NamingServer/Nodes/{node}")
    public String addNodeREST(@PathVariable(value = "node") String name, @RequestBody String IP) throws IOException {
        int nodeID = HashFunction.hash(name);
        if (nodeMap.putIfAbsent(nodeID, IP) == null) {
            nodeMap.exportMap();
            return "[NS REST] Node added with ID=" + nodeID + "!";
        } else {
            return "[NS REST] This name is not available!\n";
        }
    }


    @DeleteMapping("/NamingServer/Nodes/{node}")
    public String removeNodeREST(@PathVariable(value = "node") String name) throws IOException {
        int nodeID = HashFunction.hash(name);
        if(nodeMap.remove(nodeID) == null) {
            return "[NS REST] Node " + name + " does not exist\n" ;

        } else {
            nodeMap.exportMap();
            return "[NS REST] Removed node " + name + "\n";
        }
    }


    @GetMapping("/NamingServer/Nodes/{node}")
    public String getNodes(@PathVariable(value = "node") String name){
        int nodeID = HashFunction.hash(name);
        String send;
        Set<Map.Entry<Integer,String>> entries = nodeMap.entrySet();
        if(nodeMap.containsKey(nodeID)) {
            int i = 1;
            StringBuilder nodes = new StringBuilder();

            for (Map.Entry<Integer, String> entry : entries) {
                nodes.append("Node #");
                nodes.append(i);
                nodes.append(": ");
                nodes.append(entry.getKey().toString());
                nodes.append(" with IP ");
                nodes.append(entry.getValue());
                nodes.append(",");
                i++;
            }

            send = "{\"Node status\":\"node exists\"," + "\"Node hash\":" + nodeID + "," +
                    "\"Nodes in network\":" + nodeMap.size() +
                    "\"All nodes\":\"" + nodes.toString() + "\"}";
        }
        else{
            send = "{\"Node does not exist\"}";
        }
        return send;
    }

    public String addNode(String name, String IP) throws IOException {
        int nodeID = HashFunction.hash(name);
        if (nodeMap.putIfAbsent(nodeID, IP) == null) {
            nodeMap.exportMap();
            return "Added node" + "node to database\n";
        } else {
            return "Name " + name + " not available\n";
        }
    }

    public String deleteNode(String name) throws IOException {
        int nodeID = HashFunction.hash(name);
        if(nodeMap.remove(nodeID) == null) {
            return "Node" + name + " does not exist\n";
        } else {
            nodeMap.exportMap();
            return "Node " + name + "is deleted\n";
        }
    }


    public void run(){
        System.out.println("Starting NameServer...");
        NamingServer nameServer = new NamingServer();
    }
}
