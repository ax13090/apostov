package strength;

import java.util.Comparator;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Suit;
import apostov.Value;

public class SetRanking extends PokerHandRanking {
	
	public final Value valueOfTheSet;
	public final Suit firstSuit;
	public final Suit secondSuit;
	public final Suit thirdSuit;
	public final Card firstKicker;
	public final Card secondKicker;
	
	public SetRanking(
			final Value valueOfTheSet,
			final Suit firstSuit,
			final Suit secondSuit,
			final Suit thirdSuit,
			final Card firstKicker,
			final Card secondKicker)
	{
		super(PokerHandKind.THREE_OF_A_KIND);
		this.valueOfTheSet = valueOfTheSet;
		this.firstSuit = firstSuit;
		this.secondSuit = secondSuit;
		this.thirdSuit = thirdSuit;
		this.firstKicker = firstKicker;
		this.secondKicker = secondKicker;
	}
	
	public static Ordering<SetRanking> ordering() {
		return Ordering.from(
				Comparator.comparing((SetRanking s) -> s.valueOfTheSet)
					.thenComparing(s -> s.firstKicker.value)
					.thenComparing(s -> s.secondKicker.value)
		);
	}

	@Override
	public int compareTo(final PokerHandRanking o) {
		final SetRanking other = (SetRanking) o;
		return ordering().compare(this, other);
	}
	
	
	@Override
	public String toString() {
		return "Three " + valueOfTheSet.plural();
	}
}
