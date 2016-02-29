package apostov;

public class Card {
	public final Value value;
	public final Suit suit;

	public Card(final Value value, final Suit suit) {
		this.value = value;
		this.suit = suit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (suit == null ? 0 : suit.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Card other = (Card) obj;
		if (suit != other.suit)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		final char valueAsCharacter = value.name().charAt(0);
		final char suitAsCharacter = Character.toLowerCase(suit.name().charAt(0));
		return new String(new char[]{valueAsCharacter, suitAsCharacter});
	}
}
