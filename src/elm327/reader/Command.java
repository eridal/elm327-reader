package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public interface Command {

    String toCommandString();

    public enum Get implements Command {
        DeviceName("@1"),
        DeviceIdentifier("@2"),
        ProtocolName("DP"),
        ProtocolCode("DPN");

        private final String code;

        Get(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toCommandString() {
            return code;
        }
    }

    public class Read<T> implements Command {

        public static final Read<String> IgnitionInputLevel = new Read<String>("IGN", Functions.<String>identity());
        public static final Read<String> Voltage = new Read<String>("RV", Functions.<String>identity());

        private final String code;
        private final Function<String, T> parser;

        Read(String cmd, Function<String, T> parser) {
            code = "AT" + cmd;
            this.parser = parser;
        }

        @Override public String toCommandString() {
            return code;
        }

        public T parse(String result) {
            return parser.apply(result);
        }
    }

    public enum Set implements Command {
        AllToDefaults("D");

        final String code;

        Set(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toCommandString() {
            return code;
        }
    }

    public enum Do implements Command {
        Reset("Z");

        private final String code;

        Do(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toCommandString() {
            return code;
        }
    }

    public enum Disable implements Command {
        Echo(Action.Echo),
        LineFeeds(Action.LineFeeds),
        PrintSpaces(Action.PrintSpaces);

        private final Action action;

        Disable(Action action) {
            this.action = action;
        }

        @Override public String toCommandString() {
            return String.format("AT%s", action.toCommandString(Param.NO));
        }
    }

    public enum Enable implements Command {
        Echo(Action.Echo),
        LineFeeds(Action.LineFeeds),
        PrintSpaces(Action.PrintSpaces);

        private final Action action;

        Enable(Action action) {
            this.action = action;
        }

        @Override public String toCommandString() {
            return String.format("AT%s", action.toCommandString(Param.YES));
        }
    }

    enum Action {
      LineFeeds("E"),
      Echo("E"),
      PrintSpaces("S");

      private final String code;

      Action(String code) {
          this.code = code;
      }

      public String toCommandString(Param p) {
          return String.format("%s%s", code, p.value);
      }
    }

    enum Param {
        NO ("0"),
        YES("1");

        final public String value;

        Param(String value) {
            this.value = value;
        }
    }
}