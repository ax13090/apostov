package strength.ranking;

import java.util.Comparator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import apostov.Suit;
import apostov.Value;
import strength.PokerHandKind;

public class FlushRanking extends PokerHandRanking {
	
	public final Suit suit;
	public final Value firstValue;
	public final Value secondValue;
	public final Value thirdValue;
	public final Value fourthValue;
	public final Value fifthValue;
	
	public FlushRanking(
			final Suit suit, 
			final Value firstValue, 
			final Value secondValue, 
			final Value thirdValue,
			final Value fourthValue, 
			final Value fifthValue)
	{
		super(PokerHandKind.FLUSH);
		this.suit = suit;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.thirdValue = thirdValue;
		this.fourthValue = fourthValue;
		this.fifthValue = fifthValue;
	}
	
	public static Ordering<FlushRanking> ordering() {
		return Ordering.compound(ImmutableList.of(
				Comparator.comparing((FlushRanking f) -> f.firstValue),
				Comparator.comparing((FlushRanking f) -> f.secondValue),
				Comparator.comparing((FlushRanking f) -> f.thirdValue),
				Comparator.comparing((FlushRanking f) -> f.fourthValue),
				Comparator.comparing((FlushRanking f) -> f.fifthValue)
		));
	}

	@Override
	public int compareTo(final PokerHandRanking o) {
		final FlushRanking other = (FlushRanking) o;
		return ordering().compare(this, other);
	}
	
	@Override 
	public String toString() {
		return "Flush: " + firstValue.plural();
	}
}
