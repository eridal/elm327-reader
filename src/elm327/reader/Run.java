package elm327.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import elm327.reader.Protocol.InitializationException;

public class Run {

    public static void main(String[] args) {

        if (args.length != 1) {
            printUsage();
            return;
        }

        Protocol proto;

        try {
            proto = new Protocol(
                Channels.fromFile(new File(args[0]))
            );
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            return;
        } catch (InitializationException e) {
            System.out.println("ERROR: Protocol startup error");
            return;
        }

        new Program(proto).go();
    }

    private static void printUsage() {
        System.out.println(
            String.format("USAGE: %s <file>", Run.class.getName())
        );
    }

    private static class Program {

        private final Protocol proto;

        Program(Protocol proto) {
            this.proto = proto;
        }

        public void go() {
            headers();
            for (;;) {
                values();
            }
        }

        private void headers() {
            for (Map.Entry<String, Command.Get> header : HEADERS.entrySet()) {
                Command.Get cmd = header.getValue();
                String result = proto.read(cmd);
                System.out.println(
                    String.format("%s: %s", header.getKey(), result)
                );
            }
        }

        private void values() {
            for (Map.Entry<String, Command.Read<?>> header : VALUES.entrySet()) {
                Command.Read<?> cmd = header.getValue();
                Object result = proto.read(cmd);
                System.out.println(
                    String.format("%s: %s", header.getKey(), result)
                );
            }
        }

        private static final Map<String, Command.Get> HEADERS = new ImmutableMap.Builder<String, Command.Get>()
                .put("Device", Command.Get.DeviceName)
                .build();

        private static final Map<String, Command.Read<?>> VALUES = new ImmutableMap.Builder<String, Command.Read<?>>()
                .put("Ignition", Command.Read.IgnitionInputLevel)
                .put("Voltage", Command.Read.Voltage)
                .build();


    }
}
