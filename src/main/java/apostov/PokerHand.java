package apostov;

public class PokerHand {
	public final Card firstCard;
	public final Card secondCard;
	public final Card thirdCard;
	public final Card fourthCard;
	public final Card fifthCard;

	public PokerHand(
			final Card firstCard,
			final Card secondCard,
			final Card thirdCard,
			final Card fourthCard,
			final Card fifthCard)
	{
		this.firstCard = firstCard;
		this.secondCard = secondCard;
		this.thirdCard = thirdCard;
		this.fourthCard = fourthCard;
		this.fifthCard = fifthCard;
	}

}
