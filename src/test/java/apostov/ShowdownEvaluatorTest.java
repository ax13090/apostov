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
import static apostov.strength.PokerHandKind.FLUSH;
import static apostov.strength.PokerHandKind.FOUR_OF_A_KIND;
import static apostov.strength.PokerHandKind.FULL_HOUSE;
import static apostov.strength.PokerHandKind.HIGH_CARD;
import static apostov.strength.PokerHandKind.PAIR;
import static apostov.strength.PokerHandKind.STRAIGHT;
import static apostov.strength.PokerHandKind.STRAIGHT_FLUSH;
import static apostov.strength.PokerHandKind.THREE_OF_A_KIND;
import static apostov.strength.PokerHandKind.TWO_PAIRS;
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

import org.junit.Test;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import apostov.strength.ranking.FlushRanking;
import apostov.strength.ranking.FullHouseRanking;
import apostov.strength.ranking.HighCardRanking;
import apostov.strength.ranking.PairRanking;
import apostov.strength.ranking.PokerHandRanking;
import apostov.strength.ranking.QuadRanking;
import apostov.strength.ranking.SetRanking;
import apostov.strength.ranking.StraightFlushRanking;
import apostov.strength.ranking.StraightRanking;
import apostov.strength.ranking.TwoPairsRanking;

public class ShowdownEvaluatorTest {

	@Test
	public void evaluateShowdownAcesVersusKings() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand kings = new HoldemHolecardHand(new Card(KING, CLUBS), new Card(KING, SPADES));
		
		final Board boardWithKing = new Board(
				new Card(FIVE, SPADES),
				new Card(SEVEN, CLUBS),
				new Card(FIVE, DIAMONDS),
				new Card(NINE, CLUBS),
				new Card(TEN, DIAMONDS));
		
		final ImmutableSet<HoldemHolecardHand> winners = new HoldemShowdownEvaluator().evaluateShowdown(
				ImmutableList.of(aces, kings),
				boardWithKing);
		
		assertEquals(ImmutableSet.of(aces), winners);
	}

	@Test
	public void evaluateShowdownAcesVersusLuckyKings() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand kings = new HoldemHolecardHand(new Card(KING, CLUBS), new Card(KING, SPADES));
		
		final Board boardWithKing = new Board(
				new Card(FIVE, SPADES),
				new Card(SEVEN, CLUBS),
				new Card(KING, DIAMONDS),
				new Card(NINE, CLUBS),
				new Card(TEN, DIAMONDS));
		
		final ImmutableSet<HoldemHolecardHand> winners = new HoldemShowdownEvaluator().evaluateShowdown(
				ImmutableList.of(aces, kings),
				boardWithKing);
		
		assertEquals(ImmutableSet.of(kings), winners);
	}
	
	@Test
	public void evaluateShowdownWhenPlayingTheBoard() {
		final HoldemHolecardHand rags1 = new HoldemHolecardHand(new Card(FOUR, HEARTS), new Card(TWO, CLUBS));
		final HoldemHolecardHand rags2 = new HoldemHolecardHand(new Card(TWO, HEARTS), new Card(THREE, SPADES));
		
		final Board board = new Board(
				new Card(FIVE, SPADES),
				new Card(SEVEN, CLUBS),
				new Card(SIX, SPADES),
				new Card(NINE, CLUBS),
				new Card(ACE, SPADES));
		
		final ImmutableSet<HoldemHolecardHand> winners = new HoldemShowdownEvaluator().evaluateShowdown(
				ImmutableList.of(rags1, rags2),
				board);
		
		assertEquals(ImmutableSet.of(rags1, rags2), winners);
	}
	
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
	public void selectBestCombinationWithQuad() {
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				new Card(NINE, SPADES),
				new Card(NINE, HEARTS));

		final Card nineOfClubs = new Card(NINE, CLUBS);
		final Card nineOfDiamonds = new Card(NINE, DIAMONDS);
		final Card king = new Card(KING, DIAMONDS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				nineOfClubs,
				nineOfDiamonds,
				king,
				new Card(SEVEN, DIAMONDS),
				new Card(EIGHT, CLUBS));
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(FOUR_OF_A_KIND, handStrength.handKind);
		final QuadRanking quadRanking = (QuadRanking) handStrength;
		assertEquals(NINE, quadRanking.value);
		assertEquals(king, quadRanking.kicker);
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
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
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(STRAIGHT, handStrength.handKind);
		final StraightRanking straightRanking = (StraightRanking) handStrength;
		assertEquals(six, straightRanking.highestCard);
		assertEquals(five, straightRanking.secondHighestCard);
		assertEquals(four, straightRanking.middleCard);
		assertEquals(three, straightRanking.fourthCard);
		assertEquals(two, straightRanking.bottomCard);
	}
	
	@Test
	public void selectBestCombinationWithFlush() {
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				new Card(TEN, SPADES),
				new Card(EIGHT, SPADES)
		);

		final ImmutableCollection<Card> board = ImmutableSet.of(
				new Card(NINE, SPADES),
				new Card(TWO, HEARTS),
				new Card(EIGHT, CLUBS),
				new Card(TWO, SPADES),
				new Card(QUEEN, SPADES)
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(FLUSH, handStrength.handKind);
		final FlushRanking flushRanking = (FlushRanking) handStrength;
		assertEquals(SPADES, flushRanking.suit);
		assertEquals(QUEEN, flushRanking.firstValue);
		assertEquals(TEN, flushRanking.secondValue);
		assertEquals(NINE, flushRanking.thirdValue);
		assertEquals(EIGHT, flushRanking.fourthValue);
		assertEquals(TWO, flushRanking.fifthValue);
	}
	
	@Test
	public void selectBestCombinationWithSixCardFlush() {
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				new Card(TEN, SPADES),
				new Card(THREE, SPADES)
		);

		final ImmutableCollection<Card> board = ImmutableSet.of(
				new Card(NINE, SPADES),
				new Card(TWO, HEARTS),
				new Card(EIGHT, SPADES),
				new Card(TWO, SPADES),
				new Card(QUEEN, SPADES)
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(FLUSH, handStrength.handKind);
		final FlushRanking flushRanking = (FlushRanking) handStrength;
		assertEquals(SPADES, flushRanking.suit);
		assertEquals(QUEEN, flushRanking.firstValue);
		assertEquals(TEN, flushRanking.secondValue);
		assertEquals(NINE, flushRanking.thirdValue);
		assertEquals(EIGHT, flushRanking.fourthValue);
		assertEquals(THREE, flushRanking.fifthValue);
	}

	@Test
	public void selectBestCombinationWithFullHouse() {
		final Card twoOfSpades = new Card(TWO, SPADES);
		final Card fiveOfHearts = new Card(FIVE, HEARTS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				twoOfSpades,
				fiveOfHearts);
	
		final Card twoOfClubs = new Card(TWO, CLUBS);
		final Card fiveOfDiamonds = new Card(FIVE, DIAMONDS);
		final Card twoOfDiamonds = new Card(TWO, DIAMONDS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				twoOfClubs,
				new Card(SEVEN, DIAMONDS),
				fiveOfDiamonds,
				new Card(ACE, HEARTS),
				twoOfDiamonds
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(FULL_HOUSE, handStrength.handKind);
		final FullHouseRanking fullHouseRanking = (FullHouseRanking) handStrength;
		assertEquals(
				ImmutableSet.of(twoOfClubs, twoOfDiamonds, twoOfSpades),
				ImmutableSet.of(fullHouseRanking.firstSetCard, fullHouseRanking.secondSetCard, fullHouseRanking.thirdSetCard));
		assertEquals(
				ImmutableSet.of(fiveOfDiamonds, fiveOfHearts),
				ImmutableSet.of(fullHouseRanking.firstPairCard, fullHouseRanking.secondPairCard));
	}

	@Test
	public void selectBestCombinationWithMaxStraightFlush() {
		final Card ace = new Card(ACE, CLUBS);
		final Card jack = new Card(JACK, CLUBS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				jack,
				ace);
	
		final Card king = new Card(KING, CLUBS);
		final Card ten = new Card(TEN, CLUBS);
		final Card queen = new Card(QUEEN, CLUBS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				king,
				ten,
				queen,
				new Card(FIVE, DIAMONDS),
				new Card(SEVEN, HEARTS));
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(STRAIGHT_FLUSH, handStrength.handKind);
		final StraightFlushRanking straightRanking = (StraightFlushRanking) handStrength;
		assertEquals(ACE, straightRanking.highestCardValue);
		assertEquals(CLUBS, straightRanking.suit);
	}

	@Test
	public void selectBestCombinationWithStraightFlushToTheFive() {
		final Card three = new Card(THREE, HEARTS);
		final Card ace = new Card(ACE, HEARTS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				three,
				ace);
	
		final Card five = new Card(FIVE, HEARTS);
		final Card two = new Card(TWO, HEARTS);
		final Card four = new Card(FOUR, HEARTS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				five,
				two,
				new Card(SEVEN, SPADES),
				new Card(QUEEN, SPADES),
				four
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));

		assertSame(STRAIGHT_FLUSH, handStrength.handKind);
		final StraightFlushRanking straightRanking = (StraightFlushRanking) handStrength;
		assertEquals(FIVE, straightRanking.highestCardValue);
		assertEquals(HEARTS, straightRanking.suit);
	}

	@Test
	public void selectBestCombinationWithStraightFlushToTheSix() {
		final Card three = new Card(THREE, DIAMONDS);
		final Card six = new Card(SIX, DIAMONDS);
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				three,
				six);
	
		final Card five = new Card(FIVE, DIAMONDS);
		final Card two = new Card(TWO, DIAMONDS);
		final Card four = new Card(FOUR, DIAMONDS);
		final ImmutableCollection<Card> board = ImmutableSet.of(
				five,
				two,
				new Card(NINE, SPADES),
				new Card(QUEEN, SPADES),
				four
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));

		assertSame(STRAIGHT_FLUSH, handStrength.handKind);
		final StraightFlushRanking straightRanking = (StraightFlushRanking) handStrength;
		assertEquals(SIX, straightRanking.highestCardValue);
		assertEquals(DIAMONDS, straightRanking.suit);
	}

	@Test
	public void selectBestCombinationWithTrickyFullHouse() {
		final ImmutableCollection<Card> holeCards = ImmutableSet.of(
				new Card(TWO, SPADES),
				new Card(FIVE, HEARTS));
	
		final ImmutableCollection<Card> board = ImmutableSet.of(
				new Card(TWO, CLUBS),
				new Card(SEVEN, DIAMONDS),
				new Card(FIVE, DIAMONDS),
				new Card(FIVE, CLUBS),
				new Card(TWO, DIAMONDS)
		);
		
		final AbstractShowdownEvaluator<HoldemHolecardHand> evaluator = new HoldemShowdownEvaluator();
		final PokerHandRanking handStrength = evaluator.selectBestCombinationFromAllCards(copyOf(concat(holeCards, board)));
		
		assertSame(FULL_HOUSE, handStrength.handKind);
		final FullHouseRanking fullHouseRanking = (FullHouseRanking) handStrength;
		assertEquals(FIVE, fullHouseRanking.valueOfTheSet());
		assertEquals(TWO, fullHouseRanking.valueOfThePair());
	}
	
	
}
