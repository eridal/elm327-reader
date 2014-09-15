package elm327.reader;

public class Commands {

    private Commands() { }

    interface Read {

        enum Computer implements Command<Float> {

            Voltage("RV"),
            Ignition("IGN");

            private final Message message;

            Computer(String code) {
                message = Message.AT(code);
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

        enum Info implements Command<String> {
            DeviceName("@1"),
            DeviceIdentifier("@2"),
            ProtocolName("DP"),
            ProtocolCode("DPN");

            private final Message message;

            private Info(String code) {
                message = Message.AT(code);
            }

            @Override public Message message() {
                return message;
            }

            @Override public String parse(String data) {
                return data;
            }
        }

        interface Car {

            enum Position implements Command<Double> {
                Throttle(Pid.ThrottlePosition);

                private final Message message;

                private Position(Pid pid) {
                    message = Message.DATA(pid.code);
                }

                @Override public Message message() {
                    return message;
                }

                @Override public Double parse(String data) {
                    return Parsers.position(data);
                }
            }

            enum Distance implements Command<Integer> {
               WithLampOn(Pid.DistanceWithMalfuncionOff);

               private final Message message;

               private Distance(Pid pid) {
                   message = Message.DATA(pid.code);
               }

               @Override public Message message() {
                   return message;
               }

               @Override public Integer parse(String data) {
                   return Parsers.distance(data);
               }
            }

            enum Speed implements Command<Integer> {

                Vehicle(Pid.VehicleSpeed);

                private final Message message;

                private Speed(Pid pid) {
                    message = Message.DATA(pid.code);
                }

                @Override public Message message() {
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

        private final Message message;

        private Send(String cmd) {
            message = Message.AT(cmd);
        }

        @Override public Message message() {
            return message;
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

        private final Message message;

        private Configure(Param param, Value value) {
            message = Message.AT(param.message(value));
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
}