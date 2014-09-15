package elm327.reader;

public class Commands {

    private Commands() { }

    static class Read {

        enum Computer implements Command<Float> {

            Voltage("RV"),
            Ignition("IGN");

            private final String message;

            Computer(String code) {
                message = Obd2.AT(code);
            }

            @Override public String toMessage() {
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

        enum Info implements Command<String> {
            DeviceName("@1"),
            DeviceIdentifier("@2"),
            ProtocolName("DP"),
            ProtocolCode("DPN");

            private final String message;

            private Info(String code) {
                message = Obd2.AT(code);
            }

            @Override public String toMessage() {
                return message;
            }

            @Override public String parse(String data) {
                return data;
            }
        }

    static class Car {

            enum Position implements Command<Double> {
                Throttle(Pid.ThrottlePosition);

                private final String message;

                private Position(Pid pid) {
                    message = Obd2.DATA(pid.code);
                }

                @Override public String toMessage() {
                    return message;
                }

                @Override public Double parse(String data) {
                    return Parsers.position(data);
                }
            }

            enum Distance implements Command<Integer> {
               WithLampOn(Pid.DistanceWithMalfuncionOff);

               private final String message;

               private Distance(Pid pid) {
                   message = Obd2.DATA(pid.code);
               }

               @Override public String toMessage() {
                   return message;
               }

               @Override public Integer parse(String data) {
                   return Parsers.distance(data);
               }
            }

            enum Speed implements Command<Integer> {

                Vehicle(Pid.VehicleSpeed);

                private final String message;

                private Speed(Pid pid) {
                    message = Obd2.DATA(pid.code);
                }

                @Override public String toMessage() {
                    return message;
                }

                @Override public Integer parse(String data) {
                    return Parsers.speed(data);
                }
            }
        }
    }

    enum Send implements Command<String> {
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

    enum Configure implements Command<Boolean> {

        Disable_Echo(Param.Echo, Value.NO),
        Disable_PrintSpaces(Param.PrintSpaces, Value.NO),
        Disable_LineFeeds(Param.LineFeeds, Value.NO),
        Enable_Echo(Param.Echo, Value.YES),
        Enable_PrintSpaces(Param.PrintSpaces, Value.YES),
        Enable_LineFeeds(Param.LineFeeds, Value.YES);

        private final String message;

        private Configure(Param param, Value value) {
            message = Obd2.AT(param.toMessage(value));
        }

        @Override public Boolean parse(String data) {
            return Obd2.isOK(data);
        }

        @Override public String toMessage() {
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

    private static class Obd2 {
        static String AT(String command) {
            return "AT" + command;
        }
        static String DATA(String command) {
            return "01" + command;
        }

        static boolean isOK(String data) {
            return "OK".equals(data);
        }
    }

}