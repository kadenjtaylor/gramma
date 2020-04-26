package gramma.workspace;

import java.util.HashSet;
import java.util.Set;

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

    public Workspace(MutantGraph graph, Grammar grammar) {
        this.grammar = grammar;
        this.graph = graph;
    }

    public Set<Mutation> getOptionsForSelection(Selection selection) {
        Set<Mutation> mutations = new HashSet<>();
        for (Mutagen m : this.grammar.mutagens()) {
            Set<Frame> matches = m.matchPattern().match(graph);
            for (Frame frame : matches) {
                mutations.add(new Mutation(frame, m.actionPattern()));
            }
        }
        return mutations;
    }

    public boolean apply(Mutation mutation) {
        return this.graph.apply(mutation);
    }

    public boolean saveToFile(String fileName) {
        return true; // <=> success
    }

    public boolean loadFromFile(String fileName) {
        return true; // <=> success
    }

    public Graph getGraph() {
        return ImmutableGraph.of(graph.nodes(), graph.edges());
    }

}
