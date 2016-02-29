package apostov;

import static apostov.Suit.CLUBS;
import static apostov.Suit.DIAMONDS;
import static apostov.Suit.HEARTS;
import static apostov.Suit.SPADES;
import static apostov.Value.FIVE;
import static apostov.Value.FOUR;
import static apostov.Value.KING;
import static apostov.Value.QUEEN;
import static apostov.Value.SEVEN;
import static apostov.Value.SIX;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.concat;
import static org.junit.Assert.*;
import static strength.PokerHandKind.PAIR;

import org.junit.Test;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

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
}
