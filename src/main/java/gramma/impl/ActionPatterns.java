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

    public static ActionPattern newNode(String value) {
        String newNodeId = randomId();
        return new ActionPattern(
            "New Node",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(newNodeId, value)
                )
            )
        );
    }

    public static ActionPattern spawnParent(String parentValue) {
        String newNodeId = randomId();

        return new ActionPattern(
            "Spawn a parent",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(newNodeId, parentValue)
                ),
                (frame) -> GraphAction.addEdge(
                    new Edge(randomId(), frame.getId("node"), newNodeId)
                )
            )
        );
    }

    public static ActionPattern spawnChild(String childValue) {
        String newNodeId = randomId();
        return new ActionPattern(
            "Spawn a child",
            ImmutableList.of(
                (frame) -> GraphAction.addNode(
                    new Node(newNodeId, childValue)
                ),
                (frame) -> GraphAction.addEdge(
                    new Edge(randomId(), newNodeId, frame.getId("node"))
                )
            )
        );
    }
}