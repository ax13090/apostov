package strength;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Suit;
import apostov.Value;

public class PokerHandRankingTest {

	@Test
	public void testStraightFlushRanking() {
		final StraightFlushRanking sf1 = new StraightFlushRanking(Value.EIGHT, Suit.HEARTS);
		final StraightFlushRanking sf2 = new StraightFlushRanking(Value.TEN, Suit.HEARTS);
		final StraightFlushRanking sf3 = new StraightFlushRanking(Value.NINE, Suit.HEARTS);
		
		final Ordering<StraightFlushRanking> ordering = StraightFlushRanking.ordering();
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
				new Card(Value.TWO, Suit.HEARTS),
				new Card(Value.TWO, Suit.DIAMONDS));
		
		final FullHouseRanking fh2 = new FullHouseRanking(
				new Card(Value.TWO, Suit.CLUBS),
				new Card(Value.TWO, Suit.HEARTS),
				new Card(Value.TWO, Suit.SPADES),
				new Card(Value.ACE, Suit.HEARTS),
				new Card(Value.ACE, Suit.DIAMONDS));
		
		final List<FullHouseRanking> sortedCopy = FullHouseRanking.ordering().sortedCopy(ImmutableList.of(fh1, fh2));
		assertSame(fh1, sortedCopy.get(1));
		assertSame(fh2, sortedCopy.get(0));
		
	}
}
