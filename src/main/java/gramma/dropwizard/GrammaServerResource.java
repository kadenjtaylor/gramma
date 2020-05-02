package gramma.dropwizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import gramma.impl.CytoscapeStreamingWriter;
import gramma.model.entities.Selection;
import gramma.model.mutate.Mutation;
import gramma.app.Workspace;

@Path("/gramma-rest-v1")
@Produces(MediaType.APPLICATION_JSON)
public class GrammaServerResource {

    private final JsonFactory jsonFactory;
    private final Workspace workspace;

    /* TODO: Refactor this with the goals of:
        - removing duplication
        - showing that repeated calls to randomMutation will randomly grow the graph */
    
    public GrammaServerResource(Workspace workspace) {
        this.workspace = workspace;
        this.jsonFactory = JsonFactory.builder().build();
    }

    @GET()
    @Path("/randomMutate")
    @Timed
    public StreamingOutput randomMutate() {
        Collection<Mutation> optionsForSelection = workspace.getOptionsForSelection(Selection.any()).values();
        Optional<Mutation> mutation = randomChoice(optionsForSelection);
        if (mutation.isPresent()) {
            workspace.apply(mutation.get());
        }
        return graph();
    }

    @GET()
    @Path("/graph")
    @Timed
    public StreamingOutput graph() {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                CytoscapeStreamingWriter.writeGraph(json, workspace.graph());
            }
        };
    }

    @GET()
    @Path("/grammar")
    @Timed
    public StreamingOutput grammar() {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                CytoscapeStreamingWriter.writeGrammar(json, workspace.grammar());
            }
        };
    }

    @GET()
    @Path("/options")
    @Timed
    public StreamingOutput options() {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                CytoscapeStreamingWriter.writeOptions(json, workspace.getOptionsForSelection(Selection.any()));
            }
        };
    }

    @GET()
    @Path("/optionsForEmpty")
    @Timed
    public StreamingOutput emptyOptions() {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                CytoscapeStreamingWriter.writeOptions(json, workspace.getOptionsForSelection(Selection.empty()));
            }
        };
    }

    @GET()
    @Path("/optionsForNode")
    @Timed
    public StreamingOutput optionsForNode(@QueryParam("id") String nodeId) {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                Map<String, Mutation> optionsForSelection = workspace.getOptionsForSelection(Selection.ofNodes(nodeId));
                CytoscapeStreamingWriter.writeOptions(json, optionsForSelection);
            }
        };
    }

    @GET()
    @Path("/mutate")
    @Timed
    public StreamingOutput mutate(@QueryParam("id") String mutationId) {
        workspace.applyById(mutationId);
        return graph();
    }

    @GET()
    @Path("/history")
    @Timed
    public StreamingOutput history() {
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                List<Mutation> history= workspace.history();
                CytoscapeStreamingWriter.writeHistory(json, history);
            }
        };
    }

    private static <U> Optional<U> randomChoice(Collection<U> coll) {
        if (coll.isEmpty()) {
            return Optional.empty();
        }
        List<U> rList = new ArrayList<>(coll);
        Collections.shuffle(rList);
        return Optional.of(rList.get(0));
    }
}