package elm327.reader;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private Commands() { }

    public static class Read {

        public static enum Computer implements Command<Float> {

            Voltage("RV"),
            Ignition("IGN");

            private final Message message;

            Computer(String code) {
                message = Messages.AT(code);
            }

            @Override public Message message() {
                return message;
            }

            private final String OBDSIM_BUG = "ELM327 v1.3a OBDGPSLogger";

            @Override public Float parse(String data) {
                if (this == Ignition) {
                    if (OBDSIM_BUG.equals(data)) {
                        return 0.0f;
                    }
                }
                return Float.valueOf(data);
            }
        }

        public static enum Info implements Command<String> {
            DeviceName("@1"),
            DeviceIdentifier("@2"),
            ProtocolName("DP"),
            ProtocolCode("DPN");

            private final Message message;

            private Info(String code) {
                message = Messages.AT(code);
            }

            @Override public Message message() {
                return message;
            }

            @Override public String parse(String data) {
                return data;
            }
        }
    }

    public static enum Send implements Command<String> {
        FullReset("Z"),
        WarmStart("WS"),
        SetDefaults("D");

        private final Message message;

        private Send(String cmd) {
            message = Messages.AT(cmd);
        }

        @Override public Message message() {
            return message;
        }

        @Override public String parse(String data) {
            return data;
        }
    }

    public static enum Configure implements Command<Boolean> {

        Disable_Echo(Param.Echo, Value.NO),
        Disable_PrintSpaces(Param.PrintSpaces, Value.NO),
        Disable_LineFeeds(Param.LineFeeds, Value.NO),
        Enable_Echo(Param.Echo, Value.YES),
        Enable_PrintSpaces(Param.PrintSpaces, Value.YES),
        Enable_LineFeeds(Param.LineFeeds, Value.YES);

        private final Message message;

        private Configure(Param param, Value value) {
            message = Messages.AT(param.message(value));
        }

        @Override public Boolean parse(String data) {
            return "OK".equals(data);
        }

        @Override public Message message() {
            return message;
        }

        private enum Param {
          LineFeeds("E"),
          Echo("E"),
          PrintSpaces("S");

          private final String code;

          private Param(String code) {
              this.code = code;
          }

          public String message(Value p) {
              return String.format("%s%s", code, p.value);
          }
        }

        private enum Value {
            NO ("0"),
            YES("1");

            final public String value;

            private Value(String value) {
                this.value = value;
            }
        }
    }

    public static class Errors {

        private Errors() {
            throw new IllegalStateException();
        }

        public static final Command<String[]> Read = new ReadErrorCommand<String[]>(Messages.readDTC()) {

            @Override public String[] parse(String data) {
                final String[] lines = data.split("\n+");
                final List<String> result = new ArrayList<>();

                for (String line : lines) {
                    for (int i = 2, len = line.length(); i < len; i += 4) {
                        final String head = line.substring(i, i + 1);
                        final String tail = line.substring(i + 1, i + 4);

                        if ("000".equals(tail)) {
                            continue;
                        }
                        final int first = Integer.parseInt(head, 16);
                        final char firstChar = firstChar(first);
                        final char secondChar = secondChar(first);
                        final String errorCode = String.format("%c%c%s", firstChar, secondChar, tail);

                        result.add(errorCode);
                    }
                }

                return result.toArray(new String[0]);
            }

            private char firstChar(int hex) {
                int b =  hex / 4;
                switch (b) {
                case 0: return 'P';
                case 1: return 'C';
                case 2: return 'B';
                case 3: return 'U';
                }
                throw new IllegalArgumentException();
            }

            private char secondChar(int hex) {
                int b = hex % 4;
                switch (b) {
                case 0: return '0';
                case 1: return '1';
                case 2: return '2';
                case 3: return '3';
                }
                return 'X';
                // throw new IllegalArgumentException();
            }
        };

        public static final Command<Void> Clear = new ErrorCommand<Void>(Messages.clearDTC()) {
            @Override public Void parse(String data) {
                return null;
            }
        };

        private static abstract class ReadErrorCommand<T> extends ErrorCommand<T> implements MultiFrameCommand {
            ReadErrorCommand(Message message) {
                super(message);
            }
        }

        private static abstract class ErrorCommand<T> implements Command<T> {

            final Message message;

            private ErrorCommand(Message message) {
                this.message = message;
            }

            @Override public Message message() {
                return  message;
            }
        }
    }
}