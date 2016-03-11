package apostov;

import org.junit.Test;

public class CommandLineInterpreterTest {

	@Test
	public void test01() {
		new CommandLineInterpreter().interpretArguments(new String[]{"AsAh", "2h7d", "JhTh"});
	}
}
