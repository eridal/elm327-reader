package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class Commands {

    private Commands() { }

    static class Read<T> implements Command<T> {

        private final String code;
        private final Function<String, T> parser;

        private Read(String code, Function<String, T> parser) {
            this.code = code;
            this.parser = parser;
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public T parse(String data) {
            return parser.apply(data);
        }

        static class Computer extends Read<Float> {

            private Computer(String cmd, Function<String, Float> parser) {
                super(Obd2.AT(cmd), parser);
            }

            private static final Function<String, Float> PARSER = new Function<String, Float>() {
                @Override public Float apply(String data) {
                    return Float.valueOf(data);
                }
            };

            public static final Command<Float> Voltage = new Computer("RV", PARSER);
            public static final Command<Float> Ignition = new Computer("IGN", PARSER) {
                private final String OBDSIM_BUG = "ELM327 v1.3a OBDGPSLogger";
                @Override public Float parse(String data) {
                    if (OBDSIM_BUG.equals(data)) {
                        return 0.0f;
                    }
                    return super.parse(data);
                }
            };
        }

        static class Info extends Read<String> {

            private Info(String cmd) {
                super(Obd2.AT(cmd), Functions.<String>identity());
            }

            public static final Command<String> DeviceName = new Info("@1");
            public static final Command<String> DeviceIdentifier = new Info("@2");
            public static final Command<String> ProtocolName = new Info("DP");
            public static final Command<String> ProtocolCode = new Info("DPN");
        }
    }

    static enum Send implements Command<String> {
        FullReset("Z"),
        WarmStart("WS"),
        SetDefaults("D");

        private final String code;

        private Send(String cmd) {
            code = Obd2.AT(cmd);
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public String parse(String data) {
            return data;
        }
    }

    static class Configure implements Command<Boolean> {

        private final String message;

        public Configure(Param param, Value value) {
            message = Obd2.AT(param.toMessage(value));
        }

        @Override public Boolean parse(String data) {
            return Obd2.isOK(data);
        }

        @Override public String toMessage() {
            return message;
        }

        static class Disable extends Configure {

            public static final Configure Echo = new Disable(Param.Echo);
            public static final Configure PrintSpaces = new Disable(Param.PrintSpaces);
            public static final Configure LineFeeds = new Disable(Param.LineFeeds);

            private Disable(Param action) {
                super(action, Value.NO);
            }
        }

        static class Enable extends Configure {

            public static final Configure Echo = new Enable(Param.Echo);
            public static final Configure PrintSpaces = new Enable(Param.PrintSpaces);
            public static final Configure LineFeeds = new Enable(Param.LineFeeds);

            private Enable(Param action) {
                super(action, Value.YES);
            }
        }

        private enum Param {
          LineFeeds("E"),
          Echo("E"),
          PrintSpaces("S");

          private final String code;

          private Param(String code) {
              this.code = code;
          }

          public String toMessage(Value p) {
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

    static class Obd2 {
        static String AT(String command) {
            return "AT" + command;
        }
        static String Read(String command) {
            return "01" + command;
        }

        static boolean isOK(String data) {
            return "OK".equals(data);
        }
    }
}