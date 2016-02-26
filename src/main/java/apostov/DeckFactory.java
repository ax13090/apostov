package apostov;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class DeckFactory {

	public ImmutableSet<Card> newDeck() {
		final Set<Suit> suits = EnumSet.allOf(Suit.class);
		final Set<Value> values = EnumSet.allOf(Value.class);
		
		final ImmutableSet.Builder<Card> builder = ImmutableSet.builder();
		for (final Suit suit : suits)
			for (final Value value : values)
				builder.add(new Card(value, suit));
		
		return builder.build();
	}
	
}
