package apostov;

import com.google.common.collect.ImmutableSet;

public class HolecardHand {

	public final Card firstCard;
	public final Card secondCard;
	
	public HolecardHand(final Card firstCard, final Card secondCard) {
		this.firstCard = firstCard;
		this.secondCard = secondCard;
	}

	public ImmutableSet<Card> getHolecardsAsList() {
		return ImmutableSet.of(firstCard, secondCard);
	}
	
	@Override
	public String toString() {
		return firstCard.toString() + secondCard.toString();
	}
}
