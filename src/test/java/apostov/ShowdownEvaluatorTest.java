package apostov;

import static apostov.Suit.CLUBS;
import static apostov.Suit.DIAMONDS;
import static apostov.Suit.HEARTS;
import static apostov.Suit.SPADES;
import static apostov.Value.FIVE;
import static apostov.Value.FOUR;
import static apostov.Value.KING;
import static apostov.Value.QUEEN;
import static apostov.Value.JACK;
import static apostov.Value.TWO;
import static apostov.Value.NINE;
import static apostov.Value.SEVEN;
import static apostov.Value.SIX;
import static apostov.Value.TEN;
import static apostov.Value.ACE;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.concat;
import static org.junit.Assert.*;
import static strength.PokerHandKind.PAIR;
import static strength.PokerHandKind.HIGH_CARD;

import org.junit.Test;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import strength.HighCardRanking;
import strength.PairRanking;
import strength.PokerHandRanking;

public class ShowdownEvaluatorTest {

	@Test
	public void selectBestCombinationWithTwoKings() {
		final Card seven = new Card(SEVEN, SPADES);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				seven,
				new Card(KING, CLUBS));
		

		final Card queen = new Card(QUEEN, CLUBS);
		final Card six = new Card(SIX, DIAMONDS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				new Card(FIVE, CLUBS),
				new Card(KING, DIAMONDS),
				new Card(FOUR, HEARTS),
				queen,
				six);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(PAIR, handStrength.handKind);
		final PairRanking pairRanking = (PairRanking) handStrength;
		assertSame(KING, pairRanking.pairValue);
		assertEquals(
				ImmutableSet.of(CLUBS, DIAMONDS),
				ImmutableSet.of(pairRanking.firstSuit, pairRanking.secondSuit));
		assertEquals(queen, pairRanking.firstKicker);
		assertEquals(seven, pairRanking.secondKicker);
		assertEquals(six, pairRanking.thirdKicker);
	}
	
	@Test
	public void selectBestCombinationWithNothing() {
		final Card jack = new Card(JACK, HEARTS);
		final Card nine = new Card(NINE, DIAMONDS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				jack,
				nine);
		

		final Card ace = new Card(ACE, HEARTS);
		final Card queen = new Card(QUEEN, SPADES);
		final Card ten = new Card(TEN, HEARTS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				new Card(TWO, DIAMONDS),
				new Card(SIX, SPADES),
				queen,
				ten,
				ace);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(HIGH_CARD, handStrength.handKind);
		final HighCardRanking highCardRanking = (HighCardRanking) handStrength;
		assertEquals(ace, highCardRanking.highestCard);
		assertEquals(queen, highCardRanking.secondHighestCard);
		assertEquals(jack, highCardRanking.middleCard);
		assertEquals(ten, highCardRanking.fourthCard);
		assertEquals(nine, highCardRanking.bottomCard);
	}
}
