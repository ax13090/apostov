package apostov;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.concat;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import apostov.collections.ListCombinationIterable;
import apostov.strength.PokerHandComparator;
import apostov.strength.ranking.PokerHandRanking;

public class OmahaShowdownEvaluator extends AbstractShowdownEvaluator<OmahaHolecardHand> {

	@Override
	protected PokerHandRanking computeSingleHandStrength(final OmahaHolecardHand c, final Board board) {
		final ImmutableList<Card> playerHolecards = ImmutableList.copyOf(c.getHolecardsAsList());
		final ImmutableList<Card> boardCards = ImmutableList.copyOf(board.getBoardCardList());
		assert 5 == boardCards.size();
		assert 4 == playerHolecards.size();
		
		final Iterable<ImmutableList<Card>> twoCardsCombinations   = new ListCombinationIterable<>(2, playerHolecards);
		final Iterable<ImmutableList<Card>> threeCardsCombinations = new ListCombinationIterable<>(3, boardCards);

		final List<PokerHandRanking> rankingsForTwoSeparateCards = new ArrayList<>();
		for (final List<Card> twoCardsCombination : twoCardsCombinations) {
			for (final List<Card> threeCardsCombination : threeCardsCombinations){
				final ImmutableList<Card> allSearchedCards = copyOf(concat(twoCardsCombination, threeCardsCombination));
				final PokerHandRanking rankingForTheseTwoCards = selectBestCombinationFromAllCards(allSearchedCards);
				rankingsForTwoSeparateCards.add(rankingForTheseTwoCards);
			}
		}
		
		return new PokerHandComparator().max(rankingsForTwoSeparateCards);
	}

}
