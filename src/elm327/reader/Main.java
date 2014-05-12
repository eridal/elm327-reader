package elm327.reader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import elm327.reader.runner.FileRunner;
import elm327.reader.runner.Runner;
import elm327.reader.runner.TcpRunner;

public class Main {

	private static Map<String, Runner> runners = new ImmutableMap.Builder<String, Runner>()
			.put("tcp", new TcpRunner())
			.put("file", new FileRunner())
			.build();

	public static void main(String[] args) {
		
		if (args.length < 1) {
			showRunnersUsage();
			return;
		}
		
		String mode = args[0];
		String[] params = Arrays.copyOfRange(args, 1, args.length);
		executeRunner(runners.get(mode), params);
	}
	
	private static void executeRunner(Runner runner, String[] params) {
		
		Channel chan;
		try {
			chan = runner.connect(params);
		} catch(IOException e) {
			e.printStackTrace(System.err);
			return;
		}
		
		System.out.println(String.format("`%s`", chan.send("ATZ")));
		System.out.println(String.format("`%s`", chan.send("ATL0")));
		System.out.println(String.format("`%s`", chan.send("ATE0")));
	}
	
	public static void showRunnersUsage() {
		System.out.println("Modes:");
		for (Entry<String, Runner> entry: runners.entrySet()) {
			System.out.println(
				String.format("  %s %s", entry.getKey(), entry.getValue().params())
			);
		}
	}
}