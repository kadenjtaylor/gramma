package gramma.model.entities;

import java.util.Optional;
import java.util.Set;

public interface Graph {

	public Set<Node> nodes();

	public Set<Edge> edges();

	public Optional<Node> getNodeById(String id);

	public Optional<Edge> getEdgeById(String id);
}