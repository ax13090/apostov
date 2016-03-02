package apostov;

import static apostov.Value.ACE;
import static apostov.Value.TWO;
import static com.google.common.collect.ImmutableList.copyOf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
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
	
	public PokerHandRanking selectBestCombination(final ImmutableCollection<Card> cards) {
		assert cards.size() >= 5;
		
		final ImmutableTable<Value, Suit, Card> table = buildDoubleEntryTable(cards);
		
		/* Search for Straight-Flushes
		 * This works by assuming that there can not be two straight-flushes
		 * in different suits, which is true for Texas Holdem. */
		for (final Suit suit : Suit.values()) {
			final Set<Value> mutableValues = table.column(suit).keySet();
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
			final Set<Suit> suitsForCurrentValue = table.row(value).keySet();
			if (suitsForCurrentValue.size() == 4) {
				final Card kicker = findKicker(table, ImmutableSet.of(value));
				return new QuadRanking(value, kicker);
			}
		}
		
		/* Search for full-houses */
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleSetValue = Value.values()[i];
			final ImmutableMap<Suit, Card> cardsForSetMappedBySuit = table.row(possibleSetValue);
			assert cardsForSetMappedBySuit.size() < 4;
			if (cardsForSetMappedBySuit.size() < 3)
				continue;

			assert cardsForSetMappedBySuit.size() == 3;
			for (int j = ACE.ordinal(); TWO.ordinal() <= j; --j) {
				final Value possiblePairValue = Value.values()[j];
				if (possibleSetValue == possiblePairValue)
					continue;
				
				final ImmutableMap<Suit, Card> cardsForPairMappedBySuit = table.row(possiblePairValue);
				final Set<Suit> suitsForPossiblePairValue = cardsForPairMappedBySuit.keySet();
				assert cardsForPairMappedBySuit.size() < 3;
				
				if (suitsForPossiblePairValue.size() == 2) {
					final Iterator<Card> setCards = cardsForSetMappedBySuit.values().iterator();
					final Iterator<Card> pairCards = cardsForPairMappedBySuit.values().iterator();
					final FullHouseRanking fullHouseRanking = new FullHouseRanking(
							setCards.next(),
							setCards.next(),
							setCards.next(),
							pairCards.next(),
							pairCards.next());
					assert !setCards.hasNext();
					assert !pairCards.hasNext();
					
					return fullHouseRanking;
				}
			}
		}
		
		/* Search for flushes
		 * This works by assuming that there can not be two flushes
		 * in different suits, which is true for Texas Holdem. */
		for (final Suit suit : Suit.values()) {
			final ImmutableMap<Value, Card> cardsByValue = table.column(suit);
			final Set<Value> valuesForThisSuit = cardsByValue.keySet();
			if (valuesForThisSuit.size() < 5)
				continue;
			
			final ImmutableList<Value> descendingFlushValues = copyOf(
					copyOf(Value.values())
					.reverse()
					.stream()
					.filter(v -> valuesForThisSuit.contains(v))
					.limit(5)
					.iterator());
			assert descendingFlushValues.size() == 5;
			
			return new FlushRanking(
					suit, 
					descendingFlushValues.get(0),
					descendingFlushValues.get(1),
					descendingFlushValues.get(2),
					descendingFlushValues.get(3),
					descendingFlushValues.get(4));
		}
		
		/* Search for Straights */
		final Optional<Value> optionalStraightStrength = searchFiveConsecutiveValues(table.rowKeySet());
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
			
			final Card highestCard = Iterables.get(table.row(straightTopValue).values(), 0);
			final Card secondHighestCard = Iterables.get(table.row(secondHighestCardValue).values(), 0);
			final Card middleCard = Iterables.get(table.row(middleCardValue).values(), 0);
			final Card fourthCard = Iterables.get(table.row(fourthCardValue).values(), 0);
			final Card bottomCard = Iterables.get(table.row(bottomCardValue).values(), 0);
			
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
			final Set<Suit> suitsForPossibleSetValue = table.row(possibleSetValue).keySet();
			assert suitsForPossibleSetValue.size() < 4;
			if (suitsForPossibleSetValue.size() < 3)
				continue;
			assert suitsForPossibleSetValue.size() == 3;
			
			final Card firstKicker = findKicker(table, ImmutableSet.of(possibleSetValue));
			final Card secondKicker = findKicker(table, ImmutableSet.of(possibleSetValue, firstKicker.value));
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
			final ImmutableMap<Suit, Card> highPairCardsBySuit = table.row(possibleHighPairValue);
			if (highPairCardsBySuit.size() < 2)
				continue;
			assert highPairCardsBySuit.size() == 2;

			final Card firstCardOfHighestPair, secondCardOfHighestPair;
			{
				final Iterator<Card> highPairCards = highPairCardsBySuit.values().iterator();
				firstCardOfHighestPair = highPairCards.next();
				secondCardOfHighestPair = highPairCards.next();
				assert !highPairCards.hasNext();
			}

			for (int j = i - 1; TWO.ordinal() <= j; --j) {
				final Value possibleLowPairValue = Value.values()[j];
				final ImmutableMap<Suit, Card> lowPairCardsBySuit = table.row(possibleLowPairValue);

				if (lowPairCardsBySuit.size() < 2)
					continue;
				assert lowPairCardsBySuit.size() == 2;
				
				final Card kicker = findKicker(table, ImmutableSet.of(possibleHighPairValue, possibleLowPairValue));
				
				
				final Card firstCardOfLowestPair, secondCardOfLowestPair;
				{
					final Iterator<Card> lowPairCards = lowPairCardsBySuit.values().iterator();
					firstCardOfLowestPair = lowPairCards.next();
					secondCardOfLowestPair = lowPairCards.next();
					assert !lowPairCards.hasNext();
				}
				
				return new TwoPairsRanking(
						firstCardOfHighestPair,
						secondCardOfHighestPair,
						firstCardOfLowestPair,
						secondCardOfLowestPair,
						kicker);
			}

			final Card firstKicker = findKicker(
					table,
					ImmutableSet.of(possibleHighPairValue));
			
			final Card secondKicker = findKicker(
					table,
					ImmutableSet.of(possibleHighPairValue, firstKicker.value));
			
			final Card thirdKicker = findKicker(
					table,
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
			final ImmutableMap<Suit, Card> cardsBySuit = table.row(value);
			if (cardsBySuit.isEmpty())
				continue;
			
			final Card card = Iterables.get(cardsBySuit.values(), 0);
			bestCards.add(card);
			
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

	private ImmutableTable<Value, Suit, Card> buildDoubleEntryTable(final ImmutableCollection<Card> cards) {
		final ImmutableTable.Builder<Value, Suit, Card> builder = ImmutableTable.builder();
		for (final Card card : cards) {
			builder.put(card.value, card.suit, card);
		}
		return builder.build();
	}

	private Card findKicker(
			final ImmutableTable<Value, Suit, Card> table,
			final ImmutableSet<Value> excludedValues)
	{
		for (int i = ACE.ordinal(); TWO.ordinal() <= i; --i) {
			final Value possibleKickerValue = Value.values()[i];
			if (excludedValues.contains(possibleKickerValue))
				continue;
			
			final ImmutableMap<Suit, Card> row = table.row(possibleKickerValue);
			if (row.size() > 0)
				return Iterables.get(row.values(), 0);
		}
		
		throw new RuntimeException("Failed to find a kicker");
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
