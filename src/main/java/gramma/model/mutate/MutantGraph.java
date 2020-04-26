package gramma.model.mutate;

import gramma.model.entities.Graph;

public interface MutantGraph extends Graph {

    public boolean apply(Mutation mutation);
}