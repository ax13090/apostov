package apostov;

public class Main {

	public static void main(String[] args) {
		try {
			new CommandLineInterpreter().interpretArguments(args);
		} catch (final InvalidArgumentsException e) {
			throw new RuntimeException(e);
		}
	}

}
