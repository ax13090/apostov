package apostov;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.ImmutableList;

public class CommandLineInterpreter {

	public void interpretArguments(final String[] arguments) {
		if (arguments.length >= 2) {
			
			final List<HolecardHand> competingHands = 
					Arrays.stream(arguments)
						.map(arg -> holecardHandFromFourCharacters(arg))
						.collect(Collectors.toList());
			
			
			final Map<HolecardHand, Fraction> winsByHand = new HandResultEnumerator().enumerateBoardsAndMeasureWins(ImmutableList.copyOf(competingHands));
			for (final Map.Entry<HolecardHand, Fraction> entry : winsByHand.entrySet()) {
				final HolecardHand hand = entry.getKey();
				final Fraction fraction = entry.getValue();
				
				System.out.println(hand + "\t" + String.format("%.02f%%", 100 * fraction.doubleValue()));
			}
			
		} else {
			throw new RuntimeException("Unexpected number of arguments");
		}
	}
	
	private HolecardHand holecardHandFromFourCharacters(final String representation) {
		if (representation.length() != 4)
			throw new RuntimeException(representation + " is not a valid hand");

		final Card c1 = cardFromTwoLetterRepresentation(representation.substring(0, 2));
		final Card c2 = cardFromTwoLetterRepresentation(representation.substring(2, 4));
		
		return new HolecardHand(c1, c2);
	}
	
	private Card cardFromTwoLetterRepresentation(final String representation) {
		if (representation.length() != 2)
			throw new RuntimeException(representation + " is not a valid card");

		final char valueCharacter = representation.charAt(0);
		final char suitCharacter = representation.charAt(1);
		
		final Value value = Value.byShortName.get(valueCharacter);
		final Suit suit = Suit.byShortName.get(suitCharacter);
		
		if (suit == null || value == null)
			throw new RuntimeException(representation + " is not a valid card");
		
		return new Card(value, suit);
	}
}
