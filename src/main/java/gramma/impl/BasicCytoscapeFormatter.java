package gramma.impl;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gramma.model.entities.ContextFreeFormatter;
import gramma.model.entities.Edge;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;

public class BasicCytoscapeFormatter implements ContextFreeFormatter<String> {

    private final Gson gson;

    private BasicCytoscapeFormatter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String format(Node node) {
        Map<String, Object> data = ImmutableMap.of("id", node.id());
        return gson.toJson(wrapWith("data", data));
    }

    @Override
    public String format(Edge edge) {
        Map<String, Object> data = ImmutableMap.of("id", edge.id(), "source", edge.source(), "target", edge.target());
        return gson.toJson(wrapWith("data", data));
    }

    @Override
    public String format(Graph graph) {
        Map<String, Collection<? extends Object>> elements = ImmutableMap.of("nodes",
                graph.nodes().stream().map(d -> wrapWith("data", d)).collect(Collectors.toList()), "edges",
                graph.edges().stream().map(d -> wrapWith("data", d)).collect(Collectors.toList()));
        return gson.toJson(elements);
    }

    private static Map<Object, Object> wrapWith(String wrap, Object object) {
        return ImmutableMap.of(wrap, object);
    }

    public static BasicCytoscapeFormatter create() {
        return new BasicCytoscapeFormatter(new GsonBuilder().setPrettyPrinting().serializeNulls().create());
    }
}