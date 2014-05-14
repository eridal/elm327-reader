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

    private static void executeRunner(Runner runner, String[] params) {

        Protocol proto;
        
        try {
            proto = new Protocol(runner.connect(params));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        
        // Device info
        System.out.println(String.format("Device: %s"    , proto.getDeviceName()));
        System.out.println(String.format("Identifier: %s", proto.getDeviceIdentifier()));

        System.out.println(String.format("Protocol: %s (%s)", proto.getProtocolName(),    // proto name
                                                              proto.getProtocolNumber())); // proto number
        System.out.println("\nValues:");

        while (true) {
            for (PID pid : PID.values()) {
                System.out.println(String.format("  %s: %s", pid.name(), proto.getValue(pid)));
            }
            System.out.println(" --");
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
