package strength.ranking;

import java.util.Comparator;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Suit;
import apostov.Value;
import strength.PokerHandKind;

public class PairRanking extends PokerHandRanking {
	
	public final Value pairValue;
	public final Suit firstSuit;
	public final Suit secondSuit;
	public final Card firstKicker;
	public final Card secondKicker;
	public final Card thirdKicker;
	
	public PairRanking(
			final Value pairValue,
			final Suit firstSuit,
			final Suit secondSuit,
			final Card firstKicker,
			final Card secondKicker,
			final Card thirdKicker)
	{
		super(PokerHandKind.PAIR);
		this.pairValue = pairValue;
		this.firstSuit = firstSuit;
		this.secondSuit = secondSuit;
		this.firstKicker = firstKicker;
		this.secondKicker = secondKicker;
		this.thirdKicker = thirdKicker;
	}
	
	public static Ordering<PairRanking> ordering() {
		return Ordering.from(
				Comparator.comparing((PairRanking s) -> s.pairValue)
					.thenComparing(s -> s.firstKicker.value)
					.thenComparing(s -> s.secondKicker.value)
					.thenComparing(s -> s.thirdKicker.value)
		);
	}

	@Override
	public int compareTo(PokerHandRanking o) {
		final PairRanking other = (PairRanking) o;
		return ordering().compare(this, other);
	}
	
	@Override
	public String toString() {
		return "Pair of " + pairValue.plural() 
		+ " with kickers " + firstKicker.value.singular()
		+ ", " + secondKicker.value.singular()
		+ ", " + thirdKicker.value.singular();
	}
}
