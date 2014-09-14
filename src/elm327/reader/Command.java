package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public interface Command<T> {

    String toMessage();
    T parse(String data);
    boolean matches(String message);

    public enum Get implements Command<String> {
        DeviceName("@1"),
        DeviceIdentifier("@2"),
        ProtocolName("DP"),
        ProtocolCode("DPN");

        private final String code;

        Get(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public String parse(String data) {
            return data;
        }

        @Override public boolean matches(String message) {
            return toMessage().equals(message);
        }
    }

    public class Read<T> implements Command<T> {

        public static final Read<String> IgnitionInputLevel = new Read<String>("IGN", Functions.<String>identity());
        public static final Read<String> Voltage = new Read<String>("RV", Functions.<String>identity());

        private final String code;
        private final Function<String, T> parser;

        Read(String cmd, Function<String, T> parser) {
            code = "AT" + cmd;
            this.parser = parser;
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public T parse(String data) {
            return parser.apply(data);
        }

        @Override public boolean matches(String message) {
            return toMessage().equals(message);
        }
    }

    public enum Do implements Command<Boolean> {
        Reset("Z"),
        SetAllToDefaults("D");

        private final String code;

        Do(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toMessage() {
            return code;
        }

        @Override public Boolean parse(String data) {
            return "OK" == data;
        }

        @Override public boolean matches(String message) {
            return toMessage().equals(message);
        }
    }

    public class Disable extends Setup {

        public static final Disable Echo = new Disable(Param.Echo);
        public static final Disable PrintSpaces = new Disable(Param.PrintSpaces);
        public static final Disable LineFeeds = new Disable(Param.LineFeeds);

        private Disable(Setup.Param action) {
            super(action, Value.NO);
        }
    }

    public class Enable extends Setup {

        public static final Enable Echo = new Enable(Param.Echo);
        public static final Enable PrintSpaces = new Enable(Param.PrintSpaces);
        public static final Enable LineFeeds = new Enable(Param.LineFeeds);

        private Enable(Param action) {
            super(action, Value.YES);
        }
    }

    public class Setup implements Command<Boolean> {

        private final Param param;
        private final Value value;

        public Setup(Param action, Value param) {
            this.param = action;
            this.value = param;
        }

        @Override public String toMessage() {
            return String.format("AT%s", param.toMessage(value));
        }

        @Override public Boolean parse(String data) {
            return "OK" == data;
        }

        @Override public boolean matches(String message) {
            return toMessage().equals(message);
        }

        enum Param {
          LineFeeds("E"),
          Echo("E"),
          PrintSpaces("S");

          private final String code;

          Param(String code) {
              this.code = code;
          }

          public String toMessage(Value p) {
              return String.format("%s%s", code, p.value);
          }
        }

        enum Value {
            NO ("0"),
            YES("1");

            final public String value;

            Value(String value) {
                this.value = value;
            }
        }
    }
}