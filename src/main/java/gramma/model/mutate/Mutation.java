package gramma.model.mutate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gramma.model.action.ActionPattern;
import gramma.model.action.GraphAction;
import gramma.model.action.PartialAction;
import gramma.model.action.PartialActionResult;
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
        List<GraphAction> graphActions = new ArrayList<>();
        Map<String, String> idsByLabel = new HashMap<>();
        for (PartialAction p : actionPattern.partialActions()) {
            PartialActionResult result = p.resolve(frame, new Frame(idsByLabel));
            graphActions.add(result.getGraphAction());
            if (result.id() != null && result.label() != null) {
                idsByLabel.put(result.label(), result.id());
            }
        }
        return graphActions;
    }

    @Override
    public String toString() {
        return "\n{\n\tpattern: " + actionPattern.name() + ",\n\tframe: " + frame + "\n}";
    }

}
