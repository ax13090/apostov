package strength;

/*
 * Note: this class implements Comparable in a peculiar way. One is supposed
 * to call the compareTo() method only on two instances of the same subclass.
 * For instance, if o1 and o2 are instances of PairRanking, the
 * comparison o1.compareTo(o2) will work.
 * But if o1 is an instance of PairRanking and o2 is an instance
 * of StraightRanking, then o1.compareTo(o2) will throw a ClassCastException. 
 */
public abstract class PokerHandRanking implements Comparable<PokerHandRanking> {

	public final PokerHandKind handKind;

	public PokerHandRanking(final PokerHandKind handKind) {
		this.handKind = handKind;
	}
	
}
