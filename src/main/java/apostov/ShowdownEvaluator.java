package apostov;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import strength.PokerHandRanking;

public class ShowdownEvaluator {

	public void evaluateShowdown(final ImmutableList<HolecardHand> candidates, final Board board) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public PokerHandRanking selectBestCombination(final ImmutableCollection<Card> cards) {
		assert cards.size() >= 5;
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
