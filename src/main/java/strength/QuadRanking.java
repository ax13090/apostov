package strength;

import java.util.Comparator;

import com.google.common.collect.Ordering;

import apostov.Card;
import apostov.Value;

public class QuadRanking extends PokerHandRanking implements Comparable<PokerHandRanking> {

	public final Value value;
	public final Card kicker;
	
	public QuadRanking(final Value value, final Card kicker) {
		super(PokerHandKind.FOUR_OF_A_KIND);
		this.value = value;
		this.kicker = kicker;
	}
	
	public static Ordering<QuadRanking> ordering() {
		return Ordering.from(
				Comparator.comparing((QuadRanking q) -> q.value)
				.thenComparing(q -> q.kicker.value));
	}

	@Override
	public int compareTo(final PokerHandRanking o) {
		final QuadRanking other = (QuadRanking) o;
		return ordering().compare(this, other);
	}
}
