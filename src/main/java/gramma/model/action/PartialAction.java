package gramma.model.action;

import gramma.model.entities.Frame;

public interface PartialAction {

    public GraphAction resolve(Frame frame);
}
