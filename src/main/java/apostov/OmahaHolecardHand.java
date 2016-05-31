package apostov;

import com.google.common.collect.ImmutableSet;

public class OmahaHolecardHand extends AbstractHolecardHand {

	public final Card firstCard;
	public final Card secondCard;
	public final Card thirdCard;
	public final Card fourthCard;
	
	
	public OmahaHolecardHand(final Card firstCard, final Card secondCard, final Card thirdCard, final Card fourthCard) {
		this.firstCard = firstCard;
		this.secondCard = secondCard;
		this.thirdCard = thirdCard;
		this.fourthCard = fourthCard;
	}

	@Override
	public ImmutableSet<Card> getHolecardsAsList() {
		return ImmutableSet.of(firstCard, secondCard, thirdCard, fourthCard);
	}
}
