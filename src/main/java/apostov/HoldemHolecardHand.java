package apostov;

import com.google.common.collect.ImmutableSet;

public class HoldemHolecardHand extends AbstractHolecardHand {

	public final Card firstCard;
	public final Card secondCard;
	
	public HoldemHolecardHand(final Card firstCard, final Card secondCard) {
		this.firstCard = firstCard;
		this.secondCard = secondCard;
	}
	
	@Override
	public ImmutableSet<Card> getHolecardsAsList() {
		return ImmutableSet.of(firstCard, secondCard);
	}
}
