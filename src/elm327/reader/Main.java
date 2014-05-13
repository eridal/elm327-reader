package elm327.reader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Throwables;
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
	
	private static final Map<String, String> commands = new ImmutableMap.Builder<String, String>()
			.put("IGN", "Ignition")
			.put("RV", "Volts")
			.put("RD", "Data")
			.build();
	
	private static void executeRunner(Runner runner, String[] params) {
		
		Channel chan;
		try {
			chan = runner.connect(params);
		} catch(IOException e) {
			throw Throwables.propagate(e);
		}
		
		chan.send("ATZ");  			    // Reset
		chan.setBoolean("ATL", false);  // Linefeed
		chan.setBoolean("ATE0", false); // Echo
		
		// Device info
		System.out.println(String.format("Device: %s"    , chan.getString("AT@1")));
		System.out.println(String.format("Identifier: %s", chan.getString("AT@2")));
		
		System.out.println(String.format("Protocol: %s (%s)", chan.getString("ATDP"),    // proto name
														      chan.getString("ATDPN"))); // proto number
		
		System.out.println("\nValues:");
		
		while (true) {
			for (Entry<String, String> entry: commands.entrySet()) {
				System.out.println(String.format("  %s: %s", entry.getValue(), chan.getString(entry.getKey())));
			}
			System.out.println(" --");
			break;
		}
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