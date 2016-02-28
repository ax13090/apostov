package apostov;

import static org.junit.Assert.fail;

import org.junit.Test;

public class EnvironmentTest {

	@Test
	public void testAssertionsAreEnabled() {
		try {
			assert false;
			fail("Assertions are not enabled");
		} catch (@SuppressWarnings("unused") final AssertionError e) {
			// expected behavior
		}
	}
}
