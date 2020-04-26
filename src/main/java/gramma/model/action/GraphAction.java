package gramma.model.action;

import gramma.model.entities.Edge;
import gramma.model.entities.Node;

public class GraphAction {

    private final ActionType type;
    private final Edge edge;
    private final Node node;
    private final String id;

    private GraphAction(ActionType type, Edge edge, Node node, String id) {
        this.type = type;
        this.edge = edge;
        this.node = node;
        this.id = id;
    }

    public static GraphAction addNode(Node node){
        return new GraphAction(ActionType.ADD_NODE, null, node, null);
    }

    public static GraphAction addEdge(Edge edge){
        return new GraphAction(ActionType.ADD_EDGE, edge, null, null);
    }

    public static GraphAction removeNodeById(String nodeId){
        return new GraphAction(ActionType.REMOVE_NODE_BY_ID, null, null, nodeId);
    }

    public static GraphAction removeEdgeById(String edgeId){
        return new GraphAction(ActionType.REMOVE_EDGE_BY_ID, null, null, edgeId);
    }

    public ActionType type() {
        return type;
    }

    public Edge edge() {
        return edge;
    }

    public Node node() {
        return node;
    }

    public String id() {
        return id;
    }
}