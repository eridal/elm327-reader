package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class Commands {

    private Commands() { }

    static enum Get implements Command<String> {
        DeviceName("@1"),
        DeviceIdentifier("@2"),
        ProtocolName("DP"),
        ProtocolCode("DPN");

        private final String code;

        private Get(String cmd) {
            code = Obd2.AT(cmd);
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public String parse(String data) {
            return data;
        }
    }

    static class Read<T> implements Command<T> {

        public static final Read<String> IgnitionInputLevel = new Read<String>("IGN", Functions.<String>identity());
        public static final Read<String> Voltage = new Read<String>("RV", Functions.<String>identity());

        private final String code;
        private final Function<String, T> parser;

        private Read(String cmd, Function<String, T> parser) {
            code = Obd2.AT(cmd);
            this.parser = parser;
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public T parse(String data) {
            return parser.apply(data);
        }
    }

    static enum Send implements Command<String> {
        Reset("Z"),
        Defaults("D");

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

    static class Disable extends Setup {

        public static final Disable Echo = new Disable(Param.Echo);
        public static final Disable PrintSpaces = new Disable(Param.PrintSpaces);
        public static final Disable LineFeeds = new Disable(Param.LineFeeds);

        private Disable(Setup.Param action) {
            super(action, Value.NO);
        }
    }

    static class Enable extends Setup {

        public static final Enable Echo = new Enable(Param.Echo);
        public static final Enable PrintSpaces = new Enable(Param.PrintSpaces);
        public static final Enable LineFeeds = new Enable(Param.LineFeeds);

        private Enable(Param action) {
            super(action, Value.YES);
        }
    }

    static class Setup implements Command<Boolean> {

        private final String message;

        public Setup(Param param, Value value) {
            message = Obd2.AT(param.toMessage(value));
        }

        @Override public Boolean parse(String data) {
            return "OK" == data;
        }

        @Override public String toMessage() {
            return message;
        }

        protected enum Param {
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

        protected enum Value {
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
    }
}