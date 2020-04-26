package gramma.workspace;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import gramma.impl.ActionDrivenMutantGraph;
import gramma.impl.BasicCytoscapeFormatter;
import gramma.impl.SimpleGrammar;
import gramma.model.action.ActionPattern;
import gramma.model.action.GraphAction;
import gramma.model.action.PartialAction;
import gramma.model.entities.ContextFreeFormatter;
import gramma.model.entities.Edge;
import gramma.model.entities.Frame;
import gramma.model.entities.Graph;
import gramma.model.entities.Node;
import gramma.model.entities.Selection;
import gramma.model.grammar.Grammar;
import gramma.model.grammar.Mutagen;
import gramma.model.match.MatchPattern;
import gramma.model.mutate.MutantGraph;
import gramma.model.mutate.Mutation;

public class WorkspaceTest {

        private static String randomId() {
                return UUID.randomUUID().toString();
        }

        private static <U> U cond(boolean b, U t, U f) {
                return b ? t : f;
        }

        MatchPattern empty = new MatchPattern(
                "EMPTY",
                (g)-> {
                        boolean isEmpty = g.nodes().isEmpty() && g.edges().isEmpty();
                        Set<Frame> unboundMatch = ImmutableSet.of(Frame.empty());
                        Set<Frame> noMatch = ImmutableSet.of();
                        return cond(isEmpty, unboundMatch, noMatch);
                });

        MatchPattern aMessage = new MatchPattern(
                "A Message",
                (g) ->  {
                        Set<Frame> matches = g.nodes().stream()
                                .filter(n -> n.value() == "MESSAGE")
                                .map(n -> new Frame(ImmutableMap.of("original", n.id())))
                                .collect(Collectors.toSet());
                        return matches;
                });

        String replyId = randomId();
        PartialAction addMessage = (frame) -> GraphAction.addNode(new Node(randomId(), "MESSAGE"));
        PartialAction replyNode = (frame) -> GraphAction.addNode(new Node(replyId, "REPLY"));
        PartialAction replyEdge = (frame) -> GraphAction
                        .addEdge(new Edge(randomId(), replyId, frame.getId("original")));

        ActionPattern newMessage = new ActionPattern("New Message", ImmutableList.of(addMessage));
        ActionPattern replyToMessage = new ActionPattern("Reply to Message", ImmutableList.of(replyNode, replyEdge));

        Mutagen startAConversation = new Mutagen(empty, newMessage);
        Mutagen replyToAMessage = new Mutagen(aMessage, replyToMessage);

        ContextFreeFormatter<String> formatter = BasicCytoscapeFormatter.create();

        @Test
        public void testRun() {

                MutantGraph graph = new ActionDrivenMutantGraph();

                System.out.println("-> Starting with:\n" + formatter.format(graph) + "\n");

                Grammar grammar = SimpleGrammar.of(
                                // Mutagens you want to use go here
                                startAConversation, replyToAMessage
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
                Set<Mutation> options = workspace.getOptionsForSelection(mySelection);

                assertEquals("There should only be one applicable option", 1, options.size());

                Mutation chosenMutation = options.iterator().next();

                System.out.println("-> My Choice (Mutation): " + chosenMutation + "\n");

                boolean success = workspace.apply(chosenMutation);

                System.out.println("-> Applying chosen mutation.");
                System.out.println("-> Result:\n{\n\tsuccess: " + success + "\n}\n");

                Graph resultGraph = workspace.getGraph();

                System.out.println("-> Finishing with:\n" + formatter.format(resultGraph) + "\n");

                System.out.println("\n\t########## ROUND 2 ##########\n");

                System.out.println("-> Starting with:\n" + formatter.format(graph) + "\n");

                // Make a selection -- nothing to select yet
                Selection secondSelection = new Selection(ImmutableSet.of(resultGraph.nodes().iterator().next().id()), ImmutableSet.of());
                // TODO: Maybe some utility functions for different kinds of selections
                // But careful - we're not trying to write a query language here

                System.out.println("-> Selection #2: " + secondSelection + "\n");

                System.out.println("-> My Grammar (Same as Before): ");
                for (Mutagen mutagen : grammar.mutagens()) {
                        System.out.println("\t- " + mutagen.name());
                }
                System.out.println();

                System.out.println("-> Getting options for my selection given graph and grammar...\n");
                options = workspace.getOptionsForSelection(secondSelection);

                assertEquals("There should only be one applicable option", 1, options.size());

                chosenMutation = options.iterator().next();

                System.out.println("-> My Choice (Mutation): " + chosenMutation + "\n");

                success = workspace.apply(chosenMutation);

                System.out.println("-> Applying chosen mutation.");
                System.out.println("-> Result:\n{\n\tsuccess: " + success + "\n}\n");

                resultGraph = workspace.getGraph();

                System.out.println("-> Finishing with:\n" + formatter.format(resultGraph) + "\n");
        }
}
