package strength;

import com.google.common.collect.Ordering;

import apostov.Suit;
import apostov.Value;

public class StraightFlushRanking extends PokerHandRanking<StraightFlushRanking> {
	
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
	public int compareTo(final StraightFlushRanking other) {
		return ordering().compare(this, other);
	}
}
