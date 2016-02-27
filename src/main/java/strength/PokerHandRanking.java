package strength;

/**
 * 
 */
public abstract class PokerHandRanking implements Comparable<PokerHandRanking> {

	public final PokerHandKind handKind;

	public PokerHandRanking(final PokerHandKind handKind) {
		this.handKind = handKind;
	}
	
}
