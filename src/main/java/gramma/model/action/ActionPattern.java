package gramma.model.action;

import java.util.List;

public class ActionPattern {

    private final String name;
    private List<PartialAction> partialActions;

    public ActionPattern(String name, List<PartialAction> partialActions) {
        this.name = name;
        this.partialActions = partialActions;
    }

    public String name() {
        return "ACTION: " + name;
    }

    public List<PartialAction> partialActions() {
        return partialActions;
    }
}
