package gramma.impl;

import gramma.model.grammar.Mutagen;

public class Mutagens {

    private Mutagens(){} // To prevent you from creating one

    /* TODO: I think that if you create a "labeling" that goes into a
    mutagen, you won't have the stringly typed parameters issue */

    public static Mutagen ifEmptyNewNode(String newNodeValue) {
        return new Mutagen(MatchPatterns.empty, ActionPatterns.newNode(newNodeValue));
    }

    public static Mutagen ifNodeWithValueSpawnParent(String value, String newNodeValue) {
        return new Mutagen(MatchPatterns.nodesWithValue(value), ActionPatterns.spawnParent(newNodeValue));
    }

    public static Mutagen ifNodeWithValueSpawnChild(String value, String newNodeValue) {
        return new Mutagen(MatchPatterns.nodesWithValue(value), ActionPatterns.spawnChild(newNodeValue));
    }
}