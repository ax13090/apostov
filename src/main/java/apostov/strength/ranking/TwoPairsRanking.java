package apostov.strength.ranking;

import java.util.Comparator;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.strength.PokerHandKind;

public class TwoPairsRanking extends PokerHandRanking {

	public final Card firstCardOfHighestPair;
	public final Card secondCardOfHighestPair;
	public final Card firstCardOfLowestPair;
	public final Card secondCardOfLowestPair;
	public final Card kicker;
	
	public TwoPairsRanking(
			final Card firstCardOfHighestPair, 
			final Card secondCardOfHighestPair,
			final Card firstCardOfLowestPair, 
			final Card secondCardOfLowestPair,
			final Card kicker)
	{
		super(PokerHandKind.TWO_PAIRS);
		this.firstCardOfHighestPair = firstCardOfHighestPair;
		this.secondCardOfHighestPair = secondCardOfHighestPair;
		this.firstCardOfLowestPair = firstCardOfLowestPair;
		this.secondCardOfLowestPair = secondCardOfLowestPair;
		this.kicker = kicker;
	}
	
	public static Ordering<TwoPairsRanking> ordering() {
		return Ordering.from(
				Comparator.comparing((TwoPairsRanking tp) -> tp.firstCardOfHighestPair.value)
				.thenComparing((TwoPairsRanking tp) -> tp.firstCardOfLowestPair.value)
				.thenComparing((TwoPairsRanking tp) -> tp.kicker.value)
		);
	}
	
	@Override
	public int compareTo(final PokerHandRanking o) {
		final TwoPairsRanking other = (TwoPairsRanking) o;
		return ordering().compare(this, other);
	}	
	
	@Override
	public String toString() {
		return "Two pairs: " + firstCardOfHighestPair.value.plural() 
		+ " and " + firstCardOfLowestPair.value.singular()
		+ ", with kicker " + kicker.value.singular();
	}
}
