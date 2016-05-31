package apostov;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.concat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import apostov.strength.ranking.PokerHandRanking;

public class HoldemShowdownEvaluator extends AbstractShowdownEvaluator<HoldemHolecardHand> {

	@Override
	protected PokerHandRanking computeSingleHandStrength(final HoldemHolecardHand c, final Board board) {
		final ImmutableSet<Card> playerHolecards = c.getHolecardsAsList();
		final ImmutableSet<Card> boardCards = board.getBoardCardList();
		
		final ImmutableList<Card> allCards = copyOf(concat(playerHolecards, boardCards));
		
		return selectBestCombinationFromAllCards(allCards);
	}
}
