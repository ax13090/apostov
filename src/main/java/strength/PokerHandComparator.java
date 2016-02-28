package strength;

import java.util.Comparator;

import com.google.common.collect.Ordering;

public class PokerHandComparator extends Ordering<PokerHandRanking<?>> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public int compare(final PokerHandRanking h1, final PokerHandRanking h2) {

		{
			final int handKindComparisonResult = Comparator.comparing((PokerHandRanking r) -> r.handKind).compare(h1, h2);
			if (handKindComparisonResult != 0)
				return handKindComparisonResult;
		}

		assert h1.handKind == h2.handKind;
		return h1.compareTo(h2);
	}

}
