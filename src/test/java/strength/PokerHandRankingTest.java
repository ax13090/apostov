package strength;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import apostov.Card;
import apostov.Suit;
import apostov.Value;
import apostov.strength.PokerHandComparator;
import apostov.strength.ranking.FullHouseRanking;
import apostov.strength.ranking.PokerHandRanking;
import apostov.strength.ranking.StraightFlushRanking;
import apostov.strength.ranking.StraightRanking;

public class PokerHandRankingTest {

	@Test
	public void testStraightFlushRanking() {
		final StraightFlushRanking sf1 = new StraightFlushRanking(Value.EIGHT, Suit.HEARTS);
		final StraightFlushRanking sf2 = new StraightFlushRanking(Value.TEN, Suit.HEARTS);
		final StraightFlushRanking sf3 = new StraightFlushRanking(Value.NINE, Suit.HEARTS);
		
		final PokerHandComparator ordering = new PokerHandComparator();
		final StraightFlushRanking bestRanking = ordering.max(sf1, sf2, sf3);
		final StraightFlushRanking worstRanking = ordering.min(sf1, sf2, sf3);
		assertEquals(Value.TEN, bestRanking.highestCardValue);
		assertEquals(Value.EIGHT, worstRanking.highestCardValue);
	}
	
	@Test
	public void testFullHouseRanking() {
		final FullHouseRanking fh1 = new FullHouseRanking(
				new Card(Value.SIX, Suit.CLUBS),
				new Card(Value.SIX, Suit.HEARTS),
				new Card(Value.SIX, Suit.SPADES),
				new Card(Value.FOUR, Suit.HEARTS),
				new Card(Value.FOUR, Suit.DIAMONDS));
		
		final FullHouseRanking fh2 = new FullHouseRanking(
				new Card(Value.SIX, Suit.CLUBS),
				new Card(Value.SIX, Suit.HEARTS),
				new Card(Value.SIX, Suit.SPADES),
				new Card(Value.TWO, Suit.HEARTS),
				new Card(Value.TWO, Suit.DIAMONDS));
		
		final FullHouseRanking fh3 = new FullHouseRanking(
				new Card(Value.TWO, Suit.CLUBS),
				new Card(Value.TWO, Suit.HEARTS),
				new Card(Value.TWO, Suit.SPADES),
				new Card(Value.ACE, Suit.HEARTS),
				new Card(Value.ACE, Suit.DIAMONDS));
		
		final PokerHandComparator ordering = new PokerHandComparator();
		final List<FullHouseRanking> sortedCopy = ordering.sortedCopy(ImmutableList.of(fh1, fh2, fh3));
		assertSame(fh3, sortedCopy.get(0));
		assertSame(fh2, sortedCopy.get(1));
		assertSame(fh1, sortedCopy.get(2));
		
	}
	
	@Test
	public void testStraightRanking() {
		final StraightRanking s1 = StraightRanking.create(
				new Card(Value.ACE, Suit.CLUBS),
				new Card(Value.KING, Suit.HEARTS),
				new Card(Value.QUEEN, Suit.HEARTS),
				new Card(Value.JACK, Suit.SPADES),
				new Card(Value.TEN, Suit.SPADES));
		final StraightRanking s2 = StraightRanking.create(
				new Card(Value.ACE, Suit.CLUBS),
				new Card(Value.KING, Suit.HEARTS),
				new Card(Value.QUEEN, Suit.HEARTS),
				new Card(Value.JACK, Suit.SPADES),
				new Card(Value.TEN, Suit.SPADES));
		final StraightRanking s3 = StraightRanking.create(
				new Card(Value.KING, Suit.HEARTS),
				new Card(Value.QUEEN, Suit.HEARTS),
				new Card(Value.JACK, Suit.SPADES),
				new Card(Value.TEN, Suit.SPADES),
				new Card(Value.NINE, Suit.DIAMONDS));
		final StraightRanking s4 = StraightRanking.create(
				new Card(Value.FIVE, Suit.HEARTS),
				new Card(Value.FOUR, Suit.HEARTS),
				new Card(Value.THREE, Suit.SPADES),
				new Card(Value.TWO, Suit.DIAMONDS),
				new Card(Value.ACE, Suit.DIAMONDS));
		
		final PokerHandComparator ordering = new PokerHandComparator();
		final List<PokerHandRanking> sortedCopy = ordering.sortedCopy(ImmutableList.of(s1, s2, s3, s4));
		assertSame(s4, sortedCopy.get(0));
		assertSame(s3, sortedCopy.get(1));
		assertTrue(s2 == sortedCopy.get(2) || s1 == sortedCopy.get(2));
		assertEquals(0, ordering.compare(s1, s2));
	}
}
