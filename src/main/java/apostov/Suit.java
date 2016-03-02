package apostov;

public enum Suit {
	DIAMONDS, CLUBS, HEARTS, SPADES;
	
	public char shortName() {
		return Character.toLowerCase(name().charAt(0));
	}
}
