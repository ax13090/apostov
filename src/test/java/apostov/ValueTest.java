package apostov;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValueTest {

	@Test
	public void testCollections() {
		assertEquals(Value.ACE, Value.asDescendingList.get(0));
		assertEquals(Value.TWO, Value.asAscendingList.get(0));
	}
	
}
