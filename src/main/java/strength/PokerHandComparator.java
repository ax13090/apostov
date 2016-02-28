package strength;

import java.util.Comparator;

import com.google.common.collect.Ordering;

/**
 * A {@link Comparator} implementation for poker hand rankings.
 * This is actually a subclass of Guava's {@link Ordering} class, which
 * descends from Java's Comparator class.
 */
public class PokerHandComparator extends Ordering<PokerHandRanking> {

	@Override
	public int compare(final PokerHandRanking h1, final PokerHandRanking h2) {

		/* First, check if the hands are in the same category. If not, the result
		 * of the comparison is the result of comparing the Hand-Categories, according
		 * to the order degined in the PokerHandKindenumeration type. */
		{
			final int handKindComparisonResult = Comparator.comparing((PokerHandRanking r) -> r.handKind).compare(h1, h2);
			if (handKindComparisonResult != 0)
				return handKindComparisonResult;
		}

		/* If the two hands being compared were of the same category, then
		 * a specific comparison method for that category is called.
		 * For instance, PairRanking#compareTo(), or FlushRanking#compareTo(),
		 * according to the common class of the two hand objects.
		 * 
		 * This is where a Java warning concerning generic types is suppressed.
		 * Sugestions on how to avoid suppressing warnings here are welcome  :-) */
		assert h1.handKind == h2.handKind;
		return h1.compareTo(h2);
	}

}
