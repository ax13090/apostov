package apostov.strength.ranking;

import com.google.common.collect.Ordering;

import apostov.Suit;
import apostov.Value;
import apostov.strength.PokerHandKind;

public class StraightFlushRanking extends PokerHandRanking {
	
	public final Value highestCardValue;
	public final Suit suit;

	public StraightFlushRanking(final Value highestCardValue, final Suit suit) {
		super(PokerHandKind.STRAIGHT_FLUSH);
		this.highestCardValue = highestCardValue;
		this.suit = suit;
	}
	
	public static Ordering<StraightFlushRanking> ordering() {
		return Ordering.natural().onResultOf(r -> r.highestCardValue);
	}
	
	@Override
	public int compareTo(final PokerHandRanking o) {
		final StraightFlushRanking other = (StraightFlushRanking) o;
		return ordering().compare(this, other);
	}
	
	@Override
	public String toString() {
		if (highestCardValue == Value.ACE)
			return "Royal Flush";
		else
			return "Straight Flush: " + highestCardValue.singular();
	}
}
