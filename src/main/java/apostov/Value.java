package apostov;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.collect.ImmutableList;

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
	public static final ImmutableList<Value> asAscendingList = ImmutableList.copyOf(values());
	public static final ImmutableList<Value> asDescendingList = ImmutableList.copyOf(values()).reverse();
	
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

