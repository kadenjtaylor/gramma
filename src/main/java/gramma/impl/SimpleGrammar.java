package gramma.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gramma.model.grammar.Grammar;
import gramma.model.grammar.Mutagen;

public class SimpleGrammar implements Grammar {

    private final Set<Mutagen> mutagens;

    public SimpleGrammar(Collection<Mutagen> mutagens) {
        this.mutagens = new HashSet<>(mutagens);
    }

    public static SimpleGrammar of(Mutagen...mutagens) {
        return new SimpleGrammar(Arrays.asList(mutagens));
    }

    @Override
    public Set<Mutagen> mutagens() {
        return this.mutagens;
    }

}