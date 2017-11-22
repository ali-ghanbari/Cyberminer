package edu.utdallas.searchengine;

import java.util.Set;
import java.util.function.Predicate;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.util.Triple;

public interface SearchContext {
	public Set<Triple<Line, String, Double>> select(Predicate<Line> predicate);
	public Set<Triple<Line, String, Double>> all();
}
