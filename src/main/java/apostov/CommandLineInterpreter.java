package apostov;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.ArithmeticUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class CommandLineInterpreter {

	public static final String BOARD_ARG_NAME = "--board";
	
	private enum PokerMode {
		TEXAS_HOLDEM, OMAHA
	}

	public void interpretArguments(final String[] arguments) throws InvalidArgumentsException {
		if (arguments.length >= 2) {
			
			final ImmutableList<HoldemHolecardHand> competingHands;
			ImmutableList<Card> board = null;
			PokerMode pokerMode = null;
			
			{
				final ImmutableList.Builder<HoldemHolecardHand> builder = ImmutableList.builder();
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
			
			final long beginTime = System.nanoTime();
			final Map<HoldemHolecardHand, Fraction> winsByHand = new HoldemHandResultEnumerator().enumerateBoardsAndMeasureWins(competingHands, board);
			final long endTime = System.nanoTime();
			final long executionTimeIsMilliseconds = (endTime - beginTime) / ArithmeticUtils.pow(1000, 2);
			
			if (board.size() > 0) {
				final StringBuilder builder = new StringBuilder();
				builder.append("Board: ");
				for (final Card card : board) {
					builder.append(card + " ");
				}
				System.out.println(builder);
			}
			for (final AbstractHolecardHand hand : competingHands) {
				final Fraction fraction = winsByHand.get(hand);
				
				System.out.println(hand + "\t" + String.format("%.02f%%", fraction.percentageValue()));
			}
			System.out.println(String.format("Computed in %.02f s", executionTimeIsMilliseconds / 1000d));
			System.out.println();
		} else {
			throw new InvalidArgumentsException("Unexpected number of arguments");
		}
	}
	
	private HoldemHolecardHand holecardHandFromFourCharacters(final String representation) {
		if (representation.length() != 4)
			throw new RuntimeException(representation + " is not a valid hand");

		final Card c1 = cardFromTwoLetterRepresentation(representation.substring(0, 2));
		final Card c2 = cardFromTwoLetterRepresentation(representation.substring(2, 4));
		
		return new HoldemHolecardHand(c1, c2);
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
				final char suitCharacter = representation.charAt(i + 1);
				final Card currentBoardCard = cardFromTwoLetterRepresentation(valueCharacter, suitCharacter);
				builder.add(currentBoardCard);
			}
			return builder.build();
		} else
			throw new RuntimeException("A string of 6, 8 or 10 characters was expected as a board description");
	}
}
