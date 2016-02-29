package apostov;

import static apostov.Value.ACE;
import static apostov.Value.TWO;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import strength.FlushRanking;
import strength.FullHouseRanking;
import strength.HighCardRanking;
import strength.PairRanking;
import strength.PokerHandRanking;
import strength.QuadRanking;
import strength.SetRanking;
import strength.StraightFlushRanking;
import strength.StraightRanking;
import strength.TwoPairsRanking;

public class ShowdownEvaluator {

	public void evaluateShowdown(final ImmutableList<HolecardHand> candidates, final Board board) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	// TODO idea : use Guava's Table and ImmutableTable classes instead of two ImmutableMap<X, EnumSet<Y>> types
	public PokerHandRanking selectBestCombination(final ImmutableCollection<Card> cards) {
		assert cards.size() >= 5;
		
		/* Build a Map where each map-key is a Suit, and each map-value is a sorted Set of card Values  */
		final ImmutableMap<Suit, EnumSet<Value>> valuesBySuit = mapValuesBySuit(cards);
		
		/* Build a Map where each map-key is a card Value, and each map-value is a set of card Suits */
		final ImmutableMap<Value, EnumSet<Suit>> suitsByValue = mapSuitsByValues(cards);
		
		/* Search for Straight-Flushes
		 * This works by assuming that there can not be two straight-flushes
		 * in different suits, which is true for Texas Holdem. */
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
		
		/* Search for quads */
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value value = Value.values()[i];
			final EnumSet<Suit> suitsForCurrentValue = suitsByValue.get(value);
			if (suitsForCurrentValue == null)
				continue;
			if (suitsForCurrentValue.size() == 4) {
				final Card kicker = findKicker(suitsByValue, ImmutableSet.of(value));
				return new QuadRanking(value, kicker);
			}
		}
		
		/* Search for full-houses */
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleSetValue = Value.values()[i];
			final EnumSet<Suit> suitsForPossibleSetValue = suitsByValue.get(possibleSetValue);
			if (suitsForPossibleSetValue == null)
				continue;
			assert suitsForPossibleSetValue.size() < 4;
			if (suitsForPossibleSetValue.size() < 3)
				continue;

			assert suitsForPossibleSetValue.size() == 3;
			for (int j = ACE.ordinal(); j <= TWO.ordinal(); --j) {
				final Value possiblePairValue = Value.values()[j];
				if (possibleSetValue == possiblePairValue)
					continue;
				
				final EnumSet<Suit> suitsForPossiblePairValue = suitsByValue.get(possiblePairValue);
				if (suitsForPossiblePairValue == null)
					continue;
				assert suitsForPossiblePairValue.size() < 3;
				
				if (suitsForPossiblePairValue.size() == 2) {
					final Iterator<Suit> setSuits = suitsForPossibleSetValue.iterator();
					final Iterator<Suit> pairSuits = suitsForPossiblePairValue.iterator();
					final FullHouseRanking fullHouseRanking = new FullHouseRanking(
							new Card(possibleSetValue, setSuits.next()),
							new Card(possibleSetValue, setSuits.next()),
							new Card(possibleSetValue, setSuits.next()),
							new Card(possiblePairValue, pairSuits.next()),
							new Card(possiblePairValue, pairSuits.next()));
					assert !setSuits.hasNext();
					assert !pairSuits.hasNext();
					
					return fullHouseRanking;
				}
			}
		}
		
		/* Search for flushes
		 * This works by assuming that there can not be two flushes
		 * in different suits, which is true for Texas Holdem. */
		for (final Suit suit : Suit.values()) {
			final EnumSet<Value> mutableValues = valuesBySuit.get(suit);
			if (mutableValues == null)
				continue;
			if (mutableValues.size() < 5)
				continue;
			
			final ImmutableList<Value> flushValues = copyOf(
					copyOf(Value.values())
					.reverse()
					.stream()
					.filter(v -> mutableValues.contains(v))
					.limit(5)
					.iterator()
			);
			assert flushValues.size() == 5;
			
			return new FlushRanking(
					suit, 
					flushValues.get(0),
					flushValues.get(1),
					flushValues.get(2),
					flushValues.get(3),
					flushValues.get(4));
		}
		
		/* Search for Straights */
		final EnumSet<Value> allValues = cards
			.stream()
			.map(c -> c.value)
			.collect(Collectors.toCollection(() -> EnumSet.noneOf(Value.class)));
		final Optional<Value> optionalStraightStrength = searchFiveConsecutiveValues(allValues);
		if (optionalStraightStrength.isPresent()) {
			final Value straightTopValue = optionalStraightStrength.get();
			final Value secondHighestCardValue = Value.values()[straightTopValue.ordinal() - 1];
			final Value middleCardValue = Value.values()[straightTopValue.ordinal() - 2];
			final Value fourthCardValue = Value.values()[straightTopValue.ordinal() - 3];
			final Value bottomCardValue;
			if (straightTopValue == Value.FIVE)
				bottomCardValue = Value.ACE;
			else
				bottomCardValue = Value.values()[straightTopValue.ordinal() - 4];
			
			final Card highestCard = new Card(
					straightTopValue,
					Iterables.get(suitsByValue.get(straightTopValue), 0));
			
			final Card secondHighestCard = new Card(
					secondHighestCardValue,
					Iterables.get(suitsByValue.get(secondHighestCardValue), 0)
			);
			final Card middleCard = new Card(
					middleCardValue,
					Iterables.get(suitsByValue.get(middleCardValue), 0)
			);
			final Card fourthCard = new Card(
					fourthCardValue,
					Iterables.get(suitsByValue.get(fourthCardValue), 0)
			);
			final Card bottomCard = new Card(
					bottomCardValue,
					Iterables.get(suitsByValue.get(bottomCardValue), 0)
			);
			
			return StraightRanking.create(
					highestCard,
					secondHighestCard,
					middleCard,
					fourthCard,
					bottomCard);
		}
			
		/* Search for Sets */
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleSetValue = Value.values()[i];
			final EnumSet<Suit> suitsForPossibleSetValue = suitsByValue.get(possibleSetValue);
			if (suitsForPossibleSetValue == null)
				continue;
			assert suitsForPossibleSetValue.size() < 4;
			if (suitsForPossibleSetValue.size() < 3)
				continue;
			assert suitsForPossibleSetValue.size() == 3;
			
			final Card firstKicker = findKicker(suitsByValue, ImmutableSet.of(possibleSetValue));
			final Card secondKicker = findKicker(suitsByValue, ImmutableSet.of(possibleSetValue, firstKicker.value));
			final Suit firstSuit, secondSuit, thirdSuit;
			{
				final Iterator<Suit> setSuits = suitsForPossibleSetValue.iterator();
				firstSuit = setSuits.next();
				secondSuit = setSuits.next();
				thirdSuit = setSuits.next();
				assert !setSuits.hasNext();
			}
			
			return new SetRanking(possibleSetValue, firstSuit, secondSuit, thirdSuit, firstKicker, secondKicker);
		}
		
		/* Search for Two-Pairs and Pairs */
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleHighPairValue = Value.values()[i];
			final EnumSet<Suit> suitsForPossibleHighPairValue = suitsByValue.get(possibleHighPairValue);
			if (suitsForPossibleHighPairValue == null)
				continue;
			if (suitsForPossibleHighPairValue.size() < 2)
				continue;
			assert suitsForPossibleHighPairValue.size() == 2;

			final Card firstCardOfHighestPair, secondCardOfHighestPair;
			{
				final Iterator<Suit> highPairSuits = suitsForPossibleHighPairValue.iterator();
				firstCardOfHighestPair = new Card(possibleHighPairValue, highPairSuits.next());
				secondCardOfHighestPair = new Card(possibleHighPairValue, highPairSuits.next());
				assert !highPairSuits.hasNext();
			}

			for (int j = i - 1; TWO.ordinal() <= j; --j) {
				final Value possibleLowPairValue = Value.values()[j];
				final EnumSet<Suit> suitsForPossibleLowPairValue = suitsByValue.get(possibleLowPairValue);

				if (suitsForPossibleLowPairValue == null)
					continue;
				if (suitsForPossibleLowPairValue.size() < 2)
					continue;
				assert suitsForPossibleLowPairValue.size() == 2;
				
				final Card kicker = findKicker(suitsByValue, ImmutableSet.of(possibleHighPairValue, possibleLowPairValue));
				
				
				final Card firstCardOfLowestPair, secondCardOfLowestPair;
				{
					final Iterator<Suit> lowPairSuits = suitsForPossibleLowPairValue.iterator();
					firstCardOfLowestPair = new Card(possibleLowPairValue, lowPairSuits.next());
					secondCardOfLowestPair = new Card(possibleLowPairValue, lowPairSuits.next());
					assert !lowPairSuits.hasNext();
				}
				
				return new TwoPairsRanking(
						firstCardOfHighestPair,
						secondCardOfHighestPair,
						firstCardOfLowestPair,
						secondCardOfLowestPair,
						kicker);
			}

			final Card firstKicker = findKicker(
					suitsByValue, 
					ImmutableSet.of(possibleHighPairValue));
			
			final Card secondKicker = findKicker(
					suitsByValue,
					ImmutableSet.of(possibleHighPairValue, firstKicker.value));
			
			final Card thirdKicker = findKicker(
					suitsByValue,
					ImmutableSet.of(possibleHighPairValue, firstKicker.value, secondKicker.value));

			return new PairRanking(
					possibleHighPairValue,
					firstCardOfHighestPair.suit,
					secondCardOfHighestPair.suit,
					firstKicker,
					secondKicker,
					thirdKicker);
		}
		
		/* Finally, build a ranking for high-card hands */
		final List<Card> bestCards = new ArrayList<>(5);
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value value = Value.values()[i];
			final Set<Suit> suits = suitsByValue.get(value);
			if (suits == null)
				continue;
			if (suits.isEmpty())
				continue;
			
			final Suit suit = Iterables.get(suits, 0);
			bestCards.add(new Card(value, suit));
			
			if (bestCards.size() == 5)
				break;
		}
		assert bestCards.size() == 5;
		return new HighCardRanking(
				bestCards.get(0),
				bestCards.get(1),
				bestCards.get(2),
				bestCards.get(3),
				bestCards.get(4));
	}

	private Card findKicker(
			final ImmutableMap<Value, EnumSet<Suit>> suitsByValue,
			final ImmutableSet<Value> excludedValues)
	{
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleKickerValue = Value.values()[i];
			if (excludedValues.contains(possibleKickerValue))
				continue;
			
			final EnumSet<Suit> suitsForThisPossibleKicker = suitsByValue.get(possibleKickerValue);
			if (suitsForThisPossibleKicker != null && suitsForThisPossibleKicker.size() > 0)
				return new Card(possibleKickerValue, Iterables.get(suitsForThisPossibleKicker, 0));
		}
		
		throw new RuntimeException("Failed to find a kicker");
	}

	private ImmutableMap<Suit, EnumSet<Value>> mapValuesBySuit(final ImmutableCollection<Card> cards) {
		final Supplier<EnumSet<Value>> enumSetSupplier = () -> EnumSet.noneOf(Value.class);
		final ImmutableMap<Suit, EnumSet<Value>> valuesBySuit = copyOf(cards.stream().collect(groupingBy(
				c -> c.suit,
				mapping(c -> c.value, toCollection(enumSetSupplier))
		)));
		return valuesBySuit;
	}

	private ImmutableMap<Value, EnumSet<Suit>> mapSuitsByValues(final ImmutableCollection<Card> cards) {
		final Supplier<EnumSet<Suit>> enumSetSupplier = () -> EnumSet.noneOf(Suit.class);
		final ImmutableMap<Value, EnumSet<Suit>> valuesBySuit = copyOf(cards.stream().collect(groupingBy(
				c -> c.value,
				mapping(c -> c.suit, toCollection(enumSetSupplier))
		)));
		return valuesBySuit;
	}

	private Optional<Value> searchFiveConsecutiveValues(final Set<Value> values) {
		if (values.size() < 5)
			throw new RuntimeException("Programming error: trying to find a straight with less than five card values");
		
		int consecutiveCardsCount = 0;
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value value = Value.values()[i];
			if (values.contains(value))
				++consecutiveCardsCount;
			else
				consecutiveCardsCount = 0;
			
			if (consecutiveCardsCount == 5)
				return Optional.of(Value.values()[i + 4]);
		}
		
		if (consecutiveCardsCount == 4)
			if (values.contains(ACE))
				return Optional.of(Value.FIVE);

		
		return Optional.empty();
	}
}
