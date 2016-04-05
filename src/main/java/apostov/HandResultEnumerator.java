package apostov;

import static com.google.common.collect.Iterators.concat;
import static com.google.common.collect.Maps.toMap;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class HandResultEnumerator {

	public ImmutableMap<HolecardHand, Fraction> enumerateBoardsAndMeasureWins(final ImmutableList<HolecardHand> candidates) {
		return enumerateBoardsAndMeasureWins(candidates, ImmutableList.of());
	}
	
	public ImmutableMap<HolecardHand, Fraction> enumerateBoardsAndMeasureWins(
			final ImmutableList<HolecardHand> candidates,
			final ImmutableList<Card> partialBoard)
	{
		assert partialBoard.isEmpty() || Board.ACCEPTABLE_PARTIAL_BOARD_SIZES.contains(partialBoard.size());
		
		final ImmutableList<Card> deck = new DeckFactory().newDeck(
				Stream.concat(
						candidates.stream()
							.map(HolecardHand::getHolecardsAsList)
							.flatMap(Collection::stream),
						partialBoard.stream())
					.collect(toSet())
		);
		
		final ShowdownEvaluator evaluator = new ShowdownEvaluator();
		final int numberOfUnknownCards = 5 - partialBoard.size();
		final int numberOfTestedBoards = (int) CombinatoricsUtils.binomialCoefficient(deck.size(), numberOfUnknownCards);
		
		final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		final Callable<? extends Map<HolecardHand, Fraction>> successChanceComputationTask = 
			() -> StreamSupport.stream(new Combinations(deck.size(), numberOfUnknownCards).spliterator(), true)
				.map(a -> stream(a).mapToObj(i -> deck.get(i)).collect(toList()))
				.map(pickedCards -> {
					assert pickedCards.size() == numberOfUnknownCards;
					final Iterator<Card> fullBoard = concat(partialBoard.iterator(), pickedCards.iterator());
					return Board.from(fullBoard);
				})
				.map(b -> evaluator.evaluateShowdown(candidates, b))
				.map(winningCandidates -> toMap(winningCandidates, c -> new Fraction(1, numberOfTestedBoards * winningCandidates.size())))
				.map(Map::entrySet)
				.flatMap(Collection::stream)
				.collect(Collectors.toConcurrentMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(x, y) -> x.add(y)));
		
		final Map<HolecardHand, Fraction> overallSuccessChanceByHand;
		try {
			overallSuccessChanceByHand = pool.submit(successChanceComputationTask).get();
		} catch (final InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			pool.shutdown();
		}
		
		/* In the case where one or several candidates never win, we still want them to
		 * have a Fraction representing their odds of winnings (i.e. Fraction.ZERO)
		 * instead of null */
		for (final HolecardHand candidate : candidates)
			if (overallSuccessChanceByHand.containsKey(candidate) == false)
				overallSuccessChanceByHand.put(candidate, Fraction.ZERO);
		
		return ImmutableMap.copyOf(overallSuccessChanceByHand);
	}
	
}
