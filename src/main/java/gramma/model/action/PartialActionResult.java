package gramma.model.action;

import gramma.model.entities.Edge;
import gramma.model.entities.Node;

public class PartialActionResult {

    private final GraphAction graphAction;
    private final String id;
    private final String label;

    public PartialActionResult(GraphAction graphAction, String id, String label) {
        this.graphAction = graphAction;
        this.id = id;
        this.label = label;
    }

    public GraphAction getGraphAction() {
        return graphAction;
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public static PartialActionResult addNode(Node node) {
        GraphAction addNodeAction = GraphAction.addNode(node);
        return new PartialActionResult(addNodeAction, null, null);
    }

    public static PartialActionResult addEdge(Edge edge) {
        GraphAction addEdgeAction = GraphAction.addEdge(edge);
        return new PartialActionResult(addEdgeAction, null, null);
    }

    public static PartialActionResult addNodeWithLabel(Node node, String label) {
        GraphAction addNodeAction = GraphAction.addNode(node);
        return new PartialActionResult(addNodeAction, node.id(), label);
    }

    public static PartialActionResult addEdgeWithLabel(Edge edge, String label) {
        GraphAction addEdgeAction = GraphAction.addEdge(edge);
        return new PartialActionResult(addEdgeAction, edge.id(), label);
    }

}