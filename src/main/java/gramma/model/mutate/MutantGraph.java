package gramma.model.mutate;

import java.util.List;

import gramma.model.entities.Graph;

public interface MutantGraph extends Graph {

    public boolean apply(Mutation mutation);

    public List<Mutation> history();
}