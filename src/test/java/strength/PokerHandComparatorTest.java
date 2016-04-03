package strength;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import apostov.Card;
import apostov.Suit;
import apostov.Value;
import apostov.strength.PokerHandComparator;
import apostov.strength.ranking.HighCardRanking;
import apostov.strength.ranking.PairRanking;
import apostov.strength.ranking.PokerHandRanking;
import apostov.strength.ranking.SetRanking;

public class PokerHandComparatorTest {

	@Test
	public void testComparator() {
		final PokerHandComparator comparator = new PokerHandComparator();
		
		final PokerHandRanking pair1 = new PairRanking(
				Value.NINE,
				Suit.HEARTS,
				Suit.CLUBS,
				null,
				null,
				null);
		
		final PokerHandRanking pair2 = new PairRanking(
				Value.TEN,
				Suit.SPADES,
				Suit.DIAMONDS,
				null,
				null,
				null);
		
		final PokerHandRanking set = new SetRanking(
				Value.FIVE,
				Suit.SPADES,
				Suit.DIAMONDS,
				Suit.HEARTS,
				null,
				null);
		
		final PokerHandRanking highCard = new HighCardRanking(
				new Card(Value.KING, Suit.HEARTS),
				new Card(Value.SEVEN, Suit.CLUBS),
				null,
				null,
				null);
		
		final List<PokerHandRanking> sortedCopy = comparator.immutableSortedCopy(ImmutableList.of(pair1, pair2, set, highCard));
		assertEquals(
				ImmutableList.of(highCard, pair1, pair2, set),
				sortedCopy
		);
	}
}
