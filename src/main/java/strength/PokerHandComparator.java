package strength;

import java.util.Comparator;

import com.google.common.collect.Ordering;

public class PokerHandComparator extends Ordering<PokerHandRanking> {

	@Override
	public int compare(final PokerHandRanking h1, final PokerHandRanking h2) {

		{
			final int handKindComparisonResult = Comparator.comparing((PokerHandRanking r) -> r.handKind).compare(h1, h2);
			if (handKindComparisonResult != 0)
				return handKindComparisonResult;
		}

		final PokerHandKind kind = h1.handKind;
		assert h1.handKind == h2.handKind;

		
		switch (kind)  {
			case STRAIGHT_FLUSH:
				final StraightFlushRanking sf1  = (StraightFlushRanking) h1;
				final StraightFlushRanking sf2  = (StraightFlushRanking) h2;
				return StraightFlushRanking.ordering().compare(sf1, sf2);
			case FOUR_OF_A_KIND:
				final QuadRanking q1 = (QuadRanking) h1;
				final QuadRanking q2 = (QuadRanking) h2;
				return QuadRanking.ordering().compare(q1, q2);
			case FULL_HOUSE:
				final FullHouseRanking fh1 = (FullHouseRanking) h1;
				final FullHouseRanking fh2 = (FullHouseRanking) h2;
				return FullHouseRanking.ordering().compare(fh1, fh2);
			case FLUSH:
				final FlushRanking f1 = (FlushRanking) h1;
				final FlushRanking f2 = (FlushRanking) h2;
				return FlushRanking.ordering().compare(f1, f2);
			case STRAIGHT:
				final StraightRanking str1 = (StraightRanking) h1;
				final StraightRanking str2 = (StraightRanking) h2;
				return StraightRanking.ordering().compare(str1, str2);
			case THREE_OF_A_KIND:
				final SetRanking s1 = (SetRanking) h1;
				final SetRanking s2 = (SetRanking) h2;
				return SetRanking.ordering().compare(s1, s2);
			case TWO_PAIRS:
				final TwoPairsRanking tp1 = (TwoPairsRanking) h1;
				final TwoPairsRanking tp2 = (TwoPairsRanking) h2;
				return TwoPairsRanking.ordering().compare(tp1, tp2);
			case PAIR:
				final PairRanking p1 = (PairRanking) h1;
				final PairRanking p2 = (PairRanking) h2;
				return PairRanking.ordering().compare(p1, p2);
			case HIGH_CARD:
				final HighCardRanking hc1 = (HighCardRanking) h1;
				final HighCardRanking hc2 = (HighCardRanking) h2;
				return HighCardRanking.ordering().compare(hc1, hc2);
				
		}

		throw new RuntimeException("Unexpected control flow event");
	}

}
