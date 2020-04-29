package gramma.model.action;

import gramma.model.entities.Frame;

public interface PartialAction {

    public PartialActionResult resolve(Frame frame, Frame inner);
}
