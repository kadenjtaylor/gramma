package gramma.impl;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gramma.model.entities.Edge;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;


public class ImmutableGraph implements Graph {

    private final Set<Node> nodes;
    private final Set<Edge> edges;

    private ImmutableGraph(Set<Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public static ImmutableGraph of(Set<Node> nodes, Set<Edge> edges) {
        return new ImmutableGraph(ImmutableSet.copyOf(nodes), ImmutableSet.copyOf(edges));
    }

    public static Graph empty() {
		return new ImmutableGraph(ImmutableSet.of(), ImmutableSet.of());
	}

    @Override
    public Set<Node> nodes() {
        return nodes;
    }

    @Override
    public Set<Edge> edges() {
        return edges;
    }

    @Override
    public Optional<Node> getNodeById(String id) {
        return nodes.stream().filter(n->n.id()==id).findFirst();
    }

    @Override
    public Optional<Edge> getEdgeById(String id) {
        return edges.stream().filter(e->e.id()==id).findFirst();
    }

	
}