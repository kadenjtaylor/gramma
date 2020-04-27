package gramma.model.match;

import java.util.Set;

import com.google.common.base.Function;

import gramma.model.entities.Frame;
import gramma.model.entities.Graph;

public class MatchPattern {

	private String name;
	private Function<Graph, Set<Frame>> matchFunction;

	/* Okay, here's the rationale:
			- I don't know how to implement generic graph pattern matching in a way that's snappy.
			- I DO know how to implement the special cases I want
			- Given the above, if I create a function (and a serialization) for any match pattern I want,
				 then I can add new kinds of matches as I find uses for them.
			- Maybe we go generic eventually, but that seems like a LOT of cognitive load for a feature
				set that I'm pretty sure I won't use most of.
	*/

	public MatchPattern(String name, Function<Graph, Set<Frame>> matchFunction) {
		this.name = name;
		this.matchFunction = matchFunction;
	}

	public String name() {
		return "MATCH: " + name;
	}

	// We can at least use this for now to bootstrap,
	// although we may need a way to kill this match
	// early if we start getting complicated matches
	public Set<Frame> match(Graph graph) {
		System.out.println("Trying to match (" + name + ")");
		return matchFunction.apply(graph);
	}

}