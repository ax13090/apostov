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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public abstract class HandResultEnumerator<T extends AbstractHolecardHand> {

	private AbstractShowdownEvaluator<T> showdownEvaluator;

	public HandResultEnumerator(final AbstractShowdownEvaluator<T> showdownEvaluator) {
		this.showdownEvaluator = showdownEvaluator;
	}

	public ImmutableMap<T, Fraction> enumerateBoardsAndMeasureWins(final ImmutableList<T> candidates) {
		return enumerateBoardsAndMeasureWins(candidates, ImmutableList.of());
	}
	
	public ImmutableMap<T, Fraction> enumerateBoardsAndMeasureWins(
			final ImmutableList<T> candidates,
			final ImmutableList<Card> partialBoard)
	{
		assert partialBoard.isEmpty() || Board.ACCEPTABLE_PARTIAL_BOARD_SIZES.contains(partialBoard.size());
		
		final ImmutableList<Card> deck = new DeckFactory().newDeck(
				Stream.concat(
						candidates.stream()
							.map(AbstractHolecardHand::getHolecardsAsList)
							.flatMap(Collection::stream),
						partialBoard.stream())
					.collect(toSet())
		);
		
		final AbstractShowdownEvaluator<T> evaluator = this.showdownEvaluator;
		final int numberOfUnknownCards = 5 - partialBoard.size();
		
		final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		final Callable<? extends Map<T, Fraction>> successChanceComputationTask = 
			() -> {
				/* TODO Reduce the size of this code, by using
				 * the new ListCombinationIterable instead of
				 * the raw Combinations class */
				return StreamSupport.stream(new Combinations(deck.size(), numberOfUnknownCards).spliterator(), true)
					.map(a -> stream(a).mapToObj(i -> deck.get(i)).collect(toList()))
					.map(pickedCards -> {
						assert pickedCards.size() == numberOfUnknownCards;
						final Iterator<Card> fullBoard = concat(partialBoard.iterator(), pickedCards.iterator());
						return Board.from(fullBoard);
					})
					.map(b -> evaluator.evaluateShowdown(candidates, b))
					.map(winningCandidates -> toMap(winningCandidates, c -> new Fraction(1, winningCandidates.size())))
					.map(Map::entrySet)
					.flatMap(Collection::stream)
					.collect(Collectors.toConcurrentMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(x, y) -> x.add(y)));
			};
		
		final Map<T, Fraction> unweighedSuccessChanceByHand;
		try {
			unweighedSuccessChanceByHand = pool.submit(successChanceComputationTask).get();
		} catch (final InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			pool.shutdown();
		}

		final Fraction denominatorAsFraction = 
				unweighedSuccessChanceByHand
				.values()
				.stream()
				.reduce((x, y) -> x.add(y))
				.get();
		if (1 != denominatorAsFraction.getDenominator())
			throw new RuntimeException();

		
		/* Build an ImmutableMap with the results. This map respects the order of the candidates
		 * given as a function argument. */
		final ImmutableMap.Builder<T, Fraction> builder = ImmutableMap.builder();
		for (final T candidate : candidates) {
			final Fraction weighedHandSuccessChance;
			if (unweighedSuccessChanceByHand.containsKey(candidate)) {
				final Fraction unweighedHandSuccessChance = unweighedSuccessChanceByHand.get(candidate);
				weighedHandSuccessChance = unweighedHandSuccessChance.divide(denominatorAsFraction);
			} else
				 /* In the case where one or several candidates never win, we still want them to
				  * have a Fraction representing their odds of winnings (i.e. Fraction.ZERO)
				  * instead of null */
				weighedHandSuccessChance = Fraction.ZERO;
			
			builder.put(candidate, weighedHandSuccessChance);
		}
		
		return builder.build();
	}
	
}
