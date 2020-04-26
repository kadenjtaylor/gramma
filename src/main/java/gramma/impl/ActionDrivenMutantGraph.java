package gramma.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gramma.model.action.ActionDrivenGraph;
import gramma.model.action.GraphAction;
import gramma.model.entities.Edge;
import gramma.model.entities.Node;
import gramma.model.mutate.MutantGraph;
import gramma.model.mutate.Mutation;

public class ActionDrivenMutantGraph implements MutantGraph, ActionDrivenGraph {

    private final Map<String, Node> nodesById;
    private final Map<String, Edge> edgesById;

    public ActionDrivenMutantGraph() {
        this.nodesById = new HashMap<>();
        this.edgesById = new HashMap<>();
    }

    // ---- from Graph interface ---- //
    @Override
    public Set<Node> nodes() {
        return ImmutableSet.copyOf(nodesById.values());
    }

    @Override
    public Set<Edge> edges() {
        return ImmutableSet.copyOf(edgesById.values());
    }

    @Override
    public Optional<Node> getNodeById(String id) {
        return Optional.ofNullable(nodesById.get(id));
    }

    @Override
    public Optional<Edge> getEdgeById(String id) {
        return Optional.ofNullable(edgesById.get(id));
    }

    // ---- from ActionDrivenGraph interface ---- //

    @Override
    public boolean addNode(Node node) {
        nodesById.put(node.id(), node);
        return true;
    }

    @Override
    public boolean addEdge(Edge edge) {
        edgesById.put(edge.id(), edge);
        return true;
    }

    @Override
    public boolean removeNodeById(String nodeId) {
        return nodesById.remove(nodeId) != null;
    }

    @Override
    public boolean removeEdgeById(String edgeId) {
        return edgesById.remove(edgeId) != null;
    }

    // ---- from MutantGraph interface ---- //

    /*
     * No transactional guarantees here, but you'll know if everything worked
     */
    @Override
    public boolean apply(Mutation mutation) {
        boolean success = true;
        List<GraphAction> graphActions = mutation.compile();
        for (GraphAction action : graphActions) {
            success &= this.accept(action);
        }
        return success;
    }
}