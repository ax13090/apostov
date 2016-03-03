package apostov;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class HandResultEnumerator {

	public ImmutableMap<HolecardHand, Fraction> enumerateBoardsAndMeasureWins(final ImmutableList<HolecardHand> candidates) {
		final ImmutableList<Card> deck = new DeckFactory().newDeck(
				candidates.stream()
					.map(HolecardHand::getHolecardsAsList)
					.flatMap(Collection::stream)
					.collect(toSet())
		);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final int binomialCoefficient = (int) CombinatoricsUtils.binomialCoefficient(deck.size(), 5);
		
		final Map<HolecardHand, Fraction> overallSuccessChanceByHand = StreamSupport.stream(new Combinations(deck.size(), 5).spliterator(), false)
			.map((a) -> stream(a).mapToObj(i -> deck.get(i)).collect(toList()))
			.map(l -> {
				assert l.size() == 5;
				return new Board(l.get(0), l.get(1), l.get(2), l.get(3), l.get(4));
			})
			.map(b -> evaluator.evaluateShowdown(candidates, b))
			.map(wc -> Maps.toMap(wc, w -> new Fraction(1, binomialCoefficient * wc.size())))
			.map(Map::entrySet)
			.flatMap(Collection::stream)
			.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(x, y) -> x.add(y)
					)
			);
		
		return ImmutableMap.copyOf(overallSuccessChanceByHand);
	}
	
}
