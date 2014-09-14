package elm327.reader;

import com.google.common.base.Function;

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

    static class Read implements Command<Float> {

        public static final Read Voltage = new Read("RV");
        public static final Read Ignition = new Read("IGN") {
            private final String OBDSIM_BUG = "ELM327 v1.3a OBDGPSLogger";
            @Override public Float parse(String data) {
                if (OBDSIM_BUG.equals(data)) {
                    return 0.0f;
                }
                return super.parse(data);
            }
        };

        private final String code;

        private Read(String cmd) {
            code = Obd2.AT(cmd);
        }

        @Override public String toMessage() {
            return code;
        }

        Function<String, Float> PARSER = new Function<String, Float>() {
            @Override public Float apply(String data) {
                return Float.valueOf(data);
            }
        };



        @Override public Float parse(String data) {
            return PARSER.apply(data);
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