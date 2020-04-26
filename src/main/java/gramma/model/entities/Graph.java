package gramma.model.entities;

import java.util.Optional;
import java.util.Set;

public interface Graph {

	public abstract Set<Node> nodes();

	public abstract Set<Edge> edges();

	public abstract Optional<Node> getNodeById(String id);

	public abstract Optional<Edge> getEdgeById(String id);
}