package strength;

import static apostov.Value.ACE;
import static apostov.Value.FIVE;
import static apostov.Value.FOUR;
import static apostov.Value.THREE;
import static apostov.Value.TWO;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Value;

public class StraightRanking extends PokerHandRanking {

	public final Card highestCard;
	public final Card secondHighestCard;
	public final Card middleCard;
	public final Card fourthCard;
	public final Card bottomCard;
	
	private StraightRanking(
			final Card highestCard,
			final Card secondHighestCard,
			final Card middleCard,
			final Card fourthCard,
			final Card bottomCard)
	{
		super(PokerHandKind.STRAIGHT);
		this.highestCard = highestCard;
		this.secondHighestCard = secondHighestCard;
		this.middleCard = middleCard;
		this.fourthCard = fourthCard;
		this.bottomCard = bottomCard;
		
	}
	
	public static StraightRanking create(
			final Card highestCard,
			final Card secondHighestCard,
			final Card middleCard,
			final Card fourthCard,
			final Card bottomCard) 
	{
		final ImmutableList<Card> allCards = ImmutableList.of(
				highestCard,
				secondHighestCard,
				middleCard,
				fourthCard,
				bottomCard);
		
		if (allCards.stream().map(c -> c.suit).distinct().count() < 2)
			throw new RuntimeException("Programming error: trying to make a Straight ranking with 5 same-suit cards");
		
		checkStraightValues(allCards);
		return new StraightRanking(highestCard, secondHighestCard, middleCard, fourthCard, bottomCard);
	}

	private static void checkStraightValues(final ImmutableList<Card> allCards) {
		final Card bottomCard = allCards.get(4);
		final ImmutableList<Value> allCardsValues = ImmutableList.copyOf(allCards.stream().map(c -> c.value).iterator());
		
		if (allCardsValues.equals(ImmutableList.of(ACE, TWO, THREE, FOUR, FIVE)))
			return;
		
		for (int i = 0; i < 4; ++i) {
			if (bottomCard.value.ordinal() + i != allCards.get(i).value.ordinal())
				throw new RuntimeException("Programming error: a Straight ranking is ill-formed");
		}
	}
	
	public static Ordering<StraightRanking> ordering() {
		return Ordering.natural().onResultOf(s -> s.highestCard.value);
	}
}
