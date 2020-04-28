package gramma.model.entities;

public class Node {

    private final String id;
    private final String value;

    public Node(String id, String value) {
        this.id = id;
        this.value = value;
    }

	public String id() {
		return id;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("{id: %s, value: %s}", id, value);
    }
}