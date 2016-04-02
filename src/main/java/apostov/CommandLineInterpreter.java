package apostov;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class CommandLineInterpreter {

	public static final String BOARD_ARG_NAME = "--board";

	public void interpretArguments(final String[] arguments) {
		if (arguments.length >= 2) {
			
			final ImmutableList<HolecardHand> competingHands;
			ImmutableList<Card> board = null;
			
			{
				final ImmutableList.Builder<HolecardHand> builder = ImmutableList.builder();
				final Iterator<String> argIterator = Iterators.forArray(arguments);
				while (argIterator.hasNext()) {
					final String argument = argIterator.next();
					if (argument.equals(BOARD_ARG_NAME)) {
						if (board != null)
							throw new RuntimeException("Cannot define board twice");
						
						if (argIterator.hasNext() == false)
							throw new RuntimeException("You must define board values after " + BOARD_ARG_NAME);
						
						final String boardAsString = argIterator.next();
						board = boardFromStringRepresentation(boardAsString);
					} else {
						builder.add(holecardHandFromFourCharacters(argument));
					}
				}
				if (board == null)
					board = ImmutableList.of();
				competingHands = builder.build();
			}
			final Map<HolecardHand, Fraction> winsByHand = new HandResultEnumerator().enumerateBoardsAndMeasureWins(competingHands, board);
			
			if (board.size() > 0) {
				final StringBuilder builder = new StringBuilder();
				builder.append("Board: ");
				for (final Card card : board) {
					builder.append(card + " ");
				}
				System.out.println(builder);
			}
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
		
		return cardFromTwoLetterRepresentation(valueCharacter, suitCharacter);
	}

	private Card cardFromTwoLetterRepresentation(final char valueCharacter, final char suitCharacter) {
		final Value value = Value.byShortName.get(valueCharacter);
		final Suit suit = Suit.byShortName.get(suitCharacter);
		
		if (suit == null || value == null) {
			final String invalidString = String.valueOf(valueCharacter) + String.valueOf(suitCharacter);
			throw new RuntimeException(invalidString + " is not a valid card");
		}
		
		return new Card(value, suit);
	}
	
	private ImmutableList<Card> boardFromStringRepresentation(final String representation) {
		final int stringLength = representation.length();
		if (stringLength % 2 == 0 && Board.ACCEPTABLE_PARTIAL_BOARD_SIZES.contains(stringLength / 2)) {
			final ImmutableList.Builder<Card> builder = ImmutableList.builder();
			for (int i = 0; i < stringLength; i += 2) {
				final char valueCharacter = representation.charAt(i);
				final char suitCharacter = representation.charAt(i +1);
				final Card currentBoardCard = cardFromTwoLetterRepresentation(valueCharacter, suitCharacter);
				builder.add(currentBoardCard);
			}
			return builder.build();
		} else
			throw new RuntimeException("A string of 6, 8 or 10 characters was expected as a board description");
	}
}
