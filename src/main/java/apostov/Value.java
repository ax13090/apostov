package apostov;

import org.apache.commons.lang3.text.WordUtils;

// TODO Add two methods returning there values into immutable lists, in normal and reverse orders.
public enum Value {
	
	TWO('2'),
	THREE('3'),
	FOUR('4'),
	FIVE('5'),
	SIX('6'),
	SEVEN('7'),
	EIGHT('8'),
	NINE('9'),
	TEN('T'),
	JACK('J'),
	QUEEN('Q'),
	KING('K'),
	ACE('A');
	
	private Value(final char shortName) {
		this.shortName = shortName;
	}
	
	public final char shortName;
	
	/**
	 * @return the singular form of this Value, such as <tt>"Ace"</tt> or <tt>"King"</tt>.
	 */
	public String singular() {
		return WordUtils.capitalize(name());
	}	
	/**
	 * @return the plural form of this Value, such as <tt>"Aces"</tt> or <tt>"Kings"</tt>.
	 */
	public String plural() {
		return singular() + 's';
	}
}

