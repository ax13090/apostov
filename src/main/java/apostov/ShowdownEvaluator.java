package apostov;

import static apostov.Value.ACE;
import static apostov.Value.TWO;
import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import strength.PokerHandRanking;
import strength.StraightFlushRanking;

public class ShowdownEvaluator {

	public void evaluateShowdown(final ImmutableList<HolecardHand> candidates, final Board board) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public PokerHandRanking selectBestCombination(final ImmutableCollection<Card> cards) {
		assert cards.size() >= 5;
		
		/* Build a Map where each key is a Suit, and each value is a sorted Set of Values  */
		final ImmutableMap<Suit, EnumSet<Value>> valuesBySuit = mapValuesBySuit(cards);
		
		/* Search for Straight-Flushes */
		for (final Suit suit : Suit.values()) {
			final EnumSet<Value> mutableValues = valuesBySuit.get(suit);
			if (mutableValues == null)
				continue;
			if (mutableValues.size() < 5)
				continue;
			
			final Optional<Value> optionalStraightFlushHighValue = searchFiveConsecutiveValues(mutableValues);
			if (optionalStraightFlushHighValue.isPresent()) {
				final Value straightFlushHighValue = optionalStraightFlushHighValue.get();
				return new StraightFlushRanking(straightFlushHighValue, suit);
			}
		}
		
		
		throw new UnsupportedOperationException("Not implemented yet");
	}

	private ImmutableMap<Suit, EnumSet<Value>> mapValuesBySuit(final ImmutableCollection<Card> cards) {
		final Supplier<EnumSet<Value>> enumSetSupplier = () -> EnumSet.noneOf(Value.class);
		final ImmutableMap<Suit, EnumSet<Value>> valuesBySuit = copyOf(cards.stream().collect(groupingBy(
				c -> c.suit,
				mapping(c -> c.value, toCollection(enumSetSupplier))
		)));
		return valuesBySuit;
	}

	private Optional<Value> searchFiveConsecutiveValues(final Set<Value> values) {
		if (values.size() < 5)
			return Optional.empty();
		
		int consecutiveCardsCount = 0;
		for (int i = ACE.ordinal(); i >= TWO.ordinal(); --i) {
			if (values.contains(i))
				++consecutiveCardsCount;
			else
				consecutiveCardsCount = 0;
			
			if (consecutiveCardsCount == 5)
				return Optional.of(Value.values()[i + 5]);
		}
		
		if (consecutiveCardsCount == 4)
			if (values.contains(ACE))
				return Optional.of(Value.FIVE);

		
		return Optional.empty();
	}
}
