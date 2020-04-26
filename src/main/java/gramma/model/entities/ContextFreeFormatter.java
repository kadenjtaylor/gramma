package gramma.model.entities;

public interface ContextFreeFormatter<U> {

    public U format(Node node);

    public U format(Edge node);

    public U format(Graph node);
    
}