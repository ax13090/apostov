package apostov.strength.ranking;

import java.util.Comparator;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.strength.PokerHandKind;

public class HighCardRanking extends PokerHandRanking {

	public final Card highestCard;
	public final Card secondHighestCard;
	public final Card middleCard;
	public final Card fourthCard;
	public final Card bottomCard;
	
	public HighCardRanking(
			final Card highestCard,
			final Card secondHighestCard,
			final Card middleCard,
			final Card fourthCard,
			final Card bottomCard)
	{
		super(PokerHandKind.HIGH_CARD);
		this.highestCard = highestCard;
		this.secondHighestCard = secondHighestCard;
		this.middleCard = middleCard;
		this.fourthCard = fourthCard;
		this.bottomCard = bottomCard;
	}
	
	public static Ordering<HighCardRanking> ordering() {
		return Ordering.from(
				Comparator.comparing((HighCardRanking h) -> h.highestCard.value)
				.thenComparing((HighCardRanking h) -> h.secondHighestCard.value)
				.thenComparing((HighCardRanking h) -> h.middleCard.value)
				.thenComparing((HighCardRanking h) -> h.fourthCard.value)
				.thenComparing((HighCardRanking h) -> h.bottomCard.value)
		);
	}

	@Override
	public int compareTo(final PokerHandRanking o) {
		final HighCardRanking other = (HighCardRanking) o;
		return ordering().compare(this, other);
	}
	
	@Override
	public String toString() {
		return "High card: " + highestCard.value.singular();
	}
}
