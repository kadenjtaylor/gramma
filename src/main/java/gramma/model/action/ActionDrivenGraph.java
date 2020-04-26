package gramma.model.action;

import gramma.model.entities.Edge;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;

public interface ActionDrivenGraph extends Graph {

    boolean addNode(Node node);

    boolean addEdge(Edge edge);

    boolean removeNodeById(String nodeId);

    boolean removeEdgeById(String edgeId);

    default boolean accept(GraphAction action) {
        switch (action.type()) {
            case ADD_EDGE:
                return addEdge(action.edge());
            case ADD_NODE:
                return addNode(action.node());
            case REMOVE_EDGE_BY_ID:
                return removeEdgeById(action.id());
            case REMOVE_NODE_BY_ID:
                return removeNodeById(action.id());
            default:
                return false;
        }
    }
}