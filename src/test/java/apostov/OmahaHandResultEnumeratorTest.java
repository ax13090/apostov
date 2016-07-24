package apostov;

import static apostov.Suit.CLUBS;
import static apostov.Suit.DIAMONDS;
import static apostov.Suit.HEARTS;
import static apostov.Suit.SPADES;
import static apostov.Value.ACE;
import static apostov.Value.KING;
import static apostov.Value.SEVEN;
import static apostov.Value.TWO;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class OmahaHandResultEnumeratorTest extends HandResultEnumeratorTest {

	@Test
	public void acesAndKingsDoubleSuitedVersusTrashPreflop() {
		final OmahaHolecardHand aces = new OmahaHolecardHand(new Card(ACE, CLUBS), new Card(ACE, SPADES), new Card(KING, CLUBS), new Card(KING, SPADES));
		final OmahaHolecardHand trash = new OmahaHolecardHand(new Card(TWO, CLUBS), new Card(TWO, SPADES), new Card(TWO, HEARTS), new Card(SEVEN, DIAMONDS));
		
		final Map<OmahaHolecardHand, Fraction> percentages = computePercentages(aces, trash);
		assertTrue(percentages.get(aces).doubleValue() > 0.8);
		assertTrue(percentages.get(trash).doubleValue() < 0.2);
	}
	
}
