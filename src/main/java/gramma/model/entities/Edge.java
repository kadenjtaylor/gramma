package gramma.model.entities;

public class Edge {

    private final String id;
    private final String source;
    private final String target;

    public Edge(String id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public String id() {
        return id;
    }

    public String source() {
        return source;
    }

    public String target() {
        return target;
    }
    
    @Override
    public String toString() {
        return String.format("{id: %s, source: %s, target: %s}", id, source, target);
    }
}