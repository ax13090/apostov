package apostov;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class HandResultEnumeratorTest {

	protected <T extends AbstractHolecardHand> Map<T, Fraction> computePercentages(final HandResultEnumerator<T> handResultEvaluator, final T h1, final T h2) {
		final Map<T, Fraction> result = handResultEvaluator.enumerateBoardsAndMeasureWins(ImmutableList.of(h1, h2));
		final Fraction sum = result.values().stream().reduce((x,y) -> x.add(y)).get();
		assertEquals(Fraction.ONE, sum);
		displayResults(result);
		return result;
	}

	protected Map<HoldemHolecardHand, Fraction> computePercentages(final HoldemHolecardHand h1, HoldemHolecardHand h2) {
		return computePercentages(new HoldemHandResultEnumerator(), h1, h2);
	}
	
	protected Map<OmahaHolecardHand, Fraction> computePercentages(final OmahaHolecardHand h1, OmahaHolecardHand h2) {
		return computePercentages(new OmahaHandResultEnumerator(), h1, h2);
	}
	
	protected void displayResults(final Map<? extends AbstractHolecardHand, Fraction> result) {
		System.out.println(Maps.transformValues(result, f -> String.format("%.02f%%", f.percentageValue())));
	}

}
