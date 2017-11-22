package edu.utdallas.searchengine;

import java.util.Set;
import java.util.function.BiFunction;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.util.Triple;

public interface SearchExpression {	
	public Set<Triple<Line, String, Double>> evaluate(SearchContext context,
			BiFunction<String, String, Boolean> wordComparator,
			boolean caseSensitive);
}
