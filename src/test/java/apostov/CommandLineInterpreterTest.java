package apostov;

import org.junit.Test;

public class CommandLineInterpreterTest {

	@Test
	public void test01() throws Exception {
		new CommandLineInterpreter().interpretArguments(new String[]{"AsAh", "2h7d", "JhTh"});
	}
	
	@Test
	public void test02() throws Exception {
		new CommandLineInterpreter().interpretArguments(new String[]{
				"AsAh", "2h7d", "JhTh",
				"--board", "2s2cJd"
		});
	}
}
