package apostov;

import static apostov.Suit.CLUBS;
import static apostov.Suit.DIAMONDS;
import static apostov.Suit.HEARTS;
import static apostov.Suit.SPADES;
import static apostov.Value.ACE;
import static apostov.Value.KING;
import static apostov.Value.QUEEN;
import static apostov.Value.SEVEN;
import static apostov.Value.TEN;
import static apostov.Value.TWO;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class HoldemHandResultEnumeratorTest extends HandResultEnumeratorTest {

	@Test
	public void acesVersusTrashPreflop() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand trash = new HoldemHolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		final Map<HoldemHolecardHand, Fraction> percentages = computePercentages(aces, trash);
		assertTrue(percentages.get(aces).doubleValue() > 0.8);
		assertTrue(percentages.get(trash).doubleValue() < 0.2);
	}

	@Test
	public void aceTenVersusQueenTenPreflop() {
		final HoldemHolecardHand aceTen = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(TEN, SPADES));
		final HoldemHolecardHand queenTen = new HoldemHolecardHand(new Card(QUEEN, DIAMONDS), new Card(TEN, HEARTS));
		
		final Map<HoldemHolecardHand, Fraction> percentages = computePercentages(aceTen, queenTen);
		assertTrue(percentages.get(aceTen).doubleValue() > 0.7);
		assertTrue(percentages.get(queenTen).doubleValue() < 0.3);
	}
	
	@Test
	public void acesVersusTrashOnLuckySetBoard() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand trash = new HoldemHolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		final ImmutableList<Card> board = ImmutableList.of(new Card(TWO, DIAMONDS), new Card(TWO, SPADES), new Card(TEN, HEARTS));
		
		final Map<HoldemHolecardHand, Fraction> percentages = new HoldemHandResultEnumerator().enumerateBoardsAndMeasureWins(
				ImmutableList.of(aces, trash),
				board);
		System.out.println("Board: " + board);
		displayResults(percentages);
		assertTrue(percentages.get(aces).doubleValue() < percentages.get(trash).doubleValue());
	}
	
	@Test
	public void acesVersusTrashOnLuckyQuadBoard() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand trash = new HoldemHolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		final ImmutableList<Card> board = ImmutableList.of(new Card(TWO, DIAMONDS), new Card(TWO, SPADES), new Card(TWO, HEARTS));
		
		final Map<HoldemHolecardHand, Fraction> percentages = new HoldemHandResultEnumerator().enumerateBoardsAndMeasureWins(
				ImmutableList.of(aces, trash),
				board);
		System.out.println("Board: " + board);
		displayResults(percentages);
		assertTrue(percentages.get(aces).doubleValue() < percentages.get(trash).doubleValue());
	}
	
	@Test
	public void acesVersusTrashShowdown() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand trash = new HoldemHolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		final ImmutableList<Card> board = ImmutableList.of(
				new Card(TWO, DIAMONDS),
				new Card(TWO, SPADES),
				new Card(TWO, HEARTS),
				new Card(ACE, DIAMONDS),
				new Card(ACE, HEARTS)
		);
		
		final Map<HoldemHolecardHand, Fraction> percentages = new HoldemHandResultEnumerator().enumerateBoardsAndMeasureWins(
				ImmutableList.of(aces, trash),
				board);
		System.out.println("Board: " + board);
		displayResults(percentages);
		assertTrue(percentages.get(aces).equals(Fraction.ONE));
		assertTrue(percentages.get(trash).equals(Fraction.ZERO));
	}
	
	@Test
	public void acesVersusTrashBadBeatShowdown() {
		final HoldemHolecardHand aces = new HoldemHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HoldemHolecardHand trash = new HoldemHolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		final ImmutableList<Card> board = ImmutableList.of(
				new Card(TWO, DIAMONDS),
				new Card(TWO, SPADES),
				new Card(TWO, HEARTS),
				new Card(ACE, DIAMONDS),
				new Card(KING, HEARTS)
		);
		
		final Map<HoldemHolecardHand, Fraction> percentages = new HoldemHandResultEnumerator().enumerateBoardsAndMeasureWins(
				ImmutableList.of(aces, trash),
				board);
		System.out.println("Board: " + board);
		displayResults(percentages);
		assertTrue(percentages.get(aces).equals(Fraction.ZERO));
		assertTrue(percentages.get(trash).equals(Fraction.ONE));
	}
	
}
