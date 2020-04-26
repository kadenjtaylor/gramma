package gramma.model.grammar;

import gramma.model.action.ActionPattern;
import gramma.model.match.MatchPattern;

public class Mutagen {

    private final MatchPattern matchPattern;
    private final ActionPattern actionPattern;

    public Mutagen(MatchPattern matchPattern, ActionPattern actionPattern) {
        this.matchPattern = matchPattern;
        this.actionPattern = actionPattern;
    }

    public String name(){
        return "[IF]->("+ matchPattern.name() +  ")->[THEN]->(" + actionPattern.name() + ")";
    }

    public MatchPattern matchPattern() {
        return matchPattern;
    }

    public ActionPattern actionPattern() {
        return actionPattern;
    }
}