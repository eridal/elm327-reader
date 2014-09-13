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

    public enum Do implements Command {
        Reset("Z"),
        SetAllToDefaults("D");

        private final String code;

        Do(String cmd) {
            code = "AT" + cmd;
        }

        @Override public String toCommandString() {
            return code;
        }
    }

    public class Disable extends Send {

        public static final Disable Echo = new Disable(Action.Echo);
        public static final Disable PrintSpaces = new Disable(Action.PrintSpaces);
        public static final Disable LineFeeds = new Disable(Action.LineFeeds);

        private Disable(Send.Action action) {
            super(action, Param.NO);
        }
    }

    public class Enable extends Send {

        public static final Enable Echo = new Enable(Action.Echo);
        public static final Enable PrintSpaces = new Enable(Action.PrintSpaces);
        public static final Enable LineFeeds = new Enable(Action.LineFeeds);

        private Enable(Action action) {
            super(action, Param.YES);
        }
    }

    public class Send implements Command {

        private final Action action;
        private final Param param;

        public Send(Action action, Param param) {
            this.action = action;
            this.param = param;
        }

        @Override public String toCommandString() {
            return String.format("AT%s", action.toCommandString(param));
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
}