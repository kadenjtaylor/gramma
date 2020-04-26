package gramma.model.mutate;

import java.util.List;
import java.util.stream.Collectors;

import gramma.model.action.ActionPattern;
import gramma.model.action.GraphAction;
import gramma.model.entities.Frame;

public class Mutation {

    private final Frame frame;
    private final ActionPattern actionPattern;

    public Mutation(Frame frame, ActionPattern actionPattern) {
        this.frame = frame;
        this.actionPattern = actionPattern;
    }

    public Frame frame() {
        return frame;
    }

    public ActionPattern actionPattern() {
        return actionPattern;
    }

    public List<GraphAction> compile() {
        return actionPattern.partialActions().stream().map(pa -> pa.resolve(this.frame)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "\n{\n\tpattern: " + actionPattern.name() + ",\n\tframe: " + frame + "\n}";
    }

}
