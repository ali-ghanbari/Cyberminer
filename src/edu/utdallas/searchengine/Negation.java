package edu.utdallas.searchengine;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.util.Triple;

public class Negation implements SearchExpression {
	private SearchExpression operand;
	
	public Negation(SearchExpression operand) {
		this.operand = operand;
	}
	
	private Set<Triple<Line, String, Double>> compelement(Set<Triple<Line, String, Double>> universe,
			Set<Triple<Line, String, Double>> s) {
		return universe.parallelStream()
				.filter(e -> !s.contains(e))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Triple<Line, String, Double>> evaluate(SearchContext context,
			BiFunction<String, String, Boolean> wordComparator,
			boolean caseSensitive) {
		return compelement(context.all(), operand.evaluate(context, wordComparator, caseSensitive));
	}

}
