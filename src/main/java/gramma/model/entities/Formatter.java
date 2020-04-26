package gramma.model.entities;

public interface Formatter<U> {

    public U format(Node node, Graph graph);

    public U format(Edge node, Graph graph);

    public U format(Graph node, Graph graph);
}