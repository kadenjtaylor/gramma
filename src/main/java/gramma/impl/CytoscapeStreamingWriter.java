package gramma.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import gramma.model.action.ActionPattern;
import gramma.model.entities.Edge;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;
import gramma.model.grammar.Grammar;
import gramma.model.grammar.Mutagen;
import gramma.model.match.MatchPattern;
import gramma.model.mutate.Mutation;

public class CytoscapeStreamingWriter {

    // TODO: deal with the idea that nodes have data RIGHT HERE first -- maybe
    // abstract that reduction to another object/concept?
    // It's basically just the last level... WAIT IS FORMATTING THE LAST LEVEL??? OH
    // FUCK THAT'S SO COOL!
    // Okay breathe... For now, we're doing 1:1 nodes and edges - upgrade later

    public static void writeGraph(JsonGenerator json, Graph graph) throws IOException {
        json.writeStartObject();
        json.writeArrayFieldStart("nodes");
        for (Node node : graph.nodes()) {
            writeNode(json, node);
        }
        json.writeEndArray();
        json.writeArrayFieldStart("edges");
        for (Edge edge : graph.edges()) {
            writeEdge(json, edge);
        }
        json.writeEndArray();
        json.writeEndObject();
    }

    public static void writeGrammar(JsonGenerator json, Grammar grammar) throws IOException {
        json.writeStartObject();
        json.writeArrayFieldStart("mutagens");
        for( Mutagen m : grammar.mutagens()) {
            writeMutagen(json, m);
        }
        json.writeEndArray();
        json.writeEndObject();
    }

    private static void writeNode(JsonGenerator json, Node node) throws IOException {
        json.writeStartObject();
        json.writeFieldName("data");
        json.writeStartObject();
        json.writeStringField("id", node.id());
        json.writeStringField("value", node.value());
        json.writeEndObject();
        json.writeEndObject();
    }

    private static void writeEdge(JsonGenerator json, Edge edge) throws IOException {
        json.writeStartObject();
        json.writeFieldName("data");
        json.writeStartObject();
        json.writeStringField("id", edge.id());
        json.writeStringField("source", edge.source());
        json.writeStringField("target", edge.target());
        json.writeEndObject();
        json.writeEndObject();
    }

    public static void writeOptions(JsonGenerator json, Map<String, Mutation> options) throws IOException {
        json.writeStartObject();
        json.writeArrayFieldStart("options");
        for (String key : options.keySet()) {
            json.writeStartObject();
            json.writeStringField("id", key);
            json.writeStringField("name", options.get(key).actionPattern().name());
            json.writeEndObject();
        }
        json.writeEndArray();
        json.writeEndObject();
    }

    public static void writeHistory(JsonGenerator json, List<Mutation> history) throws IOException {
        json.writeStartObject();
        json.writeArrayFieldStart("history");
        for (Mutation mutation : history) {
            json.writeString(mutation.actionPattern().name());
        }
        json.writeEndArray();
        json.writeEndObject();
    }

    private static void writeMutagen(JsonGenerator json, Mutagen mutagen) throws IOException {
        json.writeStartObject();
        json.writeFieldName("matchPattern");
        writeMatchPattern(json, mutagen.matchPattern());
        json.writeFieldName("actionPattern");
        writeActionPattern(json, mutagen.actionPattern());
        json.writeEndObject();
    }

    private static void writeMatchPattern(JsonGenerator json, MatchPattern matchPattern) throws IOException {
        json.writeStartObject();
        json.writeStringField("name", matchPattern.name());
        json.writeEndObject();
    }

    private static void writeActionPattern(JsonGenerator json, ActionPattern actionPattern) throws IOException {
        json.writeStartObject();
        json.writeStringField("name", actionPattern.name());
        json.writeEndObject();
    }

    public static String toJson(Graph graph) {
        StringWriter writer = new StringWriter();
        JsonGenerator g;
        try {
            g = new JsonFactory().createGenerator(writer);
            g.useDefaultPrettyPrinter();
            CytoscapeStreamingWriter.writeGraph(g, graph);
            g.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static String toJson(Grammar graph) {
        StringWriter writer = new StringWriter();
        JsonGenerator g;
        try {
            g = new JsonFactory().createGenerator(writer);
            g.useDefaultPrettyPrinter();
            CytoscapeStreamingWriter.writeGrammar(g, graph);
            g.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

}