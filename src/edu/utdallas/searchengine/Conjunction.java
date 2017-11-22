package edu.utdallas.searchengine;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.util.Triple;

public class Conjunction implements SearchExpression {
	private SearchExpression leftConjunct;
	private SearchExpression rightConjunct;
	
	public Conjunction(SearchExpression leftConjunct, SearchExpression rightConjunct) {
		this.leftConjunct = leftConjunct;
		this.rightConjunct = rightConjunct;
	}
	
	private Set<Triple<Line, String, Double>> intersection(Set<Triple<Line, String, Double>> op1,
			Set<Triple<Line, String, Double>> op2) {
		return op1.parallelStream()
				.filter(e -> op2.contains(e))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Triple<Line, String, Double>> evaluate(SearchContext context,
			BiFunction<String, String, Boolean> wordComparator,
			boolean caseSensitive) {
		Set<Triple<Line, String, Double>> l = leftConjunct.evaluate(context, wordComparator, caseSensitive);
		Set<Triple<Line, String, Double>> r = rightConjunct.evaluate(context, wordComparator, caseSensitive);
		return intersection(l, r);
	}

}
