package edu.utdallas.searchengine.db;

import edu.utdallas.util.Pair;
import edu.utdallas.util.Quadruple;
import edu.utdallas.util.Triple;

public class ThreePlaceRelation extends Relation {
	public ThreePlaceRelation() {
		super();
	}

	@Override
	public Relation join(Relation rhs, Pair<Integer, Integer>[] agreeOn) {
		if(!(rhs instanceof TwoPlaceRelation) || agreeOn != null) {
			throw new RuntimeException("Operation not supported exception!");
		}
		
		FourPlaceRelation result = new FourPlaceRelation();
		
		tuples.stream().map(lhsTuple -> (Triple<?, ?, ?>) lhsTuple)
			.forEach(lhsTuple -> {
				rhs.tuples.stream().map(rhsTuple -> (Pair<?, ?>) rhsTuple)
					.forEach(rhsTuple -> {
						if(rhsTuple.first().equals(lhsTuple.third())) {
							result.tuples.add(new Quadruple<>(lhsTuple.first(),
									lhsTuple.second(),
									lhsTuple.third(),
									rhsTuple.second()));
						}
					});
		});
		return result;
	}
}
