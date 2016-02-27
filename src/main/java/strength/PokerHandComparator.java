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
		}

		throw new UnsupportedOperationException();
	}

}
