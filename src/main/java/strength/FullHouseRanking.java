package strength;

import java.util.Comparator;
import java.util.stream.Stream;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Value;

public class FullHouseRanking extends PokerHandRanking {

	public final Card firstSetCard;
	public final Card secondSetCard;
	public final Card thirdSetCard;
	public final Card firstPairCard;
	public final Card secondPairCard;
	
	public FullHouseRanking(
			final Card firstSetCard,
			final Card secondSetCard,
			final Card thirdSetCard,
			final Card firstPairCard,
			final Card secondPairCard)
	{
		super(PokerHandKind.FULL_HOUSE);
		this.firstSetCard = firstSetCard;
		this.secondSetCard = secondSetCard;
		this.thirdSetCard = thirdSetCard;
		this.firstPairCard = firstPairCard;
		this.secondPairCard = secondPairCard;

		if (1 != Stream.of(firstPairCard, secondPairCard).map(c -> c.value).distinct().count()) {
			throw new RuntimeException("Programming error: trying to make a full-house from an ill-formed pair");
		}
		
		if (1 != Stream.of(firstSetCard, secondSetCard, thirdSetCard).map(c -> c.value).distinct().count()) {
			throw new RuntimeException("Programming error: trying to make a full-house from an ill-formed set");
		}
	}

	public Value valueOfTheSet() {
		assert firstSetCard.value == secondSetCard.value;
		assert secondSetCard.value == thirdSetCard.value;
		return firstSetCard.value;
	}

	public Value valueOfThePair() {
		assert firstPairCard.value == secondPairCard.value;
		return firstPairCard.value;
	}
	
	public static Ordering<FullHouseRanking> ordering() {
		return Ordering.from(
				Comparator.comparing(FullHouseRanking::valueOfTheSet)
				.thenComparing(FullHouseRanking::valueOfThePair));
	}

	@Override
	public int compareTo(final PokerHandRanking o) {
		final FullHouseRanking other = (FullHouseRanking) o;
		return ordering().compare(this, other);
	}
}
