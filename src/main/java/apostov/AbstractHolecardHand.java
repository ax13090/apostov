package apostov;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

public abstract class AbstractHolecardHand {

	public abstract ImmutableSet<Card> getHolecardsAsList();

	@Override
	public String toString() {
		return Joiner.on("").join(getHolecardsAsList());
	}

}
