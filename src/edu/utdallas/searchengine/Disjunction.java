package edu.utdallas.searchengine;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.util.Triple;

public class Disjunction implements SearchExpression {
	private SearchExpression leftDisjunct;
	private SearchExpression rightDisjunct;
	
	public Disjunction(SearchExpression leftDisjunct, SearchExpression rightDisjunct) {
		this.leftDisjunct = leftDisjunct;
		this.rightDisjunct = rightDisjunct;
	}
	
	private Set<Triple<Line, String, Double>> union(Set<Triple<Line, String, Double>> op1,
			Set<Triple<Line, String, Double>> op2) {
		Set<Triple<Line, String, Double>> result = new HashSet<>();
		result.addAll(op1);
		result.addAll(op2);
		return result;
	}

	@Override
	public Set<Triple<Line, String, Double>> evaluate(SearchContext context,
			BiFunction<String, String, Boolean> wordComparator,
			boolean caseSensitive) {
		Set<Triple<Line, String, Double>> l = leftDisjunct.evaluate(context, wordComparator, caseSensitive);
		Set<Triple<Line, String, Double>> r = rightDisjunct.evaluate(context, wordComparator, caseSensitive);
		return union(l, r);
	}

}
