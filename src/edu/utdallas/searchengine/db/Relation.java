package edu.utdallas.searchengine.db;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.utdallas.util.Pair;
import edu.utdallas.util.Tuple;

public abstract class Relation {
	protected List<Tuple> tuples;
	
	protected Relation() {
		tuples = new ArrayList<>();
	}
	
	protected Relation(List<Tuple> tuples) {
		this.tuples = tuples;
	}
	
	public Relation select(Predicate<Tuple> predicate) {
		return new Relation(tuples.parallelStream().filter(predicate).collect(Collectors.toList())) {
			@Override
			public Relation join(Relation rhs, Pair<Integer, Integer>[] agreeOn) {
				return Relation.this.join(rhs, agreeOn);
			}
		};
	}
	
	public abstract Relation join(Relation rhs, Pair<Integer, Integer>[] agreeOn);
}
