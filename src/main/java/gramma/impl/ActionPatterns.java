package gramma.impl;

import gramma.model.action.GraphAction;

import java.util.UUID;

import com.google.common.collect.ImmutableList;

import gramma.model.action.ActionPattern;
import gramma.model.entities.Edge;
import gramma.model.entities.Node;

public class ActionPatterns {

    private ActionPatterns() {
    } // To prevent you from creating one

      // TODO: Need a way for action patterns to request named parameters...

    private static String randomId() {
        return UUID.randomUUID().toString();
    }


    /*
        TODO: OHHHHH.... Because of the way we're creating ActionPatterns,
        the Strings are decided in advance, and therefore we only have as many unique new node ids
        as we do action patterns. We need that new nodeId to be decided at application time, not creation
        time. BUT, we do have to ensure that I can use node IDs between actions in a mutation,

        Bottom line: these static creation functions don't work because newNodeId is fixed to the mutagen.

        Damn, that's a weird one.
    */




    public static ActionPattern newNode(String value) {
        return new ActionPattern(
            "New Message",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(randomId(), value)
                )
            )
        );
    }

    public static ActionPattern spawnParent(String parentValue) {
        String newNodeId = UUID.randomUUID().toString();
        return new ActionPattern(
            "Reply to message",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(newNodeId, parentValue)
                ),
                (frame) -> GraphAction.addEdge(
                    new Edge("REPLYING_TO_" + randomId(), newNodeId, frame.getId("node"))
                )
            )
        );
    }

    public static ActionPattern spawnChild(String childValue) {
        String newNodeId = UUID.randomUUID().toString();
        return new ActionPattern(
            "Add reaction to message",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(newNodeId, childValue)
                ),
                (frame) -> GraphAction.addEdge(
                    new Edge("GOT_REACTION_Of_" + randomId(), frame.getId("node"), newNodeId)
                )
            )
        );
    }
}