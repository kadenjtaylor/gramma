package gramma.model.entities;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;


public class Selection {

    private final Set<String> nodeIds;
    private final Set<String> edgeIds;
    private final boolean isAny;

    private Selection(Set<String> nodeIds, Set<String> edgeIds, boolean isAny) {
        this.nodeIds = nodeIds;
        this.edgeIds = edgeIds;
        this.isAny = isAny;
    }

    public Set<String> nodeIds() {
        return nodeIds;
    }

    public Set<String> edgeIds() {
        return edgeIds;
    }

    public boolean isAny() {
        return isAny;
    }

    public boolean isEmpty() {
        return nodeIds.isEmpty() && edgeIds.isEmpty() && !isAny();
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
		return new Selection(ImmutableSet.of(), ImmutableSet.of(), false);
    }

    public static Selection ofNodes(String... nodeIds) {
        return new Selection(ImmutableSet.of(nodeIds), ImmutableSet.of(), false);
    }

    public Selection andEdges(String... edgeIds) {
        return new Selection(this.nodeIds, ImmutableSet.of(edgeIds), false);
    }

    public static Selection any() {
        return new Selection(ImmutableSet.of(), ImmutableSet.of(), false);
    }

    @Override
    public String toString() {
        return "\n{\n\tedgeIds: " + edgeIds + ",\n\tnodeIds: " + nodeIds + "\n}";
    }
    
}
