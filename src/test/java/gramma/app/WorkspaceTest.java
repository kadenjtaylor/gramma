package gramma.app;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import gramma.impl.ActionDrivenMutantGraph;
import gramma.impl.CytoscapeStreamingWriter;
import gramma.impl.Mutagens;
import gramma.impl.SimpleGrammar;
import gramma.model.entities.Graph;
import gramma.model.entities.Selection;
import gramma.model.grammar.Grammar;
import gramma.model.grammar.Mutagen;
import gramma.model.mutate.MutantGraph;
import gramma.model.mutate.Mutation;

public class WorkspaceTest {

        @Test
        public void testRun() {

                MutantGraph graph = new ActionDrivenMutantGraph();

                System.out.println("-> Starting with:\n" + CytoscapeStreamingWriter.toJson(graph) + "\n");

                Grammar grammar = SimpleGrammar.of(
                                // Mutagens you want to use go here
                                Mutagens.ifEmptyNewNode("MESSAGE"),
                                Mutagens.ifNodeWithValueSpawnParent("MESSAGE", "MESSAGE"),
                                Mutagens.ifNodeWithValueSpawnParent("MESSAGE", "REACTION")
                        );

                Workspace workspace = new Workspace(graph, grammar);

                // Make a selection -- nothing to select yet
                Selection mySelection = Selection.empty();

                System.out.println("-> My Selection: " + mySelection + "\n");

                System.out.println("-> My Grammar: ");
                for (Mutagen mutagen : grammar.mutagens()) {
                        System.out.println("\t- " + mutagen.name());
                }
                System.out.println();

                System.out.println("-> Getting options for my selection given graph and grammar...\n");
                Map<String, Mutation> options = workspace.getOptionsForSelection(mySelection);

                assertEquals("There should only be one applicable option", 1, options.size());

                Mutation chosenMutation = options.values().iterator().next();

                System.out.println("-> My Choice (Mutation): " + chosenMutation + "\n");

                boolean success = workspace.apply(chosenMutation);

                System.out.println("-> Applying chosen mutation.");
                System.out.println("-> Result:\n{\n\tsuccess: " + success + "\n}\n");

                Graph resultGraph = workspace.graph();

                System.out.println("-> Finishing with:\n" + CytoscapeStreamingWriter.toJson(resultGraph) + "\n");

                System.out.println("\n\t########## ROUND 2 ##########\n");

                System.out.println("-> Starting with:\n" + CytoscapeStreamingWriter.toJson(graph) + "\n");

                // Make a selection -- nothing to select yet
                String nodeId = resultGraph.nodes().iterator().next().id();
                Selection secondSelection = Selection.ofNodes(nodeId);

                System.out.println("-> Selection #2: " + secondSelection + "\n");

                System.out.println("-> My Grammar (Same as Before): ");
                for (Mutagen mutagen : grammar.mutagens()) {
                        System.out.println("\t- " + mutagen.name());
                }
                System.out.println();

                System.out.println("-> Getting options for my selection given graph and grammar...\n");
                options = workspace.getOptionsForSelection(secondSelection);

                assertEquals("There should be two options this time", 2, options.size());

                chosenMutation = options.values().iterator().next();

                System.out.println("-> My Choice (Mutation): " + chosenMutation + "\n");

                success = workspace.apply(chosenMutation);

                System.out.println("-> Applying chosen mutation.");
                System.out.println("-> Result:\n{\n\tsuccess: " + success + "\n}\n");

                resultGraph = workspace.graph();

                System.out.println("-> Finishing with:\n" + CytoscapeStreamingWriter.toJson(resultGraph) + "\n");
        }
}
