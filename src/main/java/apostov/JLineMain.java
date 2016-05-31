package apostov;

import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;

public class JLineMain {

	public static void main(final String[] args) throws IOException {
		
		final ConsoleReader reader = new ConsoleReader();

		reader.setPrompt("> ");
		reader.addCompleter(new StringsCompleter("exit", CommandLineInterpreter.BOARD_ARG_NAME));


		final CommandLineInterpreter clInterpreter = new CommandLineInterpreter();
		String line;

		while ((line = reader.readLine()) != null) {
			final String trimmedLine = line.trim();
			if (trimmedLine.isEmpty()) {
				continue;
			} else if (trimmedLine.equalsIgnoreCase("exit")) {
				break;
			}  else if (trimmedLine.equalsIgnoreCase("gc")) {
				System.gc();
			} else {
				final String[] splitLine = line.split("\\s+");
				try {
					clInterpreter.interpretArguments(splitLine);
				} catch (final InvalidArgumentsException e) {
					e.printStackTrace();
				}
			}
		}

		reader.flush();

	}

}
