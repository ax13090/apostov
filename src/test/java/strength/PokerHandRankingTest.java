package strength;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Ordering;

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
}
