package gramma.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gramma.impl.CytoscapeStreamingWriter;
import gramma.impl.ImmutableGraph;
import gramma.model.entities.Frame;
import gramma.model.entities.Graph;
import gramma.model.entities.Selection;
import gramma.model.grammar.Grammar;
import gramma.model.grammar.Mutagen;
import gramma.model.mutate.MutantGraph;
import gramma.model.mutate.Mutation;

public class Workspace {

    private Grammar grammar;
    private MutantGraph graph;
    private final Map<String, Mutation> mutationsById;

    public Workspace(MutantGraph graph, Grammar grammar) {
        this.grammar = grammar;
        this.graph = graph;
        this.mutationsById = new HashMap<>();
    }

    public Map<String, Mutation> getOptionsForSelection(Selection selection) {
        this.mutationsById.clear();
        Graph subgraph = subset(selection, graph);
        System.out.println(CytoscapeStreamingWriter.toJson(subgraph));
        System.out.println(CytoscapeStreamingWriter.toJson(grammar));
        for (Mutagen m : this.grammar.mutagens()) {
            System.out.println("Checking for matches of: " + m.name());
            Set<Frame> matches = m.matchPattern().match(subgraph);
            System.out.println("Found " + matches.size() + " matches!");
            for (Frame frame : matches) {
                this.mutationsById.put(UUID.randomUUID().toString(), new Mutation(frame, m.actionPattern()));
            }
        }
        System.out.println("Found " + this.mutationsById.size() + "mutations");
        return this.mutationsById;
    }

    public boolean apply(Mutation mutation) {
        this.mutationsById.clear();
        return this.graph.apply(mutation);
    }

    public boolean applyById(String mutationId) {
        Mutation mutation = this.mutationsById.get(mutationId);
        if (mutation == null) {
            return false;
        }
        return apply(mutation);
	}

    public boolean saveToFile(String fileName) {
        return true; // <=> success
    }

    public boolean loadFromFile(String fileName) {
        return true; // <=> success
    }

    public Graph graph() {
        return ImmutableGraph.of(graph.nodes(), graph.edges());
    }

    public List<Mutation> history() {
        // TODO: I think this passthrough is incorrect
        return this.graph.history();
    }


    public Grammar grammar() {
        return grammar;
    }

    public static Graph subset(Selection selection, Graph graph) {
        if (selection.isAny()) {
            return graph;
        } else if (selection.isEmpty()) {
            return ImmutableGraph.empty();
        }
        if (selection.isValidSubgraph(graph)) {
            return ImmutableGraph.of(
                graph.nodes().stream().filter(n -> selection.nodeIds().contains(n.id())).collect(Collectors.toSet()),
                graph.edges().stream().filter(e -> selection.edgeIds().contains(e.id())).collect(Collectors.toSet())
            );
        } else {
            // TODO: What to do is it's not a valid subgraph... come back to this
            System.out.println("[Warning]: Selection is not a valid subgraph!");
            return ImmutableGraph.empty();
        }
    }

}
