package elm327.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

public class Main {

    private static Map<String, Mode> MODES = new ImmutableMap.Builder<String, Mode>()
            .put("tcp", new TcpMode())
            .put("file", new FileMode())
            .build();

    public static void main(String[] args) {

        if (args.length < 1) {
            showModesUsage();
            return;
        }

        String mode = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        executeMode(MODES.get(mode), params);
    }

    private static void executeMode(Mode runner, String[] params) {

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

    private static void showModesUsage() {
        System.out.println("Modes:");
        for (Entry<String, Mode> entry: MODES.entrySet()) {
            System.out.println(
                String.format("  %s %s", entry.getKey(), entry.getValue().params())
            );
        }
    }
    
    private interface Mode {

        public String params();
        Channel connect(String[] params) throws IOException;
    }
    
    private static class FileMode implements Mode {

        @Override public String params() {
            return "<path>";
        }

        @Override public Channel connect(String[] params) throws FileNotFoundException {
            return Channels.fromFile(new File(params[0]));
        }
    }
    
    private static class TcpMode implements Mode {
        
        @Override public String params() {
            return "<host> <port>";
        }
       
        @Override public Channel connect(String[] args) throws IOException {
            String host = args[0];
            int port = Integer.parseInt(args[1], 10);
            return Channels.fromSocket(new Socket(host, port));
        }
    }

}


