package strength;

import java.util.Comparator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import apostov.Suit;
import apostov.Value;

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
				Comparator.comparing(f -> f.firstValue),
				Comparator.comparing(f -> f.secondValue),
				Comparator.comparing(f -> f.thirdValue),
				Comparator.comparing(f -> f.fourthValue),
				Comparator.comparing(f -> f.fifthValue)
		));
	}
}
