package apostov;
import org.junit.Test;

import apostov.Library;

import static org.junit.Assert.*;

public class LibraryTest {
	@Test
	public void testSomeLibraryMethod() {
		Library classUnderTest = new Library();
		assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
	}
}
