package apostov;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

public enum Suit {
	DIAMONDS, CLUBS, HEARTS, SPADES;
	
	public char shortName() {
		return Character.toLowerCase(name().charAt(0));
	}

	public static final ImmutableMap<Character, Suit> byShortName = ImmutableMap.copyOf(
			Arrays.stream(values())
			.collect(Collectors.toMap(Suit::shortName, Function.identity())));
}
