package gramma.impl;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import gramma.model.entities.Frame;
import gramma.model.match.MatchPattern;

public class MatchPatterns {

    private MatchPatterns() {
    } // To prevent you from creating one

    public static MatchPattern empty = new MatchPattern("EMPTY", (g) -> {
        boolean isEmpty = g.nodes().isEmpty() && g.edges().isEmpty();
        Set<Frame> unboundMatch = ImmutableSet.of(Frame.empty());
        Set<Frame> noMatch = ImmutableSet.of();
        return isEmpty ? unboundMatch : noMatch;
    });


    public static MatchPattern nodesWithValue(String value) {
        return new MatchPattern("Nodes with Value: " + value, (g) -> {
            Set<Frame> frames = g.nodes().stream()
                .filter(n -> n.value() == value)
                .map(n -> new Frame(ImmutableMap.of("node", n.id())))
                .collect(Collectors.toSet());
            return frames;
        });
    };
}