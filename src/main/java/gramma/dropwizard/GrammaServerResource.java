package gramma.dropwizard;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
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
        Set<Mutation> optionsForSelection = workspace.getOptionsForSelection(Selection.any());
        for (Mutation m : optionsForSelection) {
            System.out.println("\t\t- " + m.frame());
        }
        Optional<Mutation> mutation = randomChoice(optionsForSelection);
        if (mutation.isPresent()) {
            return mutate(mutation.get());
        } else {
            return graph();
        }
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
    public StreamingOutput options() { // TODO: Add Selection as
        return output -> {
            try (final JsonGenerator json = jsonFactory.createGenerator(output)) {
                CytoscapeStreamingWriter.writeOptions(json, workspace.getOptionsForSelection(Selection.any()));
            }
        };
    }

    @GET()
    @Path("/optionsForEmpty")
    @Timed
    public StreamingOutput emptyOptions() { // TODO: Add Selection as
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
                CytoscapeStreamingWriter.writeOptions(json,
                        workspace.getOptionsForSelection(Selection.ofNodes(nodeId)));
            }
        };
    }

    @POST()
    @Path("/mutate")
    @Timed
    public StreamingOutput mutate(Mutation mutation) {
        workspace.apply(mutation);
        return graph();
    }

    private static <U> Optional<U> randomChoice(Set<U> collection) {
        int size = collection.size();
        System.out.println("Size of collection to randomly draw from: " + size);
        if (size == 0) {
            return Optional.empty();
        } else {
            int choice = new Random().nextInt(size);
            System.out.println("Choice: " + choice);
            Iterator<U> iterator = collection.iterator();
            while (choice > 0) {
                iterator.next();
                choice--;
            }
            return Optional.of(iterator.next());
        }
    }
}