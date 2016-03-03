package apostov;

import static apostov.Suit.CLUBS;
import static apostov.Suit.SPADES;
import static apostov.Suit.DIAMONDS;
import static apostov.Suit.HEARTS;
import static apostov.Value.ACE;
import static apostov.Value.SEVEN;
import static apostov.Value.TWO;
import static apostov.Value.QUEEN;
import static apostov.Value.TEN;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class HandResultEnumeratorTest {

	@Before
	public void sleepForAwhile() throws InterruptedException {
		//Thread.sleep(15000);
	}
	
	@Test
	public void test01() {
		final HolecardHand aces = new HolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES));
		final HolecardHand trash = new HolecardHand(new Card(TWO, CLUBS), new Card(SEVEN, SPADES));
		
		displayPercentages(aces, trash);
	}

	@Test
	public void test02() {
		final HolecardHand aceTen = new HolecardHand(new Card(ACE, CLUBS), new Card(TEN, SPADES));
		final HolecardHand queenTen = new HolecardHand(new Card(QUEEN, DIAMONDS), new Card(TEN, HEARTS));
		
		displayPercentages(aceTen, queenTen);
	}

	private void displayPercentages(final HolecardHand h1, final HolecardHand h2) {
		final Map<HolecardHand, Fraction> result = new HandResultEnumerator().enumerateBoardsAndMeasureWins(ImmutableList.of(h1, h2));
		final Fraction sum = result.values().stream().reduce((x,y) -> x.add(y)).get();
		assertEquals(1, sum.intValue());
		
		System.out.println(Maps.transformValues(result, f -> String.format("%.02f%%", 100*f.doubleValue())));
	}
	
}
