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
import static apostov.Value.THREE;
import static apostov.Value.NINE;
import static apostov.Value.EIGHT;
import static apostov.Value.SEVEN;
import static apostov.Value.SIX;
import static apostov.Value.TEN;
import static apostov.Value.ACE;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.concat;
import static org.junit.Assert.*;
import static strength.PokerHandKind.HIGH_CARD;
import static strength.PokerHandKind.PAIR;
import static strength.PokerHandKind.TWO_PAIRS;
import static strength.PokerHandKind.THREE_OF_A_KIND;
import static strength.PokerHandKind.STRAIGHT;
import static strength.PokerHandKind.FLUSH;
import static strength.PokerHandKind.FULL_HOUSE;
import static strength.PokerHandKind.FOUR_OF_A_KIND;
import static strength.PokerHandKind.STRAIGHT_FLUSH;


import org.junit.Test;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import strength.HighCardRanking;
import strength.PairRanking;
import strength.PokerHandRanking;
import strength.SetRanking;
import strength.StraightFlushRanking;
import strength.StraightRanking;
import strength.TwoPairsRanking;

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
	
	
	@Test
	public void selectBestCombinationWithSet() {
		final Card queen = new Card(QUEEN, SPADES);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				queen,
				new Card(NINE, HEARTS));

		final Card nineOfClubs = new Card(NINE, CLUBS);
		final Card nineOfDiamonds = new Card(NINE, DIAMONDS);
		final Card king = new Card(KING, DIAMONDS);
		final Card ace = new Card(ACE, HEARTS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				nineOfClubs,
				nineOfDiamonds,
				king,
				ace,
				new Card(EIGHT, CLUBS));
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(THREE_OF_A_KIND, handStrength.handKind);
		final SetRanking setRanking = (SetRanking) handStrength;
		assertEquals(NINE, setRanking.valueOfTheSet);
		assertEquals(
				ImmutableSet.of(CLUBS, DIAMONDS, HEARTS),
				ImmutableSet.of(setRanking.firstSuit, setRanking.secondSuit, setRanking.thirdSuit));
		assertEquals(ace, setRanking.firstKicker);
		assertEquals(king, setRanking.secondKicker);
	}
	
	@Test
	public void selectBestCombinationTwoPairs() {
		final Card firstKing = new Card(KING, SPADES);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				firstKing,
				new Card(FIVE, CLUBS));

		final Card secondKing = new Card(KING, CLUBS);
		final Card firstQueen = new Card(QUEEN, DIAMONDS);
		final Card secondQueen = new Card(QUEEN, CLUBS);
		final Card nine = new Card(NINE, SPADES);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				firstQueen,
				new Card(THREE, DIAMONDS),
				nine,
				secondQueen,
				secondKing);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(TWO_PAIRS, handStrength.handKind);
		final TwoPairsRanking twoPairsRanking = (TwoPairsRanking) handStrength;
		assertEquals(
				ImmutableSet.of(firstKing, secondKing),
				ImmutableSet.of(twoPairsRanking.firstCardOfHighestPair, twoPairsRanking.secondCardOfHighestPair));
		assertEquals(
				ImmutableSet.of(firstQueen, secondQueen),
				ImmutableSet.of(twoPairsRanking.firstCardOfLowestPair, twoPairsRanking.secondCardOfLowestPair));
		assertEquals(nine, twoPairsRanking.kicker);
	}
	
	@Test
	public void selectBestCombinationWithMaxStraight() {
		final Card ace = new Card(ACE, CLUBS);
		final Card jack = new Card(JACK, HEARTS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				jack,
				ace);

		final Card king = new Card(KING, CLUBS);
		final Card ten = new Card(TEN, SPADES);
		final Card queen = new Card(QUEEN, DIAMONDS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				king,
				ten,
				queen,
				new Card(FIVE, DIAMONDS),
				new Card(SEVEN, HEARTS));
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(STRAIGHT, handStrength.handKind);
		final StraightRanking straightRanking = (StraightRanking) handStrength;
		assertEquals(ace, straightRanking.highestCard);
		assertEquals(king, straightRanking.secondHighestCard);
		assertEquals(queen, straightRanking.middleCard);
		assertEquals(jack, straightRanking.fourthCard);
		assertEquals(ten, straightRanking.bottomCard);
	}
	
	@Test
	public void selectBestCombinationWithStraightToTheFive() {
		final Card three = new Card(THREE, HEARTS);
		final Card ace = new Card(ACE, HEARTS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				three,
				ace);

		final Card five = new Card(FIVE, HEARTS);
		final Card two = new Card(TWO, HEARTS);
		final Card four = new Card(FOUR, CLUBS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				five,
				two,
				new Card(SEVEN, SPADES),
				new Card(QUEEN, SPADES),
				four
		);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(STRAIGHT, handStrength.handKind);
		final StraightRanking straightRanking = (StraightRanking) handStrength;
		assertEquals(five, straightRanking.highestCard);
		assertEquals(four, straightRanking.secondHighestCard);
		assertEquals(three, straightRanking.middleCard);
		assertEquals(two, straightRanking.fourthCard);
		assertEquals(ace, straightRanking.bottomCard);
	}
	
	@Test
	public void selectBestCombinationWithStraightToTheSix() {
		final Card three = new Card(THREE, HEARTS);
		final Card six = new Card(SIX, DIAMONDS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				three,
				six);

		final Card five = new Card(FIVE, HEARTS);
		final Card two = new Card(TWO, HEARTS);
		final Card four = new Card(FOUR, CLUBS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				five,
				two,
				new Card(NINE, SPADES),
				new Card(QUEEN, SPADES),
				four
		);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombination(copyOf(concat(holeCards, board)));
		
		assertSame(STRAIGHT, handStrength.handKind);
		final StraightRanking straightRanking = (StraightRanking) handStrength;
		assertEquals(six, straightRanking.highestCard);
		assertEquals(five, straightRanking.secondHighestCard);
		assertEquals(four, straightRanking.middleCard);
		assertEquals(three, straightRanking.fourthCard);
		assertEquals(two, straightRanking.bottomCard);
	}
}
