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

import elm327.reader.Protocol.InitializationException;

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

        Mode mode = MODES.get(args[0]);
        String[] params = Arrays.copyOfRange(args, 1, args.length);

        executeMode(mode, params);
    }

    private final static PID<?>[] PIDS = new PID[] {
        PID.ENGINE_RPM,
        PID.FUEL_PRESSURE,
        PID.ENGINE_LOAD_VALUE,
        PID.ENGINE_COOLANT_TEMP,

        PID.AIR_FLOW_RATE,
        PID.BANK_1_SENSOR_1,
        PID.BANK_1_SENSOR_2,
        PID.BANK_1_SENSOR_3,
        PID.BANK_1_SENSOR_4,
        PID.BANK_2_SENSOR_1,
        PID.BANK_2_SENSOR_2,
        PID.BANK_2_SENSOR_3,
        PID.BANK_2_SENSOR_4,
        PID.FUEL_SYSTEM_STATUS,
        PID.INTAKE_AIR_TEMPERATURE,
        PID.LONG_TERM_FUEL_BANK_1,
        PID.LONG_TERM_FUEL_BANK_2,
        PID.MONITOR_STATUS_SINCE_DTC_CLEARED,
        PID.ODB_STANDARD,
        PID.SHORT_TERM_FUEL_BANK_1,
        PID.SHORT_TERM_FUEL_BANK_2,
        PID.TIMING_ADVANCE,
    };

    private static void executeMode(Mode runner, String[] params) {

        Protocol proto;

        try {
            Channel channel = runner.connect(params);
            proto = new Protocol(channel);
        } catch (IOException | InitializationException e) {
            throw Throwables.propagate(e);
        }

        // Device info
        System.out.println(String.format("Device: %s"    , proto.read(Commands.Read.Info.DeviceName)));
        System.out.println(String.format("Identifier: %s", proto.read(Commands.Read.Info.DeviceIdentifier)));

        System.out.println(String.format("Protocol: %s (%s)", proto.read(Commands.Read.Info.ProtocolName),    // proto name
                                                              proto.read(Commands.Read.Info.ProtocolCode))); // proto number
        System.out.println("\nValues:");

        while (true) {
            System.out.println(String.format(" Ignition: %s", proto.read(Commands.Read.Computer.Ignition)));
            System.out.println(String.format(" Voltage: %s" , proto.read(Commands.Read.Computer.Voltage)));
            System.out.println(" PIDs:");

            for (PID<?> pid : PIDS) {
                System.out.println(String.format("%s: %s %s", pid.code, proto.read(pid), pid.unit));
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


