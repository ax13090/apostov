package apostov;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public class DeckFactory {

	public ImmutableList<Card> newFullDeck() {
		final Set<Suit> suits = EnumSet.allOf(Suit.class);
		final Set<Value> values = EnumSet.allOf(Value.class);
		
		final ImmutableList.Builder<Card> builder = ImmutableList.builder();
		for (final Suit suit : suits)
			for (final Value value : values)
				builder.add(new Card(value, suit));
		
		return builder.build();
	}
	
	public ImmutableList<Card> newDeck(final Set<Card> excludedCards) {
		final Set<Suit> suits = EnumSet.allOf(Suit.class);
		final Set<Value> values = EnumSet.allOf(Value.class);
		
		final ImmutableList.Builder<Card> builder = ImmutableList.builder();
		for (final Suit suit : suits)
			for (final Value value : values) {
				final Card newCard = new Card(value, suit);
				if (excludedCards.contains(newCard))
					continue;
				else
					builder.add(newCard);
			}
		
		return builder.build();
	}
	
}
