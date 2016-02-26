package strength;

import com.google.common.collect.Ordering;

import apostov.Value;

public class StraightFlushRanking extends PokerHandRanking {
	
	public final Value highestCardValue;

	public StraightFlushRanking(final Value highestCardValue) {
		super(PokerHandKind.STRAIGHT_FLUSH);
		this.highestCardValue = highestCardValue;
	}
	
	public static Ordering<StraightFlushRanking> ordering() {
		return Ordering.natural().onResultOf(x -> x.highestCardValue);
	}
}
