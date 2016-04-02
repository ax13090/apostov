package apostov;

import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Board {
	
	public static final ImmutableSet<Integer> ACCEPTABLE_PARTIAL_BOARD_SIZES = ImmutableSet.of(3, 4, 5);
	
	public final Card firstFlopCard;
	public final Card secondFlopCard;
	public final Card thirdFlopCard;
	public final Card turnCard;
	public final Card riverCard;

	public Board(
			final Card firstFlopCard,
			final Card secondFlopCard,
			final Card thirdFlopCard,
			final Card turnCard,
			final Card riverCard)
	{
		this.firstFlopCard = firstFlopCard;
		this.secondFlopCard = secondFlopCard;
		this.thirdFlopCard = thirdFlopCard;
		this.turnCard = turnCard;
		this.riverCard = riverCard;
	}

	public ImmutableSet<Card> getBoardCardList() {
		return ImmutableSet.of(firstFlopCard, secondFlopCard, thirdFlopCard, turnCard, riverCard);
	}
	
	public static Board from(final ImmutableList<Card> cardList) {
		final int cardCount = cardList.size();
		assert 5 == cardCount;
		
		final Iterator<Card> iterator = cardList.iterator();
		return from(iterator);
	}

	public static Board from(final Iterator<Card> iterator) {
		final Board fullBoard = new Board(
				iterator.next(),
				iterator.next(),
				iterator.next(),
				iterator.next(),
				iterator.next()
		);
		
		assert iterator.hasNext() == false;
		return fullBoard;
	}
}
