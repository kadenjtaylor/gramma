package gramma.model.entities;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Selection {

    private final Set<String> nodeIds;
    private final Set<String> edgeIds;

    public Selection(Set<String> nodeIds, Set<String> edgeIds) {
        this.nodeIds = nodeIds;
        this.edgeIds = edgeIds;
    }

    public Set<String> getNodes() {
        return nodeIds;
    }

    public Set<String> getEdges() {
        return edgeIds;
    }

    public boolean isEmpty() {
        return nodeIds.isEmpty() && edgeIds.isEmpty();
    }

    public boolean isValidSubgraph(Graph parent) {
        boolean nodesGood = allNodesPresent(parent);
        boolean edgesGood = allEdgesPresentAndConnected(parent);
        return nodesGood && edgesGood;
    }

    private boolean allNodesPresent(Graph g) {
        for (String id : this.nodeIds) {
            if (!g.getNodeById(id).isPresent()) {
                return false;
            }
        }
        return true;
    }

    private boolean allEdgesPresentAndConnected(Graph g) {
        for (String id : this.edgeIds) {
            Optional<Edge> optEdge = g.getEdgeById(id);
            if (!optEdge.isPresent()) {
                return false;
            } else {
                Edge e = optEdge.get();
                boolean sourceAndTargetInSelection = !this.nodeIds.containsAll(ImmutableSet.of(e.source(), e.target()));
                if (!sourceAndTargetInSelection) {
                    return false;
                }
            }
        }
        return true;
    }

	public static Selection empty() {
		return new Selection(ImmutableSet.of(), ImmutableSet.of());
    }

    @Override
    public String toString() {
        return "\n{\n\tedgeIds: " + edgeIds + ",\n\tnodeIds: " + nodeIds + "\n}";
    }
    
}
