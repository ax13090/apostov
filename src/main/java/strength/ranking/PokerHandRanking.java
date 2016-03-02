package strength.ranking;

import strength.PokerHandComparator;
import strength.PokerHandKind;

/*
 * Note: this class implements Comparable in a peculiar way. One is supposed
 * to call the compareTo() method only on two instances of the same subclass.
 * For instance, if o1 and o2 are instances of PairRanking, the
 * comparison o1.compareTo(o2) will work.
 * But if o1 is an instance of PairRanking and o2 is an instance
 * of StraightRanking, then o1.compareTo(o2) will throw a ClassCastException.
 * 
 * Also, while this class defines a natural ordering for itself, this ordering
 * is not consistent with equals().
 */
public abstract class PokerHandRanking implements Comparable<PokerHandRanking> {

	public final PokerHandKind handKind;

	public PokerHandRanking(final PokerHandKind handKind) {
		this.handKind = handKind;
	}
	
	/**
	 * Only call this method to compare objects of the same type.
	 * Otherwise a ClassCastException will be raised.
	 * 
	 * To compare two Poker-Hand-Rankings of different classes, use
	 * a {@link PokerHandComparator}.
	 */
	@Override
	@Deprecated
	public abstract int compareTo(PokerHandRanking h);
	
}
