package apostov;

import com.google.common.collect.ImmutableSet;

public class Board {
	public final Card firstFlopCard;
	public final Card secondFlopCard;
	public final Card thirdFlopCard;
	public final Card turnCard;
	public final Card riverCard;

	public Board(
			final Card firstFlopCard,
			final Card secondFlopCard,
			final Card thirdFlopCard,
			final Card turnCard,
			final Card riverCard)
	{
		this.firstFlopCard = firstFlopCard;
		this.secondFlopCard = secondFlopCard;
		this.thirdFlopCard = thirdFlopCard;
		this.turnCard = turnCard;
		this.riverCard = riverCard;
	}

	public ImmutableSet<Card> getBoardCardList() {
		return ImmutableSet.of(firstFlopCard, secondFlopCard, thirdFlopCard, turnCard, riverCard);
	}
}
