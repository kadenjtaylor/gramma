package gramma.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import gramma.model.entities.Edge;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;

public class CytoscapeStreamingWriterTest {

    private static String randomId() {
        return UUID.randomUUID().toString();
    }

    private static Node node(String value) {
        return new Node(randomId(), value);
    }

    private static Edge edge(String id, String source, String target) {
        return new Edge(id, source, target);
    }

    private final Node bob = node("Bob");
    private final Node bill = node("Bill");
    private final Node bobsJob = node("Builder");
    private final Node yes = node("Yes we can!");
    private final Node clueless = node("No idea");
    private final Node billsJob = node("Banker");
    private final Edge bobDoesBobsJob = edge("Does Job", bob.id(), bobsJob.id());
    private final Edge canBobBuild = edge("Can we build it, Bob?", bob.id(), yes.id());
    private final Edge canBillBuild = edge("Can we build it, Bill?", bill.id(), clueless.id());
    private final Edge triesToCopy = edge("Tries to copy", bill.id(), bob.id());
    private final Edge billDoesBillsJob = edge("Bill Does Bill's Job", bill.id(), billsJob.id());

    @Test
    public void testNodeFormat() throws IOException {
        Graph graph = ImmutableGraph.of(
            ImmutableSet.of(bob, bill, bobsJob, yes, clueless, billsJob, billsJob),
            ImmutableSet.of(bobDoesBobsJob, canBobBuild, canBillBuild, triesToCopy, billDoesBillsJob)
        );

        System.out.println(toJson(graph));
    }


    private String toJson(Graph graph) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        g.useDefaultPrettyPrinter();
        CytoscapeStreamingWriter.writeGraph(g , graph);
        g.close();
        return writer.toString();
    }

}