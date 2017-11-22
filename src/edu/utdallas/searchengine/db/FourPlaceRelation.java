package edu.utdallas.searchengine.db;

import edu.utdallas.util.Pair;

public class FourPlaceRelation extends Relation {
	public FourPlaceRelation() {
		super();
	}

	@Override
	public Relation join(Relation rhs, Pair<Integer, Integer>[] agreeOn) {
		throw new RuntimeException("Operation not supported exception!");
	}
}
