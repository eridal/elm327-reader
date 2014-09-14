package elm327.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import elm327.reader.Protocol.InitializationException;

public class Debug {

    private final Protocol proto;

    private Debug(Protocol proto) {
        this.proto = proto;
    }

    private void loop() {
        output(HEADERS);
        for (;;) {
            output(VALUES);
            output(PIDS);
        }
    }

    private void output(Map<String, Command<?>> values) {
        for (Map.Entry<String, Command<?>> header : values.entrySet()) {
            output(header.getKey(), header.getValue());
        }
    }

    private void output(String header, Command<?> cmd) {
        Object result = proto.read(cmd);
        System.out.println(
            String.format("%s: %s", header, result)
        );
    }

    private static final Map<String, Command<?>> HEADERS = new ImmutableMap.Builder<String, Command<?>>()
            .put("Device", Commands.Read.Info.DeviceName)
            .put("Protocol", Commands.Read.Info.ProtocolName)
            .build();

    private static final Map<String, Command<?>> VALUES = new ImmutableMap.Builder<String, Command<?>>()
            .put("Ignition", Commands.Read.Computer.Ignition)
            .put("Voltage", Commands.Read.Computer.Voltage)
            .build();

    private static final Map<String, Command<?>> PIDS = new ImmutableMap.Builder<String, Command<?>>()
            .put("Throttle", PID.THROTTLE_POSITION)
            .put("Speed", PID.VEHICLE_SPEED)
            .build();

    public static void main(String[] args) {

        if (args.length != 1) {
            printUsage();
            return;
        }

        final Channel channel;
        final Protocol proto;

        try {
            channel = Channels.fromFile(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            return;
        }

        try {
            proto = new Protocol(channel);
        } catch (InitializationException e) {
            System.out.println("ERROR: Protocol startup error");
            return;
        }

        new Debug(proto).loop();
    }

    private static void printUsage() {
        System.out.println(
            String.format("USAGE: %s <file>", Debug.class.getName())
        );
    }
}
